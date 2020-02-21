package com.boot.auth.starter.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Session {

    public String userNo;
    public String platformId;
    public String unionId;
    public String username;
    public String openId;
    public String avatar;
    public String roles;
    public String mobile;
    public Object obj;

    public static final Session FAKED = new Session("FAKED", "", "", "", "", "", "", "","");
}
