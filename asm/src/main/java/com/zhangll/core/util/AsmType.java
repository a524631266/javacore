package com.zhangll.core.util;

import com.zhangll.core.generateclass.InterfaceToClass;
import org.objectweb.asm.Type;

import java.util.Date;

public class AsmType {
    public static void main(String[] args) {
        System.out.println(getInterName(String.class));
        System.out.println(getInterName(Date.class));
        System.out.println(getInterName(InterfaceToClass.class));


        System.out.println(getDescriptor(double.class));
        System.out.println(getDescriptor(Double.class));
        System.out.println(getDescriptor(InterfaceToClass.class));


        System.out.println(Type.INT_TYPE.getDescriptor());
        System.out.println(Type.SHORT_TYPE.getDescriptor());
        System.out.println(Type.LONG_TYPE.getDescriptor());

        // 只有一个数组的method描述符I
        System.out.println(Type.getArgumentTypes("(I)V")[0]);
    }

    /**
     * 内部名的概念
     * String.class ===> java/lang/String
     * InterfaceToClass.class ==> com/zhangll/core/generateclass/InterfaceToClass
     * @param cClass
     * @return
     */
    public static String getInterName(Class<?> cClass){
        String internalName = Type.getType(cClass).getInternalName();
//        System.out.println(internalName);
        return internalName;
    }

    /**
     * double.class => D
     * Double.class => Ljava/lang/Double;
     * 内置类Lcom/zhangll/core/generateclass/InterfaceToClass;
     * @param cClass
     * @return
     */
    public static String getDescriptor(Class<?> cClass){
        String descriptor = Type.getType(cClass).getDescriptor();
        return descriptor;
    }


    public static void getProperty(){
        Type.getType("(ILjava/lang/String;)V");

        Type.getArgumentTypes("ILjava/lang/String;");

    }


    public static Class<?> getClass(String desc) {
        try {
            return Class.forName(desc.replace("/", ".").replace("L","").replace(";",""));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static Class<?> getClass(Type t) {
        if (t.getSort() == Type.OBJECT) {
            return getClass(t.getInternalName());
        }
        return getClass(t.getDescriptor());
    }

}
