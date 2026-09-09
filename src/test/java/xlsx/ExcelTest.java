package xlsx;

import com.aspose.cells.*;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.ext.processor.ReplaceProcessor;
import org.dromara.pdf.pdfbox.handler.PdfHandler;
import org.dromara.pdf.shade.org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExcelTest {
    public static void main(String[] args) throws Exception {
        Workbook workbook = new Workbook("C:\\Users\\admin\\Desktop\\可行性研究报告财务分析)--银行贷款或债券用 (1).xlsx");
        // 遍历所有sheet
        for (Worksheet sheet : workbook.getWorksheets()) {
            // 1. 自动适配列宽【核心！解决#####】
            sheet.autoFitColumns();
            // 可选：自动适配行高（多行文本）
            //sheet.autoFitRows();

//            // 2. 页面打印设置：把所有列压缩到一页宽度（报表常用）
//            PageSetup pageSetup = sheet.getPageSetup();
//            pageSetup.setFitToPagesWide(1);  // 宽度缩成1页
//            pageSetup.setFitToPagesTall(0); // 高度不限，自动分页；如果写1代表整表放1页

            // 可选：单元格开启自动换行（长文本）
            Cells cells = sheet.getCells();
            Style style = workbook.createStyle();
            style.setTextWrapped(true);
            StyleFlag flag = new StyleFlag();
            flag.setWrapText(true);
            cells.applyStyle(style, flag);
        }
        PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
        // pdfSaveOptions.setOnePagePerSheet(true); // 整张sheet输出在单页，列多会字体很小
//        pdfSaveOptions.setAllColumnsInOnePagePerSheet(true);

        workbook.save("C:\\Users\\admin\\Desktop\\demo.pdf", pdfSaveOptions);
    }

    @Test
    public void test() {
        try (
                // 加载文档
                Document document = PdfHandler.getDocumentHandler().load("C:\\Users\\admin\\Desktop\\demo.pdf")
        ) {
            // 获取处理器
            ReplaceProcessor processor = PdfHandler.getDocumentProcessor(document).getReplaceProcessor();
            //  获取字体
            PDFont font = document.getContext().getFont("仿宋");
            // 创建替换字典
            Map<String, String> map = new HashMap<>();
            // 添加替换数据
            map.put("Evaluation Only. Created with Aspose.Cells for Java. Copyright 2003 - 2026 Aspose Pty Ltd.", "");
            // 替换第一页文本
            processor.replaceText(font, map);
            // 保存文档
            document.save("C:\\Users\\admin\\Desktop\\demo2.pdf");
        }
    }
}
