package com.techstar.om.dasi.repos.info;

import com.techstar.om.dasi.jpa.info.CheckTargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckTargetGroupRepos extends JpaRepository<CheckTargetGroup, Long> {
    CheckTargetGroup findFirstByCodeIgnoringCase(String code);
}
