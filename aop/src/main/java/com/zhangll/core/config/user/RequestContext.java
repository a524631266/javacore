package com.zhangll.core.config.user;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/27
 */
public interface RequestContext extends Serializable {

    Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * getter UserId
     *
     * @return
     */
    Long getUserId();

    /**
     * setter UserId
     *
     * @param userId
     */
    void setUserId(Long userId);

    /**
     * getter CorpId
     *
     * @return
     */
    String getCorpId();

    /**
     * setter CorpId
     *
     * @param corpId
     */
    void setCorpId(String corpId);

    /**
     * getter Username
     *
     * @return
     */
    String getUsername();

    /**
     * setter Username
     *
     * @param username
     */
    void setUsername(String username);

    /**
     * getter UserUuid
     *
     * @return
     */
    String getUserUuid();

    /**
     * setter UserUuid
     *
     * @param userUuid
     */
    void setUserUuid(String userUuid);

    /**
     * getter Name
     *
     * @return
     */
    String getName();

    /**
     * setter Name
     *
     * @param name
     */
    void setName(String name);

    /**
     * getter AccountType
     *
     * @return
     */
    Integer getAccountType();

    /**
     * setter AccountType
     *
     * @param accountType
     */
    void setAccountType(Integer accountType);

    /**
     * getter RequestId
     *
     * @return
     */
    String getRequestId();

    /**
     * setter RequestId
     *
     * @param requestId
     */
    void setRequestId(String requestId);

    /**
     * getter Locale
     *
     * @return
     */
    Locale getLocale();

    /**
     * setter Locale
     *
     * @param locale
     */
    void setLocale(Locale locale);


    /**
     * getter AppName
     *
     * @return
     */
    String getAppName();

    /**
     * setter AppName
     *
     * @param appName
     */
    void setAppName(String appName);

    /**
     * getter AppVersion
     *
     * @return
     */
    String getAppVersion();

    /**
     * setter AppVersion
     *
     * @param appVersion
     */
    void setAppVersion(String appVersion);

    /**
     * getter RemoteAddress
     *
     * @return
     */
    String getRemoteAddress();

    /**
     * setter RemoteAddress
     *
     * @param remoteAddress
     */
    void setRemoteAddress(String remoteAddress);

    /**
     * getter DataPermission
     *
     * @return dataPermission  is "0" represent to without handling data permission，otherwise needing to handle.
     */
    Boolean getEnableDataPermission();

    /**
     * setter DataPermission
     *
     * @param enableDataPermission
     */
    void setEnableDataPermission(Boolean enableDataPermission);


    static String generateRequestId() {
        return "req" + System.currentTimeMillis() + System.nanoTime();
    }
}