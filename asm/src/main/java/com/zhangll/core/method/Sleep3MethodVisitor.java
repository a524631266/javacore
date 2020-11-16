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
    public void visitCode() {
        System.out.println("start visit Code");
        super.visitCode();
    }

    @Override
    public void visitAttribute(Attribute attr) {
        System.out.println(attr.type);
        super.visitAttribute(attr);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println(desc);
        return super.visitAnnotation(desc, visible);
    }

    /**
     * 延迟加载
     */
    @Override
    protected void visitInst() {
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
        visitInst();
        System.out.println("visitMethodInsn: "+ new FieldMethodItem(opcode, owner, name, desc, itf));
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
        // 177 == 0xb1 == return
        System.out.println(" opcode :" + opcode);
        // 删除nop指令
        if(opcode != NOP){
            super.visitInsn(opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.println(" opcode:" + opcode + " :: operand:" + operand);
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
    }
}
