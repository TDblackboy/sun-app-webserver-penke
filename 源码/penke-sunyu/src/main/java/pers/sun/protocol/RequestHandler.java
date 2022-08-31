package pers.sun.protocol;

import pers.sun.protocol.dto.HttpRequest;

import java.io.IOException;
import java.net.Socket;

/**
 * socket请求"处理"接口的定义，不是“请求”的定义
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public interface RequestHandler {


    /**
     * 请求解析
     * @param socket accept()接受到连接请求，一般只用socket提取出的输入流
     * @return Request保存数据
     * @throws IOException
     */
    HttpRequest requestParse(Socket socket) throws IOException;

}
