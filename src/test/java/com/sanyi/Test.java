package com.sanyi;

import com.sanyi.sanyiopc.bean.Pumpdata;
import com.sanyi.sanyiopc.bean.SocketFrame;
import com.sanyi.sanyiopc.com.zs.opc.server.OPCServer;
import javafish.clients.opc.JOpc;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        OPCServer opcServer=new OPCServer();
        JOpc.coInitialize();
        SocketFrame socketFrame=new SocketFrame();
        Pumpdata[] pumpdata=new Pumpdata[1];
        Pumpdata pumpdata1=new Pumpdata();
        pumpdata1.setName("Pump_TurnOff");
        pumpdata1.setPname("LJ_PUMP1");
        pumpdata[0]=pumpdata1;
        socketFrame.setPumpdata(pumpdata);
        opcServer.server(socketFrame);
        JOpc.coUninitialize();
    }
}
