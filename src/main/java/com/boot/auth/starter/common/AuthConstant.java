package com.boot.auth.starter.common;

public interface AuthConstant {
    /**
     * header中设置token的cookie名字
     */
    String HEAD_TOKEN_NAME = "x-token";
    /**
     * header中设置token 分隔符
     */
    String HEAD_TOKEN_SEPARATOR = ",";
    /**
     * session key
     */
    String ATTR_SESSION = "ATTR_SESSION";
    /**
     * session-用户昵称
     */
    String SESSION_NICK_NAME = "nickName";
    /**
     * session-微信 openId
     */
    String SESSION_OPEN_ID = "openId";
    /**
     * session-微信 unionId
     */
    String SESSION_UNION_ID = "unionId";
    /**
     * session-用户编号
     */
    String SESSION_USER_NO = "userNo";
    /**
     * session-头像
     */
    String SESSION_AVATAR = "avatar";
    /**
     * session-权限
     */
    String SESSION_ROLES = "roles";
    /**
     * session-手机号
     */
    String SESSION_MOBILE = "mobile";
    /**
     * session-自定义 Object类
     */
    String SESSION_OBJECT = "obj";

    /**
     * token解析 key- token
     */
    String MAP_KEY_TOKEN = "token";
    /**
     * token解析 key- userNo
     */
    String MAP_KEY_USER_NO = "userNo";
    /**
     * token解析 key- group
     */
    String MAP_KEY_GROUP = "group";
    /**
     * token解析 key- time
     */
    String MAP_KEY_TIME = "time";
    /**
     * token解析 key- key
     */
    String MAP_KEY_KEY = "key";

    /**
     * header-平台 key
     */
    String HEADER_KEY_PLATFORM = "platform";
    /**
     * header-渠道 key
     */
    String HEADER_KEY_CHANNEL = "channel";
    /**
     * header-版本 key
     */
    String HEADER_KEY_VERSION = "version";
    /**
     * header-版本 key
     */
    String HEADER_KEY_DEVICEID = "deviceId";


}



