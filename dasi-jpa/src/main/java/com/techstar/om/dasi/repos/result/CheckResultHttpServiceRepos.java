package com.techstar.om.dasi.repos.result;

import com.techstar.om.dasi.jpa.result.CheckResultHttpService;
import com.techstar.om.dasi.jpa.result.CheckResultHttpServiceId;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CheckResultHttpServiceRepos extends JpaRepository<CheckResultHttpService, CheckResultHttpServiceId>,
        JpaSpecificationExecutor<CheckResultHttpService> {
    void deleteByIdAppliedTimeLessThan(DateTime appliedTime);
}
