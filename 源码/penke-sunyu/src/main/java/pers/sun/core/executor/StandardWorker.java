package pers.sun.core.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.sun.core.factory.ApplicationContext;
import pers.sun.protocol.ResponseHandler;
import pers.sun.protocol.dto.HttpRequest;
import pers.sun.protocol.dto.HttpResponse;

import java.net.Socket;

/**
 * 是一个线程类，处理一个socket
 * <p>
 * 共享资源？没有，资源=一个socket，每一个socket都是独立的资源，不共享
 *
 * @author 曹沫
 * @date 2021/8/31
 */
public class StandardWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StandardWorker.class);

    //线程拥有的核心私有资源，
    private Socket socket = null;

    //线程运行中用到的上下文环境组件
    ApplicationContext applicationContext = null;

    /**
     * 构造函数完成初始化
     *
     * @param socket
     * @param applicationContext
     */
    public StandardWorker(Socket socket, ApplicationContext applicationContext) {
        this.socket = socket;
        this.applicationContext = applicationContext;
    }


    /**
     * 核心：解析HTTP请求，返回响应
     */
    @Override
    public void run() {

        //io流处理 用什么流处理？
        //  读：BufferedReader
        //  写：字节流？
        //  缓冲区大小设置:内存中byte[size]

        handle();

    }


    /**
     * 请求处理
     */
    private void handle() {

        try {
            //解析请求的所有参数
            HttpRequest request = applicationContext.getRequestHandler("defaultRequestHandler").requestParse(socket);
            logger.info("HTTP请求已解析封装 " + Thread.currentThread().getName());

            logger.info(request.getMethod());
            logger.info(request.getProtocol());
            logger.info(request.getUri());
            logger.info(request.getRequestHeader().toString());

            //request和response相互关联
            HttpResponse response = new HttpResponse();
            request.setResponse(response);
            response.setRequest(request);

            //分析请求，得到响应
            //选择一个servlet处理，根据“请求路径”找到对应映射？！
            //servlet作用：设置response的状态行（status code、status）、body；
            applicationContext.getServlet("defaultServlet").doService(request, response);
            //设置response的status，header，blank=默认的
            ResponseHandler responseHandler = applicationContext.getResponseHandler("defaultResponseHandler");
            responseHandler.setReponseStatus(response);//设置response的status
            responseHandler.setResponseHeader(response);//设置response的header

            logger.info("HTTP请求已通过处理 " + Thread.currentThread().getName());
            logger.info(response.getStatus());
            logger.info(response.getResponseHeader().toString());
            logger.info(response.getBody());

            //响应：将响应实体写回客户端
            responseHandler.responseReply(socket.getOutputStream(), response);
            logger.info("HTTP请求已写回客户端 " + Thread.currentThread().getName());

            //关闭socket,一次请求响应结束！
            socket.close();

            //线程结束

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
