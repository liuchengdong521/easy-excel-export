package aiseeyou.icu.core.annotation;


import java.lang.annotation.*;


/**
 * excel变化形式
 *
 * @author 柳成冬
 * @date 2023/05/04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelChangeToForm {

    /**
     * 默认值,在需要转换为form格式的实体类 添加此注解
     *
     * @return boolean
     * @author 柳成冬
     */
    boolean value() default true;
}
