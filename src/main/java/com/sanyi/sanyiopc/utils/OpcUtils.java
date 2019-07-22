package com.sanyi.sanyiopc.utils;

import org.nutz.ioc.impl.PropertiesProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class OpcUtils {
    //读取配置文件中的值
    public static Object getParamFromProp(String key){
        String filePath=OpcUtils.class.getClassLoader().getResource("opc.properties").getFile();
        System.out.println(filePath);
        InputStream is=OpcUtils.class.getClassLoader().getResourceAsStream("opc.properties");
        System.out.println(is);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        Properties props = new Properties();
        try {
            props.load(br);
            return props.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配置信息
     */
    public String getProperties(String sign){
        PropertiesProxy propertiesProxy=new PropertiesProxy("opc.properties");
        String Properties= propertiesProxy.get(sign);
        return Properties;
    }

}
