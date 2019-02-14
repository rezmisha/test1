package com.rez.test;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class StepAndroid extends StepObj {


    static  WebDriver driver = null;

    public  void setDriver() throws Exception{
        File app = new File(Android_driver.app_dir, Android_driver.app_name);

        //To create an object of Desired Capabilities

        DesiredCapabilities capability = new DesiredCapabilities();

        //OS Name

        capability.setCapability("device","Android");

        capability.setCapability(CapabilityType.BROWSER_NAME, "");

        //Mobile OS version. In My case its running on Android 4.2

        capability.setCapability(CapabilityType.VERSION, Android_driver.android_version);

        capability.setCapability("app", app.getAbsolutePath());

        //To Setup the device name

        capability.setCapability("deviceName", Android_driver.device_name);

        capability.setCapability("platformName","Android");

        //set the package name of the app

        capability.setCapability("app-package", Android_driver.app_package);

        //set the Launcher activity name of the app

        capability.setCapability("app-activity", Android_driver.activity);

        //driver object with new Url and Capabilities
        // driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);

        AndroidDriver mobiledriver = new AndroidDriver(new URL(Android_driver.appium_url), capability);
    }



}
