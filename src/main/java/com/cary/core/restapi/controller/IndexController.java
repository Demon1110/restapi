package com.cary.core.restapi.controller;

import cn.hutool.core.map.MapUtil;
import com.cary.core.restapi.entity.User;
import com.cary.core.restapi.service.VikaService;
import com.cary.core.restapi.task.TaskJob;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Inject
    VikaService vikaService;

    @Mapping("/jd")
    public Result jd(@Body Map<String, Object> body) throws IOException {
        System.out.println(body);
        List<String> cookies = MapUtil.get(body, "cookies", List.class);
        String cookieStr = String.join(";", cookies);
        System.out.println(cookieStr);
        return Result.succeed(vikaService.updateField("recWShPpT20z8","多行文本", cookieStr));
    }


    @Mapping("/test2")
    public Result test2() throws IOException {
        Solon.context().getBean(TaskJob.class).execute();
        return Result.succeed();
    }

    @Mapping("/test")
    public Result test(User user, Context ctx) {
        System.out.println(user);
        return Result.succeed();
    }
}