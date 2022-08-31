package pers.sun.test.design.model;

import java.util.List;

/**
 * 观察者模式
 *
 * @author 曹沫
 * @date 2021/9/8
 */
public class ObserverPattern {

}



//主题：发生变化的主体
class Subject{
    List<Observer> observers;//观察者

    //绑定一个新的观察者
    void attachObserver(Observer observer){

    }

    //解除绑定
    void detachObserver(){

    }

    //通知所有该主题的观察者
    void notifyObservers(){

    }

}

//观察者：根据变化做出响应的动作
class Observer{

}