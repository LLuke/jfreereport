package com.jrefinery.report.demo;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.jrefinery.report.JFreeReport;
//import org.xml.sax.iParserConfigurationException;

import com.jrefinery.report.io.ReportDefinitionContentHandler;

public class Test {

    public Test() {
    }

    public static void main(String[] args) {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            ReportDefinitionContentHandler handler = new ReportDefinitionContentHandler();

            try {
                InputStream in = new FileInputStream("/home/dgilbert/report.xml");
                parser.parse(in, handler);
                JFreeReport report = handler.getReport();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
        catch (SAXException e) {
            System.out.println(e.getMessage());
        }

    }

}