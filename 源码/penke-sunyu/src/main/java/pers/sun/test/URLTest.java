package pers.sun.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 曹沫
 * @date 2021/9/10
 */
public class URLTest {
    public static void main(String[] args) {
        //t1();
        t2();
    }

    public static void t1() {
        String uri = "http://www.runoob.com/index.html?language=cn#j2se";
        try {
            URL url = new URL(uri);
            System.out.println(url.getProtocol());
            System.out.println(url.getHost());
            URLConnection urlConnection = url.openConnection();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void t2() {

        //(1)请求的 URI
        URL url = null;
        try {
            url = new URL("http://www.w3cschool.cn");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //(2)获取“连接”
        URLConnection urlConnection = null;
        try {
            assert url != null;
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        if (urlConnection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) urlConnection;
        } else {
            System.out.println("Please enter an HTTP URL.");
        }

        //(3)从连接中“读写”资源:获取“输入输出”流
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String urlString = "";
        String current;
        try {
            assert in != null;
            while ((current = in.readLine()) != null) {
                urlString += current;
            }
            System.out.println(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


