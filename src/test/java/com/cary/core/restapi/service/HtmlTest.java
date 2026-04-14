package com.cary.core.restapi.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cary.core.restapi.utils.GoSeedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlTest {

    public static void main(String[] args) {
        //
//        parseHtml();
        createDownload();
    }

    private static void createDownload() {
        List<String> list = FileUtil.readUtf8Lines("C:\\Users\\admin\\Desktop\\imgs.txt");
        AtomicInteger count = new AtomicInteger(0);
        for (String url : list) {
            String newUrl = StrUtil.replace(url, "/bthumb/", "/");
            ThreadUtil.execute(() -> {
                GoSeedUtils.createTask(newUrl);
                System.out.println(
                        "count.incrementAndGet()=" + count.incrementAndGet() + "======" + newUrl + "执行完成");
            });
        }
        System.out.println("任务完成");
        System.out.println("任务完成");
    }

    //解析html文件

    /**
     * 解析html文件
     * https://images.hdqwalls.com/wallpapers/bthumb/minimal-mountains-wr.jpg
     * https://images.hdqwalls.com/wallpapers/minimal-mountains-wr.jpg
     */
    public static void parseHtml() {
        String file = "C:\\Users\\admin\\Desktop\\html.txt";
        FileUtil.readUtf8Lines(FileUtil.file(file), (LineHandler) line -> {
//            System.out.println(line);
            Document document = Jsoup.parse(URLUtil.decode(line));
//            System.out.println(document.title());
            Elements imgs = document.select("img.img-responsive");
            for (int i = 0; i < imgs.size(); i++) {
                Element element = imgs.get(i);
                String src = element.attr("src");
                if (StrUtil.startWith(src, "http")) {
                    src = StrUtil.replace(src, "/bthumb/", "/");
                    FileUtil.appendUtf8String(src + "\n", "C:\\Users\\admin\\Desktop\\img.txt");
                }
            }
            System.out.println(imgs.size());
        });
    }
}
