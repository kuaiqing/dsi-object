package com.techstar.om.dasi.repos.result;

import com.techstar.om.dasi.jpa.result.CheckResultRecordNumber;
import com.techstar.om.dasi.jpa.result.CheckResultRecordNumberId;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CheckResultRecordNumberRepos extends JpaRepository<CheckResultRecordNumber, CheckResultRecordNumberId>,
        JpaSpecificationExecutor<CheckResultRecordNumber> {
    void deleteByIdAppliedTimeLessThan(DateTime appliedTime);

    CheckResultRecordNumber findFirstByIdPointAndIdAppliedTimeLessThan(long point, DateTime appliedTime);

    List<CheckResultRecordNumber> findByIdPointAndIdAppliedTime(long point, DateTime appliedTime);
}
