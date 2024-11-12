package com.cary.core.restapi.service;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.net.http.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VikaServiceTest {

    @Test
    public void testGet() throws IOException {
        VikaService vikaService = new VikaService();
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum","1");
        params.put("recordIds","recWShPpT20z8");
        String records = vikaService.getRecords(params);
        System.out.println(records);
        ONode oNode = ONode.load(records);
        Optional<ONode> optional = Optional.ofNullable(oNode.get("data")).map(data -> data.get("records")).map(r -> r.getOrNull(0)).map(d -> d.get("fields"));
        if (optional.isPresent()) {
            ONode data = optional.get();
            System.out.println(data.get("标题").toString());
        }
        ONode select = oNode.selectOrNew("$.data.records[0].fields.标题1");
        System.out.println(select.toString());
    }


    public static void main(String[] args) throws IOException {
        String body= HttpUtil.get("https://api.vika.cn/fusion/v1/datasheets/dstvpyxSRujo9BzNr1/records?pageSize=100&maxRecords=1000&pageNum=1&sort=&fields=&filterByFormula=&cellFormat=json&fieldKey=name");
        System.out.println(body);
        String s = HttpUtils.http("https://api.vika.cn/fusion/v1/datasheets/dstvpyxSRujo9BzNr1/records").header("Authorization", "Bearer uskSenkuMRMoQs17QMHOtD8").get();
        System.out.println(s);
    }
}