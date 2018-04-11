package com.soho.spring.shiro.initialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadow
 */
public class InitDefinition {

    private String loginUrl = "/login";
    private String successUrl = "/index";
    private String unauthorizedUrl = "/unauthorized";
    private List<RuleChain> ruleChains = new ArrayList<>();

    public InitDefinition() {

    }

    public InitDefinition(List<RuleChain> ruleChains) {
        this.ruleChains = ruleChains;
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

    public List<RuleChain> getRuleChains() {
        return ruleChains;
    }

    public void setRuleChains(List<RuleChain> ruleChains) {
        this.ruleChains = ruleChains;
    }

}
