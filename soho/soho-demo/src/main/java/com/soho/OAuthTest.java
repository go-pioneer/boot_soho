package com.soho;

import com.soho.spring.utils.EMath;
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
            nvps.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/oauth2.0/callback"));
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
        String loginUrl = domain + "/oauth2.0/v1.0/userinfo";
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("access_token", token));
            nvps.add(new BasicNameValuePair("access_pbk", pbk));
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
        String loginUrl = domain + "/oauth2.0/v1.0/logout";
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("access_token", token));
            nvps.add(new BasicNameValuePair("access_pbk", pbk));
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

    private static String oauth2_refresh(String token, String pbk, String refresh_token) {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl = domain + "/oauth2.0/v1.0/refresh_token";
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("client_id", "c522f0c158d4c9d5be2f1032c38a8148"));
            nvps.add(new BasicNameValuePair("client_secret", "c522f0c158d4c9d5be2f1032c38a8148"));
            nvps.add(new BasicNameValuePair("refresh_token", refresh_token));
            nvps.add(new BasicNameValuePair("access_token", token));
            nvps.add(new BasicNameValuePair("access_pbk", pbk));
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

    private static String testapi() {
        HttpClient httpClient = new DefaultHttpClient();
        String loginUrl = "http://www.lianok.pro/mapi/mch/etzc";
        HttpPost httpPatch = new HttpPost(loginUrl);
        // httpPatch.setHeader("Content-type", "application/json");
        httpPatch.setHeader("Charset", HTTP.UTF_8);
        // httpPatch.setHeader("Accept", "application/json");
        httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("$time", String.valueOf(System.currentTimeMillis())));
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
        // localhost:8011/oauth2.0/v1.0/authorize?client_id=c522f0c158d4c9d5be2f1032c38a8148&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2.0%2Fcallback&state=1
        // http://119.23.23.55:7070/oauth2.0/authorize?client_id=c522f0c158d4c9d5be2f1032c38a8148&response_type=code&state=1&redirect_uri=http%3A%2F%2F119.23.23.55%3A7070%2Fcallback
        // http://localhost:8080/oauth2.0/token?client_id=c522f0c158d4c9d5be2f1032c38a8148&client_secret=ab58ba1691bbc45f3925e916d5cf01c7&grant_type=authorization_code&state=1&redirect_uri=http://localhost:8080/oauth2.0/callback&code=6ce74e7c1d9aeee1998f90b45143c8a1
//        String code = "150c4beb97d0a0ad10cce32a44b937a0";
//        String access_token = "5cffbf0a4de888c76e1a1fc9d5066740";
//        String access_pbk = "72c9f5ad12c6fb33495d09f8b3b62484";
//        String refresh_token = "b65e18d465c2e692dc51ac4e6f9194b1";
//        oauth2_token(code);
//        oauth2_userinfo(access_token, access_pbk);
//        oauth2_logout(access_token, access_pbk);
//        oauth2_refresh(access_token, access_pbk, refresh_token);
//        String uid = "123456";
//        String md5 = MD5Utils.MD5PBK(uid);
//        System.out.println(AESUtils.encrypt(uid)+"---"+uid+"---"+md5);
        // 1472a4c61c68561922fe2a5c6ca597fe---123456---10019a5e35b8ccc34a686479893016d8
//        System.out.println(AESUtils.decrypt("1472a4c61c68561922fe2a5c6ca597fe"));
//        testapi();
        System.out.println(EMath.D(1, 1.23));
        // System.out.println(String.format("2.1", "%.2f"));
        // System.out.println(new java.text.DecimalFormat("#").format(new BigDecimal(3.101546578978)));
    }

}
