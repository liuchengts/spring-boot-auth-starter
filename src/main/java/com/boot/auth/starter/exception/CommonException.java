package com.boot.auth.starter.exception;

import com.boot.auth.starter.vo.ClientMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 异常的基类
 */
@Slf4j
public abstract class CommonException extends RuntimeException {

    protected static final Locale locale = Locale.SIMPLIFIED_CHINESE;

    /**
     * bundle FQN
     */
    protected static final String BUNDLE_FQN = "i18n.YtxErrors";
    /**
     * resource bundle
     */
    protected static ResourceBundle resourceBundle;

    /**
     * 未命名的异常
     */
    protected static final String NO_NAMED_EXCEPTION_MASSAGE = "NO NAMED EXCEPTION";

    /**
     * 程序发生一个未知错误!
     */
    private static final String ERR_COMMON_EXCEPTION = "ERR_COMMON_EXCEPTION";

    /**
     * 空指针异常!
     */
    private static final String ERR_NULL_POINTER_CODE = "ERR_NULL_POINTER_CODE";

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable e) {
        super(message, e);
    }

    public CommonException(Throwable e) {
        super(e);
    }

    public CommonException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static String getMessage(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            log.warn("commonException: " + key + " no named");
        }
        return NO_NAMED_EXCEPTION_MASSAGE;
    }

    public static ClientMessage getErrorMessage(Exception exception) {
        ClientMessage msg = new ClientMessage();
        if (exception instanceof NullPointerException) {
            msg.setCode(ERR_NULL_POINTER_CODE);
            msg.setMessage(getMessage(ERR_NULL_POINTER_CODE));
        } else {
            msg.setCode(ERR_COMMON_EXCEPTION);
            msg.setMessage(getMessage(ERR_COMMON_EXCEPTION));
        }
        msg.setMessage(msg.getMessage());
        return msg;
    }
}
