package com.techstar.om.dasi.scheduler;

import com.techstar.om.dasi.handler.HandlerBase;
import com.techstar.om.dasi.jpa.info.CheckPointScheduler;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.util.CronExpression;
import org.joda.time.DateTime;

import java.util.Date;

@Getter
@Setter
@Log4j2
public class CheckTask implements Runnable {
    private HandlerBase handlerBase;
    private CheckPointScheduler pointScheduler;
    private CheckPointSchedulerRepos pointSchedulerRepos;

    public CheckTask(HandlerBase handlerBase, CheckPointScheduler pointScheduler,
                     CheckPointSchedulerRepos pointSchedulerRepos) {
        this.handlerBase = handlerBase;
        this.pointScheduler = pointScheduler;
        this.pointSchedulerRepos = pointSchedulerRepos;
    }

    @Override
    public void run() {
        try {
            pointSchedulerRepos.updateLastRunTimeById(pointScheduler.getId(), DateTime.now());
            handlerBase.execute(pointScheduler.getPoint());
            pointSchedulerRepos.updateLastCompleteTimeById(pointScheduler.getId(), DateTime.now());
            // 计算下次运行时间
            CronExpression cron = new CronExpression(pointScheduler.getScheduler().getCron());
            Date nextRunDate = cron.getNextValidTimeAfter(DateTime.now().toDate());
            pointSchedulerRepos.updateNextSchedulerTimeById(pointScheduler.getId(), new DateTime(nextRunDate));
        } catch (Exception e) {
            log.error(e);
        }
    }


}
