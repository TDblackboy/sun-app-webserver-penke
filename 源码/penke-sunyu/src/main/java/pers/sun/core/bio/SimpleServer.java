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
import java.util.HashMap;
import java.util.Map;

/**
 * 核心Socket服务类：开启、监听端口，管理线程资源（开启线程个数）
 *
 * @author 曹沫
 * @date 2021/8/31
 */
public class SimpleServer implements Server {

    //运行上下文
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);
    private ServerProperties serverProperties = null;
    private ApplicationContext applicationContext=null;//server运行环境：各种组件

    //server运行时自己的控制变量
    private ServerSocket serverSocket = null;//核心启动对象！
    private Boolean shutdown = false;//是否关闭ServerSocket

    //线程池
    private Map<String, Thread> threadMap = null;//保存已开启的线程的引用

    /**
     * 初始化
     * @param serverProperties
     * @param applicationContext
     */
    public void init(ServerProperties serverProperties, ApplicationContext applicationContext) {
        logger.info("初始化服务...");
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
    }



    /**
     * 死循环，一直监听端口，
     * 有连接就开一个线程,但是最大支持线程数量由配置决定
     *
     * @throws IOException
     */
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

        //启动服务后，等待处理连接请求
        threadMap = new HashMap<>();
        doService();

    }

    @Override
    public void stop() {
        shutdown=true;
    }


    /**
     * 处理连接请求
     * <p>
     * 代码结构的 “重构”：使用监听器模式，定义一个connector 监听连接请求，监听到请求后（将socket）交给相应的处理者处理
     */
    private void doService() {

        int index = 0;
        int loopFlag=0;
        while (!shutdown) {
            Socket socket = null;
            //限制最大线程数量
            if (index < serverProperties.getThreadNum()) {
                try {
                    logger.info("等待连接...");
                    socket = serverSocket.accept();//监听、阻塞等待-----没有连接会一直阻塞

                    //多线程，处理连接：每一个连接对应一个线程，（1）先用new来创建线程，（2）后期优化为线程池
                    logger.info("已有连接，开启线程处理...");
                    Thread thread = new Thread(new StandardWorker(socket,applicationContext));
                    //Thread thread = new Thread(new SimpleExecutor(socket));
                    thread.setName("Thread-" + (index + 1));
                    threadMap.put("Thread-" + (index + 1), thread);
                    index++;
                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                    //logger.info("");
                }
            } else{

//                //判断线程的状态
//                if(loopFlag<1){
//                    int i=index;
//                    while(i>0){
//                        Thread thread= threadMap.get("Thread-"+i);
//                        System.out.println(thread.getName()+",thread.getState():"+thread.getState());
//                        System.out.println(thread.getName()+",thread.isAlive():"+thread.isAlive());
//                        i--;
//                    }
//                    loopFlag++;
//                }
//
//                //往日志中写
//                int i=index;
//                while(i>0){
//                    Thread thread= threadMap.get("Thread-"+i);
//                    logger.info(thread.getName()+",thread.getState():"+thread.getState());
//                    logger.info(thread.getName()+",thread.isAlive():"+thread.isAlive());
//                    i--;
//                }

            }

        }


    }

}



//问题：开启等待连接-循环进入serversocket的accept()等待，结束服务是不等待了！同时结束所有线程！
// shutdown标志位：判断是否开启循环等待连接！

//问题：限制线程开启的个数
//设置一个变量：标识线程开启个数
//      增加时机：创建一个线程，就+1
//      减小时机：线程结束时得到反馈，-1？怎么得到线程结束的反馈？

//问题：开启线程-怎么关闭线程？
//（1）线程开启时机：new 一个线程实例，调用start(),进入“可运行”状态
//（2）线程结束时机：什么时候表明线程“顺利结束”？
//          开启的线程在执行run方法中，执行结束，进入terminated状态。
//          线程结束后便会销毁，不能再次start,只能重新建立新的线程对象
//          socket解析完成，并关闭了，run方法结束了，标识线程就结束了。

//问题：获得socket，怎么处理？，怎么结束？
//socket创建时机：serverSocket accept()接收到一个连接请求后创建 socket
//socket结束时机：通过socket获取输入输出流，完成一次读、写，执行socket.close()

//问题：从socket获取的输入输出流什么时候关闭？
//输入流-开启时机：socket.getInputStream()开启
//输出流-关闭时机：
//输出流-开启时机：socket.getOutputStream()开启
//输出流-关闭时机：