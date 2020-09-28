package com.zhangll.core.annotaions.functionalinterface;
//@FunctionalInterface
// 加不加注解都一样，目的是为了表示只有一个方法的接口是 functionInterface
interface Stragegy{
    String approach(String msg);
}

class Soft implements Stragegy{
    /**
     * ??
     * @param msg
     * @return
     */
    @Override
    public String approach(String msg) {
        return msg.toLowerCase() + "?";
    }
}
class Unrelated{
    static String twice(String msg){
        return  msg + " " + msg;
    }
}

public class Strategize {

    Stragegy stragegy;
    String msg;

    public Strategize(String msg) {
        stragegy = new Soft();
        this.msg = msg;
    }
    void communicate(){
        System.out.println(stragegy.approach(msg));
    }

    void changeStrategy(Stragegy stragegy){
        this.stragegy = stragegy;
    }

    public static void main(String[] args) {
        // 可以有非常不错的效果
        Stragegy[] stragegies = {
                new Stragegy() {
                    @Override
                    public String approach(String msg) {
                        return msg.toUpperCase() + "!";
                    }
                },
                msg -> msg.substring(0 , 5),
                Unrelated::twice
        };
        Strategize s = new Strategize("Hello there");
        s.communicate();
        for (Stragegy stragegy : stragegies) {
            s.changeStrategy(stragegy);
            s.communicate();
        }
    }
}
