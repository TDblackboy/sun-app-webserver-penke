package pers.sun.core.listener;

import java.net.Socket;

/**
 * 网络监听器接口
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public interface NetListener {
    /**
     * 接受一个“网络事件”，怎么对它处理由其实现类完成！
     * @param event
     */
    void onNetEvent(NetEvent event);
}
