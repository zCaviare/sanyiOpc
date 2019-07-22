package com.sanyi.sanyiopc.web;

import com.sanyi.sanyiopc.com.zs.opc.server.OPCShake;
import com.sanyi.sanyiopc.server.Server;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OpcServer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();
        OPCShake opcShake = new OPCShake();
        Thread opcShakeThread = new Thread(opcShake);
        opcShakeThread.start();
    }
}
