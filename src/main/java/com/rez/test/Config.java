package com.rez.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.lang.reflect.Field;

public class Config {

    public static String testDirPath ;
    public static String testListFile ;
    public static String driverDirPath ;
    public static String logDirPath ;
    public static String testVer = "";
    public static int maxThreadCount = 2;
    public static Properties configList = new Properties();


    public static void init(String conFile) throws Exception {

        //setDefault();
        configList = readFileConfig (conFile);

        for (Object key: configList.keySet() ) {
            Field field = Config.class.getField((String) key);
            if (field.getType().getName().equals("int"))
                field.set(Config.class, Integer.parseInt(configList.getProperty((String)key)));
            else
                field.set(Config.class, configList.getProperty((String) key));
        }

        //configList((key, value) -> {};
    }

    public static Properties readFileConfig (String conFile) throws IOException {
        Properties configList = new Properties();
        String filestr = Utils.readFile(conFile, "#");
        String[] lines =  filestr.split(System.getProperty("line.separator"));
        int count = lines.length;
        // убираем послед. пустую строчку
        if ((lines[count-1]).trim().equals(""))
            count--;
        for (int i=0;i<count;i++) {
            if (lines[i].indexOf("#")==0) // убрать ??
                continue;
            if (lines[i].trim().length()==0)
                continue;
            String[] confStr = lines[i].split("=");
            configList.setProperty(confStr[0].trim(), confStr[1].trim().replace("\"",""));
        }
        return configList;
    }

  /*  public static Element readTestFile(String fileName) throws Exception {
        String testStr =  Utils.readFile (fileName);

        do {
            String includeStr = Utils.getCutStr(testStr, "#include(\"", "\")");
            if (includeStr.equals(""))
                break;
            String includeFile = Utils.readFile(Config.dirPath + File.separator + includeStr);// !! do абсол пути
            testStr = testStr.replace("#include(\"" + includeStr + "\")", includeFile);
        }
        while (true);

        return rootXMLs(testStr);
    }*/

  public static boolean checkVer(String ver) {
      String[] verList = ver.split(",");
      List<String> listTrue = new ArrayList<String>();
      List<String> listFalse = new ArrayList<String>();

      for(String s: verList) {
          s = s.trim();
          if (s.indexOf("not") == 0)
              listFalse.add( (s.replace("not","")).trim());
          else
              listTrue.add(s);
      }

      String[] testVerList = testVer.split(",");

      for (String s :testVerList) {
          if (listFalse.contains(s))
              return false;
      }

      for (String s :testVerList) {
          if (listTrue.contains(s))
              return true;
      }

      //if (testVer.equals(ver))
          //return  true;

      return false;
  }
}
