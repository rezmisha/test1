package com.rez.test;

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*; // обратите внимание !
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import static com.rez.test.MainClass.testList;

public class XmlParserMake {

    static  Properties arrayLib = new Properties();

    public static void readTestListFile(String fileName) throws Exception {

        Test test;

        String testStr =  Utils.readFile (fileName,"//"); //читаются коменты!!!

        // обработка include_file  (вставка файла)
        do {
            String includeStr = Utils.getCutStr(testStr, "#test(\"", "\")");
            testStr = testStr.replace("#test(\"" + includeStr + "\")", "");
            if (includeStr.equals(""))
                break;

            test = new Test();
            test.name = includeStr;
            test.file = includeStr + ".xml";
            testList.add(test);
        }
        while (true);
    }

    public static org.w3c.dom.Element readTestFile(String fileName) throws Exception {

        readLibIncUnit ();

        String testStr =  Utils.readFile (fileName,"//"); //читаются коменты!!!

        // обработка include_file  (вставка файла)
        do {
            String includeStr = Utils.getCutStr(testStr, "#include_file(\"", "\")");
            if (includeStr.equals(""))
                break;
            String includeFile = Utils.readFile(Config.testDirPath + File.separator + includeStr, "//");// !! do абсол пути
            testStr = testStr.replace("#include_file(\"" + includeStr + "\")", includeFile);
        }
        while (true);

        // обработка include  (вставка из библиотеки)
        do {
            String includeStr = Utils.getCutStr(testStr, "#include(\"", "\")");
            if (includeStr.equals(""))
                break;
            if (!arrayLib.containsKey(includeStr))
                throw new Exception("Не найден блок " + includeStr);
            String includeCode = (String) arrayLib.get(includeStr); // если не нашли?
            testStr = testStr.replace("#include(\"" + includeStr + "\")", includeCode);
        }
        while (true);

        testStr = testStr.replace("<inc>","");
        testStr = testStr.replace("</inc>","");

        return rootXMLs(testStr);
    }

   /* public static String makeIncUnit () throws Exception{
        readLibIncUnit ();
        return "";
    }*/

    // загрузка блоков в библиотеку
    public static void readLibIncUnit () throws Exception {

        org.w3c.dom.Element root = rootXMLf(Config.testDirPath + File.separator + "lib.xml");
        NodeList list = root.getChildNodes();

        for (int i = 1; i < list.getLength(); i=i+2) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) list.item(i); // текущий нод
            String ss =  innerXml(list.item(i));
            arrayLib.put( element.getAttribute("name"), ss);
        }
    }

    // !!!!!!!! ????  http://qaru.site/questions/255234/get-a-nodes-inner-xml-as-string-in-java-dom
    public static String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < childNodes.getLength();  i=i+2) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>","");
    }


    public static org.w3c.dom.Element rootXMLf(String fileName) throws Exception{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //создали фабрику строителей,
            // f.setValidating(false); // не делать проверку валидации
            dbf.setIgnoringComments(true);

            DocumentBuilder db = dbf.newDocumentBuilder(); // создали конкретного строителя документа

            Document doc = db.parse(new File(fileName)); // стооитель построил документ

            return doc.getDocumentElement();
    }

    public static org.w3c.dom.Element rootXMLs(String testStr) throws Exception{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //создали фабрику строителей,
            // f.setValidating(false); // не делать проверку валидации  !!!!
            dbf.setIgnoringComments(true);

            DocumentBuilder db = dbf.newDocumentBuilder(); // создали конкретного строителя документа

            Document doc = db.parse(new InputSource(new StringReader(testStr))); // стооитель построил документ

            return doc.getDocumentElement();
    }

    // Загузка данных
    public static Map<String, TestData> initData() throws Exception {

            Map<String, TestData> arrayMap = new HashMap<String, TestData>();
        org.w3c.dom.Element root = rootXMLf(Config.testDirPath + File.separator + "data.xml");
            NodeList list = root.getChildNodes();

            for (int i = 1; i < list.getLength(); i=i+2) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) list.item(i);

                //проверка версии
                if ( !element.getAttribute("ver").equals(""))
                    if (!Config.checkVer(element.getAttribute("ver")))
                        continue;

                TestData testData = new TestData();
                testData.unitName = element.getAttribute("unit");
                String separator = element.getAttribute("separator");
                if (separator.equals(""))
                    separator = ",";  //!!!

                testData.arrayData = ( element.getTextContent()).split(separator);
                arrayMap.put( element.getTagName(),testData);
            }
            return arrayMap;
    }

    public static List<TestStep> getStep(Element root, TestStep parentStep) throws Exception {

            List<TestStep> steps = new ArrayList<TestStep>();
            org.w3c.dom.NodeList list = root.getChildNodes();
            //NodeList listUnit = null;
            for (int i = 1; i < list.getLength(); i=i+2) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) list.item(i); // текущий нод
                if ( !element.getAttribute("ver").equals(""))
                    if (!Config.checkVer(element.getAttribute("ver")))
                        continue;

                if (element.getTagName().equals("web") || element.getTagName().equals("mobile")) {
                    StepObj step = null;
                    if (element.getTagName().equals("web") || element.getTagName().equals("mobile"))
                        step = new StepWeb();

                    if (element.getTagName().equals("mobile"))
                        step = new StepMobile();
                    //listUnit = elementUnit(list.item(i));
                    step.newStepSet(AllElements(list.item(i)));
                    step.makeTarget();
                    steps.add(step);
                }
                if (element.getTagName().equals("unit")) {

                    //XmlParser.getStep(element);
                    TestUnit step = new TestUnit();
                    step.newStepSet(AllElements(list.item(i)));
                    step.steps = XmlParserMake.getStep(element, step);

                    if (step.tagsList.containsKey("loopType")  && step.tagsList.get("loopType").equals("FullCheckFalse")) {// !!
                        step.makeLoopFieldList();
                    }
                    //step.makeTarget();  ????

                    //if (step.tagsList.get("loopType")!=null)  ??
                       //step.makeLoopFieldList();

                    steps.add(step);
                }
                if (element.getTagName().equals("error")) {

                    //XmlParser.getStep(element);
                    TestUnit step = new TestUnit();
                    step.newStepSet(AllElements(list.item(i)));
                    step.steps = XmlParserMake.getStep(element, step);
                    parentStep.stepError = step;
                }


                // ?????
                if (element.getTagName().equals("after")) {
                    Properties properties = new Properties();
                    properties.setProperty("name",element.getAttribute("name"));
                    properties.setProperty("data",element.getAttribute("data"));
                    properties.setProperty("loopCount",element.getAttribute("loopCount"));
                    //Log.log(element.getAttribute("name"));
                    //XmlParser.getStep(element);
                    parentStep.tagsList = properties;
                    //parentStep.stepsAfter = XmlParserMake.getStep(element, parentStep);;
                }
                if (element.getTagName().equals("wait")) {
                    StepWait step = new  StepWait();
                    step.newStepSet(AllElements(list.item(i)));
                    steps.add(step);
                }
                if (element.getTagName().equals("data")) {
                    StepSet step = new  StepSet();
                    step.newStepSet(AllElements(list.item(i)));
                    steps.add(step);
                }
                if (element.getTagName().equals("stop")) {
                    StepStop step = new  StepStop();
                    step.newStepSet(AllElements(list.item(i)));
                    steps.add(step);
                }
                if (element.getTagName().equals("sql")) {
                    StepSql step = new StepSql();;
                    step.newStepSet(AllElements(list.item(i)));
                    steps.add(step);
                }

                if (element.getTagName().equals("http")) {
                    StepHttp step = new StepHttp();;
                    step.newStepSet(AllElements(list.item(i)));
                    steps.add(step);
                }

                if (element.getTagName().equals("sql_connect")) {

                    Sql_connect sql_connect = new Sql_connect();

                    sql_connect.base = element.getAttribute("base");
                    sql_connect.driver = element.getAttribute("driver");
                    sql_connect.login = element.getAttribute("login");
                    sql_connect.password = element.getAttribute("password");
                    sql_connect.url = element.getAttribute("url");

                    Global.sqlCcnnectList.put(element.getAttribute("name"),sql_connect);
                }

                if (element.getTagName().equals("url")) {

                    UrlObj url = new UrlObj();

                    url.host = element.getAttribute("host");
                    url.path = element.getAttribute("path");
                    url.port = element.getAttribute("port");
                    url.scheme = element.getAttribute("scheme");

                    Global.urlList.put(element.getAttribute("name"),url);
                }

                if (element.getTagName().equals("android_driver")) {
                    Android_driver.setAndroid_driver(element);
                }
            }

            return steps; //  если нет его?
    }

    public static String elem(org.w3c.dom.Element el,String name){
        org.w3c.dom.Element elname = (org.w3c.dom.Element) el.getElementsByTagName(name).item(0);
            //System.out.println(elname.getTextContent());
            return elname.getTextContent();
    }


    public static Properties AllElements(Node node) {

            NodeList list = ((Element) node).getChildNodes();
            //Log.log("l="+list.getLength());
            Properties tagsList = new Properties();

            for (int i = 1; i < list.getLength(); i=i+2) {
                Element element = (Element) list.item(i); // текущий нод
                if (!element.getTagName().equals("unit"))
                    tagsList.setProperty(element.getTagName(),element.getTextContent());
                //Log.log(element.getTagName());
                //Log.log(element.getTextContent());
            }

            NamedNodeMap attributes = node.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                tagsList.setProperty(attribute.getNodeName(),attribute.getNodeValue());
            }
            return tagsList;
    }

    public static NodeList elementUnit(Node node) {
        org.w3c.dom.Element element = (org.w3c.dom.Element) node;
            NodeList list = element.getElementsByTagName("unit");
            return list;
    }

    public static void visit(Node node, int level) {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node childNode = list.item(i); // текущий нод
                process(childNode, level + 1); // обработка
                visit(childNode, level + 1); // рекурсия
            }
    }

    public static void process(Node node, int level) {
            for (int i = 0; i < level; i++) {
                System.out.print('\t');
            }
            System.out.print(node.getNodeName());
            if (node instanceof Element){
                Element e = (Element) node;
                // работаем как с элементом (у него есть атрибуты и схема)
            }
            System.out.println();
        }

    }

//String r = "(<!--.*?-->)";
