package pers.sun.conf;

/**
 * 资源配置参数
 *
 * @author 曹沫
 * @date 2021/9/10
 */
public enum ResourcesType {

    IMAGES("images"),
    HOME("home");

    public String value;

    ResourcesType(String value) {
        this.value = value;
    }
}
