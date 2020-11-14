package com.zhangll.core.transformClass;

import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.objectweb.asm.Opcodes.*;

/**
 * 仅仅只是添加一个field类型,不添加value
 */
public class AddFieldAdapter extends ClassVisitor {
    private final int acc;
    private final String fName;
    private final String fDesc;
    private final Object value;
    private boolean isExistField = false;

    /**
     * Constructs a new {@link ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public AddFieldAdapter(ClassVisitor cv, int access, String fName, String fDesc, Object value) {
        super(ASM4, cv);
        this.acc = access;
        this.fName = fName;
        this.fDesc = fDesc;
        this.value = value;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if(acc == access && fName.equals(name) && fDesc.equals(desc)){
            isExistField = true;
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {

        if(!isExistField){
            FieldVisitor fv = super.visitField(acc, fName, fDesc, null, value);
            if(fv!=null){
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 给Person添加一个id字段
        InputStream inputstream = Person.class.getClassLoader().getResourceAsStream("com/zhangll/core/transformClass/Person.class");
        ClassReader cr = new ClassReader(inputstream);
        ClassWriter cw = new ClassWriter(0);
        // 只有通过public 和 static可以获取到数据
        AddFieldAdapter addFieldVisitor = new AddFieldAdapter(cw, ACC_PUBLIC + ACC_STATIC, "address", "Ljava/lang/String;", "长河");
        cr.accept(addFieldVisitor, 0);
        byte[] bytes = cw.toByteArray();
        Class<?> aClass = new MySimpleClassLoader(bytes).findClass("com.zhangll.core.transformClass.Person");
        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            System.out.println(String.format("field :: %s", declaredField.getName()));
            Object object = aClass.newInstance();
            System.out.println(declaredField.get(object));
        }
    }
}
