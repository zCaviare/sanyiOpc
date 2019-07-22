package com.sanyi.sanyiopc.web;

import com.sanyi.sanyiopc.bean.SocketFrame;
import com.sanyi.sanyiopc.com.zs.opc.server.OPCServer;
import javafish.clients.opc.JOpc;
import org.nutz.json.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/use")
public class OpcController {
    private int i = 0;
    private OPCServer opcServer = new OPCServer();

    @RequestMapping(value = "/live", method = RequestMethod.POST)
    @ResponseBody
    public synchronized Object data(@RequestBody SocketFrame socketFrame) throws Exception {
        i++;
        if (i == 1) {
            System.out.println("只执行一次！");
            JOpc.coInitialize();
        }
        SocketFrame pump = new SocketFrame();
        pump = socketFrame;
        System.out.println("传递值：" + i);
        try {
            if (socketFrame.getType().equals("select")) {
                pump = opcServer.server(socketFrame);
            } else if (socketFrame.getType().equals("update")) {
                opcServer.update(socketFrame);
                pump = socketFrame;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Json.toJson(pump.getPumpdata());
        }
        System.out.println("返回值：" +i);
        return Json.toJson(pump.getPumpdata());
    }

  /*  public static void main(String[] argv) throws Exception {
        String command = "taskkill /f /im sopcsrvrwincc.exe";
        Runtime.getRuntime().exec(command);
    }*/
}
