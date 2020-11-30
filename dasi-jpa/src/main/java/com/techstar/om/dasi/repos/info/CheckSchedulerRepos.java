package com.techstar.om.dasi.repos.info;

import com.techstar.om.dasi.jpa.info.CheckScheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckSchedulerRepos extends JpaRepository<CheckScheduler, Long> {
    CheckScheduler findFirstByNameIgnoringCase(String name);
}
