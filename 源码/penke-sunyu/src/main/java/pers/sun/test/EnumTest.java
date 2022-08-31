package pers.sun.test;

/**
 * @author 曹沫
 * @date 2021/9/10
 */
public class EnumTest {
    public static void main(String[] args) {
        System.out.println(TypeA.T1.toString());
        //System.out.println(TypeA.T2.ordinal());
        //System.out.println(TypeA.T1.getClass());
        //System.out.println(Arrays.toString(TypeA.values()));
        //System.out.println(TypeA.valueOf("1"));
        System.out.println(TypeA.T2.getDesc());
        System.out.println(TypeA.T1.getDesc());
        TypeA.getDesc();
    }
}

//枚举类型中可以定义什么
enum TypeA {
    T1(1),
    T2(2),
    T3(3),
    T4(3);


    //构造方法
    TypeA(int index) {

        this.index = index;
    }
    private TypeA(int in,String des) {

        index = 0;
    }

    private final int index;
    private static String desc;

    public final static String getDesc(){
        return desc;
    }
}



