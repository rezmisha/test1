package com.rez.test;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverManager {

    public Map<String, Object> driverList = new HashMap<String, Object>();

    public AndroidDriver androidDriver = null;

    public  Object getDriver(String driverName) {

        if (driverList.containsKey(driverName))
            return driverList.get(driverName);
        else {
            return null;
        }
    }

    public void putDriver(String driverName, Object driver) {

        driverList.put(driverName, driver);
    }

    public void quitAllDrivers () {
        for (Object dr : driverList.values()) {
            if (dr !=null)
                ((WebDriver) dr).quit(); // !! только WebDriver  close quit ??
                //((WebDriver) dr).quit();
        }
        driverList.clear();
        if (androidDriver!= null)
            androidDriver.close();
    }
}


