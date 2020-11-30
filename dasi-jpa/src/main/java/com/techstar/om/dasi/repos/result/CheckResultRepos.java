package com.techstar.om.dasi.repos.result;

import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.jpa.result.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CheckResultRepos extends JpaRepository<CheckResult, Long>, JpaSpecificationExecutor<CheckResult> {
    void deleteByPointId(long point);

    void deleteByTargetId(long target);

    int countByPriorityGreaterThanEqual(EPriority priority);
}
