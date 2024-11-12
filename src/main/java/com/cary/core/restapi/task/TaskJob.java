package com.cary.core.restapi.task;

import com.cary.core.restapi.service.QingLongService;
import com.cary.core.restapi.service.VikaService;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TaskJob {

    @Inject
    VikaService vikaService;
    @Inject
    QingLongService qingLongService;

    @Scheduled(cron = "0 0 0,6,12,18 * * ?")
    public void execute() throws IOException {
        System.out.println("TaskJob execute");
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum","1");
        params.put("recordIds","recWShPpT20z8");
        String records = vikaService.getRecords(params);
        System.out.println(records);

        ONode oNode = ONode.load(records);
        Optional<ONode> optional = Optional.ofNullable(oNode.get("data")).map(data -> data.get("records")).map(r -> r.getOrNull(0)).map(d -> d.get("fields")).map(fields -> fields.get("多行文本"));
        if (optional.isPresent()) {
            ONode data = optional.get();
            System.out.println(data);
            qingLongService.updateEnv("JD_COOKIE",data.toString());
        }
    }
}
