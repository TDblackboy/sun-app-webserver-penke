package pers.sun.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 曹沫
 * @date 2021/9/6
 */
public class GrammarTest {

    public static void main(String[] args) {
        //listTest();
        //cloneAble();
        regg();
    }


    public static void listTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(8);
        int result = list.stream().map(d -> 1).reduce(0, (a, b) -> a + b);
        System.out.println(result);
    }

    public static void regex() {
        //用正则表达式解析uri
        String uu = "/Amumu.png";
        String regex = "/[\\w]+.png";
        //Pattern pattern=Pattern.compile(regex);
        //Matcher matcher = pattern.matcher(uri2);
        boolean matches = Pattern.matches(regex, uu);
        System.out.println(matches);

        String root = System.getProperty("user.dir") + "/resource";
        File file = new File(root + uu);
        if (file.exists()) {
            System.out.println("yes");
        } else {
            System.out.println("not found");
        }
    }

    public static void cloneAble() {
        A a1 = new A();
        a1.setName("aa");
        B b = new B();
        b.setAdd("tt");
        a1.setB(b);

        A a2 = new A();
        a2 = (A) a1.clone();
        System.out.println(a1.getB().getAdd());
        System.out.println(a2.getB().getAdd());

        b.setAdd("hh");
        System.out.println(a1.getB().getAdd());
        System.out.println(a2.getB().getAdd());
    }

    public static void regg() {
        Long start = System.currentTimeMillis();
        String str = "111223";
        String reg = "[1,2,3]*";
        boolean matches = Pattern.matches(reg, str);
        System.out.println(matches);
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}

//深克隆，浅克隆
class A implements Cloneable {
    String name;
    B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object clone() {
        A a = null;
        try {
            a = (A) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return a;
    }
}

class B {
    String add;

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}