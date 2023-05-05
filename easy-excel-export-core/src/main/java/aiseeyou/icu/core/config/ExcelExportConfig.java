package aiseeyou.icu.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * excel导出配置
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
@ConfigurationProperties(prefix = "excel.export")
public class ExcelExportConfig {

    /**
     * 分隔符
     */
    private final String SEPARATOR = "@";


    /**
     * 使用线程池
     */
    private final boolean useThreadPool = true;

    /**
     * 线程数
     */
    private final int threadCount = 8;

    public String getSEPARATOR() {
        return SEPARATOR;
    }

    public boolean isUseThreadPool() {
        return useThreadPool;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
