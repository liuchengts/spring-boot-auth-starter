package com.boot.auth.starter.vo;

import com.boot.auth.starter.common.RestStatus;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 与客户端交互的返回结果
 *
 */
public class AResponse implements AMessage, AData, Serializable {

    /**
     * 操作成功与否 由后台返回前台
     */
    private String success = SuccessFlag.TRUE.toString();

    /**
     * 处理信息代码
     */
    private String code = "Y00-000200";

    /**
     * 信息
     */
    private String message = "SUCCESS";

    /**
     * 返回客户端所带的数据
     */
    private Map<String, Object> data = new LinkedHashMap<>();

    public static AResponse success() {
        AResponse response = new AResponse();
        response.success = SuccessFlag.TRUE.toString();
        response.message = "SUCCESS";
        return response;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    public void setSuccessFalse() {
        this.success = SuccessFlag.FALSE.value;
    }

    public void setSuccessTrue() {
        this.success = SuccessFlag.TRUE.value;
    }

    /**
     * 操作是不是成功
     *
     * @return true：成功, false：未成功
     */
    public Boolean isSucceed() {
        return SuccessFlag.TRUE.toString().equals(success);
    }

    /**
     * 操作是不是失败
     *
     * @return true：失败, false：无误
     */
    public Boolean isFailed() {
        return !SuccessFlag.TRUE.toString().equals(success);
    }

    @Override
    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Return the underlying {@code data} instance (never {@code null}).
     */
    @Override
    public Map<String, Object> getData() {
        if (this.data == null) {
            this.data = new LinkedHashMap<>();
        }
        return this.data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(String dataName) {
        if (this.data == null) {
            this.data = new LinkedHashMap<>();
        }
        return (T) this.data.get(dataName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T addData(String dataName, T dataValue) {
        if (dataName == null) {
            throw new IllegalArgumentException("Model attribute name must not be null");
        }
        if (this.data == null) {
            this.data = new LinkedHashMap<>();
        }
        this.data.put(dataName, dataValue);
        return dataValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> addAllData(Map<String, T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Model attribute must not be null");
        }
        if (this.data == null) {
            this.data = new LinkedHashMap<>(data.size());
        }
        this.data.putAll(data);
        return data;
    }

    public enum SuccessFlag {

        TRUE("TRUE"), FALSE("FALSE");

        String value;

        SuccessFlag(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public AResponse() {
        //默认为true
        setSuccessTrue();
    }

    public AResponse setRestStatus(Boolean fag, RestStatus restStatus) {
        if (fag) setSuccessTrue();
        else setSuccessFalse();
        this.code = String.valueOf(restStatus.value());
        this.message = restStatus.getMsg();
        return this;
    }

    public AResponse setRestStatus(RestStatus restStatus) {
        this.code = String.valueOf(restStatus.value());
        this.message = restStatus.getMsg();
        return this;
    }

    public AResponse(RestStatus restStatus) {
        this.code = String.valueOf(restStatus.value());
        this.message = restStatus.getMsg();
    }

    public AResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public AResponse(String code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private AResponse(String success, String code, String message, Map<String, Object> data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 操作成功与否 由后台返回前台
         */
        private String success = SuccessFlag.TRUE.toString();

        /**
         * 处理信息代码
         */
        private String code = "Y00-000200";

        /**
         * 信息
         */
        private String message = "SUCCESS";

        /**
         * 返回客户端所带的数据
         */
        private Map<String, Object> data = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder success(String success) {
            this.success = success;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> Builder addData(String dataName, T dataValue) {
            if (this.data == null) {
                this.data = new LinkedHashMap<>();
            }
            if (dataName == null) {
                throw new IllegalArgumentException("Model attribute name must not be null");
            }
            this.data.put(dataName, dataValue);
            return this;
        }

        public Builder addData(Map<String, Object> dataMap) {
            if (this.data == null) {
                this.data = new LinkedHashMap<>();
            }
            if (dataMap == null) {
                throw new IllegalArgumentException("Model dataMap must not be null");
            }
            this.data.putAll(dataMap);
            return this;
        }


        @Override
        public String toString() {
            return "Builder{" +
                    "success='" + success + '\'' +
                    ", code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }

        public AResponse build() {
            return new AResponse(success, code, message, data);
        }
    }
}
