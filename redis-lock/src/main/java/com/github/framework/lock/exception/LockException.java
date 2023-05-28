package com.github.framework.lock.exception;
import org.github.framework.common.exception.BaseException;

/**
 * 锁异常，获取锁失败要抛出此异常
 *
 *
 */
public class LockException extends BaseException {

    private static final String SYS_CODE = "";

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String code, String message) {
        super(SYS_CODE + code, message);
    }

    public LockException(String code, String message, Throwable cause) {
        super(SYS_CODE+code, message, cause);
    }
}
