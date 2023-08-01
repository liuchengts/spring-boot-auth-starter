package com.boot.auth.starter;

import com.boot.auth.starter.annotation.Auth;
import com.boot.auth.starter.annotation.IgnoreLogin;
import com.boot.auth.starter.annotation.NoAuthGetSession;
import com.boot.auth.starter.annotation.OperLog;
import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.LogicSession;
import com.boot.auth.starter.common.Session;
import com.boot.auth.starter.model.OperLogAnnotationEntity;
import com.boot.auth.starter.service.AuthService;
import com.boot.auth.starter.service.LogService;
import com.boot.auth.starter.utils.IPUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    private final SessionResolver sessionResolver;
    private final String loginRequired;
    private final String tokenInvalid;
    private final String authNoInvalid;
    private final AuthService authService;
    private final LogService logService;

    public AuthInterceptor(SessionResolver sessionResolver,
                           String loginRequired,
                           String tokenInvalid,
                           String authNoInvalid,
                           AuthService authService,
                           LogService logService) {
        this.sessionResolver = sessionResolver;
        this.loginRequired = loginRequired;
        this.tokenInvalid = tokenInvalid;
        this.authNoInvalid = authNoInvalid;
        this.authService = authService;
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LogicSession logicSession = getSession(response, request);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
        if (auth == null) auth = handlerMethod.getMethodAnnotation(Auth.class);
        //没有auth直接认证通过
        if (auth == null) {
            requestAttribute(request, logicSession);
            return true;
        }
        // 不校验登录信息
        IgnoreLogin ignoreToken = handlerMethod.getMethod().getDeclaringClass().getAnnotation(IgnoreLogin.class);
        if (ignoreToken == null) {
            ignoreToken = handlerMethod.getMethodAnnotation(IgnoreLogin.class);
        }
        //不校验登录信息通过
        if (null != ignoreToken && ignoreToken.ignore()) {
            requestAttribute(request, logicSession);
            return true;
        }
        //不强制校验权限
        NoAuthGetSession noAuthGetSession = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoAuthGetSession.class);
        if (noAuthGetSession == null) noAuthGetSession = handlerMethod.getMethodAnnotation(NoAuthGetSession.class);
        //不强制校验权限通过
        if (noAuthGetSession != null) {
            if (noAuthGetSession.loginRequired()) {
                requestAttribute(request, logicSession);
            }
            return true;
        }
        if (logicSession == null || !logicSession.getValidLogin()) {
            //未登录
            send(response, loginRequired);
            log.warn("用户未登录,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        if (!logicSession.getValidToken()) {
            //token失效
            send(response, tokenInvalid);
            log.warn("用户token失效,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        //开始校验权限
        List<String> roles = logicSession.getSessionOptional()
                .map(session -> Arrays.asList(session.getRoles().split(",")))
                .orElse(new ArrayList<>());
        Optional<String> optionalRole = Arrays.stream(auth.roles()).filter(roles::contains).findFirst();
        if (!optionalRole.isPresent()) {
            send(response, authNoInvalid);
            log.warn("用户不具备访问权限,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        requestAttribute(request, logicSession);
        return true;
    }

    private void requestAttribute(HttpServletRequest request, LogicSession logicSession) {
        if (logicSession == null) return;
        Optional<Session> sessionOptional = logicSession.getSessionOptional();
        sessionOptional.ifPresent(s -> request.setAttribute(AuthConstant.ATTR_SESSION, s));
    }

    /**
     * 往客户端回写消息
     *
     * @param response 返回
     * @param json     输出的json信息
     * @throws Exception
     */
    private void send(HttpServletResponse response, String json) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
        response.getWriter().close();
    }

    /**
     * 获得session
     *
     * @param request HttpServletRequest
     * @return 返回逻辑session对象
     */
    private LogicSession getSession(HttpServletResponse response, HttpServletRequest request) {
        try {
            return sessionResolver.resolve(authService.analysisToken(request), getHeaderValue(request, AuthConstant.HEADER_KEY_PLATFORM),
                    getHeaderValue(request, AuthConstant.HEADER_KEY_VERSION), IPUtils.getClientIP(request));
        } catch (Exception e) {
            log.error("getSession error ", e);
            authService.deleteAuth(response, request);
            return null;
        }
    }

    /**
     * 记录用户操作日志
     */
    private void saveOperLog(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            OperLog operLog = handlerMethod.getMethod().getDeclaringClass().getAnnotation(OperLog.class);
            if (operLog == null) operLog = handlerMethod.getMethodAnnotation(OperLog.class);
            if (operLog != null && operLog.flag()) {
                OperLogAnnotationEntity logEntity = new OperLogAnnotationEntity();
                logEntity.setOperType(operLog.operType());
                logEntity.setChannel(getHeaderValue(request, AuthConstant.HEADER_KEY_CHANNEL));
                logEntity.setDeviceId(getHeaderValue(request, AuthConstant.HEADER_KEY_DEVICEID));
                LogicSession logicSession = getSession(response, request);
                Optional<Session> sessionOptional = logicSession.getSessionOptional();
                if (sessionOptional.isPresent()) {//当前访问者信息
                    Session session = sessionOptional.get();
                    logEntity.setUserNo(session.getUserNo());
                    logEntity.setUsername(session.getUsername());
                    logEntity.setRoles(session.getRoles());
                    logEntity.setObj(session.getObj());
                    logEntity.setVersion(session.getVersion());
                    logEntity.setPlatform(session.getPlatform());
                } else {
                    logEntity.setIp(IPUtils.getClientIP(request));
                    logEntity.setVersion(getHeaderValue(request, AuthConstant.HEADER_KEY_VERSION));
                    logEntity.setPlatform(getHeaderValue(request, AuthConstant.HEADER_KEY_PLATFORM));
                }
                logService.addLog(logEntity);
            }
        }
    }

    /**
     * 获取 header的内容
     *
     * @param request HttpServletRequest
     * @param key     key
     * @return 返回对应的值
     */
    private String getHeaderValue(HttpServletRequest request, String key) {
        String value = "";
        try {
            value = request.getHeader(key);
        } catch (Exception e) {
            log.warn("header key is null", e);
        }
        return value;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        saveOperLog(request, response, handler);
    }
}
