package com.boot.auth.starter.exception;

import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.vo.ClientMessage;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.regex.Matcher;

/**
 * 业务类型的Exception, 提供给用户看
 * <p>
 * 描述字数限制15~20
 * <p>
 */
@Slf4j
public class AuthException extends CommonException {

    private List<ClientMessage> errorList = new ArrayList<>();

    public AuthException() {
    }

    public AuthException(String msg) {
        super(msg);
        log.error(msg);
    }

    public AuthException(Exception e) {
        super(e);
        log.error(e.getMessage(), e);
    }

    public AuthException(String msg, Exception e) {
        super(msg, e);
        log.error(msg, e);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        log.error(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 构造业务异常
     *
     * @param code    按规范定义的错误码
     * @param message 错误或异常消息
     * @param values  需要替换的{} 按顺序
     */
    public AuthException(String code, String message, Object... values) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setCode(code);
        String msg;
        if ((NO_NAMED_EXCEPTION_MASSAGE.equalsIgnoreCase(message)) && values.length > 0) {
            msg = NO_NAMED_EXCEPTION_MASSAGE;
        } else {
            msg = parseMessage(message, values);
        }
        clientMessage.setMessage(msg);
        this.errorList.add(clientMessage);
    }

    /**
     * 构造业务异常
     *
     * @param restStatus    按规范定义的错误码
     */
    public AuthException(RestStatus restStatus) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setCode(String.valueOf(restStatus.value()));
        clientMessage.setMessage(restStatus.getMsg());
        this.errorList.add(clientMessage);
    }

    /**
     * 构造业务异常
     *
     * @param code    按规范定义的错误码
     * @param message 错误或异常消息
     */
    public AuthException(String code, String message) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setCode(code);
        clientMessage.setMessage(message);
        this.errorList.add(clientMessage);
    }

    /**
     * 构造业务异常
     *
     * @param code    按规范定义的错误码
     * @param message 错误或异常消息
     * @param map     动态替换的内容
     */
    public AuthException(String code, String message, Map<?, ?> map) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setCode(code);
        clientMessage.setContent(map);
        clientMessage.setMessage(parseMessageFromMap(code, message, map));
        this.errorList.add(clientMessage);

    }

    /**
     * 使用ClientMessage列表构造异常
     *
     * @param errorList 异常信息。
     */
    public AuthException(List<ClientMessage> errorList) {
        this.errorList.addAll(errorList);
    }

    /**
     * 获取全局的errorList。
     *
     * @return List<ClientMessage> 全局的errorList。
     */
    public List<ClientMessage> getErrorList() {
        return errorList;
    }

    /**
     * 添加业务抛出的异常exception中的errorList到全局的errorList中。
     *
     * @param bizException 业务抛出的异常
     * @param <T>          业务异常的子类型
     */
    public <T extends AuthException> void addErrorToList(T bizException) {
        this.errorList.addAll(bizException.getErrorList());
    }

    /**
     * 获取转换好的错误或异常信息。
     *
     * @return 转换好的错误或异常信息。
     */
    @Override
    public String getMessage() {
        String rlt = "";
        if (null == errorList || errorList.isEmpty()) {
            return rlt;
        }
        for (ClientMessage err : errorList) {
            if (null != err.getMessage() && !"".equals(err.getMessage())) {
                rlt += err.getMessage();
            }
        }
        return rlt;
    }

    public static String getMessage(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            //
        }
        return NO_NAMED_EXCEPTION_MASSAGE;
    }

    /**
     * 使用构建 exception 对象时的Map参数来构造错误信息。
     *
     * @param errorCode 按规范定义的错误码
     * @param message   错误或异常信息
     * @param map       <p>动态替换的信息。</p>
     *                  <p>key：跟消息里面的 [] 方括号里面的String一致，value：动态替换进出的内容。</p>
     * @return 构建好的的消息。
     */
    private String parseMessageFromMap(String errorCode, String message, Map<?, ?> map) {
        String[] splits = message.split("\\[");
        if (splits.length > 0) {
            for (String split : splits) {
                if (split.indexOf("]") > 0) {
                    String mapKey = split.substring(0, split.indexOf("]"));
                    String mapValue = "";
                    if (map == null) {
                        mapValue = "[]";
                    } else if (map.containsKey(mapKey)) {
                        mapValue = "[" + String.valueOf(map.get(mapKey)) + "]";
                    } else {
                        System.out.println("when throw exception[" + errorCode + "], you hadn't set key [" + mapKey + "] and value into map ");
                    }
                    // replaceAll之前, 对特殊字符的处理
                    mapValue = Matcher.quoteReplacement(mapValue);
                    message = message.replaceAll("\\[" + mapKey + "\\]", mapValue);
                }
            }
            return message;
        } else {
            return message;
        }
    }

    /**
     * 按顺序替换 {} 里面的内容。
     *
     * @param message 错误或异常消息。
     * @param params  替换的参数。
     * @return 替换好的的消息。
     */
    private static String parseMessage(String message, Object... params) {
        if (params == null || params.length == 0) {
            return message;
        }
        if (params.length > 0 && message.indexOf('{') >= 0) {
            message = MessageFormat.format(message, params);
        }
        return message;
    }

}
