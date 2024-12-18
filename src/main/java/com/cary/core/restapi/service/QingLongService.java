package com.cary.core.restapi.service;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Component;
import org.noear.solon.net.http.HttpUtils;

import java.io.IOException;

@Component
public class QingLongService {
    private final String serverUrl = "http://192.168.195.133:5700/open";
    private final String client_id = "5XxMo4QSC_d9";
    private final String client_secret = "7-K_iv-_qMZqnvmlXzLb0BOq";
    public static Cache<String, String> cache = CacheUtil.newFIFOCache(10, 1000 * 60 * 60 * 24);

    public String login() throws IOException {
        return cache.get("token", () -> {
            String url = serverUrl + "/auth/token?client_id=" + client_id + "&client_secret=" + client_secret;
            String content = HttpUtils.http(url).get();
            ONode load = ONode.loadStr(content);
//            System.out.println(load.select("$.data"));
            return load.select("$.data.token").getString();
        });
    }

    public String updateEnv(String key, String value) throws IOException {
        String md5 = SecureUtil.md5(value);
        if (cache.containsKey(md5)) {
            return cache.get(md5);
        } else {
            cache.put(md5, "1");
        }
        String url = serverUrl + "/envs";
        ONode body = ONode.newObject();
        body.set("id", 1);
        body.set("name", key);
        body.set("value", value);
        body.set("remarks", "server env update" + DateUtil.now());
        System.out.println("更新环境变量：" + body.toJson());
        String content = HttpUtils.http(url).header("Authorization", "Bearer " + login()).bodyJson(body.toJson()).put();
        return content;
    }

    public static void main(String[] args) throws IOException {
        QingLongService service = new QingLongService();
        try {
            String token = service.login();
            System.out.println(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
