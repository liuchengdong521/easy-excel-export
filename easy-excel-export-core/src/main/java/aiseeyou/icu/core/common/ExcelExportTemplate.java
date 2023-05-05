package aiseeyou.icu.core.common;

import java.util.List;

/**
 * excel导出模板
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public abstract class ExcelExportTemplate {

    /**
     * 表sheet页名字
     */
    private String sheetName;

    /**
     * 表sheet页排序
     * 不要大于1000
     */
    private Float sheetNo = 1000.0f;

    /**
     * 结果数据
     */
    private List<Object> resultData;

    /**
     * 返回类型
     */
    private Class<?> returnType = null;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Float getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Float sheetNo) {
        this.sheetNo = sheetNo;
    }

    public List<Object> getResultData() {
        return resultData;
    }

    public void setResultData(List<Object> resultData) {
        this.resultData = resultData;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    protected ExcelExportTemplate() {
    }

    protected ExcelExportTemplate(String sheetName, Float sheetNo) {
        this.sheetName = sheetName;
        this.sheetNo = sheetNo;
    }

    protected ExcelExportTemplate(String sheetName, Float sheetNo, Class<?> returnType) {
        this.sheetName = sheetName;
        this.sheetNo = sheetNo;
        this.returnType = returnType;
    }
}
