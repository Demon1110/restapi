package com.cary.core.restapi.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexinUtil {

    public static void main(String[] args) {

        String loginName = "15330213553";
        String password = "123456aa";
        JSONObject loginData = login(loginName, password);
        String accessToken = loginData.getStr("accessToken");
        String userId = loginData.getStr("userId");
        System.out.println(accessToken);
        int stepNum = 78965;
        uploadStep(accessToken, userId, stepNum);
    }

    public static JSONObject login(String loginName, String password) {
        String loginUrl = "https://sports.lifesense.com/sessions_service/login?systemType=2&version=4.6.7";
        Map<String, Object> data = new HashMap<>();
        data.put("loginName", loginName);
        data.put("password", SecureUtil.md5(password));
        data.put("clientId", "49a41c9727ee49dda3b190dc907850cc");
        data.put("roleType", Integer.valueOf(0));
        data.put("appType", Integer.valueOf(6));
        HttpRequest httpRequest = HttpUtil.createPost(loginUrl);
        httpRequest.header("Content-Type", "application/json; charset=utf-8");
        httpRequest.header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; LIO-AN00 Build/LIO-AN00)");
        String body = JSONUtil.toJsonStr(data);
        System.out.println(body);
        httpRequest.body(body);
        String post = httpRequest.execute().body();
        System.out.println(post);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        JSONObject object = jsonObject.getJSONObject("data");
        return object;
    }

    public static String uploadStep(String accessToken, String userId, int stepNum) {
        String stepUrl = "https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2?systemType=2&version=4.6.7";
        Map<String, Object> data2 = new HashMap<>();
        List list = new ArrayList();
        Map<String, Object> step2 = new HashMap<>();
        step2.put("DataSource", Integer.valueOf(2));
        step2.put("active", Integer.valueOf(1));
        step2.put("calories", Integer.valueOf(stepNum / 4));
        step2.put("dataSource", Integer.valueOf(2));
        step2.put("deviceId", "M_NULL");
        step2.put("distance", Integer.valueOf(stepNum / 3));
        step2.put("exerciseTime", Integer.valueOf(0));
        step2.put("isUpload", Integer.valueOf(0));
        step2.put("measurementTime", DateUtil.now());
        step2.put("priority", Integer.valueOf(0));
        step2.put("step", Integer.valueOf(stepNum));
        step2.put("type", Integer.valueOf(2));
        step2.put("updated", DateUtil.current());
        step2.put("userId", userId);
        list.add(step2);
        data2.put("list", list);

        HttpRequest httpRequest2 = HttpUtil.createPost(stepUrl);
        httpRequest2.header("Content-Type", "application/json; charset=utf-8");
        httpRequest2.header("User-Agent", StrUtil.format("accessToken={}", accessToken));
        System.out.println(JSONUtil.toJsonStr(data2));
        httpRequest2.body(JSONUtil.toJsonStr(data2));
        String post2 = httpRequest2.execute().body();
        System.out.println(post2);
        return post2;
    }

//    public String mainHandler(JSONObject event, Context context) {
//        System.out.println("event=" + event.toStringPretty());
//        String message = event.getStr("Message");
//        JSONObject msgJson = null;
//        if (StrUtil.isNotBlank(message)) {
//            try {
//                msgJson = JSONUtil.parseObj(message);
//            } catch (Exception e) {
//            }
//        }
//        int max = 3000;
//        if (msgJson != null) {
//            max = Convert.toInt(msgJson.getStr("max", "3000"));
//            System.out.println("max = " + max);
//        }
//        String loginUrl = "https://sports.lifesense.com/sessions_service/login?systemType=2&version=4.6.7";
//        String loginName = "15330213553";
//        String password = "123456aa";
//        Map<String, Object> data = new HashMap<>();
//        data.put("loginName", loginName);
//        data.put("password", SecureUtil.md5(password));
//        data.put("clientId", "49a41c9727ee49dda3b190dc907850cc");
//        data.put("roleType", Integer.valueOf(0));
//        data.put("appType", Integer.valueOf(6));
//        HttpRequest httpRequest = HttpUtil.createPost(loginUrl);
//        httpRequest.header("Content-Type", "application/json; charset=utf-8");
//        httpRequest.header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; LIO-AN00 Build/LIO-AN00)");
//        String body = JSONUtil.toJsonStr(data);
//        System.out.println(body);
//        httpRequest.body(body);
//        String post = httpRequest.execute().body();
//        System.out.println(post);
//        JSONObject jsonObject = JSONUtil.parseObj(post);
//        JSONObject object = jsonObject.getJSONObject("data");
//        String accessToken = object.getStr("accessToken");
//        String userId = object.getStr("userId");
//
//        String stepUrl = "https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2?systemType=2&version=4.6.7";
//        int stepNum = RandomUtil.randomInt(1000, max);
//        stepNum *= DateUtil.thisHour(true);
//        Map<String, Object> data2 = new HashMap<>();
//        List list = new ArrayList();
//        Map<String, Object> step2 = new HashMap<>();
//        step2.put("DataSource", Integer.valueOf(2));
//        step2.put("active", Integer.valueOf(1));
//        step2.put("calories", Integer.valueOf(stepNum / 4));
//        step2.put("dataSource", Integer.valueOf(2));
//        step2.put("deviceId", "M_NULL");
//        step2.put("distance", Integer.valueOf(stepNum / 3));
//        step2.put("exerciseTime", Integer.valueOf(0));
//        step2.put("isUpload", Integer.valueOf(0));
//        step2.put("measurementTime", DateUtil.now());
//        step2.put("priority", Integer.valueOf(0));
//        step2.put("step", Integer.valueOf(stepNum));
//        step2.put("type", Integer.valueOf(2));
//        step2.put("updated", Long.valueOf(DateUtil.current(true)));
//        step2.put("userId", userId);
//        list.add(step2);
//        data2.put("list", list);
//
//        HttpRequest httpRequest2 = HttpUtil.createPost(stepUrl);
//        httpRequest2.header("Content-Type", "application/json; charset=utf-8");
//        httpRequest2.header("User-Agent", StrUtil.format("accessToken={}", new Object[]{accessToken}));
//        System.out.println(JSONUtil.toJsonStr(data2));
//        httpRequest2.body(JSONUtil.toJsonStr(data2));
//        String post2 = httpRequest2.execute().body();
//        System.out.println(post2);
//        return post2;
//    }
}
