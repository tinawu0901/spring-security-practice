package com.tinawu.springSecuritybase.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "auth_token")
public class TokenPO implements Serializable {
    private static final long serialVersionUID = -463617152839237139L;
    @Id
    @Column(name = "token")
    private String token;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "expiration_time")
    private Timestamp expirationTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }
}
