package pers.sun.protocol;

import pers.sun.protocol.dto.HttpResponse;

import java.io.OutputStream;

/**
 * 定义响应“处理”接口
 * 方便支持：HTTP协议应答、自定义协议的应答 等
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public interface ResponseHandler {


    /**
     * 对请求的响应
     * @param outputStream 从socket提取的输出流
     * @param response
     */
    void responseReply(OutputStream outputStream, HttpResponse response) throws Exception;

    /**
     * 设置响应的状态行
     * @param response
     */
    void setReponseStatus(HttpResponse response);

    /**
     * 设置响应的头
     * @param response
     */
    void setResponseHeader(HttpResponse response);

}
