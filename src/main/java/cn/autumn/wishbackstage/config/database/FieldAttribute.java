package cn.autumn.wishbackstage.config.database;

import java.lang.annotation.*;

/**
 * @author cf
 * Created in 2022/11/4
 * Field definition
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAttribute {

    String name() default "";

    String fieldType() default "";

    boolean isNull() default true;

    String comment() default "";

}
