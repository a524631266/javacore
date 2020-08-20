package coderead.nio.netty.rpc.service;

public interface UserService {
    String getName(String name);
    String getName(long id);
    int getAge(String name);
}
