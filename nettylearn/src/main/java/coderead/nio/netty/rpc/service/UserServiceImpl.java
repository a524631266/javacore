package coderead.nio.netty.rpc.service;

import org.apache.commons.lang.math.IntRange;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserServiceImpl implements UserService {
    public static AtomicLong counter = new AtomicLong(0);
    public String name = "eeeee";

    @Override
    public String getName(String name) {
        Random random = new Random();
        int i = random.nextInt();
        return "id:" + counter.getAndIncrement() + ";" + name + ": random =  " + i + "";
    }

    @Override
    public String getName(long id) {
        return "zzzz";
    }

    @Override
    public int getAge(String name) {
        Random random = new Random();
        return random.nextInt(100);
    }

}
