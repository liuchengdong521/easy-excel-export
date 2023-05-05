package aiseeyou.icu.core.subscriber;


import aiseeyou.icu.core.common.ExcelDownloadSubscriberTemplate;

/**
 * 技术架构评审导出订阅接口
 *
 * @author 柳成冬
 * @date 2023/04/11
 */
public interface ExportSubscriber {

    /**
     * 写excel
     * 用于处理 表格内容中含有 字典内容 的表格
     * 或 处理 表单类别的表 以及拓展其他自定义方式
     *
     * @param item      项
     * @param condition 条件
     * @return {@link ExcelDownloadSubscriberTemplate }
     * @author 柳成冬
     */
    ExcelDownloadSubscriberTemplate writeExcelSheet(ExcelDownloadSubscriberTemplate item,Object condition);
}
