package com.rez.test;

import com.rez.test.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public  class Log {

    private  static String testVer;
    private  static String logPash;

    private  static List<String> errorList =  new ArrayList<String>();
    private  static List<String> okList =  new ArrayList<String>();

    public static  int errorCount = 0;


    public  static void init () {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy_MM_dd HH mm ss");

        testVer = formatForDateNow.format(date);
        //logPash = Config.testDirPath + File.separator + testVer;
        logPash = Config.logDirPath;

        File folder = new File(logPash);
        if (!folder.exists()) {
            folder.mkdir();
        }

        for (int i=0;i<MainClass.testList.size();i++) {
            errorList.add("");
            okList.add("");
        }
    }

    // удаление старых логов
    public static void thread_init_() throws  Exception{

        String logErrorFileName = Thread.currentThread().getName() + ".txt";
        FileWriter writerErrorOne = new FileWriter(Config.testDirPath + File.separator + logErrorFileName, false);

        String logOkFileName = Thread.currentThread().getName() + "ok.txt";
        FileWriter writerOkOne = new FileWriter(Config.testDirPath + File.separator + logOkFileName, false);
    }


    public static void logError(String s) {
        String testName = ((TestThread)Thread.currentThread()).testName;
        int number = ((TestThread)Thread.currentThread()).number;
        s = testName + "  " + s + System.getProperty("line.separator");
        errorList.set(number, errorList.get(number) + s );
        System.out.println(s);
    }

    public static void logOk(String s) {
        String testName = ((TestThread)Thread.currentThread()).testName;
        int number = ((TestThread)Thread.currentThread()).number;
        s = testName + "  " + s + System.getProperty("line.separator");
        okList.set(number, okList.get(number) + s );
        System.out.println(s);
    }


    public static void saveLog(String addErr, String addOk) {
        String sErr = "";
        String sOk = "";
        for (int i=0;i<errorList.size();i++) {
            sErr += errorList.get(i);
        }
        sErr += System.getProperty("line.separator") + addErr;
        for (int i=0;i<okList.size();i++) {
            sOk += okList.get(i);
        }
        sOk += System.getProperty("line.separator") + addOk;
        try {
            String logErrorFileName = "ErrorReports.txt";
            FileWriter writerErrorOne = new FileWriter(Config.logDirPath + File.separator + logErrorFileName, true);
            writeToFile(sErr, writerErrorOne);
            String logOkFileName = "Reports.txt";
            FileWriter writerOkOne = new FileWriter(Config.logDirPath + File.separator + logOkFileName, true);
            writeToFile(sOk, writerOkOne);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void logError_(String s) {
        s = Thread.currentThread().getName() + "  " +s;
        System.out.println(s);
		try {
		    String logErrorFileName = Thread.currentThread().getName() + ".txt";
            //FileWriter writerError = new FileWriter(logPash + File.separator + logErrorFileName, true);
            FileWriter writerErrorOne = new FileWriter(Config.testDirPath + File.separator + logErrorFileName, true);
            //writeToFile(s, writerError);
            writeToFile(s, writerErrorOne);
		}
		catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
	
	public static void logOk_(String s) {

        s = Thread.currentThread().getName() + "  " +s;
        System.out.println(s);
		try {
		    /*
            //сохранить скриншот экрана java
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            ImageIO.write(capture, "bmp", new File("C:\\work\\img.bmp"));*/

		    String logOkFileName = Thread.currentThread().getName() + "ok.txt";
            //FileWriter writerOk = new FileWriter(logPash + File.separator + logOkFileName, true);
            FileWriter writerOkOne = new FileWriter(Config.testDirPath + File.separator + logOkFileName, true);
            //writeToFile(s, writerOk);
            writeToFile(s, writerOkOne);
		}
		catch(Exception ex){
            System.out.println(ex.getMessage());
        }
		
    }
	
    public static void writeToFile(String s, FileWriter writer){
        //if (!isLog)
                //return;
        try
        {
            writer.write(s);
            // запись по символам
            writer.write(System.getProperty("line.separator")); //("\r\n");
            //writer.append('E');
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static  void totals(Date dateBegin){
        //logError("Найдено ошибок: " + errorCount);
        Date dateEnd = new Date();
        long milliseconds = dateEnd.getTime() - dateBegin.getTime();

        int vrem = (int) (milliseconds / (1000));
        saveLog("Время исполнения:" + vrem +" Найдено ошибок: " + errorCount, "");
    }
}

//http://proglang.su/java/date-and-time