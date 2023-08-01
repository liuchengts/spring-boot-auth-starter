package com.boot.auth.test.custom;

import com.boot.auth.starter.common.DefaultRolesConstant;

/**
 * 当前服务自定义的权限，权限为平级验证，不是包含关系
 * 即 某资源  满足 user1或者user2即可
 * <p>
 * <p>
 * extends DefaultRolesConstant 这个继承不是必须的
 */
//todo 在这里自己定义权限角色
public interface RolesConstant extends DefaultRolesConstant {
    /**
     * user1
     **/
    String USER_1 = "USER1";
    /**
     * user2
     **/
    String USER_2 = "USER2";
}
