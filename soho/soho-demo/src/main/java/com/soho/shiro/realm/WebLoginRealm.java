package com.soho.shiro.realm;

public class WebLoginRealm {

   /* @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("username", token.getUsername());
        SessionUtils.doCreateNewSession(map, token.getPrincipal(), false);
        return new SimpleAuthenticationInfo(token.getUsername(), token.getCredentials(), getName());
    }*/

}