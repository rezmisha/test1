package com.rez.test;

import java.io.File;
import java.net.MalformedURLException;

import java.net.URL;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class StepWin {

    public  void test () throws Exception {


    // запуск драйвера
    ProcessBuilder pro = new ProcessBuilder("C:\\work\\"  + "Winium.Desktop.Driver.exe", "");
    Process shell = pro.start();


        DesiredCapabilities capability = new DesiredCapabilities();

        capability.setCapability("app","C:\\Program Files (x86)\\Microsoft\\Skype for Desktop\\Skype.exe"); //если хотим сразу запускать какую-либо программу
        capability.setCapability("launchDelay","5"); //задержка после запуска программы
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:9999"),capability); //на этом порту по умолчанию висит Winium драйвер

        shell.destroy();
    }
}

//https://habr.com/company/2gis/blog/263347/
//https://cap.com/post/331292/