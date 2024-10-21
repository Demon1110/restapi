package com.cary.core.restapi.controller;

import cn.hutool.core.map.MapUtil;
import com.cary.core.restapi.entity.User;
import com.cary.core.restapi.service.QingLongService;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Inject
    QingLongService qingLongService;

    @Mapping("/jd")
    public Result jd(@Body Map<String, Object> body) throws IOException {
        System.out.println(body);
        List<String> cookies = MapUtil.get(body, "cookies", List.class);
        String cookieStr = String.join(";", cookies);
        System.out.println(cookieStr);
        return Result.succeed(qingLongService.updateEnv("JD_COOKIE", cookieStr));
    }

    @Mapping("/test")
    public Result test(User user, Context ctx) {
        System.out.println(user);
        return Result.succeed();
    }
}