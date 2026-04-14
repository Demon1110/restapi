package com.cary.core.restapi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class WallpaperCollectUtils {
    static Map<String, String> lastCookies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String url = StrUtil.format("https://hdqwalls.com/category/artist-wallpapers/3840x2160/page/{}", "1");
        System.out.println(getHtml(url));
    }

    public static String getUrlWithParams(String url, Map<String, ?> paramMap) {
        String params = URLUtil.buildQuery(paramMap, Charset.defaultCharset());
        return url + "?" + params;
    }

    public static Map<String, String> getCookies() throws IOException {
        Map<String, String> cookies = new HashMap<>();
        if (CollUtil.isEmpty(lastCookies)) {
            cookies.put("hdqwalls_csrf", "58b1b0737a92930cdccc0827a7bf4ecf");
        } else {
            cookies.clear();
            cookies.putAll(lastCookies);
        }
        return cookies;
    }


    public static String getHtml(String url) throws IOException {

        Connection connection = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Referer", "https://hdqwalls.com/category/artist-wallpapers/3840x2160/page/1")
                .header("Accept-Encoding", "gzip, deflate, sdch, br")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
//                .header("cookie", "hdqwalls_csrf=58b1b0737a92930cdccc0827a7bf4ecf; hdqwalls_theme=light; cf_clearance=j2aOzozA9Ts4xHCBf_W.HWZ8jjYOI6179YyOjteIk3g-1773975571-1.2.1.1-jJHdS7T4rbAQ6J2P4UL2xqvdohUW3XdsdxJK3nycJxFZC4VJlPYvAvgIE23YnkLtEC8QNz98A.eJ0UYHt80GgNuxZoNmQv2MYPP6l5PL2fmO7fqHRXZ6Ca1qrwgv.vO0NDcwS73JYDVePHZEH6IHwE.H99i8xboaLlTNX5tp7GK3OkcRtTa5thu2SeJDqaiz9Cqfy2M3xKqTKj.c928S7oiXGUC57KgkssdWIprF00I; hdqwalls_random=beb37c21600f0b14b0f948dbc11388095589971780e524c77400d714544feaba631a1339665d73468ca2450130a3fed79ddff36da61bb4a1f912aad6962ee3e619ciemfkpIaYbLgub%2BfCuu4eb%2FQYdJv8kybctY%2F3Ifs%3D")
                .timeout(30000);
        connection.cookies(getCookies());
        Connection.Response execute = connection.execute();
        Map<String, String> newCookies = execute.cookies();
        lastCookies.clear();
        lastCookies.putAll(newCookies);
        System.out.println(execute);
        System.out.println(execute.statusCode());
        Document document = connection
                .get();
        return document.html();
    }
}
