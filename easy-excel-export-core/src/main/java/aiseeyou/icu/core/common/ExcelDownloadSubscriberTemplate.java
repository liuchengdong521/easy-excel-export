package aiseeyou.icu.core.common;


import aiseeyou.icu.core.subscriber.ExportSubscriber;

/**
 * excel下载注册模板
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class ExcelDownloadSubscriberTemplate extends ExcelExportTemplate{

    private ExportSubscriber exportSubscriber;

    public ExportSubscriber getExportSubscriber() {
        return exportSubscriber;
    }

    public void setExportSubscriber(ExportSubscriber exportSubscriber) {
        this.exportSubscriber = exportSubscriber;
    }

    public ExcelDownloadSubscriberTemplate(ExportSubscriber exportSubscriber) {
        this.exportSubscriber = exportSubscriber;
    }
}
