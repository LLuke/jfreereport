package com.jrefinery.report.io;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import com.jrefinery.report.JFreeReport;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The reportgenerator initializes the parser and provides an interface
 * the the default parser.
 *
 * To create a report from an URL, use
 * 
 * <code>
 * ReportGenerator.getInstance().parseReport (URL myURl, URL contentBase);
 * </code>
 */
public class ReportGenerator
{
  private static String defaultDtd;
  private static ReportGenerator generator;
  private SAXParserFactory factory;
  private String dtd;
  
  protected ReportGenerator ()
  {
    dtd = defaultDtd;
  }
  
  /**
   * Defines a DTD used to validate the report definition. Your XMLParser
   * must be an validating parse for this feature to work.
   */
  public void setDTDLocation (String dtd)
  {
    this.dtd = dtd;
  }
  
  public String getDTDLocation ()
  {
    return dtd;
  }
  
  public void initFromSystem ()
  {
    String reportDtd = System.getProperty ("com.jrefinery.report.dtd");
    if (reportDtd == null)
      return;
   
    File f = new File (reportDtd);
    if (f.exists() && f.isFile () && f.canRead())
    {
      defaultDtd = reportDtd;
    }
  }
  
  public JFreeReport parseReport (String file) throws IOException, ReportDefinitionException
  {
    if (file == null) 
      throw new NullPointerException ("File may not be null");
      
    return parseReport (new File (file));    
  }

  /** 
   * Parses an XML file which is loaded using the given URL. All
   * needed relative file- and resourcespecification are loaded 
   * using the URL <code>contentBase</code> as base.
   */
  public JFreeReport parseReport (URL file, URL contentBase)
  throws ReportDefinitionException
  {
    if (file == null) 
      throw new NullPointerException ("File may not be null");
    
    InputSource in = new InputSource (file.toString ());
    in.setSystemId (file.toString ());
    return parseReport (in, contentBase);
  }

  /** 
   * Parses an XML file which is loaded using the given file. All
   * needed relative file- and resourcespecification are loaded 
   * using the parent directory of the file <code>file</code> as base.
   */
  public JFreeReport parseReport (File file) throws IOException, ReportDefinitionException
  {
    if (file == null)
       throw new NullPointerException ();

    File contentBase = file.getParentFile();
    return parseReport (file.toURL (), contentBase.toURL());
  }

  /**
   * @returns an SAXParser.
   */
  protected SAXParser getParser () throws ParserConfigurationException, SAXException
  {
    if (factory == null)
    {
      factory = SAXParserFactory.newInstance();
    }
    return factory.newSAXParser ();
  }
  
  protected AbstractReportDefinitionHandler createDefaultHandler (URL contentBase)
  {
    ReportDefinitionContentHandler handler = new ReportDefinitionContentHandler();
    handler.setContentBase (contentBase);
    return handler;
  }
  
  /**
   * @returns an created JFreeReport. 
   * @throws ReportDefinitionException if an error occured
   */
  public JFreeReport parseReport (InputSource input, URL contentBase)
    throws ReportDefinitionException
  {
    try 
    {
        SAXParser parser = getParser ();
        AbstractReportDefinitionHandler handler = createDefaultHandler (contentBase);
        try {
            parser.parse(input, handler);
            return handler.getReport();
        }
        catch (IOException e) {
            e.printStackTrace ();
            throw new ReportDefinitionException (e.getMessage ());
        }
    }
    catch (ParserConfigurationException e) 
    {
        e.printStackTrace ();
        throw new ReportDefinitionException (e.getMessage());
    }
    catch (SAXException e) {
       e.printStackTrace ();
        throw new ReportDefinitionException (e.getMessage());
    }
  }
  
  /**
   * Returns a new ReportGenerator reference.
   */
  public static ReportGenerator getInstance ()
  {
    if (generator == null)
    {
      generator = new ReportGenerator ();
      generator.initFromSystem ();
    }
    return generator;
  }
}
