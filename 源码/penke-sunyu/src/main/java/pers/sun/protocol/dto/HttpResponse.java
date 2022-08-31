package pers.sun.protocol.dto;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体：封装了相应的数据
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public class HttpResponse {

    //说明
    //1 状态行：key=status,value="xxx code xxx"
    //2 响应头：xxx:xxx
    //3 空行： key=blank,value="\r\n"
    //4 响应体： key=body

    private String status = "";//状态描述信息，在写到输出流中为响应的状态行
    private int statusCode = 404;//状态码

    private Map<String, String> responseHeader = null;

    private String blank = "\r\n";

    private String body = "";//404时为错误的HTML字符串，200时保存图片的位置
    //private byte[] pic = null;

    //该response关联的request
    private HttpRequest request = null;

    //关联输出流
    private OutputStream outputStream;

//    public byte[] getPic() {
//        return pic;
//    }
//
//    public void setPic(byte[] pic) {
//        this.pic = pic;
//    }
    //初始化一些属性
    public HttpResponse(){
        if(responseHeader==null){
            responseHeader=new HashMap<>();
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(Map<String, String> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getBlank() {
        return blank;
    }

    public void setBlank(String blank) {
        this.blank = blank;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
