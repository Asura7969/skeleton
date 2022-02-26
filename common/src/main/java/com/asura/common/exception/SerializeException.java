package com.asura.common.exception;

/**
 * @author gongwenzhou
 */
public class SerializeException extends RuntimeException{


    public SerializeException(Exception e) {
        super(e);
    }

    public SerializeException(String impossible) {
        super(impossible);
    }
}
