package com.rez.test;

import com.rez.test.Log;
import com.rez.test.TestThread;

import java.util.Properties;

public class TestStep {

    ErrorType errorType;

    TestDataMaster testDataMaster = ((TestThread)Thread.currentThread()).testDataMaster;

    public Properties tagsList = new Properties(); //map

    public TestStep stepError = null;

    public AdminMaster admin = ((TestThread)Thread.currentThread()).admin;

    public String driverName;

    /*public void TestStep (){
       // admin = ((TestThread)Thread.currentThread()).admin;
    }*/

    //значения по умолчаню  нуж но ли?
    public void newStepSet (Properties p) {
        tagsList = p;
        setDefValue();
    }

    public void setDefValue(){
        if (!tagsList.containsKey("step_type"))
            tagsList.put("step_type","action");

        if (!tagsList.containsKey("blockExitName"))
            tagsList.put("blockExitName","0"); //!!!!!!!!!!!! почему break

        if (!tagsList.containsKey("name"))
            tagsList.put("name","");

        if (!tagsList.containsKey("loopType"))
            tagsList.put("loopType","");

    }

    /*public TestStep (TestDataMaster testDataMaster) {
        this.testDataMaster = testDataMaster;
    }*/


    public  String do_step(int testNum) throws Exception{
        return "";
    }

    public void logOk(String s) {
        Log.logOk(stackStr() + tagsList.get("name") + " " + s);
    }

    public void logError(String s) {
        Log.errorCount++;
        Log.logError(stackErrorStr() + "Ошибка: " + tagsList.get("name") + " " + s);
        //Log.logError("Ошибка: " + tagsList.get("name") + " " + s);
    }

    public String stackStr () {
        String sp = "";
        for ( int i=0; i<admin.currentStackPos; i++ )
        {
            sp += "   ";
        }
        return sp;
    }

    public String stackErrorStr () {
        return "";
        /*String s = "";
        String sp = "";
        for ( int i=0; i<admin.currentStackPos; i++ )
        {
            s += sp + admin.logErrorStack.get(i) + System.getProperty("line.separator");
            sp += "   ";
        }
        return s;*/
    }
    // подстановка переменных значениями.
    public   String incVar(String name) throws Exception {

        if (!tagsList.containsKey(name))
            return "";

        name = "%" + (String) tagsList.get(name) + "%";
        return ((TestThread)Thread.currentThread()).testDataMaster.makeIncludeVariable (name) ;
    }

    public  String getTagsList (String name)  {
        if (!tagsList.containsKey(name))
            return "";
        return (String) tagsList.get(name);
    }

    public void putData (String dataStr) {
        ((TestThread)Thread.currentThread()).testDataMaster.putData(getTagsList("toData"), dataStr);
    }

}

enum ErrorType {
   testError,    syntaxError, ok
}

