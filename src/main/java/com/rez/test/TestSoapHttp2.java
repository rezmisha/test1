package com.rez.test;
//www.sql.ru/forum/1236694/podkluchenie-k-soap-zapros-strokoy
//http://svn.apache.org/repos/asf/httpcomponents/oac.hc3x/trunk/src/examples/PostSOAP.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestSoapHttp2 {

    public static void main() throws Exception {

        String addr = "http://ws.cdyne.com/emailverify/Emailvernotestemail.asmx";
        String request = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:example=\"http://ws.cdyne.com/\"><SOAP-ENV:Header/><SOAP-ENV:Body>\n" +
                "<example:VerifyEmail><example:email>mutantninja@gmail.com</example:email>\n" +
                "<example:LicenseKey>123</example:LicenseKey></example:VerifyEmail>\n" +
                "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

        URL url = null;
        try {
            url = new URL(addr);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("SoapAction", "http://ws.cdyne.com/VerifyEmail");
        connection.setDoOutput(true);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(connection.getOutputStream());
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        pw.write(request);
        pw.flush();

        try {
            connection.connect();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String line;
        String respond = "";
        try {
            respond = rd.readLine();
            while ((line = rd.readLine()) != null)
                respond = line;

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println(respond);
    }
}
