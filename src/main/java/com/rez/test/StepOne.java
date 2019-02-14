package com.rez.test;

import java.util.Scanner;

public class StepOne extends TestStep {

    public String driverName;

    public String blockExitNameDefault= "0";

    public String do_step(int testNum) throws Exception {

        if (do_stepMain(testNum))
            return "MyOk13";

        // Если не задано берем значение по умолчанию
        if (getTagsList("blockExitName").equals(""))
            tagsList.setProperty("blockExitName", blockExitNameDefault);

        if (getTagsList("blockExitName").equals("0"))
            return "MyOk13";

        return getTagsList("blockExitName");
    }

    public boolean do_stepMain(int testNum) throws Exception{
        return true;
    }

    public String getTestData(int testNum) throws Exception{
        return ((TestThread)Thread.currentThread()).testDataMaster.getTestData(this, testNum);
        /*if (tagsList.containsKey("data"))
            return (String) tagsList.get("data");
        else
            return testDataMaster.getTestData(this);*/
    }

    public  Object getDriver() throws Exception {
        Object driver;
        driver = ((TestThread)Thread.currentThread()).driverManager.getDriver(driverName);
        if (driver != null)
            return driver;
        else {
            driver = makeDriver();
            ((TestThread)Thread.currentThread()).driverManager.putDriver(driverName, driver);
        }
        return driver;
    }

    public  Object makeDriver() throws Exception{
        return null;
    }
}

class StepWait extends StepOne {

    public String do_step(int testNum) throws Exception {
        try {
            Thread.currentThread().sleep(Integer.parseInt(getTestData(testNum)));
            return "MyOk13";

        } catch (Exception e) {
            logError("Wait");
            return "MyOk13";
        }
    }

}

class StepSet extends StepOne {

    public String do_step(int testNum) throws Exception {

        //String[] s = getTestData(testNum)
        String[] s = getTagsList("set").split("=");
        ((TestThread)Thread.currentThread()).testDataMaster.putData(s[0],s[1]);
        return "MyOk13";
    }

}

class StepStop extends StepOne {

    public String do_step(int testNum) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.print("введите для продолжения работы "+Thread.currentThread().getName());
        // останавливае все потоки!
        String str = in.nextLine();
        return "MyOk13";
    }
}