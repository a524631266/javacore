package coderead.nio.netty.rpc;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class IdCodeSerializableTest {

    @Test
    public void testSerilazable(){
        Request request = new Request("asd", "asd", new String[]{"asdf"});
        if (request instanceof Serializable) {
            System.out.println("yes");
        }
        assert request instanceof Serializable;
    }
    @Test
    public void getIdCode() {
    }

    @Test
    public void getSerializeBody() {
    }

    @Test
    public void updateTarget() {
    }

    @Test
    public void deSerialize() {
    }
}