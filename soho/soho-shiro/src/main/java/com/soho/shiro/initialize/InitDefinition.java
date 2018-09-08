package com.soho.shiro.initialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadow
 */
public class InitDefinition {

    private String loginUrl = "/login";
    private String successUrl = "/index";
    private String unauthorizedUrl = "/unauthorized";
    private List<RuleChain> anonRuleChains = new ArrayList<>();
    private List<RuleChain> roleRuleChains = new ArrayList<>();

    public InitDefinition() {

    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public List<RuleChain> getAnonRuleChains() {
        return anonRuleChains;
    }

    public void setAnonRuleChains(List<RuleChain> anonRuleChains) {
        this.anonRuleChains = anonRuleChains;
    }

    public List<RuleChain> getRoleRuleChains() {
        return roleRuleChains;
    }

    public void setRoleRuleChains(List<RuleChain> roleRuleChains) {
        this.roleRuleChains = roleRuleChains;
    }
}
