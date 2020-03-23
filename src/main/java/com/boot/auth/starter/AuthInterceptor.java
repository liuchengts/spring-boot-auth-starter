package com.boot.auth.starter;

import com.boot.auth.starter.annotation.Auth;
import com.boot.auth.starter.annotation.IgnoreLogin;
import com.boot.auth.starter.annotation.NoAuthGetSession;
import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.Session;
import com.boot.auth.starter.service.AuthService;
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
import java.util.stream.Collectors;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final SessionResolver sessionResolver;
    private final String loginRequired;
    private final String tokenInvalid;
    private final String authNoInvalid;
    private final AuthService authService;

    public AuthInterceptor(SessionResolver sessionResolver,
                           String loginRequired,
                           String tokenInvalid,
                           String authNoInvalid,
                           AuthService authService) {
        this.sessionResolver = sessionResolver;
        this.loginRequired = loginRequired;
        this.tokenInvalid = tokenInvalid;
        this.authNoInvalid = authNoInvalid;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Map<String, String> tokenMap = authService.analysisToken(request);
        Optional<Session> session = sessionResolver.resolve(tokenMap);
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
        List<String> requireds = Arrays.asList(auth.roles());
        Optional<String> optionalRole = requireds.stream().filter(roles::equals).findFirst();
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
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
        response.getWriter().close();
    }

    /**
     * 记录用户操作日志
     *
     * @param request
     * @param response
     * @param handler
     */
    private void saveOperLog(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String> tokenMap = authService.analysisToken(request);
        Optional<Session> session = sessionResolver.resolve(tokenMap);

//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            OperLogAnnotation operLogAnnotation = handlerMethod.getMethodAnnotation(OperLogAnnotation.class);
//            if (operLogAnnotation != null && operLogAnnotation.flag()) {
//                String operType = operLogAnnotation.operType();
//                String platform = request.getHeader("platform");
//                String channel = request.getHeader("channel");
//                String versionName = request.getHeader("version");
//                String deviceid = request.getHeader("deviceid");
//                String ip = IPUtil.getClientIP(request);
//            }
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        saveOperLog(request, response, handler);
    }
}