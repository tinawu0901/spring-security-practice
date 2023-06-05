package com.tinawu.springSecuritybase.service;



import com.tinawu.springSecuritybase.exception.MessageException;
import io.jsonwebtoken.Claims;

import java.sql.Timestamp;

public interface TokenService {

    String generateToken(final Integer userId, final String userName, final Timestamp createTime, final Timestamp expirationTime);

    Claims getClaimsFromToken(final String token);

    Integer getUserId(final String token);

    void deleteToken(final Integer userId);

    void validateToken(final String token) throws MessageException;

    void extendTokenExpirationTime(final String token);
}
