package pers.sun.core.listener;

import java.util.EventObject;

/**
 * 在tomcat源码中LifecycleEvent有三个属性
 *          Object context; - 环境上下文
 *          String type; - 事件类型
 *          EventObject的 Object source; - 元数据
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public final class NetEvent extends EventObject {
    //继承EventObject，还有一个属性 source
    private final EventType type;
    private final Object context;

    public NetEvent(Object source, EventType type, Object data) {
        super(source);
        this.type = type;
        this.context = data;
    }

    public EventType getType() {
        return type;
    }

    public Object getContext() {
        return context;
    }
}
