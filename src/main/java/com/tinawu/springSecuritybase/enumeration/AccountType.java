package com.tinawu.springSecuritybase.enumeration;

public enum AccountType {
    LOCAL("local"),
    SIMPLE_LDAP("simpleLDAP"),
    ADVANCE_LDAP("advanceLDAP"),
    GOOGLE_SSO("google"),
    SSO("sso");
    private String type;

    AccountType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
