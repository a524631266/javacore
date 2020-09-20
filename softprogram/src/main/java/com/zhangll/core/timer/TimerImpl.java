package com.zhangll.core.timer;

import com.zhangll.core.model.TransferMap;
import com.zhangll.core.view.MapInterface;

public class TimerImpl implements Timer {
    private final MapInterface map;
    private final TransferMap transferPolicy;
    private TimerImpl(){
        this(null, null);
    }

    public TimerImpl(MapInterface map, TransferMap transferPolicy) {
        this.map = map;
        this.transferPolicy = transferPolicy;
    }

    @Override
    public int fireFreqquence() {
        return 1000;
    }

    @Override
    public void run() {
        int second = fireFreqquence();

        while (true){
            // 每 2秒钟转换一次
            transferPolicy.updateState(map);
            map.print();
            try {
                Thread.sleep(second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
