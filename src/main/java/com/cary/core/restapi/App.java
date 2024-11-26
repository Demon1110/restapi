package com.cary.core.restapi;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.AppContext;

@SolonMain
@EnableScheduling
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args, app -> {
            // 添加应用启动完成的监听器
            app.onEvent(AppContext.class, new EventListener<AppContext>() {
                @Override
                public void onEvent(AppContext context) {
                    System.out.println("应用启动完成！");
                }
            });
        });
    }
}