package com.zhangll.core.method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.objectweb.asm.*;

/**
 * 修改值操作
 */
public class Sleep3MethodVisitor extends PatternMehodAdapter{
    protected final static int SEE_TIMEUNIT = 1;
    protected final static int SEE_LDC = 1 << 1;
    protected Object value ;

    public Sleep3MethodVisitor(MethodVisitor mv) {
        super(mv);
    }

    @Override
    public void visitLabel(Label label) {
        System.out.println("Label : " + label.info);
        super.visitLabel(label);
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        System.out.println("Frame ");
        super.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override
    public void visitCode() {
        System.out.println("start visit Code");
        super.visitCode();
    }

    @Override
    public void visitAttribute(Attribute attr) {
        System.out.println("Attribute:" + attr.type);
        super.visitAttribute(attr);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("visitAnnotation: "+desc);
        return super.visitAnnotation(desc, visible);
    }

    /**
     * 延迟加载 LDC
     * 在 指令之后的
     * TimeUnit.SECONDS.sleep(time);不能直接替换
     * TimeUnit.SECONDS.sleep(字面量); 可以直接替换
     */
    @Override
    protected void visitInst() {
        System.out.println("##############");
        if(state == (SEE_TIMEUNIT + SEE_LDC)){
//            super.visitLdcInsn(this.value);
//            state = SEEN_NOTHING;
            super.visitLdcInsn(3L);
            state = SEEN_NOTHING;
        } else{
            if((state & SEE_LDC) > 0){
                super.visitLdcInsn(this.value);
            }
            state = SEEN_NOTHING;
        }
    }

    /**
     * 在字节码块中触发field或者触发method的给人看的描述情况
     */
    @Data
    @ToString
    @AllArgsConstructor
    class FieldMethodItem {
        int opcode;
        String owner;
        String name;
        String desc;
        boolean itf;
    }



    /**
     * 在方法内部 访问静态变量
     * TimeUnit.SECONDS.sleep(2); 到SECONDS为止
     * opcode=178, owner=java/util/concurrent/TimeUnit, name=SECONDS, desc=Ljava/util/concurrent/TimeUnit;
     * @param opcode
     * @param owner
     * @param name
     * @param desc
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        FieldMethodItem item = new FieldMethodItem(opcode, owner, name, desc, false);
        System.out.println("visitFieldInsn:" + item);
        visitInst();
        if(opcode == GETSTATIC && isTimeMethod(item)){
            state = SEE_TIMEUNIT;
        } else{
            state = SEEN_NOTHING;
        }
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    private boolean isTimeMethod(FieldMethodItem item) {
        if("java/util/concurrent/TimeUnit".equals(item.getOwner())){
            return true;
        }
        return false;
    }


    /**
     * 触发方法的操作。invoc
     * @param opcode
     * @param owner
     * @param name
     * @param desc
     * @param itf
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        System.out.println("visitMethodInsn: "+ new FieldMethodItem(opcode, owner, name, desc, itf));
        visitInst();
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
    /**
     * 返回 行号 和 标签
     * @param line
     * @param start
     */
    @Override
    public void visitLineNumber(int line, Label start) {
        System.out.println(String.format("line: %d, start: %s", line, start.info));
        super.visitLineNumber(line, start);
    }

    /**
     * 只访问 只有操作码的情况
     * 177 == 0xb1 == return
     * 101 == 0x65 == lsub  long相减
     * 10 == 0x0a ==  lconst_1 把long类型的数值1 推至栈顶
     * @param opcode
     */
    @Override
    public void visitInsn(int opcode) {
//        System.out.println("visitInsn");
//        if((state & SEE_TIMEUNIT) > 0){
////            visitInst();
//            state += SEE_LDC;
//////            visitInst();
//            super.visitLdcInsn(3L);
//        }else{
//            visitInst();
//            // 177 == 0xb1 == return
//
//            // 删除nop指令
//        }
        System.out.println(" opcode :" + opcode);
        if(opcode != NOP){
            super.visitInsn(opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.println(" opcode:" + opcode + " :: operand:" + operand);
        visitInst();
        super.visitIntInsn(opcode, operand);
    }

    /**
     * LDC LOADCONSTANT 加载常量池中的数据
     * 添加三秒中 把所有LDC
     *  // 延迟加载
     * @param cst 加载的数据大小
     */
    @Override
    public void visitLdcInsn(Object cst) {
        System.out.println("visit   cst");
        this.value = cst;
        this.state += SEE_LDC;
        // 在触发ldc之前没有看到
        if((state & SEE_TIMEUNIT) ==  0){
            super.visitLdcInsn(cst);
            this.state = SEEN_NOTHING;
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        System.out.println("visitMaxs");
        super.visitMaxs(maxStack + 2, maxLocals);
    }

//    /**
//     *  TimeUnit.SECONDS.sleep会定义一个var，所以，最好就是过滤掉这个var
//     *  防止
//     * @param opcode
//     * @param var
//     */
//    @Override
//    public void visitVarInsn(int opcode, int var) {
//        System.out.println("visitVarInsn: opcode: " + opcode + ":: var:" + var);
////        if((state & SEE_TIMEUNIT) >  0 && opcode == ILOAD){
////            return;
////        }
//        super.visitVarInsn(opcode, var);
//    }
}
