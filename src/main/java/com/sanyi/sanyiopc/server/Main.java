package com.sanyi.sanyiopc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Main {
    public static void main(String[] args) {
        String host = "10.0.0.4";// server
        String domain = "";// domain
        String progId = "OPCServer.WinCC.1";
        String user = "SanyiopcUser";// server上的访问用户
        String password = "OPCsanyi!@#2019";// 访问用户的密码

        OpcClient opcClient = new OpcClient();
        // 1.显示server上的opc server应用列表
        opcClient.showAllOPCServer(host, user, password, domain);

        // 2.连接指定的opc server
        boolean ret = opcClient.connectServer(host, progId, user, password, domain);
        if (!ret) {
            System.out.println("connect opc server fail");
            return;
        }

        // 3.检查opc server上的检测点
        List<String> itemIdList = new ArrayList<String>();
        itemIdList.add("GX_YTH_Pump_1.UA");
        itemIdList.add("GX_YTH_Pump_1.UB");
        ret = opcClient.checkItemList(itemIdList);
        if (!ret) {
            System.out.println("not contain item list");
            return;
        }

        // 4.注册回调
        opcClient.subscribe(new Observer() {
            @Override
            public void update(Observable observable, Object arg) {
                Result result = (Result) arg;
                System.out.println("update result=" + result);
            }
        });

        // 5.添加监听检测点的数据
        // client和server在不同网段，可以访问
        opcClient.syncReadObject("GX_YTH_Pump_1.UA", 500);
        /**
         * TODO 问题
         * client和server在不同网段，访问失败，比如：server为10.1.1.132，该网段下面又连接了扩展路由器，192.168.1.x，client为192.168.1.100
         */
        opcClient.asyncReadObject("GX_YTH_Pump_1.UB", 500);

        // 延迟
        delay(5*60*1000);
    }

    private static void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
