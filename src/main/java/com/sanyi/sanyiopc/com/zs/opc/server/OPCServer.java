package com.sanyi.sanyiopc.com.zs.opc.server;

import com.sanyi.sanyiopc.bean.Pumpdata;
import com.sanyi.sanyiopc.bean.SocketFrame;
import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

import java.util.ArrayList;
import java.util.List;

public class OPCServer {

    private String host = "127.0.0.1";

    private String serverProgID = "OPCServer.WinCC.1";

    private String serverClientHandle = "JOPC1";

    private String groupName = "MyTEST1";

    private Boolean active = true;

    private int hostupdateRate = 1000;

    private float percentDeadBand = 0.0f;

    public synchronized SocketFrame server(SocketFrame pumps) throws Exception {
        JEasyOpc jopc = new JEasyOpc(host, serverProgID, serverClientHandle);
        OpcGroup group = new OpcGroup(groupName, active, hostupdateRate, percentDeadBand);
        jopc.connect();
        List<OpcItem> opcItems = new ArrayList<OpcItem>();
        for (int i = 0; i < pumps.getPumpdata().length; i++) {
            OpcItem opcItem = new OpcItem(pumps.getPumpdata()[i].getPname() + "." + pumps.getPumpdata()[i].getName(), active, "");
            opcItems.add(opcItem);
            group.addItem(opcItem);
        }
        jopc.addGroup(group);
        //jopc.registerGroups();
        jopc.registerGroup(group);
        try{
            for (OpcItem opcItem : opcItems) {
                jopc.registerItem(group, opcItem);
            }
        }catch (Exception e){
            throw e;
        }
        for (int i = 0; i < jopc.synchReadGroup(group).getItemCount(); i++) {
            String s = jopc.synchReadGroup(group).getItems().get(i).getItemValueBytesToString();
            if (s == null) {
                s = jopc.synchReadGroup(group).getItems().get(i).getValue().getString();
            }
            pumps.getPumpdata()[i].setValue(s);
        }
            /*for(OpcItem opcItem:opcItems){
                group.removeItem(opcItem);
            }*/
        jopc.unregisterGroup(group);
        return pumps;
    }

    public synchronized Boolean update(SocketFrame pumps) throws Exception {
        for(Pumpdata pumpdata:pumps.getPumpdata()){
            JEasyOpc jopc = new JEasyOpc(host, serverProgID, serverClientHandle);
            OpcGroup group = new OpcGroup(groupName, active, hostupdateRate, percentDeadBand);
            jopc.connect();
            OpcItem opcItem = new OpcItem(pumpdata.getPname() + "." + pumpdata.getName(), active, "");
            Variant variant = new Variant(pumpdata.getValue());
            opcItem.setValue(variant);
            group.addItem(opcItem);
            jopc.addGroup(group);
            jopc.registerGroups();
            jopc.synchWriteItem(group, opcItem);
        }
        return true;
    }

}
