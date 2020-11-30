package com.techstar.om.dasi.handler.annotation;

import com.techstar.om.dasi.domain.ECheckTargetType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DsiHandler {

    String id();

    String name();

    ECheckTargetType type();
}
