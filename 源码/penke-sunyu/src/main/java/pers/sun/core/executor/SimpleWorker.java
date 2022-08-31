package pers.sun.core.executor;

import pers.sun.util.ObjectUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Sokcet处理的 简单实现
 *
 * @author 曹沫
 * @date 2021/9/1
 */
public class SimpleWorker implements Runnable{

    //线程拥有的核心私有资源，
    private Socket socket;

    /**
     * 构造函数完成初始化
     *
     * @param socket1
     */
    public SimpleWorker(Socket socket1){
        this.socket=socket1;
    }


    /**
     * 核心：解析HTTP请求，返回响应
     */
    @Override
    public void run() {
        System.out.println("哪个线程："+Thread.currentThread().getName());
        System.out.println("哪个连接Socket："+ ObjectUtils.printObjectAddress(socket));

        //io流处理
        //用什么流处理？
        //1 BufferedReader
        //2 直接用 InputStream inputStream = socket.getInputStream();
        //缓冲区大小设置？

        //使用字符流
        handleByBufferIO();

        //使用字节流
        //handleByByteIO();

    }

    //简单实现
    //通过BufferIO流（字符流）处理
    private void handleByBufferIO(){
        try {
            //读：一行一行的读
            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = bufferedReader.readLine())!=null){
                //输出打印
                System.out.println(str);
            }

            //写回
            BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            str="Hello "+socket.getInetAddress()+":"+socket.getPort();
            bufferedWriter.write(str);

            //释放资源
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //通过字节流：InputStream，OutPutStream
    private void handleByByteIO(){
        try {
            //读：一行一行的读
            InputStream inputStream=socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
            while ((len = inputStream.read(bytes)) != -1) {
                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
                System.out.println(sb);
            }

            //写回
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello Client,I get the message.".getBytes(StandardCharsets.UTF_8));

            //释放资源
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
