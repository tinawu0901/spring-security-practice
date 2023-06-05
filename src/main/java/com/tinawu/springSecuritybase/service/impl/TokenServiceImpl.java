package com.tinawu.springSecuritybase.service.impl;


import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.po.TokenPO;
import com.tinawu.springSecuritybase.repository.TokenRepository;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {
    private Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
    @Value("${authpro.signingkey}")
    private String authproSigningKey;
    @Value("${token.max.effective.minutes}")
    private int tokenMaxEffectiveMinutes;
    @Value("${token.extend.minutes}")
    private int tokenExtendMinutes;
    @Autowired
    private TokenRepository tokenRepository;

    @Override//產生token
    public String generateToken(final Integer userId, final String userName, final Timestamp createTime, final Timestamp expirationTime) {
        logger.info("[generateToken][start] userId : " + userId);
        final Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId);
        claims.put("name", userName);
        claims.put("created", createTime.getTime());
        //產生token 中間儲存 使用者資訊，用HS512加密
        final String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, authproSigningKey).compact();
        // 儲存token
        final TokenPO tokenPO = new TokenPO();
        tokenPO.setToken(token);
        tokenPO.setUserId(userId);
        tokenPO.setExpirationTime(expirationTime);
        tokenRepository.save(tokenPO);
        logger.info("[generateToken][end] userId : " + userId);
        return token;
    }

    @Override
    public Claims getClaimsFromToken(final String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(authproSigningKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    @Override
    public Integer getUserId(final String token) {
        final Claims claims = this.getClaimsFromToken(token);//retunr Calim

        final String userId = claims.getSubject();

        return Integer.valueOf(userId);
    }

    @Override
    public void deleteToken(final Integer userId) {
        final List<TokenPO> tokenPOList = tokenRepository.findByUserId(userId);
        tokenRepository.deleteAll(tokenPOList);
    }

    @Override
    public void validateToken(String token) throws MessageException {
        if (!ObjectUtils.isEmpty(token)) {
            final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
            final Optional<TokenPO> tokenOptional = tokenRepository.findById(token);
            if (tokenOptional.isPresent()) {
                final TokenPO tokenPO = tokenOptional.get();
                if (systemCurrentTime.after(tokenPO.getExpirationTime())) {//token過期
                    logger.info("驗證TOKEN失敗，TOKEN已過期");
                    throw new MessageException("401", "Please login again.");
                }
            } else {//token不存在
                logger.info("驗證TOKEN失敗，TOKEN不存在");
                throw new MessageException("401", "Please login again.");
            }
        } else {
            logger.info("驗證TOKEN失敗，TOKEN為空");
            throw new MessageException("401", "Please login again.");
        }
    }

    @Override
    public void extendTokenExpirationTime(final String token) {
        final Optional<TokenPO> tokenOptional = tokenRepository.findById(token);
        if (tokenOptional.isPresent()) {
            final TokenPO tokenPO = tokenOptional.get();
            final Timestamp expirationTime = tokenPO.getExpirationTime();
            final Timestamp tokenCreateTime = this.getTokenCreateTime(token);
            if (expirationTime.before(DateUtil.addMinute(tokenCreateTime, tokenMaxEffectiveMinutes))) {
                tokenPO.setExpirationTime(DateUtil.addMinute(expirationTime, tokenExtendMinutes));
                tokenRepository.save(tokenPO);
            }

        }
    }

    private Timestamp getTokenCreateTime(final String token) {
        final Claims claims = this.getClaimsFromToken(token);
        final Timestamp createTime = new Timestamp((Long) claims.get("created"));
        return createTime;
    }
}