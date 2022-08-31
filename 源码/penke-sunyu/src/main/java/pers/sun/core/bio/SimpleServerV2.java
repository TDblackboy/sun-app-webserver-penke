package pers.sun.core.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.sun.core.factory.ApplicationContext;
import pers.sun.core.Server;
import pers.sun.core.executor.StandardWorker;
import pers.sun.properties.ServerProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 核心服务类 第二个版本
 * 利用线程池执行连接请求
 *
 * @author 曹沫
 * @date 2021/9/7
 */
public class SimpleServerV2 implements Server {

    //运行上下文
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);
    private ServerProperties serverProperties = null;
    private ApplicationContext applicationContext = null;//server运行环境：各种组件

    //server运行时自己的控制变量
    private ServerSocket serverSocket = null;//核心启动对象！
    private Boolean shutdown = false;//是否关闭ServerSocket

    //线程池
    private ExecutorService executorService = null;


    @Override
    public void init(ServerProperties serverProperties, ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        //利用配置文件的配置信息创建线程池，核心线程数=最大线程数
        this.executorService = Executors.newFixedThreadPool(serverProperties.getThreadNum());

        logger.info("初始化服务...");
    }

    @Override
    public void start() {
        logger.info("启动服务...");
        try {
            //连接设置
            //配置的端口，连接请求队列最大5，ip：配置的IP
            //InetAddress.getLocalHost()
            serverSocket = new ServerSocket(
                    serverProperties.getServerPort(),
                    5,
                    InetAddress.getByName(serverProperties.getServerIp()));

            logger.info("服务已启动...");
        } catch (IOException e) {
            logger.info("服务启动失败...");
            e.printStackTrace();
        }

        //核心服务
        doService();

    }

    @Override
    public void stop() {
        shutdown = true;
    }


    /**
     * 核心服务
     */
    private void doService() {
        while (!shutdown) {
            Socket socket = null;
            try {
                logger.info("等待连接...");
                socket = serverSocket.accept();//监听、阻塞等待-----没有连接会一直阻塞

                //多线程，处理连接：每一个连接对应一个线程，（1）先用new来创建线程，（2）后期优化为线程池
                logger.info("已有连接，开启线程处理...");
                executorService.execute(new StandardWorker(socket, applicationContext));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
