package com.zhangll.core.util;

import com.zhangll.core.Interface2;
import com.zhangll.core.Interface2Impl;
import com.zhangll.core.transformClass.Person;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * 先查看
 */
public class TraceClassVisitorDemo {
    public static Logger LOG = LoggerFactory.getLogger(TraceClassVisitorDemo.class);
    private static PrintWriter printWriter = new PrintWriter(System.out);
    private static TraceClassVisitor tcv = new TraceClassVisitor(printWriter);
    public static void main(String[] args) throws IOException {

//        InputStream targetClass = Person.class.getClassLoader().getResourceAsStream("com/zhangll/core/transformClass/Person.class");
//        ClassReader cr = new ClassReader(targetClass);
//        cr.accept(tcv, 0);

//        showCode(Person.class);
        showCode(Interface2.class);
        showCode(Interface2Impl.class);

    }

    public static void showCode(Class<?>  cClass) throws IOException {
        String path = String.format("%s.class", AsmType.getInterName(cClass));
        InputStream resourceAsStream = cClass.getClassLoader().getResourceAsStream(path);
        ClassReader classReader = new ClassReader(resourceAsStream);
        classReader.accept(tcv, 0);
    }


    public static void showCode(byte[] bytes) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(tcv, 0);
    }

    /**
     * 覆盖源项目路径中的class类
     * @param bytes
     * @param cClass
     * @throws IOException
     */
    public static void storeClass(byte[] bytes, Class<?> cClass) throws IOException {
        // /E:/github/javacore/asm/target/classes/com/zhangll/core/method/
        String dir = cClass.getResource("").getPath();
        String path = dir + cClass.getSimpleName() + ".class";
        String path2 = "data/" + cClass.getSimpleName() + ".class";
        // 写内容
        File file = writeIntoFile(bytes, path);
        File file2 = writeIntoFile(bytes, path2);

        LOG.info("请查看你当前项目的路径: (" + file2.getName() +":1)");
    }

    private static File writeIntoFile(byte[] bytes, String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        return file;
    }
}
