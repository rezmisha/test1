package com.rez.test;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Global {
    static public Map<String, Sql_connect> sqlCcnnectList = new HashMap<String, Sql_connect> ();
    static public Map<String, UrlObj> urlList = new HashMap<String, UrlObj> ();

    //!!!!!!!!!!! подстановка переменных значениями.
    static String makeIncludeVariable(String value) {
        // todo !!!
        return  value;
    }
}

 class  Sql_connect {
     String driver;
     String base;
     String url;
     String login;
     String password;
}

class  UrlObj {
    String host;
    String port;
    String path;
    String scheme;
}

class  Android_driver {

    public static String app_dir = "";
    public static String app_name = "";
    public static String app_package = "";
    public static String activity = "";
    public static String android_version = "";
    public static String device_name = "";
    public static String appium_url = "";
    public static String avd = "";
    public static String browser_name = "";

    static void setAndroid_driver (Element element) throws Exception{

        NamedNodeMap attributes = element.getAttributes();

        for (int j = 0; j < attributes.getLength(); j++) {
            Node attribute = attributes.item(j);
            String n = attribute.getNodeName();
            Field field = Android_driver.class.getField(attribute.getNodeName());
            field.set(Android_driver.class, Global.makeIncludeVariable(attribute.getNodeValue()));
        }
    }
}