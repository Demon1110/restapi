package com.cary.core.restapi.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import com.cary.core.restapi.service.StockCollectService;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Result;

import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@Mapping("/stock")
public class StockController {

    @Inject
    StockCollectService stockCollectService;

    /**
     * 获取股票列表
     *
     * @param body
     * @return
     * @throws IOException
     */
    @Mapping("/list")
    @Post
    public Result list(@Body Map<String, Object> body) throws IOException {
        String stockType = Convert.toStr(body.get("stock_type"), "a");
        String type = Convert.toStr(body.get("type"), "day");
        String listType = Convert.toStr(body.get("list_type"), "normal");
        JSONArray topStockList = stockCollectService.getTopStockList(stockType, type, listType);
        System.out.println(topStockList);
        return Result.succeed(topStockList);
    }

    /**
     * 获取上一次收集的数据
     *
     * @param body
     * @return
     * @throws IOException
     */
    @Mapping("/last")
    @Post
    public Result last(@Body Map<String, Object> body) throws IOException {
        String stockType = Convert.toStr(body.get("stock_type"), "a");
        String type = Convert.toStr(body.get("type"), "day");
        String listType = Convert.toStr(body.get("list_type"), "normal");
        String last = stockCollectService.getLast(stockType, type, listType);
        return Result.succeed(last, "收集成功");
    }

    /**
     * 获取对比结果
     *
     * @param body
     * @return
     * @throws IOException
     */
    @Mapping("/result")
    @Post
    public Result result(@Body Map<String, Object> body) throws IOException {
        String stockType = Convert.toStr(body.get("stock_type"), "a");
        String type = Convert.toStr(body.get("type"), "day");
        String listType = Convert.toStr(body.get("list_type"), "normal");
        Map<String, Object> map = stockCollectService.analyzeStockList(stockType, type, listType);
        String text = stockCollectService.analyzeText(map);
        log.info("Text: {}", text);
        return Result.succeed(text, "收集成功");
    }

}