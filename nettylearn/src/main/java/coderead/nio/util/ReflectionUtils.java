package coderead.nio.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    /**
     *
     * @param cls
     * @param name
     * @return null means cannot find the field from special class
     */
    public static Field findField(Class<?> cls,String name){
        Field filed = null;
        try {
            filed = cls.getDeclaredField(name);
            filed.setAccessible(true);
        } catch ( NoSuchFieldException e ) {
            e.printStackTrace();
        }
        return filed;
    }
}
