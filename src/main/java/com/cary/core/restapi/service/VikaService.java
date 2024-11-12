package com.cary.core.restapi.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.noear.solon.annotation.Component;
import org.noear.solon.net.http.HttpUtils;

import java.io.IOException;
import java.util.Map;


@Component
public class VikaService {
    String serviceUrl = "https://api.vika.cn/fusion/v1";
    String token = "uskSenkuMRMoQs17QMHOtD8";
    String databasesId = "dstvpyxSRujo9BzNr1";

    /**
     * //PATCH /datasheets/dstvpyxSRujo9BzNr1/records
     *
     * @return
     * @throws IOException
     */
    public String updateField(String recordId, String fieldName, String fieldValue) throws IOException {
        String md5 = SecureUtil.md5(fieldValue);
        if (QingLongService.cache.containsKey(md5)) {
            return QingLongService.cache.get(md5);
        } else {
            QingLongService.cache.put(md5, "1");
        }
        System.out.println("updateField: " + fieldName + " = " + fieldValue);
        HttpUtils http = HttpUtils.http(StrUtil.format("{}/datasheets/{}/records", serviceUrl, databasesId));
        http.header("Authorization", "Bearer " + token);
        String body = "{\n" +
                "    \"records\": [\n" +
                "        {\n" +
                "            \"recordId\": \"" + recordId + "\",\n" +
                "            \"fields\": {\n" +
                "                \"多行文本\": \"" + fieldValue + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        http.bodyJson(body);
        return http.patch();
    }

    /**
     * //GET /datasheets/dstvpyxSRujo9BzNr1/records
     *
     * @return
     */
    public String getRecords(Map<String, Object> params) throws IOException {
//        String url = StrUtil.format("{}/datasheets/{}/records", serviceUrl, databasesId);
//        return HttpUtil.createGet(url).header("Authorization", "Bearer " + token).form(params).execute().body();
        String baseUrl = "https://api.vika.cn/fusion/v1/datasheets/dstvpyxSRujo9BzNr1/records?";
        String url = baseUrl + HttpUtils.toQueryString(params);
        HttpUtils http = HttpUtils.http(url);
        http.header("Authorization", "Bearer " + token);
        System.out.println(http);
        return http.get();
    }

}
