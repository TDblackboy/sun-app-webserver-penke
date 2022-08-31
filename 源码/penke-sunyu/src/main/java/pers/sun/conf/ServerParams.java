package pers.sun.conf;

/**
 * 服务器运行配置参数
 *
 * @author 曹沫
 * @date 2021/9/10
 */
public enum ServerParams {
    IP("ip"),
    PORT("port"),
    THREAD_NUM("threadNum");

    public String value;

    ServerParams(String value) {
        this.value=value;
    }

}
