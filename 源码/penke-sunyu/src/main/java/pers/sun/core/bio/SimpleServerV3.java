package pers.sun.core.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.sun.core.factory.ApplicationContext;
import pers.sun.core.Server;
import pers.sun.core.executor.SimpleExecutor;
import pers.sun.core.listener.EventType;
import pers.sun.core.listener.NetEvent;
import pers.sun.core.listener.NetEventMulticaster;
import pers.sun.core.listener.NetListener;
import pers.sun.properties.ServerProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 核心服务类 第三个版本
 * 利用监听器模式实现：连接请求的处理,将连接请求广播给监听器，存在一个合适的监听器来处理此事件（请求连接的事件）
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public class SimpleServerV3 implements Server, NetEventMulticaster {

    //运行上下文
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);
    private ServerProperties serverProperties = null;
    private ApplicationContext applicationContext = null;//server运行环境：各种组件,在这个版本中其实没必要保留这个字段了

    //server运行时自己的控制变量
    private ServerSocket serverSocket = null;//核心启动对象！
    private Boolean shutdown = false;//是否关闭ServerSocket

    //线程处理核心类
    private SimpleExecutor executor = null;

    @Override
    public void init(ServerProperties serverProperties, ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        executor = new SimpleExecutor();
        executor.init(serverProperties,applicationContext);
        logger.info("初始化服务...");
    }

    @Override
    public void start() {
        logger.info("启动服务...");
        try {
            serverSocket = new ServerSocket(
                    serverProperties.getServerPort(),
                    5,
                    InetAddress.getByName(serverProperties.getServerIp()));
            logger.info("服务已启动...");

            //等待连接请求
            while (!shutdown) {
                Socket socket = null;
                try {
                    logger.info("等待连接...");
                    socket = serverSocket.accept();//监听、阻塞等待-----没有连接会一直阻塞
                    //有一个连接请求就表明一个“网络事件”
                    multicast(new NetEvent(socket, EventType.TCP_LINKING, null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            logger.info("服务启动失败...");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        shutdown = true;
    }

    @Override
    public void multicast(NetEvent event) {
        for (NetListener netListener : applicationContext.getNetListener()) {
            netListener.onNetEvent(event);
        }
    }
}
