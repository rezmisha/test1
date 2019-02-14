//https://habr.com/post/331224/ - книга https://habr.com/post/331248/

//http://getbug.ru/rukovodstvo-po-appium-dlya-testirovaniya-ustroystv-pod-andoid-i-ios/#___Appium-2


//https://habr.com/company/badoo/blog/354296/

//github.com/appium-boneyard/sample-code/tree/master/sample-code/examples/java/appium-generic-test/src/test/java
//software-testing.ru/forum/index.php?/topic/31885-ustanovka-appium-na-windows10/

//ошибка захвата экрана
//programmerz.ru/questions/31211/error-in-using-uiautomatorviewer-for-testing-android-app-in-appium-question

// https://habr.com/company/intel/blog/146114/  установка эмулятора

//https://dataart.ru/news/avtomatizatsiya-mobil-ny-h-prilozhenij-na-baze-appium


package com.rez.test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StepMobile extends StepObj{

    //public WebDriver driver;
    //public  static AndroidDriver mobiledriver;
    public  static AndroidDriver driver = null;


    //@Override
    public boolean do_stepMain (int testNum) throws Exception {

        List listElements = null;  //список найденых эл
        logOk("");
        driverName = "Mobile";
        if (driver == null)
            setDriver();

       // mobiledriver.findElements(By.name(obj_identifier));
       // mobiledriver.findElementsByAndroidUIAutomator();

        if (getTagsList("command").equals("back")) {
            // String press = Keys.chord(Keys.LEFT_ALT, Keys.ARROW_LEFT);
            driver.navigate().back();
            return true;
        }
        if (getTagsList("command").equals("landdscape")) {
            driver.rotate(ScreenOrientation.LANDSCAPE);
            return true;
        }
        if (getTagsList("command").equals("portain")) {
            driver.rotate(ScreenOrientation.PORTRAIT);
            return true;
        }
        //driver.rotation().

        listElements = SearchElement();
        boolean rez = false;
        if (listElements != null )
            if (listElements.size() == 1)
                rez = doStepAction((WebElement) listElements.get(0),1);
            else
            if (getTagsList("get_first").equals("1"))
                rez = doStepAction((WebElement) listElements.get(0),1);
            else
                logError("Найдено несколько элементов");

        return rez;
    }

    public List SearchElement () throws Exception {

        String obj_seach_type = (String) tagsList.get("obj_seach_type");
        String obj_identifier = (String) tagsList.get("obj_identifier");
        List listElements = null;  //список найденых эл
        List<Object> listElementsDisplay = new ArrayList<Object>()  ;  //список найденых эл Display

        if (obj_seach_type.equals("id"))
            listElements = driver.findElements(By.id(obj_identifier));

        if (obj_seach_type.equals("name"))
            listElements = driver.findElements(By.name(obj_identifier));

        if (obj_seach_type.equals("className"))
            listElements = driver.findElements(By.className(obj_identifier));

        if (obj_seach_type.equals("linkText"))
            listElements = driver.findElements(By.linkText(obj_identifier));

        if (obj_seach_type.equals("xpath"))
            listElements = driver.findElements(By.xpath(obj_identifier));

        if (obj_seach_type.equals("cssSelector"))//!!
            listElements = driver.findElements(By.cssSelector(obj_identifier));

        if (listElements == null) {
            logError("Не найден тип поиска " + obj_seach_type);
            return null;
        }

        WebElement element;

// проверка на видимость
        for (int i=0;i<listElements.size();i++) {

            element = (WebElement) listElements.get(i);
            if (element.isDisplayed() ) {  //!!! true
                listElementsDisplay.add(listElements.get(i));
            }
        }
        if (listElements.size() == 0) {
            logError("Не найден элемент " + tagsList.get("obj_identifier"));
            return null;
        }
        else
        if (listElementsDisplay.size() == 0) {
            logError("Не найден отображаемый элемент " + tagsList.get("obj_identifier"));
            return null;
        }

        return listElementsDisplay;
    }



    public  void setDriver() throws Exception{

        DesiredCapabilities capability = new DesiredCapabilities();

        File app = null;

        if (!Android_driver.app_dir.equals("") && !Android_driver.app_name.equals("")) {
            app = new File(Android_driver.app_dir, Android_driver.app_name);
            capability.setCapability("app", app.getAbsolutePath());
        }

     /*   caps.setCapability(CapabilityType.BROWSER_NAME, "");

        //Mobile OS version. In My case its running on Android 4.2

        caps.setCapability(CapabilityType.VERSION, "4.2");
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Xperia Z3 Tablet Compact");
        //caps.setCapability("avd","Nexus_S_API_21");// Mention the created AVD name    AVD_Nexus_4
        caps.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
        caps.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.android.calculator2.Calculator");*/


        capability.setCapability("device","Android");
        if (!Android_driver.browser_name.equals(""))
            capability.setCapability(CapabilityType.BROWSER_NAME, Android_driver.browser_name);
        //Mobile OS version. In My case its running on Android 4.2
        capability.setCapability(CapabilityType.VERSION, Android_driver.android_version);
        if (!Android_driver.avd.equals(""))
            capability.setCapability("avd", Android_driver.avd); // "Nexus_S_API_21"

      /*  capability.setCapability("deviceName", Android_driver.device_name);
        capability.setCapability("platformName","Android");
        capability.setCapability("app-package", Android_driver.app_package);
        capability.setCapability("app-activity", Android_driver.activity);*/


        //if (!Android_driver.device_name.equals(""))
            capability.setCapability(MobileCapabilityType.DEVICE_NAME, Android_driver.device_name);
        capability.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
        capability.setCapability(MobileCapabilityType.APP_PACKAGE, Android_driver.app_package);
        capability.setCapability(MobileCapabilityType.APP_ACTIVITY, Android_driver.activity);

        capability.setCapability("newCommandTimeout", 2000);

        //driver object with new Url and Capabilities
        // driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);

        driver = new AndroidDriver(new URL(Android_driver.appium_url), capability);
        ((TestThread)Thread.currentThread()).driverManager.androidDriver = driver;

        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);

        driver.rotate(ScreenOrientation.LANDSCAPE);
        //driver.rotate(ScreenOrientation.PORTRAIT);

        //driver.findElementByName("6");
        //WebElement plusSign=driver.findElementByAccessibilityId("plus");
        //plusSign.click();
    }

}


