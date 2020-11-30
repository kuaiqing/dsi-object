package com.techstar.om.dasi.repos.info;

import com.techstar.om.dasi.jpa.info.CheckPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CheckPointRepos extends JpaRepository<CheckPoint, Long> {
    CheckPoint findFirstByTargetIdAndNameIgnoringCase(long target, String name);

    boolean existsByTargetIdAndNameIgnoringCase(long target, String name);

    List<CheckPoint> findByTargetId(long target);

    Page<CheckPoint> findByTargetId(long target, Pageable pageable);

    Page<CheckPoint> findByTargetIdAndNameLikeIgnoringCase(long target, String name, Pageable pageable);

    Page<CheckPoint> findByNameLikeIgnoringCase(String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update CheckPoint set disable=:disable where id=:id")
    void updateDisableById(long id, Boolean disable);

    void deleteByTargetId(long target);
}
