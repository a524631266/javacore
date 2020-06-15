package com.zhangll.core.jpaproxy;

import com.zhangll.core.jpaproxy.interf.Base;

public class InterfaceToObject {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Base base = Base.class.newInstance();
        base.findById();
    }
}
