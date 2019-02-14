package com.rez.test;

import java.util.HashMap;
import java.util.Map;

public class TestDataMaster {

    public Map<String, TestData> testData;

    public  Map<String, Integer> loopStek = new HashMap<String, Integer>();

    public String loopFullcurrentField = null;

    public String loopFullcurrentValue = null;

    public boolean FullCheckTrue = false;

    public TestUnit currentLoop = null; //??


    public TestDataMaster()  throws Exception {
        testData = XmlParserMake.initData();
    }

    //получкние значения из массива по текущему индексу для цикла в которой крутиться данная переменная
    public String getValueFromDataArray (String fieldName) throws Exception {
        int i;

        if (!testData.containsKey(fieldName))
            throw new Exception ("Не найдена переменная " + fieldName);

        String unitName = (testData.get(fieldName)).unitName;
        if (unitName.equals("")) {
            //unitName= ближайший цикл  !!??
            i = 0;
        }
        else
            i = loopStek.get(unitName);

        return testData.get(fieldName).arrayData [i];//!! проверить границы?
    }

    public   String makeIncludeVariable(String value) throws Exception {
        String resultStr =  value;
        String includeValue;

        do {
            String includeStr = Utils.getCutStr(resultStr, "%", "%");
            if (includeStr.equals(""))
                break;
            includeValue =  getValueFromDataArray(includeStr) ;
            resultStr = resultStr.replace("%" + includeStr + "%", includeValue);
        }
        while (true);

        return resultStr;
    }


    public String getTestData (TestStep step, int testNum) throws Exception{

        return getTestData ((String) step.tagsList.get("name"), step,  testNum);
    }

    private String getTestData (String fieldName, TestStep step, int testNum) throws Exception{
        String data = getTestDataMain (fieldName, step, testNum);
        Log.logOk(fieldName + " значение " +data);
        return data;
    }

    private String getTestDataMain (String fieldName, TestStep step, int num) throws Exception{

        if (FullCheckTrue) {
            if (currentLoop!=null)
                if (currentLoop.fieldList.get(fieldName) !=null) {
                    String fieldType = currentLoop.fieldList.get(fieldName)+"True";
                    if (num >= testData.get(fieldType).arrayData.length)
                        num = 1;
                    return testData.get(fieldType).arrayData[num];
                }
        }

        if (loopFullcurrentValue!=null)
            if (fieldName.equals(loopFullcurrentField))
                return loopFullcurrentValue;

        if (step.tagsList.containsKey("data"))
            return makeIncludeVariable ( (String)step.tagsList.get("data") );
        else {
            return makeIncludeVariable("%" + fieldName + "%");  //!!!!!!
        }
    }

    public void putData (String key, String value) {
        TestData testData1 = new TestData();
        testData1.unitName = "";//!!!!!
        testData1.arrayData = new String[1];
        testData1.arrayData[0] = value;
        //((TestThread)Thread.currentThread()).testDataMaster.testData.put(key,testData);
        testData.put(key,testData1);
    }


}

class TestData {
    public String[] arrayData;
    public String unitName;
}

