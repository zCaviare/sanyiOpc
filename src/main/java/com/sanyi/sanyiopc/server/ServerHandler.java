package com.sanyi.sanyiopc.server;

import com.sanyi.sanyiopc.bean.SocketFrame;
import com.sanyi.sanyiopc.com.zs.opc.server.OPCServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import javafish.clients.opc.JOpc;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.json.Json;

import javax.sql.DataSource;
import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static Ioc ioc;
    private int n = 0;
    private OPCServer opcServer = new OPCServer();

    public DataSource getDataSource() { // 暂不考虑线程同步的问题
        if (ioc == null)
            ioc = new NutIoc(new JsonLoader("config/ioc/dao.json"));
        return ioc.get(DataSource.class);
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        SocketFrame pumps = Json.fromJson(SocketFrame.class, msg);
        System.err.println(ctx.channel().remoteAddress() + "," + msg + "接收时间:" + new Date() + " 类型：" + pumps.getType());
        SocketFrame pump = new SocketFrame();
        try {
            JOpc.coInitialize();
            pump = opcServer.server(pumps);
            //JOpc.coUninitialize();
        } catch (Exception e) {
            System.out.println("catch");
            pump.setType("error");
        } finally {
            System.out.println("finally");
            JOpc.coUninitialize();
            ctx.channel().writeAndFlush(Json.toJson(pump));
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.channel().close();
    }
}