package aiseeyou.icu.core.style;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

/**
 * excel单元形式风格策略
 *
 * @author 柳成冬
 * @date 2023/04/13
 */
public class ExcelCellFormStyleStrategy{
    public ExcelCellFormStyleStrategy() {
        // 创建一个写出的单元格样式对象
        WriteCellStyle headWriteCellStyle  = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setWrapped(true);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

    }
}
