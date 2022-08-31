package pers.sun.properties;

/**
 * 服务器配置信息类
 *
 * @author 曹沫
 * @date 2021/8/30
 */
public class ServerProperties {

    public final static int THREAD_MAX=5;//最多5个连接线程
    private String serverIp="127.0.0.1";//默认IP-本地
    private int serverPort=8050;//默认端口
    private int threadNum=3;//默认使用3个线程

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public String toString() {
        return "ServerProperties{" +
                "serverIp='" + serverIp + '\'' +
                ", serverPort=" + serverPort +
                ", threadNum=" + threadNum +
                '}';
    }
}
