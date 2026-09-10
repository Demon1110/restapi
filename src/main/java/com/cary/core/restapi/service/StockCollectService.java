package com.cary.core.restapi.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.PropertyComparator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StockCollectService {

    private final String stockCollectUrl = "https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/stock";
    private final String fileRoot = "/app/stock/";
    private static String last = "[]";

    //查看股票详情的网页：https://m.10jqka.com.cn/stockpage/hs_300093/#&atab=geguNews

    /**
     * stock_type=a&type=day&list_type=normal
     *
     * @param stock_type: 股票类型，a表示A股，b表示B股，c表示创业板，d表示科创板
     * @param type        股票类型，day表示24小时热度，hour:表示小时热度
     * @param list_type   列表类型，normal：大家都在看，skyrocket：新股热度榜 tech：技术交易派 value：价值投资派 trend：趋势投资派
     * @return 响应示例：{"status_code":0,"data":{"stock_list":[{"market":17,"code":"688836","rate":"13526222","rise_and_fall":0.8734,"name":"宇树科技","hot_rank_chg":0,"topic":null,"tag":{"concept_tag":["新股与次新股","注册制次新股"]},"order":1}]},"status_msg":"success"}
     */
    public JSONArray getTopStockList(String stock_type, String type, String list_type) {
        String url = StrUtil.format("{}?stock_type={}&type={}&list_type={}", stockCollectUrl, stock_type, type, list_type);
        System.out.println(url);
        String body = HttpUtil.get(url, 60_000);
        JSONObject json = JSONUtil.parseObj(body);
        JSONArray stockList = json.getByPath("data.stock_list", JSONArray.class);
        String path = DateUtil.date().toString("yyyyMMdd") + ".json";
        path = StrUtil.format("{}{}/{}/{}/{}", fileRoot, stock_type, type, list_type, path);
        try {
            FileUtil.writeUtf8String(stockList.toString(), path);
        } catch (IORuntimeException e) {
            log.error("写入文件失败", e);
        }
        return stockList;
    }


    public static void main(String[] args) {
        StockCollectService stockCollectService = new StockCollectService();
//        JSONArray topStockList = stockCollectService.getTopStockList("a", "day", "normal");
//        System.out.println(topStockList);
        Map<String, Object> map = stockCollectService.analyzeStockList("a", "day", "normal");
//        log.info("Map: {}", map);
        String text = stockCollectService.analyzeText(map);
        log.info("Text: {}", text);

        DingTalkService dingTalkService = new DingTalkService();

        String dingtalkWebhookUrl = "https://oapi.dingtalk.com/robot/send?access_token=3f7920b69fddec3389c3c71daf91b2128837bcf120d41a991f2210fffa5a2174";
        boolean result = dingTalkService.sendMdMsg(dingtalkWebhookUrl, "标题", text, null);
        System.out.println("DingTalk result: " + result);
    }

    /**
     * 获取上一次的股票列表
     *
     * @return
     */
    public String getLast(String stock_type, String type, String list_type) {
        if (last == null || last.isEmpty() || last.equals("[]")) {
            String path = DateUtil.offsetDay(DateUtil.date(), -1).toString("yyyyMMdd") + ".json";
            path = StrUtil.format("{}{}/{}/{}/{}", fileRoot, stock_type, type, list_type, path);
            try {
                log.info("读取文件：{}", path);
                last = FileUtil.readUtf8String(path);
            } catch (Exception e) {
                last = "[]";
            }
        }
        return last;
    }

    /**
     * 分析股票列表
     *
     * @param stock_type
     * @param type
     * @param list_type
     * @return
     */
    public Map<String, Object> analyzeStockList(String stock_type, String type, String list_type) {
        JSONArray topStockList = this.getTopStockList("a", "day", "normal");
        log.info("Top stock list: {}", topStockList.size());
        String last = this.getLast("a", "day", "normal");
        JSONArray lastList = JSONUtil.parseArray(last);
        log.info("Last stock list: {}", lastList.size());
        Map<String, Object> map = MapUtil.newHashMap();
        for (int i = 0; i < lastList.size(); i++) {
            JSONObject stock = lastList.getJSONObject(i);
            map.put(stock.getStr("code"), stock);
        }
        List<JSONObject> newStockList = new ArrayList<>();
        for (int i = 0; i < topStockList.size(); i++) {
            JSONObject stock = topStockList.getJSONObject(i);
            String code = stock.getStr("code");
            if (!map.containsKey(code)) {
                newStockList.add(stock);
            }
        }
        List<Object> hotRankChg = CollUtil.sort(topStockList, new PropertyComparator<>("hot_rank_chg").reversed());
        List<Object> hot10 = CollUtil.sub(hotRankChg, 0, 10);
        log.info("Hot 10 stock list: {}", hot10.size());
        Map<String, Object> resultMap = MapUtil.newHashMap();
        resultMap.put("newStockList", newStockList);
        resultMap.put("hot10", hot10);
        log.info("Result map: {}", resultMap);
        return resultMap;
    }

    /**
     * 分析结果
     * {"market":17,"code":"600869","rate":"5975581","rise_and_fall":-3.3293,"name":"远东股份","hot_rank_chg":0,"topic":null,"tag":{"concept_tag":["光纤概念","PET铜箔"],"popularity_tag":"2天1板"},"order":1}
     *
     * @param map
     * @return
     */
    public String analyzeText(Map<String, Object> map) {
        List<Object> hot10 = (List<Object>) map.get("hot10");
        List<Object> newStockList = (List<Object>) map.get("newStockList");
        StringBuilder text = new StringBuilder();
        text.append("### 今日新增股票:").append(newStockList.size()).append("只\n");
        Integer size = Convert.toInt(map.get("size"), 10);
        int index = 0;
        for (Object stock : newStockList) {
            JSONObject stockObj = (JSONObject) stock;
            Integer rate = stockObj.getInt("rate");
            BigDecimal decimal = NumberUtil.div(rate, BigDecimal.valueOf(10000));
            double v = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            text.append("#### **").append(stockObj.getStr("name")).append("**(").append(stockObj.getStr("code"))
                    .append(")")
                    .append(", 热度:").append(v).append("万\n");
            index++;
            if (index >= size) {
                break;
            }
        }
        text.append("### 今日热门股票:").append(hot10.size()).append("只\n");
        index = 0;
        for (Object stock : hot10) {
            JSONObject stockObj = (JSONObject) stock;
            Integer rate = stockObj.getInt("rate");
            BigDecimal decimal = NumberUtil.div(rate, BigDecimal.valueOf(10000));
            double v = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            text.append("##### **").append(stockObj.getStr("name")).append("**(").append(stockObj.getStr("code"))
                    .append(")")
                    .append(", 热度:").append(v).append("万, 变动:").append(stockObj.getStr("hot_rank_chg"))
                    .append("\n");
            index++;
            if (index >= size) {
                break;
            }
        }
        return text.toString();
    }
}
