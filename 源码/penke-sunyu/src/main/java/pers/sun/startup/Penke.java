package pers.sun.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.sun.conf.ResourcesType;
import pers.sun.conf.ServerParams;
import pers.sun.core.factory.ApplicationContext;
import pers.sun.core.Server;
import pers.sun.core.bio.SimpleServerV3;
import pers.sun.core.factory.BeanFactory;
import pers.sun.core.listener.SocketListener;
import pers.sun.parser.YamlParser;
import pers.sun.properties.ServerProperties;
import pers.sun.protocol.Servlet;
import pers.sun.protocol.http.DefaultHttpServlet;
import pers.sun.protocol.http.HttpRequestHandler;
import pers.sun.protocol.http.HttpResponseHandler;
import pers.sun.protocol.http2.DefaultHttpServlet2;

import java.io.IOException;
import java.util.Map;

/**
 * 服务器管理类:初始化（获取配置信息）、启动服务（将程序主要的控制权交给了server）、
 *
 * @author 曹沫
 * @date 2021/8/30
 */
public class Penke {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private ServerProperties serverProperties = null;//仅是服务器的配置信息
    private Map<String, Object> configParams = null;//server.xml中所有的配置参数
    private ApplicationContext applicationContext = null;//server运行环境：各种组件
    private BeanFactory beanFactory = null;
    private Server server = null;//核心服务

    /**
     * 加载配置文件，初始化Penke实例-属性赋值
     *
     * @param args
     */
    public void load(String[] args) {
        logger.info("Penke初始化");
        //解析server配置文件,读取配置信息-serverProperties赋值，configParams赋值
        parseServerYml();
        //System.out.println(serverProperties.toString());

        //加载各种组件
        loadContext();

        //初始化组件工厂
        try {
            loadFactory(System.getProperty("server.component"));
        } catch (Exception e) {
            //throw new Error("loadFactory初始化错误");//经过反射调用的方法抛出异常 设置为Error类型
            e.printStackTrace();
        }

        //实例化、初始化server
        try {
            //核心服务的创建过程怎么处理？
            //server = new SimpleServer();
            //server = new SimpleServerV2();
            server = new SimpleServerV3();
            server.init(serverProperties, applicationContext);
            logger.info("Penke初始化完成");
        } catch (Exception e) {
            throw new Error("server初始化错误");//为什么要往外抛异常？因为还有后续执行-"start"，如果没有，可以直接结束
        }

    }

    /**
     * 解析配置文件、给ServerProperties的实例赋值
     */
    private void parseServerYml() {
        logger.info("解析服务器配置文件");
        //解析
        configParams = YamlParser.loadConfig(System.getProperty("server.configurationFile"));
        assert configParams != null;
        //赋值

        //（1）服务器运行环境参数
        serverProperties = new ServerProperties();
        Map<String, Object> server = (Map<String, Object>) configParams.get("server");//强转警告
        if (server != null) {
            if (server.get(ServerParams.IP.value) != null)
                serverProperties.setServerIp(String.valueOf(server.get(ServerParams.IP.value)));
            if (server.get(ServerParams.PORT.value) != null)//限制格式
                serverProperties.setServerPort((Integer) server.get(ServerParams.PORT.value));
            if (server.get(ServerParams.THREAD_NUM.value) != null) {
                int threadNum = (int) server.get(ServerParams.THREAD_NUM.value);
                if (threadNum > 0 && threadNum <= ServerProperties.THREAD_MAX)
                    serverProperties.setThreadNum(threadNum);
                else
                    throw new Error("配置参数有误：\"threadNum\" maximum size is 5 and minimum size is 1，but " + threadNum);
            }

        }
        //System.out.println(server.get(ServerParams.IP.value));
        //System.out.println(server.get(ServerParams.PORT.value));
        //System.out.println(server.get(ServerParams.THREAD_NUM.value));

        //（2）资源文件位置配置参数
        Map<String, Object> resources = (Map<String, Object>) configParams.get("resources");//强转警告
        System.setProperty(ResourcesType.IMAGES.value, System.getProperty("user.dir") + resources.get(ResourcesType.IMAGES.value));
        System.setProperty(ResourcesType.HOME.value, System.getProperty("user.dir") + resources.get(ResourcesType.HOME.value));

        //System.out.println(resources.get(ResourcesType.IMAGES.value));
        //System.out.println(resources.get(ResourcesType.HOME.value));

    }


    /**
     * 设置server运行中可能用到的各种组件
     */
    private void loadContext() {
        logger.info("加载各种单例组件");
        applicationContext = new ApplicationContext();
        applicationContext.addRequestHandler("defaultRequestHandler", HttpRequestHandler.getInstance());
        applicationContext.addResponseHandler("defaultResponseHandler", HttpResponseHandler.getInstance());
        applicationContext.addServlet("defaultServlet", new DefaultHttpServlet());//其实在context中保证了“单例”
        //加载监听器
        applicationContext.addNetListener(new SocketListener());
    }


    /**
     * 初始化BeanFactory，让他加载各种组件，其实个ApplicationContext作用一样，但是ApplicationContext需要一级一级的往下传递“引用”
     * BeanFactory的属性、方法设置成了static类型的了，只要类加载了就可以直接使用
     *
     * @param properties
     */
    private void loadFactory(String properties) throws Exception {
        beanFactory = BeanFactory.getFactory();

        //beanFactory初始化
        beanFactory.loadBean(properties);

        //测试是否将Bean加载进去了
//        if(BeanFactory.getBean("defaultServlet") instanceof Servlet){
//            System.out.println("defaultServlet");
//        }
//        DefaultHttpServlet2 defaultServlet = (DefaultHttpServlet2) BeanFactory.getBean("defaultServlet");
//        assert defaultServlet != null;
//        System.out.println(defaultServlet.getClass());
//        defaultServlet.test();
        logger.info("初始化BeanFactory");
    }


    /**
     * 启动服务
     */
    public void start() {
        logger.info("准备启动服务");
        if (server == null) {
            logger.error("no server found.");//没有服务直接返回
            return;
        }

        server.start();//交给server执行，

    }

    /**
     * 关闭服务
     */
    public void stop() {
        logger.info("关闭");
    }

}
