package com.boot.auth.starter.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AuthService {
    /**
     * 生成token
     *
     * @param group      权限组
     * @param userNo     用户唯一标识
     * @param roles      用户权限，多个用英文逗号隔开
     * @param parameters 除userNo和roles外要放入用户缓存的额外信息
     * @param response   http response
     * @param request    http request
     * @return 返回生成的token
     * @throws Exception 异常
     * @see com.boot.auth.starter.common.AuthConstant parameters  放入的key值参考这里
     * 需要额外放入内容请传入 com.boot.auth.starter.common.AuthConstant.SESSION_OBJECT 扩展字段
     */
    String auth(String group, String userNo, String roles, Map<String, Object> parameters, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
     * 检测token是否有效
     *
     * @param request HttpServletRequest
     * @return true 有效
     */
    Boolean checkToken(HttpServletRequest request);

    /**
     * 解析 token
     *
     * @param request HttpServletRequest
     * @return token内容
     */
    Map<String, String> analysisToken(HttpServletRequest request) throws Exception;

    /**
     * 解析 token
     *
     * @param token token
     * @return token内容
     */
    Map<String, String> analysisToken(String token) throws Exception;

    /**
     * 删除当前请求者的auth
     *
     * @param response   http response
     * @param request    http request
     */
    Boolean deleteAuth(HttpServletResponse response, HttpServletRequest request);

}
