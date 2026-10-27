package com.cary.core.restapi.task;

import cn.hutool.core.date.DateUtil;
import com.cary.core.restapi.service.DingTalkService;
import com.cary.core.restapi.service.StockCollectService;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class StockCollectJob {

    @Inject
    StockCollectService stockCollectService;
    @Inject
    DingTalkService dingTalkService;

    @Scheduled(cron = "0 0 9,12,21 * * ?")
    public void execute() throws IOException {
        log.info("TaskJob execute");
        Map<String, Object> map = stockCollectService.analyzeStockList("a", "day", "normal");
        String text = stockCollectService.analyzeText(map);
        String dingtalkWebhookUrl = Solon.cfg()
                .getProperty("dingtalk:webhook_url", "https://oapi.dingtalk.com/robot/send?access_token=3f7920b69fddec3389c3c71daf91b2128837bcf120d41a991f2210fffa5a2174");
        int thisHour = DateUtil.thisHour(true);
        if (thisHour <= 15) {
            boolean result = dingTalkService.sendMdMsg(dingtalkWebhookUrl, "每日股票分析", text, null);
            log.info("DingTalk send result: {}", result);
        }
    }
}
