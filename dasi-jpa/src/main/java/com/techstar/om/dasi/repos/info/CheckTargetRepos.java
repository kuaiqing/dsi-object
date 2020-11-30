package com.techstar.om.dasi.repos.info;

import com.techstar.om.dasi.jpa.info.CheckTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CheckTargetRepos extends JpaRepository<CheckTarget, Long> {
    boolean existsByNameIgnoringCase(String name);

    CheckTarget findFirstByNameIgnoringCase(String name);

    List<CheckTarget> findByGroupId(long group);

    Page<CheckTarget> findByGroupId(long group, Pageable pageable);

    Page<CheckTarget> findByNameLikeIgnoringCase(String name, Pageable pageable);

    Page<CheckTarget> findByGroupIdAndNameLikeIgnoringCase(long group, String name, Pageable pageable);

    boolean existsByGroupId(long group);

    @Transactional
    @Modifying
    @Query(value = "update CheckTarget set disable=:disable where id=:id")
    void updateDisableById(long id, Boolean disable);

}
