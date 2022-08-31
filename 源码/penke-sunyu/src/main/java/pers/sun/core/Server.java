package pers.sun.core;

import pers.sun.core.factory.ApplicationContext;
import pers.sun.properties.ServerProperties;

/**
 * @author 曹沫
 * @date 2021/9/1
 */
public interface Server {
    void init(ServerProperties serverProperties, ApplicationContext applicationContext);
    void start();
    void stop();
}
