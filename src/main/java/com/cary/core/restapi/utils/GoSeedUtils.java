package com.cary.core.restapi.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class GoSeedUtils {
    public static final String GO_SEED_URL = "http://127.0.0.1:9999/api/v1/";
    public static final String GO_SEED_TOKEN = "";


    public static void main(String[] args) {
        JSONObject resolve = createTask("http://192.168.112.205:8086/static/64769d34/images/title.svg");
        System.out.println(resolve);
    }

    /**
     * Resolve a request
     *
     * @param url
     * @return
     */
    public static JSONObject resolve(String url) {
        Map<String, Object> params = params();
        Map<String, Object> req = new HashMap<>();
        req.put("url", url);
        params.put("req", req);
        String methodUrl = StrUtil.format("{}resolve", GO_SEED_URL);
        String body = JSONUtil.toJsonStr(params);
        String post = HttpUtil.createPost(methodUrl).header("X-Api-Token", GO_SEED_TOKEN).body(body).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(post);
        if (jsonObject != null && jsonObject.get("code") != null && jsonObject.getInt("code") == 0) {
            return jsonObject.getJSONObject("data");
        }
        return null;
    }

    /**
     * create a task
     *
     * @param taskUrl
     * @return
     */
    public static JSONObject createTask(String taskUrl) {
        JSONObject resolve = resolve(taskUrl);
        if (resolve == null) {
            return null;
        }
        String rid = resolve.getStr("id");
        if (StrUtil.isEmpty(rid)) {
            return null;
        }
        Map<String, Object> params = params();
        params.put("rid", rid);
        Map<String, Object> opts = new HashMap<>();
        opts.put("path", "C:\\Users\\admin\\Downloads\\wrappers");
        params.put("opts", opts);
        String methodUrl = StrUtil.format("{}tasks", GO_SEED_URL);
        String body = JSONUtil.toJsonStr(params);
        String post = HttpUtil.createPost(methodUrl).header("X-Api-Token", GO_SEED_TOKEN).body(body).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(post);
        return jsonObject;
    }


    public static Map<String, Object> params() {
        Map<String, Object> params = new HashMap<>();
        return params;
    }
}
