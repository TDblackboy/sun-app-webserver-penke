package pers.sun.protocol.http;

import org.apache.commons.lang3.StringUtils;
import pers.sun.conf.ResourcesType;
import pers.sun.protocol.Servlet;
import pers.sun.protocol.dto.HttpRequest;
import pers.sun.protocol.dto.HttpResponse;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 根据请求，做出响应的，逻辑处理单元
 *
 * @author 曹沫
 * @date 2021/9/2
 */
public class DefaultHttpServlet implements Servlet {

    /**
     * 主要功能：根据请求的类型，做出不同的响应
     * 封装response：status,header,blank,body
     * 判断规则：
     * (1)method必须是Get
     * (2)uri必须是xxx.png 或 “/”
     * (3)文件必须存在
     *
     * @param request
     * @param response
     */
    @Override
    public void doService(HttpRequest request, HttpResponse response) {

        //根据请求类型，请求资源 做出响应！
        //封装response：status,header,blank,body

        //判断请求类型，不是Get
        if (StringUtils.isBlank(request.getMethod()) || !request.getMethod().toLowerCase().equals("get")) {
            resolve404Error(request, response);
            return;
        }

        //提取URI
        String uri = request.getUri();
        String regex = "/[\\w]+.png";

        if (uri.equals("/")) {
            //判断uri请求是否为主页“/”
            resolveIndex(request, response);
        } else if (Pattern.matches(regex, uri)) {
            //判断uri请求资源格式 /xxx.png
            resolvePng(request, response);
        } else {
            resolve404Error(request, response);
        }

    }


    /**
     * 资源请求表决：处理 “/” 请求-加载主页
     *
     * @param request
     * @param response
     */
    private void resolveIndex(HttpRequest request, HttpResponse response) {

        //判断文件是否存在
        String root = System.getProperty(ResourcesType.HOME.value);
        File file = new File(root + "/index.html");

        if (!file.exists()) {
            System.out.println("文件不存在:" + root + root + "/index.html");
            resolve404Error(request, response);
            return;
        }

        //文件信息怎么存？body设置为文件位置，在写入输出流时再读取文件
        response.setBody(root + "/index.html");
        response.setStatusCode(200);
        response.setStatus("OK");
    }


    /**
     * 资源请求表决：/xxx.png 请求
     *
     * @param request
     * @param response
     */
    private void resolvePng(HttpRequest request, HttpResponse response) {
        String uri = request.getUri();
        //判断文件是否存在
        String root = System.getProperty(ResourcesType.IMAGES.value);
        File file = new File(root + uri);

        if (!file.exists()) {
            System.out.println("文件不存在:" + root + uri);
            resolve404Error(request, response);
            return;
        }

        //文件信息怎么存？body设置为文件位置，在写入输出流时再读取文件
        response.setBody(root + uri);
        response.setStatusCode(200);
        response.setStatus("OK");
    }


    /**
     * 资源请求表决： 404响应
     * 设置404对应的“状态”，“body”
     *
     * @param request
     * @param response
     */
    private void resolve404Error(HttpRequest request, HttpResponse response) {
        response.setStatusCode(404);
        response.setStatus("Not Found");
        response.setBody(System.getProperty(ResourcesType.HOME.value)+"/404.html");
    }


}
