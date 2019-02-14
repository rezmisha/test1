package com.rez.test;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class StepSoap2 {

    private  boolean  belavia      = true;

    private  String   namespaceURI = null;
    private  String   soapUrl      = null;
    private  String   serviceName  = null;
    private  String   namespace    = null;
    private  String   soapAction   = null;

    public StepSoap2()
    {
        setSoapParams();
        callSoapWebService(soapUrl, soapAction);
    }
    private void setSoapParams()
    {

            namespaceURI = "http://algoritmlab.ru/all/clientclub/";
            soapUrl      = "http://algoritmlab.ru/all/clientclub/get_general.php";
            serviceName  = "get_general.php";

        namespace  = "ns"; // namespace
        soapAction = namespaceURI + "/" + serviceName;
    }

    boolean useXSLT = true;

    private void callSoapWebService(String destination,
                                    String soapAction)
    {
        SOAPConnectionFactory soapFactory  = null;
        SOAPConnection        soapConnect  = null;
        SOAPMessage           soapRequest  = null;
        SOAPMessage           soapResponse = null;
        try {
            // Создание SOAP Connection
            soapFactory = SOAPConnectionFactory.newInstance();
            soapConnect = soapFactory.createConnection();

            // Создание SOAP Message для отправки
            soapRequest  = createSOAPRequest(soapAction);
            // Получение SOAP Message
            soapResponse = soapConnect.call(soapRequest, destination);

            // Печать SOAP Response
            if (!useXSLT) {
                System.out.println("Response SOAP Message:");
                soapResponse.writeTo(System.out);
                System.out.println();
            } else
                printSOAPMessage (soapResponse);

            soapConnect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printSOAPMessage (SOAPMessage soapResponse)
    {
        TransformerFactory transformerFactory;
        Transformer transformer;
        try {
            // Создание XSLT-процессора
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            // Получение содержимого ответа
            Source content;
            content = soapResponse.getSOAPPart().getContent();
            // Определение выходного потока
            StreamResult result = new StreamResult(System.out);
            transformer.transform(content, result);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private SOAPMessage createSOAPRequest(String soapAction)
            throws Exception
    {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        // Печать XML текста запроса
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }


    private void createSoapEnvelope(SOAPMessage soapMessage)
            throws SOAPException
    {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem;
        SOAPElement soapBodyElem1;
        if (belavia) {
            soapBody.addChildElement(serviceName, namespace);
        } else {
            soapBodyElem =soapBody.addChildElement(serviceName, namespace);
            soapBodyElem1=soapBodyElem.addChildElement("USCity",namespace);
            soapBodyElem1.addTextNode("New York");
        }
    }

}


