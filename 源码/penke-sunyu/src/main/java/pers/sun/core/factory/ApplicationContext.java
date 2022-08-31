package pers.sun.core.factory;

import pers.sun.core.listener.NetListener;
import pers.sun.protocol.RequestHandler;
import pers.sun.protocol.ResponseHandler;
import pers.sun.protocol.Servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用环境-工厂模式
 *
 * @author 曹沫
 * @date 2021/9/3
 */
public class ApplicationContext {

    //说明：map中的key，默认的“defaultXXX”，或者“类名的小写”
    private Map<String, RequestHandler> requestHandlerMap;//各种不同“请求处理器”集合-暂时只有一种
    private Map<String, ResponseHandler> responseHandlerMap;//各种不同“响应处理器”集合-暂时只有一种
    private Map<String, Servlet> servletMap;//处理request，response的servlet集合-暂时只有一种
    private List<NetListener> netListeners;//网络监听器集合-暂时只有一种

    public ApplicationContext() {
        init();
    }

    //初始化各种map
    public void init() {
        requestHandlerMap = new HashMap<>();
        responseHandlerMap = new HashMap<>();
        servletMap = new HashMap<>();
        netListeners = new ArrayList<>();
    }

    //加载各种组件
    public void addRequestHandler(String key, RequestHandler requestHandler) {
        requestHandlerMap.put(key, requestHandler);
    }

    public void addResponseHandler(String key, ResponseHandler responseHandler) {
        responseHandlerMap.put(key, responseHandler);
    }

    public void addServlet(String key, Servlet servlet) {
        servletMap.put(key, servlet);
    }

    public void addNetListener(NetListener netListener) {
        netListeners.add(netListener);
    }

    //获取各种组件
    public RequestHandler getRequestHandler(String key) {
        return requestHandlerMap.get(key);
    }

    public ResponseHandler getResponseHandler(String key) {
        return responseHandlerMap.get(key);
    }

    public Servlet getServlet(String key) {
        return servletMap.get(key);
    }

    public List<NetListener> getNetListener() {
        return netListeners;
    }

}
