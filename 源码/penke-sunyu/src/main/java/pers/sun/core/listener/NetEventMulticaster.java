package pers.sun.core.listener;

/**
 * 网络事件的广播器，将 “网络请求广播” 到各个监听器中
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public interface NetEventMulticaster {

    /**
     * 广播事件
     * @param event
     */
    void multicast(NetEvent event);

}
