package pers.sun.protocol.http2;

import pers.sun.protocol.ResponseHandler;
import pers.sun.protocol.dto.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * HTTP协议的响应
 * 1、协议 状态码  描述  (例： HTTP/1.1 200 OK)
 * 2、响应头信息
 * 3、响应体
 * <p>
 * 单例效果实现方式：通过BeanFactory的单例map实现
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public class HttpResponseHandler2 implements ResponseHandler {

    //响应格式
    private static final String CRLF = "\r\n";


    /**
     * 响应
     *
     * @param outputStream 从socket提取的输出流
     * @param response
     */
    @Override
    public void responseReply(OutputStream outputStream, HttpResponse response) throws Exception {

        //输出-打印流处理-字符流，自动flush，输出缓存
        //PrintWriter printWriter = new PrintWriter(outputStream, true);
        //输出流-字节流
        PrintStream printStream = new PrintStream(outputStream, true);

        //状态行
        printStream.println(response.getStatus());

        //响应头
        Map<String, String> header = response.getResponseHeader();
        for (String s : header.keySet()) {
            printStream.println(s + ":" + header.get(s));
        }

        //空行
        printStream.println();//空行结束消息头

        //响应体
        //response的body中保存的时文件路径，需要读取文件再输出
        File file = new File(response.getBody());
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];//设置内存大小
        int read = fileInputStream.read(bytes);
        //printStream.println(bytes);//print输出的char，write输出byte!不能用println()
        printStream.write(bytes);

        //第一个版本的输出格式
//        if (response.getStatusCode() == 200) {
//            //图片，从body中取出文件保存路径，进行文件读写
//            File file = new File(response.getBody());
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte[] bytes = new byte[fileInputStream.available()];//设置内存大小
//            int read = fileInputStream.read(bytes);
//            //printStream.println(bytes);//print输出的char，write输出byte!不能用println()
//            printStream.write(bytes);
//        } else {
//            printStream.println(response.getBody());
//        }


    }


    /**
     * 设置响应的状态行
     * 状态：200，404，500
     * 判断规则：必须是Get，不带参数，请求体为空
     *
     * @param response
     */
    @Override
    public void setReponseStatus(HttpResponse response) {
        response.setStatus(response.getRequest().getProtocol() +
                " " + response.getStatusCode() +
                " " + response.getStatus());
    }


    /**
     * 设置响应头:
     * content-type,
     * content-length,
     * server
     *
     * @param response
     */
    @Override
    public void setResponseHeader(HttpResponse response) {
        Map<String, String> map = response.getResponseHeader();
        int i = response.getBody().lastIndexOf(".");
        String ext = response.getBody().substring(i + 1);

        //根据返回内容的类型设置 content-type,
        if (ext.contains("png")) {
            map.put("Content-Type", "application/binary");
        } else {
            map.put("Content-Type", "text/html");
        }

        File file = new File(response.getBody());
        map.put("Content-Length", "" + file.length());

//        if(response.getStatusCode()==200){
//            map.put("Content-Type","application/binary");
//        }else{
//            map.put("Content-Type","text/html");
//            map.put("Content-Length",String.valueOf(response.getBody().getBytes().length));
//        }
        map.put("Server", "Penke/v1");
    }


}
