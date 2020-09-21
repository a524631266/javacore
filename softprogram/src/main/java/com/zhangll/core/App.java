package com.zhangll.core;

import com.zhangll.core.model.TransferMap;
import com.zhangll.core.model.TransferMapImpl;
import com.zhangll.core.timer.TimerImpl;
import com.zhangll.core.view.MapInterfaceImpl;
import com.zhangll.core.view.strategy.BarInitStrategy;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
//        MapInterfaceImpl mapInterface = new MapInterfaceImpl(40, 20);
        MapInterfaceImpl mapInterface = new MapInterfaceImpl(10, 10, new BarInitStrategy());
        TransferMap policy = new TransferMapImpl();
        new TimerImpl(mapInterface, policy).run();
    }
}
