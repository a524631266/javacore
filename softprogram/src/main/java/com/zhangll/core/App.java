package com.zhangll.core;

import com.zhangll.core.model.TransferMap;
import com.zhangll.core.model.TransferMapImpl;
import com.zhangll.core.timer.TimerImpl;
import com.zhangll.core.view.MapInterfaceImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        MapInterfaceImpl mapInterface = new MapInterfaceImpl(40, 20);
        TransferMap policy = new TransferMapImpl();
        new TimerImpl(mapInterface, policy).run();
    }
}
