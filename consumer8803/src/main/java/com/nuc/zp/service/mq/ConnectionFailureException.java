package com.nuc.zp.service.mq;

public class ConnectionFailureException extends RuntimeException {
    private int code;

    public ConnectionFailureException(int code) {
        this(code, null);
    }

    public ConnectionFailureException(int code, String msg) {
        this(code, msg, null);
    }

    public ConnectionFailureException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }


}
