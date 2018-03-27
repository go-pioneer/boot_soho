package com.soho.shiro.realm;

import com.soho.spring.shiro.utils.SessionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.Map;

public class WebLoginRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("username", token.getUsername());
        SessionUtils.setUser(map);
        return new SimpleAuthenticationInfo(token.getUsername(), token.getCredentials(), getName());
    }

}