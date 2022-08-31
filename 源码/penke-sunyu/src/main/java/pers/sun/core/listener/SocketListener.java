package pers.sun.core.listener;

import pers.sun.core.executor.SimpleExecutor;

import java.net.Socket;

/**
 * socket监听器：事件网络监听器接口
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public class SocketListener implements NetListener {

    /**
     * 处理 “网络事件”：类型为TCP连接请求
     * 处理逻辑：开启线程处理socket
     *
     * @param event
     */
    @Override
    public void onNetEvent(NetEvent event) {
        //判断事件类型，是否支持该类型的网络事件
        if (event.getType().equals(EventType.TCP_LINKING)) {
            SimpleExecutor.execute((Socket) event.getSource());//交给他执行
        }
    }
}
