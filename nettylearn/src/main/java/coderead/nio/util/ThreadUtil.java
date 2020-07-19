package coderead.nio.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static final void sleep(int second){
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
