package pers.sun.startup;


import ch.qos.logback.classic.util.ContextInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 服务器启动类
 * 主要完成：（1）实例化服务器管理类Penke（初始化由Penke自己实现）、（2）终端命令参数的解析、交给Penke执行
 *
 * @author 曹沫
 * @date 2021/8/30
 */
public final class Bootstrap {

    //初始化：设置System的变量
    static {
        //设置日志配置文件的位置
        //"logback.configurationFile"
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, System.getProperty("user.dir") + "/conf/log.xml");

        //设置server.xml配置文件的路径
        System.setProperty("server.configurationFile", System.getProperty("user.dir") + "/conf/server.yml");

        //设置访问资源的位置 - 放到Penke中设置-因为在配置文件中有动态设置的值
        //System.setProperty("server.resource", System.getProperty("user.dir") + "/resource");

        //设置组件清单文件component.properties的位置
        System.setProperty("server.component", System.getProperty("user.dir") + "/conf/component.properties");

    }

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private static final Object daemonLock = new Object();
    private static volatile Bootstrap daemon = null;
    private Object penkeDaemon = null;//核心对象

    /**
     * 程序入口
     *
     * @param args
     */
    public static void main(String[] args) {
        logger.info("启动Bootstrap");
        //初始化:1、实例化一个启动类，2、调用初始化方法
        //防止多次实例化启动类，意思是堆中只有一个该类的实例
        synchronized (daemonLock) {
            if (daemon == null) {
                Bootstrap bootstrap = new Bootstrap();
                try {
                    bootstrap.init();
                } catch (Exception e) {
                    //e.printStackTrace();
                    logger.error("启动失败：" + e.getMessage());
                    return;
                }
                daemon = bootstrap;
            }
        }


        //解析命令 - 交付执行命令
        try {
            String command = "start";//默认
            //控制台传参
            if (args.length > 0) {
                command = args[args.length - 1];
            }
            logger.info("解析命令：" + command);

            //解析
            if (command.equals("start")) {
                daemon.load(args);//初始化核心实例 penkeDaemon
                daemon.start();
            } else if (command.equals("stop")) {
                //daemon.stop();
                System.exit(0);//直接退出
            } else {
                throw new Exception("参数错误:" + Arrays.toString(args));
            }
        } catch (Throwable t) {
            //问题：通过反射调用方法时抛出的异常如果是Exception且用Exception捕捉的话捕捉不到
            //解决：反射调用的方法抛出Error（重点），再用Throwable/Exception捕捉即可
            //InvocationTargetException异常包装了反射方法的中真实的异常类型
            if (t instanceof InvocationTargetException && t.getCause() != null) {
                t = t.getCause();
            }
            //t.printStackTrace();
            logger.error("执行错误：" + t.getMessage());
            System.exit(1);
        }

    }

    /**
     * 初始化：创建核心对象 Penke
     * 尝试使用放射机制创建对象！
     */
    private void init() throws Exception {
        logger.info("Penke实例化");
        //tomcat源码中还有设置类加载器-自定义了类加载器，类加载器工厂！，先省略！
        Class<?> coreClass = this.getClass().getClassLoader().loadClass("pers.sun.startup.Penke");
        penkeDaemon = coreClass.getConstructor().newInstance();
    }

    /**
     * 通过反射调用“核心实例”的load方法
     */
    private void load(String[] args) throws Exception {
        String methodName = "load";
        Object[] param;
        Class<?>[] paramTypes;

        paramTypes = new Class[1];
        paramTypes[0] = args.getClass();
        param = new Object[1];
        param[0] = args;

        Method method = penkeDaemon.getClass().getMethod(methodName, paramTypes);
        method.invoke(penkeDaemon, param);


    }

    /**
     * 通过反射调用“核心实例”的start方法
     */
    private void start() throws Exception {
        if (penkeDaemon == null) {
            init();
        }
        String methodName = "start";
        Method method = penkeDaemon.getClass().getMethod(methodName, (Class<?>[]) null);
        method.invoke(penkeDaemon, (Object[]) null);
    }

    /**
     * 通过反射调用“核心实例”的stop方法
     */
    public void stop() throws Exception {
        Method method = penkeDaemon.getClass().getMethod("stop", (Class<?> []) null);
        method.invoke(penkeDaemon, (Object []) null);
    }

}
