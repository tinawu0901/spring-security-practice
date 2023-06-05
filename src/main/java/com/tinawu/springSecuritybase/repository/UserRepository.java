package com.tinawu.springSecuritybase.repository;


import com.tinawu.springSecuritybase.po.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface    UserRepository extends JpaRepository<UserPO, Integer> {
    @Query(value = "SELECT COUNT(*) FROM UserPO user WHERE user.account =:account AND user.userInfoPO.status != :status ")
    int countByUserAccountAndStatusNot(@Param("account") final String account, @Param("status") final String status);

    @Query(value = "SELECT user FROM UserPO user WHERE user.userInfoPO.status != :status ")
    List<UserPO> findByStatusNot(@Param("status") final String status);

    @Query(value = "SELECT user FROM UserPO user WHERE user.account =:account AND user.userInfoPO.status != :status ")
    UserPO findByUserAccountAndStatusNot(@Param("account") final String account, @Param("status") final String status);
    
    @Query(value = "SELECT user FROM UserPO user WHERE user.userId =:userId AND user.userInfoPO.status != :status ")
    UserPO findByUserIdAndAndStatusNot(@Param("userId") final Integer userId, @Param("status") final String status);
}
