package com.cary.core.restapi.service;

import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.net.http.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        ONode select = oNode.selectOrNew("$.data.records[0].fields.标题");
        System.out.println(select.getString());
    }

    @Test
    public void oNodeTest() {
        ONode oNode = ONode.load("{\"code\":200,\"success\":true,\"data\":{\"total\":1,\"records\":[{\"recordId\":\"recWShPpT20z8\",\"createdAt\":1731074654000,\"updatedAt\":1731461102000,\"fields\":{\"标题\":\"jd_cookies\",\"创建时间\":1731074654447,\"嘎嘎\":\"进行中\",\"多行文本\":\"pt_key=app_openAAJnM_jwADA8hOYdSWHUDrsV11PDcuPJusdVhMnsypmJe68Tzy3Qho8OkAlTqHPU8LFG7XZ6_wc; pt_pin=jd_71d601b4db7e4; pwdt_id=jd_71d601b4db7e4; shshshfpb=BApXSwiEWIPZDKRaXGusWW1Tqksir0xgH27HO3HtX9xJ1ItZfQtGHlky73SmuN4QkduFLoamCsg; __jda=122270672.1731124813311914621542.1731124813.1731414014.1731414445.11; __jdv=122270672%7Cjdzt_refer_null%7Ct_108549027_1%7Cjzt-zhitou%7C_2_app_0_urmrwrxvr2clc02-p_0%7C1731321372000; mba_muid=1731124813311914621542.471.1731414941182; unpl=JF8EAJtnNSttW01QBhgFHhYYGFhTW14ISR8BbTUMVlkMG1BVHgMdFRF7XlVdWBRLFx9uZxRXXFNPUg4YBisiE0xeUllbCk8UMzQkQBEIGgo6Ax4BGBoVSF5cV18OSRIAbWcBXUFeTlcFHwYeFhhLWlBWWQxMHgtuVzVXXWhLVTUrAysVIHttXVtUCU4WM25XBGQfDBdXDBkGGhFde1xkXQ%7CJF8EAJdnNSttW0lXUUtVGEFEHF5QWw0OSEQLZmEEVA1ZGQQFH1AcQER7XlVdWBRLFh9vYBRXX1NJVQ4eAisiE0pfUVheAEgeBF9kB1xVXEtSDCsCHhB-TlVXWlsNTREHAWcFOjoAHgBbdQd1ERJDVVBeWwF7FANfZjVkXGhCVTUbAxoTE0hdVltVAU4nM25XBGRcaAA6BFYyGiIQ; 3AB9D23F7A4B3C9B=IBOXXNWA7XT4DL63WZZQBOOWEDO6XT5LCI4Q7FRJVBKXO2WJY2BZJVEULTSQDZ7WLLKDHTWBLIOM6D7BT2UCBJIH4A; 3AB9D23F7A4B3CSS=jdd03IBOXXNWA7XT4DL63WZZQBOOWEDO6XT5LCI4Q7FRJVBKXO2WJY2BZJVEULTSQDZ7WLLKDHTWBLIOM6D7BT2UCBJIH4AAAAAMTEBPESCQAAAAACZGPSBWNTSNBTYX; shshshfpa=f0f3aa6f-1543-fd6b-6444-939b4d07c7b3-1678163076; ipLoc-djd=21_1827_40847_0; wxa_level=1; SameSite=Strict; qid_fs=1731410314461; qid_ls=1731410314461; qid_ts=1731410314465; qid_uid=7a510675-d4ac-40ee-8ce2-1d4b62d43ca6; qid_vis=1; joyya=1731391352.1731392899.35.0l1dbqv; shshshfpv=JD0111d47d6lBS6x8U87173139107319504LJJYorv2n8AySbD75Pu3HJrFMac1DLaceUT4KgEAyicHr-urXyIEO3joN02pXrKC9A5-s2XtP82lVmfwvDmogZLefmSJx8nBa55WuVeeegs0b8x2fm~BApXSNLr7HfZDKRaXGusWW1Tqksir0xgH27HO3HtX9xJ1ItZfQtGHlky73SmuN4QkduFLoamCsg; sid=; cid=8; retina=1; visitkey=6077266909477459496; webp=1; shshshfpx=f0f3aa6f-1543-fd6b-6444-939b4d07c7b3-1678163076\",\"修改时间\":1731461102612}}],\"pageNum\":1,\"pageSize\":1},\"message\":\"SUCCESS\"}");
        ONode select = oNode.select("$.data.records[0].fields.标题");
        System.out.println(select.toString());
        System.out.println(select.getString());
    }


    public static void main(String[] args) throws IOException {
//        String body= HttpUtil.get("https://api.vika.cn/fusion/v1/datasheets/dstvpyxSRujo9BzNr1/records?pageSize=100&maxRecords=1000&pageNum=1&sort=&fields=&filterByFormula=&cellFormat=json&fieldKey=name");
//        System.out.println(body);
        String s = HttpUtils.http("https://api.vika.cn/fusion/v1/datasheets/dstvpyxSRujo9BzNr1/records").header("Authorization", "Bearer uskSenkuMRMoQs17QMHOtD8").get();
        System.out.println(s);
    }
}