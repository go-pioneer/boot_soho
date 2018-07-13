package com.soho.demo.controller;

import com.soho.demo.domain.OauthClient;
import com.soho.demo.domain.OauthUser;
import com.soho.spring.model.ReqData;

/**
 * Created by Administrator on 2018/7/13.
 */
public class DogVO extends ReqData<OauthUser, OauthUser> {

    private OauthClient client;
    private String smscode;

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public OauthClient getClient() {
        return client;
    }

    public void setClient(OauthClient client) {
        this.client = client;
    }

}
