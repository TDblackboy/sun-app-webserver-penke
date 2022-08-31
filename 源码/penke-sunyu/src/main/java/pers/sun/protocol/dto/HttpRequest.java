package pers.sun.protocol.dto;

import java.io.InputStream;
import java.util.Map;

/**
 * 实体：保存解析的请求数据
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public class HttpRequest {

    //HTTP协议请求组成
    //private String first="";//请求行: 请求方法 URI  协议/版本
    //private Map<String, String> firstLine =null;//请求行: 请求方法 URI  协议/版本
    private String method;
    private String protocol;
    private String uri;
    private Map<String, String> requestHeader = null;//消息报头/请求头信息 :  几行header,key-value
    private String body = null;//请求体-传参

    //这个Request关联的Response
    private HttpResponse response = null;

    //关联输入流
    private InputStream inputStream;

    //
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

//    public Map<String, String> getFirstLine() {
//        return firstLine;
//    }
//
//    public void setFirstLine(Map<String, String> firstLine) {
//        this.firstLine = firstLine;
//    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
