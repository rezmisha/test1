package com.rez.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getCutStr(String str, String beginStr, String endStr) {
        int i1 = str.indexOf(beginStr);
        if (i1 == -1)
            return "";
        int i2 = str.indexOf(endStr,i1+beginStr.length());
        return str.substring(i1+beginStr.length(),i2);
    }


    // чтениие файла по строкам без комметнариев comStrBegin и нужной версии
    public static String readFile(String file, String comStrBegin) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                if (!comStrBegin.equals("") && (line.trim()).indexOf(comStrBegin)==0)
                    continue;
                //проверка версии
                String includeVer = getCutStr(line, " ver=\"", "\"");
                //includeVer = getCutStr(line, "\"ver=\"", "\""); - для xml
                if (!includeVer.equals(""))
                    if (!Config.checkVer(includeVer))
                        continue;
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    //List<ArrayList<String>>
    public static String [][] readFileToTable (String file) throws IOException {
        String filestr = readFile(file, "");
        String[] lines =  filestr.split(";");
        int count = lines.length;
        if ((lines[count-1]).trim().equals(""))
            count--;
        String [] [] table = new String[count][];
        for (int i=0;i<count;i++) {
                table[i] = lines[i].split(",");
        }
        return table;
    }

  /*  public void throwException (String text){
    }*/
}
