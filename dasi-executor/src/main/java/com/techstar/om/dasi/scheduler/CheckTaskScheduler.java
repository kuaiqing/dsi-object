package com.techstar.om.dasi.scheduler;

import com.techstar.om.dasi.ExecutorConfiguration;
import com.techstar.om.dasi.handler.HandlerBase;
import com.techstar.om.dasi.jpa.info.CheckPointScheduler;
import com.techstar.om.dasi.jpa.info.CheckPointSchedulerId;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import com.techstar.om.dasi.repos.result.CheckResultDiskFreeRepos;
import com.techstar.om.dasi.repos.result.CheckResultHttpServiceRepos;
import com.techstar.om.dasi.repos.result.CheckResultProcessStatusRepos;
import com.techstar.om.dasi.repos.result.CheckResultRecordNumberRepos;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@Service
@EnableScheduling
@Order(value = 2)
public class CheckTaskScheduler implements SchedulingConfigurer {
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private CheckPointSchedulerRepos pointSchedulerRepos;
    @Autowired
    private ExecutorConfiguration executorConfiguration;
    @Autowired
    private CheckConfig checkConfig;

    @Autowired
    private CheckResultDiskFreeRepos diskFreeRepos;
    @Autowired
    private CheckResultHttpServiceRepos httpServiceRepos;
    @Autowired
    private CheckResultProcessStatusRepos processStatusRepos;
    @Autowired
    private CheckResultRecordNumberRepos recordNumberRepos;

    private volatile ScheduledTaskRegistrar taskRegistrar;

    private final Map<CheckPointSchedulerId, ScheduledTask> idTasks = new ConcurrentHashMap<>(16);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler);
        this.taskRegistrar = taskRegistrar;
    }

    /**
     * 因为执行器可能跑在一个不能访问的网段，因此采用定时刷新的方式，从数据库获取定时任务
     */
    @Scheduled(initialDelayString = "pt5s", fixedRateString = "pt30s")
    public void refreshTasks() {
        Map<CheckPointSchedulerId, CheckPointScheduler> idPointSchedulers = idPointSchedulers(checkConfig.getGroups());
        // 删除不用的调度任务
        idTasks.forEach((id, task) -> {
            CheckPointScheduler pointScheduler = idPointSchedulers.get(id);
            CheckTask checkTask = (CheckTask) task.getTask().getRunnable();
            if (pointScheduler == null // 调度已经删除了
                    || !pointScheduler.getScheduler().getCron().equals(checkTask.getPointScheduler().getScheduler().getCron()) // 调度时间变化
                    || pointScheduler.getPoint().getUpdateTime().isAfter(checkTask.getPointScheduler().getPoint().getUpdateTime()) // 检查项目发生变化
                    || pointScheduler.getPoint().getTarget().getUpdateTime().isAfter(checkTask.getPointScheduler().getPoint().getTarget().getUpdateTime()) // 检查对象发生变化
            ) {
                idTasks.remove(id);
                task.cancel();
            }
        });
        // 增加新增的调度任务
        idPointSchedulers.forEach((id, pointScheduler) -> {
            if (!idTasks.containsKey(id)) {
                HandlerBase handler = executorConfiguration.getIdHandlers()
                        .get(pointScheduler.getPoint().getHandler().getId());
                CheckTask checkTask = new CheckTask(handler, pointScheduler, pointSchedulerRepos);
                CronTask cronTask = new CronTask(checkTask, pointScheduler.getScheduler().getCron());
                idTasks.put(id, taskRegistrar.scheduleCronTask(cronTask));
            }
        });
    }

    /**
     * 按对象分组过滤调度任务
     */
    public Map<CheckPointSchedulerId, CheckPointScheduler> idPointSchedulers(Set<String> groups) {
        return pointSchedulerRepos.findAll().stream()
                .filter(x -> !Boolean.TRUE.equals(x.getPoint().getDisable())) // 过滤禁用的检查点
                .filter(x -> !Boolean.TRUE.equals(x.getPoint().getTarget().getDisable())) // 过滤禁用的对象
                .filter(x -> (groups == null || groups.isEmpty()) ? // 只检查指定的对象分组
                        true : groups.contains(x.getPoint().getTarget().getGroup().getId()))
                .collect(toMap(k -> k.getId(), v -> v));
    }

    /**
     * 只保留3个月的日志数据
     */
    @Scheduled(fixedRateString = "p1d")
    public void deleteHistory() {
        DateTime appliedTime = DateTime.now().withTimeAtStartOfDay().minusMonths(3);
        diskFreeRepos.deleteByIdAppliedTimeLessThan(appliedTime);
        httpServiceRepos.deleteByIdAppliedTimeLessThan(appliedTime);
        processStatusRepos.deleteByIdAppliedTimeLessThan(appliedTime);
        recordNumberRepos.deleteByIdAppliedTimeLessThan(appliedTime);
    }
}
