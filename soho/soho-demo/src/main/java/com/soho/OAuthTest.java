package com.soho;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadow on 2017/4/17.
 */
public class OAuthTest {

    private static String oauth2_token(String code) {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl = domain + "/oauth2.0/v1.0/access_token";
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("client_id", "c522f0c158d4c9d5be2f1032c38a8148"));
            nvps.add(new BasicNameValuePair("client_secret", "c522f0c158d4c9d5be2f1032c38a8148"));
            nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nvps.add(new BasicNameValuePair("code", code));
            nvps.add(new BasicNameValuePair("redirect_uri", domain + "/callback"));
            httpPatch.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = httpClient.execute(httpPatch);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("请求结果: " + result);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String oauth2_userinfo(String token, String pbk) {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl = domain + "/oauth2.0/userinfo?access_token=" + token + "&access_pbk=" + pbk;
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            httpPatch.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = httpClient.execute(httpPatch);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("请求结果: " + result);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String oauth2_logout(String token, String pbk) {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl = domain + "/oauth2.0/logout?access_token=" + token + "&access_pbk=" + pbk;
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            httpPatch.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = httpClient.execute(httpPatch);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("请求结果: " + result);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    static String domain = "http://localhost:8011";
//    static String domain = "http://119.23.23.55:7070";
//    static String domain = "http://pos.linkworld-group.com";

    public static void main(String[] args) throws Exception {
        // http://localhost:8080/oauth2.0/authorize?client_id=c522f0c158d4c9d5be2f1032c38a8148&response_type=code&state=1&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2.0%2Fcallback
        // http://119.23.23.55:7070/oauth2.0/authorize?client_id=c522f0c158d4c9d5be2f1032c38a8148&response_type=code&state=1&redirect_uri=http%3A%2F%2F119.23.23.55%3A7070%2Fcallback
        // http://localhost:8080/oauth2.0/token?client_id=c522f0c158d4c9d5be2f1032c38a8148&client_secret=ab58ba1691bbc45f3925e916d5cf01c7&grant_type=authorization_code&state=1&redirect_uri=http://localhost:8080/oauth2.0/callback&code=6ce74e7c1d9aeee1998f90b45143c8a1
        oauth2_token("27411874309d72e780d0b624ee81ac8a");
//        oauth2_userinfo("a4865dada449c89e53810e67b15a550c", "e13931f2eccee4f20efb2d928f6105e5");
//        oauth2_logout("a4865dada449c89e53810e67b15a550c", "e13931f2eccee4f20efb2d928f6105e5");
        // buildOrder();
        //        order_query("12018010215361280");
        //        order_callback("120180106090627218");
    }

}
