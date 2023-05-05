package aiseeyou.icu.core.common;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
/**
 * 表单项excel项
 *
 * @author 柳成冬
 * @date 2023/04/13
 */

@ExcelIgnoreUnannotated
public class FormItemToExcelVO {
    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND,
            fillForegroundColor = 27, verticalAlignment = VerticalAlignmentEnum.CENTER,
            horizontalAlignment = HorizontalAlignmentEnum.LEFT,
            borderBottom = BorderStyleEnum.THIN
    )
    @ExcelProperty(value = "项目", index = 0)
    private String key;
    @ContentStyle(borderBottom = BorderStyleEnum.THIN)
    @ExcelProperty(value = "内容", index = 1)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FormItemToExcelVO() {
    }

    public FormItemToExcelVO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormItemToExcelVO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
