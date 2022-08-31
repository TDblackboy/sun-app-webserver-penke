package pers.sun.core.factory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 利用工厂模式获取一个class的一个实例-且是单例的
 * 问题：
 * 预先创建哪些单例？
 * 怎么获取呢？
 *
 * @author 曹沫
 * @date 2021/9/12
 */
public class BeanFactory {

    private static BeanFactory daemon = new BeanFactory();//通过它调用非静态方法
    private static Map<String, Object> singleton;//核心属性

    //这个factory也是单例的
    private BeanFactory() {
    }

    public static BeanFactory getFactory() {
        return daemon;
    }

    /**
     * 加载 bean
     *
     * @param properties 配置清单文件的路径
     */
    public void loadBean(String properties) throws Exception {
        if (singleton == null)
            singleton = new HashMap<>();

        //创建哪些bean？ 需要一个“配置清单”-声明了多个需要加载的组件！
        //读取-解析文件
        Properties prop = new Properties();
        //加载类路径下的文件
        //InputStream in = this.getClass().getClassLoader().getResourceAsStream(properties);
        //加载文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader(properties));
        prop.load(bufferedReader);
        Set<Object> keys = prop.keySet();

        //创建实例加入map集合
        for (Object key : keys) {
            //System.out.println(key);
            //System.out.println(prop.getProperty((String) key));
            singleton.put((String) key, generateBean(prop.getProperty((String) key)));
        }


    }


    /**
     * 根据全限定类名生成一个实例
     *
     * @param className
     * @return
     */
    private Object generateBean(String className) throws Exception {
        //反射API，创建对象
        Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
        return aClass.getConstructor().newInstance();
    }


    /**
     * 获取 bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        if (singleton == null)
            return null;
        if (singleton.isEmpty())
            return null;

        return singleton.get(beanName);

    }


}
