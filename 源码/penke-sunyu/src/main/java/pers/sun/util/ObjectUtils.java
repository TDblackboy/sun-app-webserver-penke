package pers.sun.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 对象的一些操作
 *
 * @author 曹沫
 * @date 2021/8/31
 */
public class ObjectUtils {

    /**
     * Unsafe类 可以对内存操作
     * Unsafe类是不可继承的,构造函数私有化，
     */
    private final static Unsafe unsafe=getUnsafe();

    /**
     * 获取Unsafe类实例
     * @return
     */
    private static Unsafe getUnsafe() {
        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 打印对象的地址 64位
     * @param objs 可变参数 且必须再方法参数最后，一个方法只能有一个。
     *             为什么是可变参数？因为unsafe.arrayBaseOffset和unsafe.arrayIndexScale接口参数为数组
     */
    public static String printObjectAddress(Object... objs){
        assert unsafe != null;
        //System.out.println(objs.getClass());
        long offset = unsafe.arrayBaseOffset(objs.getClass());//第一个元素偏移地址
        //System.out.println(offset);
        long scale=unsafe.arrayIndexScale(objs.getClass());//增量地址
        //System.out.println(scale);
        long factor = 8;//8*8 64位
        final long i1 = (unsafe.getInt(objs, offset) & 0xFFFFFFFFL) * factor;
        return "0x" + Long.toHexString(i1);
    }


}
