package com.techstar.om.dasi.repos.result;

import com.techstar.om.dasi.jpa.result.CheckResultDiskFree;
import com.techstar.om.dasi.jpa.result.CheckResultDiskFreeId;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CheckResultDiskFreeRepos extends JpaRepository<CheckResultDiskFree, CheckResultDiskFreeId>,
        JpaSpecificationExecutor<CheckResultDiskFree> {
    void deleteByIdAppliedTimeLessThan(DateTime appliedTime);
}
