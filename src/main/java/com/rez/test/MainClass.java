package com.rez.test;

import com.rez.test.Config;
import com.rez.test.Log;
import jdk.nashorn.internal.objects.annotations.Property;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MainClass {



    //public static Properties threadList = new Properties();

    public static List<Test> testList = new ArrayList<Test>();
    public static int runCountTest = 0;

    public static void main(String[] args)  throws Exception {

        Date dateBegin = new Date();

        //System.out.println(args[0]);
		

        //new StepSoap2();

        //System.out.println("Working Directory = " +                 System.getProperty("user.dir"));

        //StepMobileTest s=new StepMobileTest();
        //s.setUp();
        //s.testApp();

        //new StepWin().test();
        //TestApp.setUp();

        //StepSoap.main();
        //TestSoapHttp2.main();

        Config.init(args[0]);
        //Log.init();!!
        XmlParserMake.readTestListFile(Config.testDirPath + File.separator + Config.testListFile);
        Log.init();

        String threadName;
        List<TestThread> threads = new ArrayList<TestThread>();
        for (int i=0; i<testList.size(); i++) {
            if (i == Config.maxThreadCount)
                break;
            runCountTest++;
            threadName = testList.get(i).name;
            //threadList.get(i).run = true;


            TestThread myThread1 = new TestThread(threadName);
            myThread1.number = i;
            threads.add(myThread1);
            myThread1.start();
        }
        // задержка до окончания
        try {
            for (TestThread thread: threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            Log.totals(dateBegin);
        }
        //Log.totals(dateBegin);

    }



    //http://www.autotest.org.ua/selenide-quick-start-in-automation-testing/

    //https://www.ibm.com/developerworks/ru/library/j-classpath-windows/

}

class Test {
    String name;
    String file;
    //boolean run = false;
}



