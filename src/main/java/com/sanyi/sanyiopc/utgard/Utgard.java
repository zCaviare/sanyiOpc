package com.sanyi.sanyiopc.utgard;

import com.sanyi.sanyiopc.bean.Pumpdata;
import com.sanyi.sanyiopc.bean.SocketFrame;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.junit.jupiter.api.Test;
import org.nutz.ioc.loader.annotation.IocBean;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.da.AutoReconnectController;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

@IocBean
public class Utgard {
    public static void main(String[] args) {
        /*
        ClsId=050b663d-142c-4e4a-9bec-e4c372e72344 ProgId=CCOPC.UAWrapper.1 Description=CCOPC.UAWrapper
        ClsId=1d3f9b73-ddb3-485f-a646-65e87a14145e ProgId=CCOPC.XMLWrapper.1 Description=CCOPC.XMLWrapper
        ClsId=34f59577-988a-4722-be7c-6901f4e269c8 ProgId=OPC.SimaticHMI.CoRtHmiRTm.1 Description=OPC.SimaticHMI.CoRtHmiRTm
        ClsId=75d00bbb-dda5-11d1-b944-9e614d000000 ProgId=OPCServer.WinCC.1 Description=OPCServer.WinCC
        ClsId=7bc0cc8e-482c-47ca-abdc-0fe7f9c6e729 ProgId=Kepware.KEPServerEX.V6 Description=KEPServerEX 6.3
        ClsId=f8582cf2-88fb-11d0-b850-00c0f0104305 ProgId=Matrikon.OPC.Simulation.1 Description=MatrikonOPC Server for Simulation and Testing
        */
        try {
            AutoReconnectController autos = null;
            JISystem.setAutoRegisteration(true);
            final ConnectionInformation ci = new ConnectionInformation("SanyiopcUser", "OPCsanyi!@#2019");
            //ci.setHost("localhost");
            //ci.setDomain("");
            //ci.setProgId("OPCServer.WinCC.1");
            ci.setClsid("75d00bbb-dda5-11d1-b944-9e614d000000"); // if ProgId is not working, try it using the Clsid instead
            final Server s = new Server(ci, Executors.newSingleThreadScheduledExecutor());
            autos = new AutoReconnectController(s);
            autos.connect();
            Thread.sleep(100);
            Group group = s.addGroup("test");
            group.setActive(true);
            SocketFrame socketFrame = new SocketFrame();
            final Item item = group.addItem("GX_YTH_Pump_1.UA");
            ItemState itemState = item.read(true);
            System.out.println(item.getId() + " : " + itemState.getValue().getType() + " : " + itemState.getValue().getObject());
        } catch (final Exception e) {
        }
    }

    public void utgardServer(SocketFrame socketFrame) throws Exception {
        AutoReconnectController autos = null;
        JISystem.setAutoRegisteration(true);
        final ConnectionInformation ci = new ConnectionInformation("SanyiopcUser", "OPCsanyi!@#2019");
        //ci.setHost("localhost");
        //ci.setDomain("");
        //ci.setProgId("OPCServer.WinCC.1");
        ci.setClsid("75d00bbb-dda5-11d1-b944-9e614d000000"); // if ProgId is not working, try it using the Clsid instead
        final Server s = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        autos = new AutoReconnectController(s);
        autos.connect();
        Thread.sleep(100);
        Group group = s.addGroup("test12");
        group.setActive(true);
        for (Pumpdata pumpdata : socketFrame.getPumpdata()) {
            final Item item = group.addItem(pumpdata.getPname() + "." + pumpdata.getName());
            ItemState itemState = item.read(true);
            System.out.println(item.getId() + " : " + itemState.getValue().getType() + " : " + itemState.getValue().getObject());
        }
        s.removeGroup(group, true);
    }

    public void utgard1(SocketFrame socketFrame) {
        ConnectionInformation ci = new ConnectionInformation();
        ci.setUser("SanyiopcUser");             // 电脑上自己建好的用户名
        ci.setPassword("OPCsanyi!@#2019");          // 密码
        ci.setClsid("75d00bbb-dda5-11d1-b944-9e614d000000"); // WINcc的注册表ID，可以在“组件服务”里看到
        // 启动服务
        Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            // 连接到服务
            server.connect();
            // add sync access, poll every 500 ms，启动一个同步的access用来读取地址上的值，线程池每500ms读值一次
            // 这个是用来循环读值的，只读一次值不用这样
            AccessBase access = new SyncAccess(server, 500);
            // 这是个回调函数，就是读到值后执行这个打印，是用匿名类写的，当然也可以写到外面去
            for (Pumpdata pumpdata : socketFrame.getPumpdata()) {
                access.addItem(pumpdata.getPname() + "." + pumpdata.getName(), new DataCallback() {
                    @Override
                    public void changed(Item item, ItemState itemState) {
                        int type = 0;
                        try {
                            type = itemState.getValue().getType(); // 类型实际是数字，用常量定义的
                            System.out.println(item.getId() + " : " + itemState.getValue().getType() + " : " + itemState.getValue().getObject());
                            // 如果读到是short类型的值
                            if (type == JIVariant.VT_I2) {
                                short n = 0;
                                n = itemState.getValue().getObjectAsShort();
                                System.out.println("-----short类型值： " + n);
                            }
                            // 如果读到是字符串类型的值
                            if (type == JIVariant.VT_BSTR) {  // 字符串的类型是8
                                JIString value = null;
                                value = itemState.getValue().getObjectAsString();
                                String str = value.getString(); // 得到字符串
                                System.out.println("-----String类型值： " + str);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // start reading，开始读值
            access.bind();
            // wait a little bit，有个10秒延时
            Thread.sleep(500);
            // stop reading，停止读取
            access.unbind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SocketFrame utgard2(SocketFrame pumps) {
        ConnectionInformation ci = new ConnectionInformation();
        ci.setUser("SanyiopcUser");             // 电脑上自己建好的用户名
        ci.setPassword("OPCsanyi!@#2019");          // 密码
        ci.setClsid("75d00bbb-dda5-11d1-b944-9e614d000000"); // WINcc的注册表ID，可以在“组件服务”里看到
        // 启动服务
        Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            // 连接到服务
            server.connect();
            // add sync access, poll every 500 ms，启动一个同步的access用来读取地址上的值，线程池每500ms读值一次
            // 这个是用来循环读值的，只读一次值不用这样
            AccessBase access = new SyncAccess(server, 500);
            // 这是个回调函数，就是读到值后执行这个打印，是用匿名类写的，当然也可以写到外面去
            for (int i = 0; i < pumps.getPumpdata().length; i++) {
                access.addItem(pumps.getPumpdata()[i].getPname() + "." + pumps.getPumpdata()[i].getName(), new DataCallback() {
                    @Override
                    public void changed(Item item, ItemState itemState) {
                        int type = 0;
                        String value;
                        try {
                            type = itemState.getValue().getType(); // 类型实际是数字，用常量定义的
                            value=itemState.getValue().getObject().toString();
                            System.out.println(item.getId() + " : " + itemState.getValue().getType() + " : " +
                                    itemState.getValue().getObject());

                            // 如果读到是short类型的值
                            if (type == JIVariant.VT_I2) {
                                short n = 0;
                                n = itemState.getValue().getObjectAsShort();
                                value=Short.toString(n);
                                System.out.println("-----short类型值： " + value);
                            }
                            // 如果读到是字符串类型的值
                            if (type == JIVariant.VT_BSTR) {  // 字符串的类型是8
                                JIString value1 = null;
                                value1 = itemState.getValue().getObjectAsString();
                                value = value1.getString(); // 得到字符串
                                System.out.println("-----String类型值： " + value);
                            }
                            for (int i = 0; i < pumps.getPumpdata().length; i++) {
                                if (item.getId().equals(pumps.getPumpdata()[i].getPname() + "." + pumps.getPumpdata()[i].getName())) {
                                    pumps.getPumpdata()[i].setValue(value);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // start reading，开始读值
            access.bind();
            // wait a little bit，有个10秒延时
            Thread.sleep(500);
            // stop reading，停止读取
            access.unbind();
        } catch (Exception e) {
            e.printStackTrace();
            return pumps;
        }
        return pumps;
    }
}
