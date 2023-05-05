package aiseeyou.icu.core;

import aiseeyou.icu.core.config.ExcelExportConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ExcelExportConfig.class})
@SpringBootApplication
public class EasyExcelExportCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyExcelExportCoreApplication.class, args);
    }

}
