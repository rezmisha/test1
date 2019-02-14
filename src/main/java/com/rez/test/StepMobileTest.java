package com.rez.test;

import java.io.File;
import java.net.MalformedURLException;

import java.net.URL;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.*;

// import org.testng.annotations.*;

public class StepMobileTest {

    
 WebDriver driver;


       // BeforeClass

public void setUp() throws Exception {


        //location of the app

        File app = new File("D:\\Work\\apk", "Shop4.apk");

       //To create an object of Desired Capabilities

       DesiredCapabilities capability = new DesiredCapabilities();

         //OS Name

        capability.setCapability("device","Android");

        capability.setCapability(CapabilityType.BROWSER_NAME, "");

        //Mobile OS version. In My case its running on Android 4.2

        capability.setCapability(CapabilityType.VERSION, "4.2");

        capability.setCapability("app", app.getAbsolutePath());

        //To Setup the device name

        capability.setCapability("deviceName","Moto G");

        capability.setCapability("platformName","Android");

        //set the package name of the app

        capability.setCapability("app-package", "com.android.calculator2");

        //set the Launcher activity name of the app

        capability.setCapability("app-activity", "com.android.calculator2.Calculator");
    capability.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
    capability.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.android.calculator2.Calculator");

    driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);
    // driver = new AndroidDriver (new URL("http://127.0.0.1:4723/wd/hub"), caps);
    //AndroidDriver mobiledriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    WebElement five=driver.findElement(By.name("5"));
    five.click();
    WebElement plus=driver.findElement(By.name("+"));
    plus.click();
    WebElement four=driver.findElement(By.name("4"));
    four.click();





         //driver object with new Url and Capabilities
      // driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);

   // AndroidDriver mobiledriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capability);

    //mobiledriver.

}

       // Test

public void testApp() throws MalformedURLException{

                System.out.println("App launched");

        // locate Add Contact button and click it

    WebElement addContactButton = driver.findElement(By.name("Add Contact"));


        addContactButton.click();

       //locate input fields and type name and email for a new contact and save it

               List<WebElement>textFields = driver.findElements(By.className("android.widget.EditText"));

        textFields.get(0).sendKeys("Neeraj Test");

        textFields.get(1).sendKeys("9999999999");

       textFields.get(2).sendKeys("testemail@domain.com");

       driver.findElement(By.name("Save")).click();

        //insert assertions here

    }

}

//https://habr.com/post/331224/
//http://getbug.ru/rukovodstvo-po-appium-dlya-testirovaniya-ustroystv-pod-andoid-i-ios/#___Appium-2


//https://habr.com/company/badoo/blog/354296/