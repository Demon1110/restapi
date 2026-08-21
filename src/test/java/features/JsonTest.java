package features;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.cary.core.restapi.entity.TaxCodeVo;
import org.junit.jupiter.api.Test;

import java.util.*;

public class JsonTest {


    @Test
    public void jsonTest2() {
        String path = "D:/1.txt";
        List<String> lines = FileUtil.readUtf8Lines(path);
        Set<String> set = new HashSet<>();
        set.addAll(lines);
//        System.out.println(set.size());
        Map<String, JSONObject> map = new HashMap<>();
        List<JSONObject> list = new ArrayList<>();
        for (String line : set) {
            if (StrUtil.startWith(line, "{")) {
                JSONObject json = JSONUtil.parseObj(line);
//                System.out.println(json);
                map.put(json.getStr("Sphfwssflhbbm"), json);
            } else if (StrUtil.startWith(line, "[")) {
                JSONArray jsonArray = JSONUtil.parseArray(line);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    list.add(json);
                }
//                System.out.println(jsonArray);
            } else {
                System.out.println(line);
            }
        }
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("Sphfwssflhbbm");
        treeNodeConfig.setNameKey("Hwhlwmc");
        treeNodeConfig.setParentIdKey("Spsjbm");
        List<Tree<String>> trees = TreeUtil.build(list, "", treeNodeConfig, (node, tree) -> {
            tree.setId(node.get("Sphfwssflhbbm", String.class));
            tree.setName(node.get("Hwhlwmc", String.class));
            tree.setParentId(node.get("Spsjbm", String.class));
            tree.putAll(node);
        });
//        System.out.println(trees.size());
        List<TaxCodeVo> taxCodeVos = new ArrayList<>();
        printTree(trees, map, taxCodeVos);
        ExcelUtil.getWriter("D:/2.xlsx").write(taxCodeVos, true).close();
    }

    private void printTree(List<Tree<String>> trees, Map<String, JSONObject> map, List<TaxCodeVo> taxCodeVos) {
        for (Tree<String> tree : trees) {
            JSONObject jsonObject = map.get(tree.getId());
            List<Tree<String>> children = tree.getChildren();
            if (jsonObject != null) {
//                System.out.println(jsonObject);
//                FileUtil.appendUtf8String(jsonObject.toString() + ",\n", "D:/2.txt");
                taxCodeVos.add(JSONUtil.toBean(jsonObject, TaxCodeVo.class));
            } else {
                tree.setChildren(null);
//                FileUtil.appendUtf8String(JSONUtil.toJsonStr(tree) + ",\n", "D:/2.txt");
//                System.out.println(JSONUtil.toJsonStr(tree));
                taxCodeVos.add(JSONUtil.toBean(JSONUtil.toJsonStr(tree), TaxCodeVo.class));
            }

            if (CollUtil.isNotEmpty(children)) {
                printTree(children, map, taxCodeVos);
            }
        }
    }
}
