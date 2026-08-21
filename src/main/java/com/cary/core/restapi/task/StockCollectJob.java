package com.cary.core.restapi.task;

import com.cary.core.restapi.service.StockCollectService;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class StockCollectJob {

    @Inject
    StockCollectService stockCollectService;

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void execute() throws IOException {
        System.out.println("TaskJob execute");
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", "1");
        params.put("recordIds", "recWShPpT20z8");
    }
}
