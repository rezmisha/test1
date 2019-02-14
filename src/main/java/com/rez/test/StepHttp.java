package com.rez.test;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class StepHttp extends StepOne {


    public String do_step(int testNum) throws Exception {

        // берем объет url из списка по названию
        UrlObj url = Global.urlList.get(tagsList.get("url"));

        // параметры для запроса
        String parametersStr = getTagsList("parameters");  //incVar!!!
        String[] paramList = parametersStr.split(","); // !!!","

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (parametersStr.length()>0) {
            // формирование списка параметров для запроса
            for (String s : paramList) {
                String[] param = s.split("=");
                params.add(new BasicNameValuePair(param[0], param[1]));
            }
        }
        HttpClient httpClient = new DefaultHttpClient();
        URI uri = new URIBuilder()
                .setScheme(url.scheme)
                .setHost(url.host)
                .setPort(Integer.parseInt(url.port))
                .setPath(url.path)
                .addParameters(params)
                .build();

        HttpGet httpGet;
        HttpPost httpPost;
        HttpResponse httpResponse;

        if (((String) tagsList.get("method")).equals("get")) {
            httpGet = new HttpGet(uri);
            httpResponse = httpClient.execute(httpGet); //...//additional parameters, handle response etc.
        } else {
            httpPost = new HttpPost(uri);
            //httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
            //httpPost.addHeader("SOAPAction", "");


           // HttpEntity httpEntity = new StringEntity("") ;
            //httpPost.setEntity(httpEntity);
            httpResponse = httpClient.execute(httpPost);
        }

        HttpEntity entity = httpResponse.getEntity();

        //System.out.println(httpResponse.getStatusLine());

        Header[] headers = httpResponse.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            //System.out.println(headers[i]);
        }

        if (entity != null) {
            String entityStr = EntityUtils.toString(entity);
            // System.out.println(entityStr);

            //entityStr = "<xmlq><pageInfo><pageName id=\"01\" >33434</pageName></pageInfo></xmlq>";

            //Если нужно вырезать значение из ответа
            if (!(getTagsList("subjson")).equals(""))
                entityStr = subJsonObject(entityStr, (String) tagsList.get("subjson"));

            if (!(getTagsList("subxml")).equals(""))
                entityStr = subXmlObject(entityStr, (String) tagsList.get("subxml"));

            if ((getTagsList("command")).equals("compare")) {
                //String italon = makeIncludeVariable((String)tagsList.get("target"), testNum); !!!
                //String varName = (String) tagsList.get("target");
                String italon = incVar("target");
                if (italon.equals(entityStr))
                    return "MyOk13";
                return "";
            }

            if (!(getTagsList("todata")).equals(""))
                putData(entityStr);

        }

        return "MyOk13";
    }

    public String subJsonObject(String str, String objectName) {

        String[] varNameArray = objectName.split("\\."); // или (Pattern.quote(".")

        JSONObject json  = new JSONObject(str);
        for(int i=0;i<varNameArray.length-1;i++) {

            json = json.getJSONObject(varNameArray[i]);
        }

        Object objlast = json.get(varNameArray[varNameArray.length-1]);
        return  objlast.toString();

          /*  JSONArray arr = obj.getJSONArray("posts");
            for (int i = 0; i < arr.length(); i++)
            {
                String post_id = arr.getJSONObject(i).getString("post_id");
            }*/
    }

    public String subXmlObject(String str, String objectName) throws Exception{

        String[] names = objectName.split("@");

        String[] varNameArray = names[0].split("\\."); // или (Pattern.quote(".")

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //создали фабрику строителей,
        // f.setValidating(false); // не делать проверку валидации  !!!!
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder(); // создали конкретного строителя документа

        Document doc = db.parse(new InputSource(new StringReader(str)));
        org.w3c.dom.Element element = doc.getDocumentElement();

        element = (Element) element.getChildNodes();

        for(int i=0;i<varNameArray.length;i++) {

            NodeList list = element.getElementsByTagName(varNameArray[i]); //ели массив !!!

            element = (Element) list.item(0);

        }
        //(varNameArray[varNameArray.length-1]);

        // если нужен атрибут
        if (names.length == 2)
            return element.getAttribute(names[1]);
        return  element.getTextContent();
    }

    public void do_action() throws Exception{

    }


}

//https://code-examples.net/ru/q/2d27d4

//  ? http://qaru.site/questions/22898/sending-http-post-request-in-java

// https://github.com/jsunsoftware/http-request
//www.javased.com/?api=org.apache.http.HttpEntity
/*


String query = URLEncodedUtils.format(params, "utf-8");

URI url = URIUtils.createURI(scheme, userInfo, authority, port, path, query, fragment); //can be null
HttpGet httpGet = new HttpGet(url);
 */