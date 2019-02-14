package com.rez.test;

import javax.xml.soap.*;

public class StepSoap {

    // SAAJ - SOAP Client Testing
    public static void main() {

        String soapEndpointUrl = "http://api.direct.yandex.ru";
        String soapAction = "API#GetCreditLimits";

        callSoapWebService(soapEndpointUrl, soapAction);
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();


        String myNamespace = "myNamespace";
        String myNamespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

      /*
      Constructed SOAP Request Message:
      <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
       <SOAP-ENV:Header/>
       <SOAP-ENV:Body>
        <myNamespace:GetInfoByCity>
         <myNamespace:USCity>New York</myNamespace:USCity>
        </myNamespace:GetInfoByCity>
       </SOAP-ENV:Body>
      </SOAP-ENV:Envelope>
      ----------------------------


      */

        SOAPHeader soapHeader = envelope.getHeader();

        SOAPElement soapHeaderElem1 = soapHeader.addChildElement("finance_token", myNamespace);
        soapHeaderElem1.addTextNode("07c2fbae9722634918bb00a70d2c467cf1bd07255012008ff249ba41b7a5cd6c");

        SOAPElement soapHeaderElem2 = soapHeader.addChildElement("operation_num", myNamespace);
        soapHeaderElem1.addTextNode("123");

        SOAPElement soapHeaderElem3 = soapHeader.addChildElement("token", myNamespace);
        soapHeaderElem1.addTextNode("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f");

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetCreditLimits", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("params", myNamespace);

    }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }

}


//https://habr.com/post/187390/
//http://www.sql.ru/forum/1236694/podkluchenie-k-soap-zapros-strokoy   !!!!!!!

//http://java-online.ru/web-service.xhtml
//http://java-online.ru/web-service-soap-client.xhtml
//http://java-online.ru/web-service-soap-auth.xhtml
//http://qaru.site/questions/62838/working-soap-client-example
// https://stackoverrun.com/ru/q/4331119

//https://tech.yandex.ru/direct/doc/dg-v4/concepts/SOAP-docpage/

//http://www.xserver.ru/computer/langprogr/razn/104/
//http://src-code.net/sozdanie-soap-poslaniya/

//proselyte.net/tutorials/soap-tutorial/envelope-element/