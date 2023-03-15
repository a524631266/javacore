package com.zhangll.core.config.user;


import java.util.Locale;


/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/27
 */
public class RequestTrace extends AbstractRequestTrace {

    public static RequestContext buildLite() {
        RequestTrace requestContext = new RequestTrace();
        requestContext.setRequestId(RequestContext.generateRequestId());
        requestContext.setLocale(Locale.CHINA);
        return requestContext;
    }

}