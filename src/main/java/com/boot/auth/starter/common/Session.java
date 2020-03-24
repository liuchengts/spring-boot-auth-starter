package com.boot.auth.starter.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Session {

    public String userNo;
    public String platform;
    public String unionId;
    public String username;
    public String openId;
    public String avatar;
    public String roles;
    public String mobile;
    public Object obj;

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public static final Session FAKED = new Session("FAKED", "", "", "", "", "", "", "","");
}
