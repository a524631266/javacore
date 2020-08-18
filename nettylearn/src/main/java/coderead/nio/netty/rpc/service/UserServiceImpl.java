package coderead.nio.netty.rpc.service;

import org.apache.commons.lang.math.IntRange;

import java.util.Random;

public class UserServiceImpl implements UserService {
    @Override
    public String getName(String name) {
        Random random = new Random();
        int i = random.nextInt();
        return "zhangll" + i;
    }

    @Override
    public int getAge(int age) {
        Random random = new Random();
        return random.nextInt(100);
    }

}
