package com.sanyi.sanyiopc.com.zs.opc.server;

import com.sanyi.sanyiopc.bean.*;
import com.sanyi.sanyiopc.utils.OpcUtils;
import javafish.clients.opc.JOpc;
import org.junit.jupiter.api.Test;
import org.nutz.json.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class OPCShake implements Runnable {
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        OpcUtils opcUtils = new OpcUtils();
        String url = "http://10.10.10.219:5656/Miku/H5?protocol_id=";
        String Login = OPCShake.sendPost(url + "62",
                "{'data':{'user':'admin','pwd':'61F68DB563029F16325393BEFBED897C'}}");
        String Token = Json.fromJson(ResponseData.class, Login).getData().getToken();
        String Factory = OPCShake.sendPost(url + "20", "{'token':'" + Token + "','data':{}}");
        Data[] FactoryDatas = Json.fromJson(ResponseFactoryData.class, Factory).getData();
        List<Pumpdata> pumpdatas = new ArrayList<Pumpdata>();
        SocketFrame socketFrame = new SocketFrame();
        for (Data FactoryData : FactoryDatas) {
            String Id = FactoryData.getId();
            String Son = OPCShake.sendPost(url + "203", "{'token':'" + Token + "','data':{'id':'" + Id + "'}}");
            System.err.println("获取指定[" + FactoryData.getName() + "]及其下所有测点信息的返回：" + Son);
            Data SonDatas = Json.fromJson(ResponseData.class, Son).getData();
            Children[] children = SonDatas.getChildren();//每个泵站
            for (Children childrens : children) {
                Children[] childrenss = childrens.getChildren();//每个泵
                for (Children childrensss : childrenss) {
                    System.err.println(childrensss.getName());
                    String pname = opcUtils.getProperties(childrensss.getId());
                    String Son1 = OPCShake.sendPost(url + "51", "{'token':'" + Token + "','data':[{'id':'" + childrensss.getId() + "'}]}");
                    System.err.println("获取指定[" + childrensss.getName() + "]及其下所有测点信息的返回：" + Son1);
                    Data data = Json.fromJson(ResponseData.class, Son1).getData();
                    Vib[] vibs = data.getVib();
                    for (int i = 0; i < vibs.length; i++) {
                        String name = opcUtils.getProperties(vibs[i].getId());
                        if (name == null) {
                            continue;
                        }
                        Pumpdata pumpdata = new Pumpdata(name, vibs[i].getEv()[0].getPp(), pname);
                        pumpdatas.add(pumpdata);
                    }
                }
            }
        }
        OPCServer opcServer = new OPCServer();
        Pumpdata[] pumpdata = new Pumpdata[pumpdatas.size()];
        for (int i = 0; i < pumpdata.length; i++) {
            pumpdata[i] = pumpdatas.get(i);
        }
        socketFrame.setPumpdata(pumpdata);
        System.err.println(Json.toJson(pumpdatas));
        JOpc.coInitialize();
        opcServer.update(socketFrame);
        JOpc.coUninitialize();
    }

    @Override
    public void run() {
        System.out.println("开启OPCShake线程！");
        while (true) {

            OpcUtils opcUtils = new OpcUtils();
            String url = "http://10.10.10.219:5656/Miku/H5?protocol_id=";
            String Login = OPCShake.sendPost(url + "62",
                    "{'data':{'user':'admin','pwd':'61F68DB563029F16325393BEFBED897C'}}");
            String Token = Json.fromJson(ResponseData.class, Login).getData().getToken();
            String Factory = OPCShake.sendPost(url + "20", "{'token':'" + Token + "','data':{}}");
            Data[] FactoryDatas = Json.fromJson(ResponseFactoryData.class, Factory).getData();
            List<Pumpdata> pumpdatas = new ArrayList<Pumpdata>();
            SocketFrame socketFrame = new SocketFrame();
            for (Data FactoryData : FactoryDatas) {
                String Id = FactoryData.getId();
                String Son = OPCShake.sendPost(url + "203", "{'token':'" + Token + "','data':{'id':'" + Id + "'}}");
                //System.err.println("获取指定[" + FactoryData.getName() + "]及其下所有测点信息的返回：" + Son);
                Data SonDatas = Json.fromJson(ResponseData.class, Son).getData();
                Children[] children = SonDatas.getChildren();//每个泵站
                for (Children childrens : children) {
                    Children[] childrenss = childrens.getChildren();//每个泵
                    for (Children childrensss : childrenss) {
                        //System.err.println(childrensss.getName());
                        String pname = opcUtils.getProperties(childrensss.getId());
                        String Son1 = OPCShake.sendPost(url + "51", "{'token':'" + Token + "','data':[{'id':'" + childrensss.getId() + "'}]}");
                        //System.err.println("获取指定[" + childrensss.getName() + "]及其下所有测点信息的返回：" + Son1);
                        Data data = Json.fromJson(ResponseData.class, Son1).getData();
                        Vib[] vibs = data.getVib();
                        for (int i = 0; i < vibs.length; i++) {
                            String name = opcUtils.getProperties(vibs[i].getId());
                            if (name == null) {
                                continue;
                            }
                            Pumpdata pumpdata = new Pumpdata(name, vibs[i].getEv()[0].getPp(), pname);
                            pumpdatas.add(pumpdata);
                        }
                    }
                }
            }
            OPCServer opcServer = new OPCServer();
            Pumpdata[] pumpdata = new Pumpdata[pumpdatas.size()];
            for (int i = 0; i < pumpdata.length; i++) {
                pumpdata[i] = pumpdatas.get(i);
            }
            socketFrame.setPumpdata(pumpdata);
            //System.err.println(Json.toJson(pumpdatas));
            JOpc.coInitialize();
            try {
                opcServer.update(socketFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JOpc.coUninitialize();
            try {
                Thread.sleep(1000*60*60*24);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
