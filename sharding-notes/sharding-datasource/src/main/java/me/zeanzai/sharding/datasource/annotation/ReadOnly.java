package me.zeanzai.sharding.datasource.annotation;

import java.lang.annotation.*;

/**
 * @author shawnwang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface ReadOnly {
}

