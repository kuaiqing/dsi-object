package com.techstar.om.dasi.repos.result;

import com.techstar.om.dasi.jpa.result.CheckResultProcessStatus;
import com.techstar.om.dasi.jpa.result.CheckResultProcessStatusId;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CheckResultProcessStatusRepos extends JpaRepository<CheckResultProcessStatus, CheckResultProcessStatusId>,
        JpaSpecificationExecutor<CheckResultProcessStatus> {
    void deleteByIdAppliedTimeLessThan(DateTime appliedTime);
}
