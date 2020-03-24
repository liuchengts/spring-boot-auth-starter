package com.boot.auth.starter;

import com.boot.auth.starter.annotation.Auth;
import com.boot.auth.starter.annotation.IgnoreLogin;
import com.boot.auth.starter.annotation.NoAuthGetSession;
import com.boot.auth.starter.annotation.OperLog;
import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.Session;
import com.boot.auth.starter.model.OperLogAnnotationEntity;
import com.boot.auth.starter.service.AuthService;
import com.boot.auth.starter.service.LogService;
import com.boot.auth.starter.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {
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
        Map<String, String> tokenMap = authService.analysisToken(request);
        Optional<Session> session = sessionResolver.resolve(tokenMap, request.getHeader(AuthConstant.HEADER_KEY_PLATFORM));
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
        if (auth == null) {
            auth = handlerMethod.getMethodAnnotation(Auth.class);
        }
        //没有auth直接认证通过
        if (auth == null) {
            session.ifPresent(s -> request.setAttribute(AuthConstant.ATTR_SESSION, s));
            return true;
        }
        // 不校验登录信息
        IgnoreLogin ignoreToken = handlerMethod.getMethod().getDeclaringClass().getAnnotation(IgnoreLogin.class);
        if (ignoreToken == null) {
            ignoreToken = handlerMethod.getMethodAnnotation(IgnoreLogin.class);
        }
        //不校验登录信息通过
        if (null != ignoreToken && ignoreToken.ignore()) {
            session.ifPresent(s -> request.setAttribute(AuthConstant.ATTR_SESSION, s));
            return true;
        }
        //不强制校验权限
        NoAuthGetSession noAuthGetSession = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoAuthGetSession.class);
        if (noAuthGetSession == null) {
            noAuthGetSession = handlerMethod.getMethodAnnotation(NoAuthGetSession.class);
        }
        //不强制校验权限通过
        if (noAuthGetSession != null) {
            if (noAuthGetSession.loginRequired())
                session.ifPresent(s -> request.setAttribute(AuthConstant.ATTR_SESSION, s));
            return true;
        }
        String json = loginRequired;
        if (tokenMap.isEmpty() || !tokenMap.containsKey(AuthConstant.MAP_KEY_TOKEN)) {
            //未登录
            send(response, json);
            log.warn("用户未登录,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        if (!session.isPresent()) {
            //token失效
            json = tokenInvalid;
            send(response, json);
            log.warn("用户token失效,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        //开始校验权限
        List<String> roles = Arrays.asList(session.get().getRoles().split(","));
        Optional<String> optionalRole = Arrays.stream(auth.roles()).filter(roles::contains).findFirst();
        if (!optionalRole.isPresent()) {
            json = authNoInvalid;
            send(response, json);
            log.warn("用户不具备访问权限,拒绝访问[" + request.getRequestURI() + "]");
            return false;
        }
        session.ifPresent(s -> request.setAttribute(AuthConstant.ATTR_SESSION, s));
        return true;
    }

    /**
     * 往客户端回写消息
     *
     * @param response 返回
     * @param json     输出的json信息
     * @throws Exception
     */
    private void send(HttpServletResponse response, String json) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
        response.getWriter().close();
    }

    /**
     * 记录用户操作日志
     */
    private void saveOperLog(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String> tokenMap = authService.analysisToken(request);
        Optional<Session> optionalSession = sessionResolver.resolve(tokenMap, request.getHeader(AuthConstant.HEADER_KEY_PLATFORM));
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            OperLog operLog = handlerMethod.getMethod().getDeclaringClass().getAnnotation(OperLog.class);
            if (operLog == null) {
                operLog = handlerMethod.getMethodAnnotation(OperLog.class);
            }
            if (operLog != null && operLog.flag()) {
                OperLogAnnotationEntity logEntity = new OperLogAnnotationEntity();
                logEntity.setOperType(operLog.operType());
                logEntity.setChannel(request.getHeader(AuthConstant.HEADER_KEY_CHANNEL));
                logEntity.setVersion(request.getHeader(AuthConstant.HEADER_KEY_VERSION));
                logEntity.setDeviceId(request.getHeader(AuthConstant.HEADER_KEY_DEVICEID));
                logEntity.setIp(IPUtils.getClientIP(request));
                if (optionalSession.isPresent()) {//当前访问者信息
                    Session session = optionalSession.get();
                    logEntity.setUserNo(session.getUserNo());
                    logEntity.setUsername(session.getUsername());
                    logEntity.setRoles(session.getRoles());
                    logEntity.setObj(session.getObj());
                }
                logService.addLog(logEntity);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        saveOperLog(request, response, handler);
    }
}