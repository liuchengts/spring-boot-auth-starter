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
     * 用户昵称
     */
    String SESSION_NICK_NAME = "nickName";
    /**
     * 微信 openId
     */
    String SESSION_OPEN_ID = "openId";
    /**
     * 微信 unionId
     */
    String SESSION_UNION_ID = "unionId";
    /**
     * 用户编号
     */
    String SESSION_USER_NO = "userNo";
    /**
     * 头像
     */
    String SESSION_AVATAR = "avatar";
    /**
     * 权限
     */
    String SESSION_ROLES = "roles";
    /**
     * 手机号
     */
    String SESSION_MOBILE = "mobile";
    /**
     * 自定义 Object类
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
}



