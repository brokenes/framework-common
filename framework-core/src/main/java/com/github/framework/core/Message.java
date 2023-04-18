package com.github.framework.core;

import java.io.Serializable;

public interface Message <T extends Serializable>{

    /**
     * 消息代码
     * @return
     */
    T getCode();

    /***
     * 消息内容
     * @return
     */
    String getMessage();

}
