package com.rez.test;

import com.google.common.collect.ImmutableMap;
import com.rez.test.Config;
import com.rez.test.StepObj;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StepWeb extends StepObj {

    public WebDriver driver;

    public  Object makeDriver() throws Exception{
        WebDriver driver;

        System.setProperty("webdriver.chrome.driver", Config.driverDirPath + File.separator + "chromedriver.exe");
        driver = new ChromeDriver();

        //driver = new HtmlUnitDriver();


       // System.setProperty("webdriver.gecko.driver", Config.driverDirPath + File.separator + "geckodriver.exe");
        //driver = new FirefoxDriver();

        /*System.setProperty("webdriver.gecko.driver", Config.driverDirPath + File.separator + "geckodriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);

        //beta 2 version
        //System.setProperty("webdriver.firefox.marionette", Config.driverDirPath + File.separator + "geckodriver.exe");

        //beta 1 version
        //System.setProperty("webdriver.gecko.driver", Config.driverDirPath + File.separator + "geckodriver.exe");

        //3.x final version onwards
        //System.setProperty("webdriver.gecko.driver.driver", Config.driverDirPath + File.separator + "geckodriver.exe");

        driver = new FirefoxDriver();*/

        //internetka.in.ua/firefoxdriver-profile/
        //selenium2.ru/news/190-selenium-34.html

        driver.manage().window().maximize();
        //driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        return driver;
    }

    //@Override
    public boolean do_stepMain (int testNum) throws Exception {
    //public String do_step(int testNum) throws Exception {
        //WebElement webElement = null;
        //List webElements = null;
        List listElements = null;  //список найденых эл
        //boolean ok = false;
        //String obj_seach_type;
        logOk("");
        driverName = "WebChrom";
        driver = (WebDriver) getDriver();
        if (getTagsList("command").equals("get")) {  //убрать!!!!!
            try {
                String s = getTestData(testNum);
                if (s.indexOf("http") != 0)
                    s = "http://" + s;
                driver.get(s); //!!
            }
            catch (Exception e) {} //!!
            return true;
        }

        if (getTagsList("command").equals("back")) {
            // String press = Keys.chord(Keys.LEFT_ALT, Keys.ARROW_LEFT);
            driver.navigate().back();
            return true;
        }

        if (getTagsList("command").equals("setCookies")) {
            String data = getTestData(testNum);
            String[] cookieData = data.split("=");
            Cookie cookie = new Cookie(cookieData[0], cookieData[1]);
            driver.manage().addCookie(cookie);
            return true;
        }

        if (getTagsList("command").equals("getCookies")) {
            Set<Cookie> allCookies = driver.manage().getCookies();
            for (Cookie loadedCookie : allCookies) {
                System.out.println(String.format("%s -> %s", loadedCookie.getName(), loadedCookie.getValue()));
            }
            return true;
        }

        if (getTagsList("command").equals("deleteAllCookies")) {
            driver.manage().deleteAllCookies();
            return true;
        }

        if (getTagsList("target").equals("alert")) {
            Alert alert;
            try {
                alert  = driver.switchTo().alert();
            } catch (Exception e) {
                Log.logError("Не найден Алерт");
                return false;
            }
            if (getTagsList("command").equals("dismiss")) {
                alert.dismiss();
                return  true;
            }
            if (getTagsList("command").equals("accept")) {
                alert.accept();
                return true;
            }
            if (getTagsList("command").equals("getText")) {
                return equalsText(alert.getText(), testNum);
            }

        }


        //obj_seach_type = (String) tagsList.get("obj_seach_type");

        if (getTagsList("command").equals("not_exist")) {
            boolean rez = NotSearchElement ();
            if (!rez && errorType != ErrorType.syntaxError)
                logError("Найден отображаемый элемент " + tagsList.get("obj_identifier"));
            return rez;
        }

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
        List list;
        int count = 0;
        if (!getTagsList("wait").equals(""))
            count = Integer.parseInt(getTagsList("wait"));

        for (int i=0;i<count;i++){
            errorType = ErrorType.ok;
            Thread.sleep(1000);
            list = SearchElementMain (false);
            if (list != null)
                return list;
            if (errorType == ErrorType.syntaxError)
                return null;
        }
        return SearchElementMain (true);
    }

    public boolean NotSearchElement () throws Exception {
        List list;
        int count = 1;
        if (!getTagsList("wait").equals(""))
            count = Integer.parseInt(getTagsList("wait"));
        for (int i=0;i<count;i++){
            errorType = ErrorType.ok;
            Thread.sleep(1000);
            list = SearchElementMain (false);
            if (list != null)
                return false;
            if (errorType == ErrorType.syntaxError)
                return false;
        }
        return true;
    }


    public List SearchElementMain (boolean err) throws Exception {

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

        if (obj_seach_type.equals("partialLinkText"))
            listElements = driver.findElements(By.partialLinkText(obj_identifier));

        if (obj_seach_type.equals("linkText"))
            listElements = driver.findElements(By.linkText(obj_identifier));

        if (obj_seach_type.equals("xpath"))
            listElements = driver.findElements(By.xpath(obj_identifier));

        if (obj_seach_type.equals("cssSelector"))//!!
            listElements = driver.findElements(By.cssSelector(obj_identifier));

        if (listElements == null) {
            errorType = ErrorType.syntaxError;
            logError("Не найден тип поиска " + obj_seach_type);
            return null;
        }

        WebElement element;
        Boolean notDisplayed = false;
        if (getTagsList("notDisplayed").equals("1"))
            notDisplayed = true;
// проверка на видимость
        for (int i=0;i<listElements.size();i++) {

            element = (WebElement) listElements.get(i);
            if (element.isDisplayed() || notDisplayed) {  //!!! true
                listElementsDisplay.add(listElements.get(i));
            }
        }
        if (listElements.size() == 0) {
            errorType = ErrorType.testError;
            if (err)
                logError("Не найден элемент " + tagsList.get("obj_identifier"));
            return null;
        }
        else
            if (listElementsDisplay.size() == 0) {
                errorType = ErrorType.testError;
                if (err)
                    logError("Не найден отображаемый элемент " + tagsList.get("obj_identifier"));
                return null;
            }

        return listElementsDisplay;
    }


}


//prostoitblog.ru/kak-sostavlyat-xpath-i-css-selektoryi/
//kreisfahrer.gitbooks.io/selenium-webdriver/content/webdriver_api_slozhnie_vzaimodeistviya/data-driven_testing.html