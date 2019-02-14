package com.rez.test;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class StepObj extends StepOne {
    public void makeTarget() {
        String[] seachTypes = {"name","xpath","className","id","cssSelector","linkText","partialLinkText"};
        String target = (String) tagsList.get("target");
        if (target!=null) {
            for (String seachType : seachTypes) {
                if (target.indexOf(seachType+"=") == 0) {
                    tagsList.put("obj_seach_type", seachType);
                    tagsList.put("obj_identifier", target.substring(seachType.length()+1));
                    return;
                }
            }
            tagsList.put("obj_seach_type", target);
            tagsList.put("obj_identifier", target);
            // do !!log error target;
        }
    }

    public boolean doStepAction (WebElement webElement, int testNum) throws Exception {
        String action_type = (String) tagsList.get("command");

        if (action_type.equals("exist") ) {
            return true;
        }
        if (action_type.equals("click")) {
            webElement.click();
            return true;
        }
        if (action_type.equals("isSelected")) {
            if (webElement.isSelected())
                return true;
            logError("Ошибка "+tagsList.get("obj_identifier")+" невыбран!");
            return false;
        }
        if (action_type.equals("isNotSelected")) {
            if (!webElement.isSelected())
                return true;
            logError("Ошибка "+tagsList.get("obj_identifier")+" выбран!");
            return false;
        }

    /*if (action_type.equals("mouseOver")) {
        webElement.mouseOver();
        return true;
    }*/

        if (action_type.equals("sendKeys")) {
            webElement.clear();
            webElement.sendKeys(getTestData(testNum));
            return true;
        }
        try {
            //http://www.autotest.org.ua/selenium-webdriver-select/
            if (action_type.equals("selectByVisibleText")) {
                Select select = new Select(webElement);
                select.selectByVisibleText(getTestData(testNum));
                return true;
            }
            if (action_type.equals("selectByValue")) {
                Select select = new Select(webElement);
                select.selectByVisibleText(getTestData(testNum));
                return true;
            }
            if (action_type.equals("selectByIndex")) {
                Select select = new Select(webElement);
                select.selectByVisibleText(getTestData(testNum));
                return true;
            }
            if (action_type.equals("getFirstSelectedOption")) {
                Select select = new Select(webElement);
                WebElement selectedOption = select.getFirstSelectedOption();
                String v = selectedOption.getText();
                return equalsText(v, testNum);
            }

        }
        catch (Exception e) {// !! не селект и не выбрать!
            logError("Невозможно выбрать  "+
                    getTestData(testNum)+" "+tagsList.get("obj_identifier"));
            return  false;
        }

        if (action_type.equals("enter")) {
            String oct = "13";
            int dec = Integer.parseInt(oct, 10);
            char keyCode = (char) dec;
            //String pressBoth = Keys.chord(Keys.CONTROL, "a");
            //webElement.sendKeys(Character.toString(keyCode)) ;
            String press = Keys.chord(Keys.ENTER);
            webElement.sendKeys(press) ;
            return true;
        }

        /*if (action_type.equals("clear")) {
            webElement.clear();
            return true;
        }*/

        if (action_type.equals("getText")) {
            String text = webElement.getText();
            return equalsText(text, testNum);
        }

        if (!(getTagsList("todata")).equals("")) {
            putData(webElement.getText());
            return  true;
        }

        throw new Exception ("Несуществующая команда " + action_type);
        //logError("Не найден " + action_type);
        //return  false;
    }

    public boolean equalsText(String text, int testNum) throws Exception{
        if ( !text.equals(  getTestData(testNum) )) {
            logError("Ошибка "+tagsList.get("obj_identifier")+" текст "+getTestData(testNum)+" вместо "+text);
            return false;
        }
        return true;
    }

}
