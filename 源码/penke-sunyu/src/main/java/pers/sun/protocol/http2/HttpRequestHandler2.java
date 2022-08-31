package pers.sun.protocol.http2;

import org.apache.commons.lang3.StringUtils;
import pers.sun.protocol.RequestHandler;
import pers.sun.protocol.dto.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP协议的请求解析
 * 设置成 单例的：因为不保存数据，只是用来提供解析数据的方法，且方法中的局部变量为线程私有的不会共享即保证了线程安全性
 * 单例效果实现方式：通过BeanFactory的单例map实现
 *
 * 支持的请求规定
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public class HttpRequestHandler2 implements RequestHandler {


    /**
     * HTTP协议请求组成
     * 请求行: 请求方法 URI  协议/版本
     * 消息报头/请求头信息 :  几行header,key-value
     * 空行： 只包含回车换行符(CRLF \n\r)
     * 请求体/传参：
     * <p>
     * 注意：get和post请求方式不同，有些浏览器发送post请求时需要服务器响应一个 100 Continue！！！！
     *
     * @param socket
     * @return
     * @throws IOException
     */
    @Override
    public HttpRequest requestParse(Socket socket) throws IOException {
        //保存解析http请求的结果
        HttpRequest request = new HttpRequest();

        //开始解析！
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        //第一行
        String str;
        str = bufferedReader.readLine();
        parseHttpRequestLine(request,str);

        //请求头-读到空行结束
        Map<String, String> map = new HashMap<>();
        while ((str = bufferedReader.readLine()) != null && StringUtils.isNotBlank(str)) {
            //System.out.println(str);
            parseHttpRequestHeader(map, str);
        }
        request.setRequestHeader(map);


        return request;
    }


    /**
     * 解析HTTP请求行
     * 包括：请求方法method，请求资源uri，协议
     * @param request
     * @param line
     */
    private void parseHttpRequestLine(HttpRequest request,String line) {
        //System.out.println(line);
        //
        if(StringUtils.isNotBlank(line)){
            //Map<String,String> map=new HashMap<>();
            String[] split = line.split(" ");
//            map.put("method", split[0]);
//            map.put("uri", split[1]);
//            map.put("protocol", split[2]);
//            request.setFirstLine(map);
            request.setMethod(split[0]);
            request.setUri(split[1]);
            request.setProtocol(split[2]);
        }

    }


    /**
     * 解析HTTP请求头
     * 包括：key-value
     *
     * @param map
     * @param line
     */
    private void parseHttpRequestHeader(Map<String, String> map, String line) {
        String[] split = line.split(": ");
        map.put(split[0], split[1]);
    }


    /**
     * 解析Get请求体
     */
    private void parseGetBody() {
        //get请求的body没有参数！
    }


    /**
     * 解析Post请求体
     */
    private void parsePostBody(Map<String, String> map, String line) {
        //如果是post请求响应一个100 Continue回复
        System.out.println("响应100");
        //response100Continue(socket);
        System.out.println(line);
    }

    /**
     * 如果是post请求时，发送一个100 continue
     */
    private void response100Continue(Socket socket) {
        try {
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("HTTP/1.1 200 OK");
            printStream.println("Content-Type:text/html;charset=utf-8");
            printStream.println("Content-Length:5");
            printStream.println();
            printStream.println("hello");
            //printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
