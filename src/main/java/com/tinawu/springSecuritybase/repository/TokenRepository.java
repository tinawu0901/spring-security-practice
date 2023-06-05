package com.tinawu.springSecuritybase.repository;




import com.tinawu.springSecuritybase.po.TokenPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<TokenPO, String> {
    List<TokenPO> findByUserId(final Integer userId);
}
