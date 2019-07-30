package com.sanyi.sanyiopc.quartz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //每隔2秒执行一次
    @Scheduled(fixedRate = 60000*10)
    public void testTasks() throws IOException {
        System.out.println("结束OPC连接服务！");
        String command = "taskkill /f /im sopcsrvrwincc.exe";
        Runtime.getRuntime().exec(command);
    }

}
