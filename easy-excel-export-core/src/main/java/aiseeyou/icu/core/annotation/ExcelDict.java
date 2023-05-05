package aiseeyou.icu.core.annotation;


import java.lang.annotation.*;

/**
 * excel dict类型
 *
 * @author 柳成冬
 * @date 2023/04/12
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelDict {

    /**
     * 存放 字典内容 的注解
     *  字典以 key@value形式 存放 key为数据库值 value为对应翻译
     *  如: {1@虚拟机,2@云容器}
     * @return {@link String[] }
     * @author 柳成冬
     */
    String[] value() default {};

    /**
     * 预留读取dict配置文件
     *
     * @return {@link String }
     * @author 柳成冬
     */
    String path() default "classpath:dict.yml";

    /**
     * 字典名
     *
     * @return {@link String }
     * @author 柳成冬
     */
    String dictName() default "@";
}
