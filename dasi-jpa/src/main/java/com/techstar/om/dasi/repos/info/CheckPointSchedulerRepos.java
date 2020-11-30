package com.techstar.om.dasi.repos.info;

import com.techstar.om.dasi.jpa.info.CheckPointScheduler;
import com.techstar.om.dasi.jpa.info.CheckPointSchedulerId;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CheckPointSchedulerRepos extends JpaRepository<CheckPointScheduler, CheckPointSchedulerId> {
    List<CheckPointScheduler> findByIdScheduler(long scheduler);

    Page<CheckPointScheduler> findByIdScheduler(long scheduler, Pageable pageable);

    Page<CheckPointScheduler> findByIdSchedulerAndPointNameLikeIgnoringCase(long scheduler, String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update CheckPointScheduler set lastRunTime=:lastRunTime where id=:id")
    void updateLastRunTimeById(CheckPointSchedulerId id, DateTime lastRunTime);

    @Transactional
    @Modifying
    @Query(value = "update CheckPointScheduler set lastCompleteTime=:lastCompleteTime where id=:id")
    void updateLastCompleteTimeById(CheckPointSchedulerId id, DateTime lastCompleteTime);

    @Transactional
    @Modifying
    @Query(value = "update CheckPointScheduler set nextSchedulerTime=:nextSchedulerTime where id=:id")
    void updateNextSchedulerTimeById(CheckPointSchedulerId id, DateTime nextSchedulerTime);

    void deleteByIdPoint(long point);

    void deleteByIdScheduler(long scheduler);

    void deleteByPointTargetId(long target);

}
