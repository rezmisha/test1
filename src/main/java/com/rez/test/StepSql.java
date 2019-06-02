package com.rez.test;

import com.rez.test.Global;
import com.rez.test.Sql_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import com.microsoft.sqlserver.jdbc.*;

public class StepSql extends StepOne {

    protected ResultSet rs;

    public String do_step(int testNum) throws Exception {

        Sql_connect sql_connect = Global.sqlCcnnectList.get(tagsList.get("sql_connect"));
        //String queryStr;// ВРЕМЕНО!!! = incVar("query");
        String queryStr = getTagsList("query");
        String url = "";

        try {
            if (sql_connect.driver.equals("jdbc:sqlserver")) {
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // jdbc:sqlserver://<server>:<port>;databaseName=AdventureWorks;user=<user>;password=<password>";

               // DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                //url = sql_connect.driver + "://" + sql_connect.url + ";" + sql_connect.base;
            }
            if (sql_connect.driver.equals("jdbc:mysql")) {
                Class.forName("com.mysql.jdbc.Driver");
                url = sql_connect.driver + "://" + sql_connect.url + "/" + sql_connect.base;
            }
            //if (sql_connect.driver.equals("postgresql"))
            //Class.forName("org.postgresql.Driver");
           // com.microsoft.sqlserver.jdbc;
            Connection con = DriverManager.getConnection(url, sql_connect.login, sql_connect.password);

            try {
                Statement stmt = con.createStatement();
                System.out.println(queryStr);
                //queryStr = "SELECT * FROM `Student` WHERE id=1";
                rs = stmt.executeQuery(queryStr);
                do_action();
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "MyOk13";
    }

    public void do_action() throws Exception{
        // Количество колонок в результирующем запросе
        int columns = rs.getMetaData().getColumnCount();
      /*  while (rs.next()) {
            for (int i = 1; i <= columns; i++){
                System.out.print( rs.getMetaData().getColumnName(i) + "  " + rs.getString(i) + "\t");
            }
            System.out.println();
        }*/
        rs.last();
        int count = rs.getRow();
        for (int i = 1; i <= columns; i++){
            TestData testData = new TestData();
            testData.unitName = "";//!!!!!
            testData.arrayData = new String[count];
            rs.beforeFirst();
            int j = 0;
            while (rs.next()) {
                testData.arrayData[j] = rs.getString(i);
                j++;
                //System.out.print( rs.getMetaData().getColumnName(i) + "  " + rs.getString(i) + "\t");
            }
            //System.out.println();
            ((TestThread)Thread.currentThread()).testDataMaster.testData.put(rs.getMetaData().getColumnName(i),testData);
        }
    }
}

//http://java-online.ru/jdbc-resultset.xhtml

/*

  public static void main(String[] args) {
        InsertDb m = new InsertDb();
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/contactdb";
            String login = "postgres";
            String password = "postgres";
            Connection con = DriverManager.getConnection(url, login, password);
            try {
                // Процедура вставки
                m.insert(con, "FirstName", "LastName", "phone", "email");
                // Процедура выборки
                m.select(con);
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(Connection con, String firstName, String lastName, String phone, String email) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("INSERT INTO JC_CONTACT (FIRST_NAME, LAST_NAME, PHONE, EMAIL) VALUES (?, ?, ?, ?)");
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, phone);
        stmt.setString(4, email);
        stmt.executeUpdate();
        stmt.close();
    }

    private void select(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM JC_CONTACT");
        while (rs.next()) {
            String str = rs.getString("contact_id") + ":" + rs.getString(2);
            System.out.println("Contact:" + str);
        }
        rs.close();
        stmt.close();
    }


catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { }
        try { stmt.close(); } catch(SQLException se) {  }
        try { rs.close(); } catch(SQLException se) {  }
        }
 */