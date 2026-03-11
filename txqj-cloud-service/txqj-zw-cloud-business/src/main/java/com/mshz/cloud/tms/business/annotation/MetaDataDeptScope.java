package com.mshz.cloud.tms.business.annotation;

import java.lang.annotation.*;

/**
 * 元数据单位过滤注解
 *
 * @author zdw
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaDataDeptScope {

    /**
     * 元模型表的别名
     */
    public String tableAlias() default "";

    /**
     * 权限字符（用于多个角色匹配符合要求的权限）默认根据权限注解//@RequiresPermissions获取，多个权限用逗号分隔开来
     */
    public String permission() default "";

}
