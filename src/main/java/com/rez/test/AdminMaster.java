package com.rez.test;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AdminMaster {
    //public Map<String, TestData> testData;

    public  Map<String, Integer> loopStek = new HashMap<String, Integer>();

    //private WebDriver driver;

    private String loopFullcurrentField = null;

    private String loopFullcurrentValue = null;

    private boolean FullCheckTrue = false;

    //private Step currentLoop = null;

    public  void setup(String fileName) throws Exception{

        Log.thread_init();

        TestStep parentStep = new TestStep();
        TestUnit step = new TestUnit();
        ((TestThread)Thread.currentThread()).testDataMaster = new TestDataMaster();
        //step.init(new TestDataMaster());
        step.tagsList.setProperty("loopCount","1");
        step.tagsList.setProperty("name","main");
        step.setDefValue();

        step.steps =  XmlParserMake.getStep(XmlParserMake.readTestFile(Config.testDirPath + File.separator + fileName),parentStep);//parentStep  ??
        //setupSelenium();
        //do_all_test(steps,1,"main");  // !! main?
        step.do_step(1);
    }

    public  void tearDown() {
        //Log.totals();
        //driver.quit();
    }

}
