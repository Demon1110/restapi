package com.cary.core.restapi.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.noear.solon.annotation.Component;

@Component
public class StockCollectService {

    private final String stockCollectUrl = "https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/stock";

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
        return json.getByPath("data.stock_list", JSONArray.class);
    }


    public static void main(String[] args) {
        StockCollectService stockCollectService = new StockCollectService();
        JSONArray topStockList = stockCollectService.getTopStockList("a", "day", "normal");
        System.out.println(topStockList);
    }
}
