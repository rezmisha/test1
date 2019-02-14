package com.rez.test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestApp {
    static  AndroidDriver     driver;
    //static WebDriver    driver;

    public static void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.BROWSER_NAME, "");

        //Mobile OS version. In My case its running on Android 4.2

        caps.setCapability(CapabilityType.VERSION, "4.2");
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Xperia Z3 Tablet Compact");
        //caps.setCapability("avd","Nexus_S_API_21");// Mention the created AVD name    AVD_Nexus_4
        caps.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
        caps.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.android.calculator2.Calculator");

        //driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver = new AndroidDriver (new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
Log.logOk("------");
        //WebElement five=driver.findElementByName("5");
        driver.pressKeyCode(160);
        driver.closeApp();
        WebElement five=driver.findElementByAndroidUIAutomator("111");
        //WebElement five=driver.findElement(By.name("5"));
        five.click();
        WebElement plus=driver.findElement(By.name("+"));
        plus.click();
        WebElement four=driver.findElement(By.name("4"));
        four.click();
       // WebElement equalTo=driver.findElementByAccessibilityId("equals");
       // equalTo.click();


        driver.closeApp();

        //driver.close();
    }
}

