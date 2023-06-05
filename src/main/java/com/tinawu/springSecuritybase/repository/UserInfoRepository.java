package com.tinawu.springSecuritybase.repository;

import com.tinawu.springSecuritybase.po.UserInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoPO, Integer> {
}
