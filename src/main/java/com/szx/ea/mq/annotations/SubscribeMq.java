package com.szx.ea.mq.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface SubscribeMq {
    String msgType();
}
