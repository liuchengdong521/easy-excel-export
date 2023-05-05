package aiseeyou.icu.core.common;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * excel下载注册模板
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
public class ExcelDownloadRegisterTemplate extends ExcelExportTemplate {

    /**
     * 全类名
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 服务clazz
     */
    private Class<?> serviceClazz;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getServiceClazz() {
        return serviceClazz;
    }

    public void setServiceClazz(Class<?> serviceClazz) {
        this.serviceClazz = serviceClazz;
    }

    public ExcelDownloadRegisterTemplate() {
    }

    public ExcelDownloadRegisterTemplate(String serviceName, String methodName, String sheetName, Float sheetNo) throws ClassNotFoundException {
        super(sheetName, sheetNo);
        this.serviceName = StringUtils.uncapitalize(serviceClazz.getSimpleName());
        this.methodName = methodName;
        this.serviceClazz = Class.forName(StringUtils.trimWhitespace(serviceName));
    }

    public ExcelDownloadRegisterTemplate(String sheetName, Float sheetNo, Class<?> returnType, String serviceName, String methodName, Class<?> serviceClazz) throws ClassNotFoundException {
        super(sheetName, sheetNo, returnType);
        this.serviceName = StringUtils.uncapitalize(serviceClazz.getSimpleName());
        this.methodName = methodName;
        this.serviceClazz = Class.forName(StringUtils.trimWhitespace(serviceName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExcelDownloadRegisterTemplate that = (ExcelDownloadRegisterTemplate) o;
        return serviceName.equals(that.serviceName) && methodName.equals(that.methodName) && serviceClazz.equals(that.serviceClazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, methodName, serviceClazz);
    }
}
