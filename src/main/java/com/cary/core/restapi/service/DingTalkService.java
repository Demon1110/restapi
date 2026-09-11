package com.cary.core.restapi.service;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;

@Component
@Slf4j
public class DingTalkService {
    public static final int DEFAULT_TIMEOUT = 60_000;

    /**
     * 发送钉钉消息
     *
     * @param webhookUrl 钉钉webhook地址
     * @param content    需要发送的消息
     * @return 发送结果
     */
    public boolean sendTextMsg(String webhookUrl, String content) {
        return sendTextMsg(webhookUrl, content, null);
    }

    /**
     * 发送钉钉消息
     *
     * @param webhookUrl 钉钉webhook地址
     * @param content    需要发送的消息
     * @param hookSecret 钉钉webhook密钥
     * @return 发送结果
     */
    public boolean sendTextMsg(String webhookUrl, String content, String hookSecret) {
        String hookUrl = webhookUrl;
        // 帮助文档地址：https://open.dingtalk.com/document/development/custom-robots-send-group-messages
        JSONObject body = new JSONObject();
        if (StrUtil.isNotBlank(hookSecret)) {
            String timestamp = System.currentTimeMillis() + "";
            String sign = getSignature(hookSecret, timestamp);
            hookUrl = hookUrl + "&timestamp=" + timestamp + "&sign=" + sign;
        }
        System.out.println("Webhook地址: " + hookUrl);
        body.put("msgtype", "text");
        body.put("text", new JSONObject().put("content", content));
        body.put("at", new JSONObject().put("isAtAll", true));
        String post = HttpUtil.post(hookUrl, body.toString(), DEFAULT_TIMEOUT);
        log.error("钉钉机器人发送成功，响应内容: {}", post);
        JSONObject response = JSONUtil.parseObj(post);
        if (response != null) {
            return response.getInt("errcode") == 0;
        }
        return false;
    }

    public boolean sendMdMsg(String webhookUrl, String title, String text, String hookSecret) {
        String hookUrl = webhookUrl;
        // 帮助文档地址：https://open.dingtalk.com/document/development/custom-robots-send-group-messages
        JSONObject body = new JSONObject();
        if (StrUtil.isNotBlank(hookSecret)) {
            String timestamp = System.currentTimeMillis() + "";
            String sign = getSignature(hookSecret, timestamp);
            hookUrl = hookUrl + "&timestamp=" + timestamp + "&sign=" + sign;
        }
        System.out.println("Webhook地址: " + hookUrl);
        body.put("msgtype", "markdown");
        body.put("markdown", new JSONObject().set("title", title).put("text", text));
        body.put("at", new JSONObject().put("isAtAll", true));
        String post = HttpUtil.post(hookUrl, body.toString(), DEFAULT_TIMEOUT);
        log.error("钉钉机器人发送成功，响应内容: {}", post);
        JSONObject response = JSONUtil.parseObj(post);
        if (response != null) {
            return response.getInt("errcode") == 0;
        }
        return false;
    }

    public static String getSignature(String secret, String timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            HMac hmac = DigestUtil.hmac(HmacAlgorithm.HmacSHA256, StrUtil.bytes(secret));
            String sign0 = hmac.digestBase64(stringToSign, false);
            return URLEncodeUtil.encode(sign0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
