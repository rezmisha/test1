package com.rez.test;

import com.rez.test.AdminMaster;
import com.rez.test.DriverManager;
import com.rez.test.Log;

public class TestThread extends Thread  {

    public DriverManager driverManager = new DriverManager();

    public TestDataMaster testDataMaster;

    public int number;

    public String testName;

    TestThread(String testName)
    {
        super(testName);
    }


    public void run()  {

        String fileName;
        try {

            AdminMaster admin = new AdminMaster();
            fileName = MainClass.testList.get(number).file;
            //System.out.println(" !!!!!!!! nnnnnnnnn=  "+number);
            testName = fileName;
            admin.setup(fileName);
            //admin.tearDown();
            end();
        }
        catch (Exception t) {
            Log.logError(t.getMessage());
            System.out.println(" !!!!!!!! Error "+t.getMessage());
            end();
        }

    }

    private void end() {
        //Log.totals();
        driverManager.quitAllDrivers();

        if (MainClass.runCountTest < MainClass.testList.size()) {
            MainClass.runCountTest++;
            number = MainClass.runCountTest - 1;
            run();
        }
    }
}
