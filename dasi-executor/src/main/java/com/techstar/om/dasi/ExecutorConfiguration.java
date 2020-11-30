package com.techstar.om.dasi;

import com.techstar.om.dasi.handler.HandlerBase;
import com.techstar.om.dasi.handler.annotation.DsiHandler;
import com.techstar.om.dasi.jpa.info.CheckHandler;
import com.techstar.om.dasi.repos.info.CheckHandlerRepos;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Configuration
@Order(value = 1)
public class ExecutorConfiguration implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private CheckHandlerRepos checkHandlerRepos;
    @Getter
    private Map<String, HandlerBase> idHandlers; // 注册的执行器对象

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(8);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("dsi-");
        return taskScheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(DsiHandler.class);
        idHandlers = beans.values().stream().collect(
                toMap(k -> k.getClass().getAnnotation(DsiHandler.class).id(), v -> (HandlerBase) v));
        registerHandlers(beans.values());
    }

    // 通过注解，自动注册CheckHandler
    private void registerHandlers(Collection<Object> handlerBeans) {
        List<CheckHandler> checkHandlers = handlerBeans.stream().map(bean -> {
            DsiHandler handler = bean.getClass().getAnnotation(DsiHandler.class);
            CheckHandler checkHandler = new CheckHandler(handler.id());
            checkHandler.setName(handler.name());
            checkHandler.setTargetType(handler.type());
            return checkHandler;
        }).collect(toList());
        checkHandlerRepos.saveAll(checkHandlers);
    }
}
