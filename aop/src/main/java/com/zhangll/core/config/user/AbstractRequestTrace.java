package com.zhangll.core.config.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/24
 */
@Getter
@Setter
public abstract class AbstractRequestTrace implements RequestContext {

    private Long userId;

    private String corpId;

    private String username;

    private String userUuid;

    private String name;

    private Integer accountType;

    private String requestId;

    private Locale locale;

//    private PlatformEnum platform;

    private String appName;

    private String appVersion;

    private String remoteAddress;

    private Boolean enableDataPermission;
}