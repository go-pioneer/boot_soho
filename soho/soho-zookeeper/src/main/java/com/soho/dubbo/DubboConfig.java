package com.soho.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DubboConfig {

    @Value("${dubbo.version}")
    private String version;
    @Value("${dubbo.filter}")
    private String filter;
    @Value("${dubbo.retries}")
    private Integer retries;
    @Value("${dubbo.timeout}")
    private Integer timeout;
    @Value("${dubbo.check}")
    private Boolean check;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

}
