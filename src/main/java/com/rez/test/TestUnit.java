package com.rez.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class TestUnit extends TestStep {

    public List<TestStep> steps = null;

    public Map<String, String> fieldList = new HashMap<String, String>();


    @Override
    public String do_step(int testNum) throws Exception {
        admin.currentStackPos++;
        String rezs = "";

        if (tagsList.containsKey("loopType") && tagsList.get("loopType").equals("FullCheckFalse"))
            rezs =  do_stepFullCheckFalse(testNum);

        if (tagsList.containsKey("loopType") && tagsList.get("loopType").equals("AllElements"))
            rezs =  do_stepAllElements(testNum);

        if (tagsList.containsKey("loopType") && tagsList.get("loopType").equals(""))
            rezs =  do_stepLoop(testNum);

        admin.currentStackPos--;
        //!!  обработка выхода!!!!
        if (rezs.equals("break") || rezs.equals(tagsList.get("name")) )
            return "MyOk13";
        // завершение блока 0 blockExitName=0
        return rezs;
    }

    public String do_stepLoop(int testNum) throws Exception {
        String rezs = "";
        int loopCount = Integer.parseInt( (String) tagsList.get("loopCount"));

        loop:
        for (int i=0;i<loopCount;i++){
            setLoopIter((String) tagsList.get("name"), i);
            rezs = do_all_Steps(i);

            if (!rezs.equals("MyOk13")) {
                if (rezs.equals("continue"))
                    continue loop;// continue

                // !! закончить выход  do

                return rezs;
            }
        }
        return rezs;
    }

    public String do_stepAllElements(int testNum) throws Exception {
        String rezs = "";
        boolean rez = false;
        List listElements;
        StepWeb step = new StepWeb();

        step.tagsList = (Properties) (this.tagsList.clone());
        step.makeTarget();
        step.driverName = "WebChrom"; //!!
        step.driver = (WebDriver) step.getDriver();

        listElements = step.SearchElement();

        if (listElements==null) {
            return "1"; //??
            }
        if (listElements.size()==0) {//!!dd
            }

        TestUnit stepUnit = new TestUnit();

        stepUnit.steps = new ArrayList<TestStep>();
        stepUnit.steps.addAll(this.steps);

        stepUnit.tagsList.setProperty("loopType","");
        stepUnit.tagsList.setProperty("loopCount","1");
        stepUnit.tagsList.setProperty("name","name"); //!!!!
        stepUnit.tagsList.setProperty("blockExitName",(String) tagsList.get("blockExitName") ); //!!!!

        int count = listElements.size();
        loop:
        for (int i=0;i<count;i++){
            setLoopIter((String) tagsList.get("name"), i);

            rezs = (String) tagsList.get("blockExitName");  // !!!!!!!!!!!! ??

            if (getTagsList("del").equals("11")) {
                listElements = step.SearchElement();
                rez = step.doStepAction((WebElement) listElements.get(0), i);
            }
            else{
                listElements = step.SearchElement();// делаем повторный поиск
                int j = i;
                if (j >= listElements.size())
                    j = 0;
                rez = step.doStepAction((WebElement) listElements.get(j), i);
            }

            if (rez)
                rezs = stepUnit.do_step(i);

            if (!rezs.equals("MyOk13")) {
                if (rezs.equals("continue"))
                    continue loop;// continue
                return rezs;
            }
        }
        return rezs;
    }

// выполнение всех шагов цикла (один проход)
    public String do_all_Steps (int testNum) throws Exception{
        String rezs = "";
        for (TestStep s : steps) {
            rezs = s.do_step(testNum);
            if (!rezs.equals("MyOk13")) {///!!
                if (rezs.equals("error") &&  this.stepError != null ) {
                    this.stepError.do_step(testNum); //!!
                    return "MyOk13";
                }
                break;
            }
        }

       // завершение блока  !!do after
        return rezs;
    }


    public void setLoopIter(String unitName, int num)
    {
        ((TestThread)Thread.currentThread()).testDataMaster.loopStek.put(unitName,num);
        String s = "Цикл " + unitName + " итерация: " + num;
        //admin.logErrorStack.set(admin.currentStackPos, s);
        Log.logOk(s);
    }

    public void makeLoopFieldList(){

        for (TestStep step: steps ) {
            if(step.tagsList.get("fieldType")!=null)
                if(step.tagsList.get("name")!=null)
                    fieldList.put((String)step.tagsList.get("name"),(String)step.tagsList.get("fieldType"));
                else
                    ;// do !!log error name
        }
    }


    public String do_stepFullCheckFalse(int testNum) throws Exception {
        String rezs = "";

        int numIter = 0;
        loop:
        for (String field : fieldList.keySet()) {
            String fieldType = fieldList.get(field) + "False";
            for (int i = 0; i < testDataMaster.testData.get(fieldType).arrayData.length; i++) {
                setLoopIter((String) tagsList.get("name"), numIter);
                numIter++;
                testDataMaster.loopFullcurrentField = field;
                testDataMaster.loopFullcurrentValue = testDataMaster.testData.get(fieldType).arrayData[i];
                rezs = do_all_Steps(1);
                if (!rezs.equals("MyOk13")) {
                    if (rezs.equals("continue"))
                        continue loop;// continue

                    testDataMaster.loopFullcurrentValue = null;
                    testDataMaster.loopFullcurrentField = null;

                    return rezs;
                }
                //Thread.currentThread().sleep(3000);
                //return rezs;  //  !! проверка блока
            }
        }
        testDataMaster.loopFullcurrentValue = null;
        testDataMaster.loopFullcurrentField = null;
        return "MyOk13";
    }

    /*
           // цикл полной проверки позитив
        if (step.steps !=null && step.tagsList.get("loopType").equals("FullCheckTrue")) {
            int loopCount = maxLenDataTrue (step);
            FullCheckTrue = true;
            currentLoop = step;
            rezs = do_all_test(step.steps,loopCount, (String) step.tagsList.get("name"));
            FullCheckTrue = false;
            currentLoop = null;
            return rezs;  //  !! проверка блок
        }
     */

}

