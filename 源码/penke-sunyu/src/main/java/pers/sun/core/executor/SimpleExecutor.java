package pers.sun.core.executor;

import pers.sun.core.factory.ApplicationContext;
import pers.sun.properties.ServerProperties;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行任务的执行器
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public class SimpleExecutor {

    //线程池
    private static ExecutorService executorService = null;
    //引用上下文
    private static ApplicationContext applicationContext = null;


    //初始化
    public void init(ServerProperties serverProperties, ApplicationContext context) {
        executorService = Executors.newFixedThreadPool(serverProperties.getThreadNum());
        applicationContext = context;
    }

    //执行
    public static void execute(Socket socket) {
        if (executorService == null) {
            return;
        }
        executorService.execute(new StandardWorker(socket, applicationContext));
    }


}
