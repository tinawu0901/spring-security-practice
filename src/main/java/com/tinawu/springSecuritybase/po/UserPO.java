package com.tinawu.springSecuritybase.po;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auth_user")
public class UserPO implements Serializable {
	private static final long serialVersionUID = 7852184173311421434L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "account")
	private String account;
	@Column(name = "password")//加密
	private String password;
	@Column(name = "pwd_expiration")
	private Timestamp pwdExpiration;
	@Column(name = "wrong_pwd_count")
	private int wrongPwdCount;//密碼錯誤次數
	@Column(name = "last_login")
	private Timestamp lastLogin;//最後一次登入時間
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//FetchType.EAGER在查詢時立刻載入關聯的物件。 都有設定cascade為CascadeType.ALL，這表示儲存其中一方實例時，若有參考至另一方實例，
	@JoinColumn(name = "user_id")
	private UserInfoPO userInfoPO;//使用者詳細資訊
//	@OneToMany(fetch = FetchType.LAZY)//FetchType.LAZY只在用到時才載入關聯的物件
//	@JoinColumn(name = "user_id")
//	private List<PwdChangeHisPO> pwdChangeHisPOList;
//	@ManyToMany(fetch = FetchType.LAZY)//角色
//	@JoinTable(name = "auth_role_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//	private Set<RolePO> rolePOSet;
//	@ManyToMany(fetch = FetchType.LAZY)//群組
//	@JoinTable(name = "auth_group_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
//	private Set<GroupPO> groupPOSet;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getPwdExpiration() {
		return pwdExpiration;
	}

	public void setPwdExpiration(Timestamp pwdExpiration) {
		this.pwdExpiration = pwdExpiration;
	}

	public int getWrongPwdCount() {
		return wrongPwdCount;
	}

	public void setWrongPwdCount(int wrongPwdCount) {
		this.wrongPwdCount = wrongPwdCount;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public UserInfoPO getUserInfoPO() {
		return userInfoPO;
	}

	public void setUserInfoPO(UserInfoPO userInfoPO) {
		this.userInfoPO = userInfoPO;
	}

//	public List<PwdChangeHisPO> getPwdChangeHisPOList() {
//		return pwdChangeHisPOList;
//	}
//
//	public void setPwdChangeHisPOList(List<PwdChangeHisPO> pwdChangeHisPOList) {
//		this.pwdChangeHisPOList = pwdChangeHisPOList;
//	}
//
//	public Set<RolePO> getRolePOSet() {
//		return rolePOSet;
//	}
//
//	public void setRolePOSet(Set<RolePO> rolePOSet) {
//		this.rolePOSet = rolePOSet;
//	}
//
//	public Set<GroupPO> getGroupPOSet() {
//		return groupPOSet;
//	}
//
//	public void setGroupPOSet(Set<GroupPO> groupPOSet) {
//		this.groupPOSet = groupPOSet;
//	}

}
