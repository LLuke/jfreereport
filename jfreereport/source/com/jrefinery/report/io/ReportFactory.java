/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------
 * ReportFactory.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *                   leonlyong;
 *
 * $Id: ReportFactory.java,v 1.17 2002/12/06 19:27:55 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 30-Aug-2002 : PageFormat definition added (thanks "leonlyong")
 */

package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.PageFormatFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

/**
 * A handler for the SAX events generated by the top level <REPORT> element in the JFreeReport
 * XML report definition file.
 *
 * @author TM
 */
public class ReportFactory extends DefaultHandler implements ReportDefinitionTags
{

  /** The report under construction. */
  private JFreeReport report;

  /**
   * the base handler used as general coordination instance. This is the DefaultHandler used
   * by sax. This base handler distributes the sax events to the different factories depending on
   * the current state
   */
  private ReportDefinitionContentHandler handler;

  /** the current band */
  private Band currentBand;

  /** The current text. */
  private StringBuffer currentText;

  /** The current property. */
  private String currentProperty;
  private String currentEncoding;

  /**
   * Constructs a new handler.
   *
   * @param handler the base handler for this report factory
   */
  public ReportFactory(ReportDefinitionContentHandler handler)
  {
    this.handler = handler;
  }

  /**
   * A SAX event indicating that an element start tag has been read.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a parsing exception.
   */
  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase().trim();
    if (elementName.equals(REPORT_TAG))
    {
      startReport(atts);
    }
    else if (elementName.equals(REPORT_HEADER_TAG)
        || elementName.equals(REPORT_FOOTER_TAG)
        || elementName.equals(PAGE_HEADER_TAG)
        || elementName.equals(PAGE_FOOTER_TAG)
        || elementName.equals(ITEMS_TAG))
    {
      // Forward the event to the newly created
      BandFactory bandFactory = handler.createBandFactory();
      handler.setExpectedHandler(bandFactory);
      bandFactory.startElement(namespaceURI, localName, qName, atts);
    }
    else if (elementName.equals(GROUPS_TAG))
    {
      startGroups(atts);
    }
    else if (elementName.equals(CONFIGURATION_TAG))
    {
      startConfiguration(atts);
    }
    else if (elementName.equals(FUNCTIONS_TAG))
    {
      startFunctions(atts);
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      startProperty (atts);
    }
    else
    {
      throw new SAXException("Expected one of <report>, <groups>, <items>, <functions>");
    }
  }


  /**
   * starts a new property entry for the current function
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is an error parsing the XML.
   */
  protected void startProperty (Attributes atts)
          throws SAXException
  {
    currentProperty = atts.getValue (NAME_ATT);
    currentEncoding = atts.getValue (PROPERTY_ENCODING_ATT);
    if (currentEncoding == null) currentEncoding = PROPERTY_ENCODING_TEXT;
    currentText = new StringBuffer ();
  }

  /**
   * Starts the report-configuration tag. Use this to configure the technical side of JFreeReport
   *
   * @param atts the element attributes
   */
  private void startConfiguration(Attributes atts)
  {
    // does nothing yet
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  the character array.
   * @param start  the first character index.
   * @param length  the length (number of valid characters).
   */
  public void characters (char[] ch, int start, int length)
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.currentText != null)
    {
      this.currentText.append (String.copyValueOf (ch, start, length));
    }
  }

  /**
   * A SAX event indicating that an element end tag has been read.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
  {
    String elementName = qName.toLowerCase().trim();
    if (elementName.equals(REPORT_TAG))
    {
      endReport();
    }
    else if (elementName.equals(CONFIGURATION_TAG))
    {
      endConfiguration();
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      endProperty ();
    }
    else
    {
      Log.error("Expected </report>");
      throw new SAXException("Expected report end tag");
    }
  }

  private void endConfiguration()
  {
  }

  /**
   * Ends the definition of a single property entry.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  protected void endProperty ()
          throws SAXException
  {
    getReport().getReportConfiguration().setConfigProperty(currentProperty, currentText.toString ());
    currentText = null;
    currentProperty = null;
  }

  /**
   * Creates a new report depending on the attributes given.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is any problem parsing the XML.
   */
  public void startReport(Attributes atts)
      throws SAXException
  {
    String name = getHandler().generateName(atts.getValue(NAME_ATT));


    JFreeReport report = new JFreeReport();
    report.setName(name);

    PageFormat format = report.getDefaultPageFormat();
    double defTopMargin = format.getImageableY();
    double defBottomMargin = format.getHeight() - format.getImageableHeight()
                                                - format.getImageableY();
    double defLeftMargin = format.getImageableX();
    double defRightMargin = format.getWidth() - format.getImageableWidth()
                                              - format.getImageableX();

    format = createPageFormat(format, atts);

    defTopMargin = parseDouble(atts.getValue(TOPMARGIN_ATT), defTopMargin);
    defBottomMargin = parseDouble(atts.getValue(BOTTOMMARGIN_ATT), defBottomMargin);
    defLeftMargin = parseDouble(atts.getValue(LEFTMARGIN_ATT), defLeftMargin);
    defRightMargin = parseDouble(atts.getValue(RIGHTMARGIN_ATT), defRightMargin);

    Paper p = format.getPaper();
    switch (format.getOrientation())
    {
      case PageFormat.PORTRAIT:
        PageFormatFactory.getInstance().setBorders(p, defTopMargin, defLeftMargin,
                                                      defBottomMargin, defRightMargin);
        break;
      case PageFormat.LANDSCAPE:
        // right, top, left, bottom
        PageFormatFactory.getInstance().setBorders(p, defRightMargin ,defTopMargin, defLeftMargin, defBottomMargin);
        break;
      case PageFormat.REVERSE_LANDSCAPE:
        PageFormatFactory.getInstance().setBorders(p, defLeftMargin, defBottomMargin, defRightMargin, defTopMargin);
        break;
    }

    format.setPaper(p);
    report.setDefaultPageFormat(format);

    //PageFormatFactory.logPageFormat(format);
    setReport(report);
  }

  /**
   * Parses an String into an double value. If the parsing failed, the given default value is
   * returned.
   *
   * @param value  the text.
   * @param defaultVal  the default value.
   *
   * @return the double value.
   */
  private double parseDouble(String value, double defaultVal)
  {
    if (value == null)
    {
      return defaultVal;
    }
    try
    {
      return Double.parseDouble(value);
    }
    catch (Exception e)
    {
      return defaultVal;
    }
  }

  /**
   * Creates the pageFormat by using the given Attributes. If an PageFormat name is given, the
   * named PageFormat is used and the parameters width and height are ignored. If no name is
   * defined, height and width attributes are used to create the pageformat. The attributes define
   * the dimension of the PageFormat in points, where the printing resolution is defined at 72
   * pixels per inch.
   *
   * @param format  the page format.
   * @param atts  the element attributes.
   *
   * @return the page format.
   *
   * @throws SAXException if there is an error parsing the report.
   */
  private PageFormat createPageFormat(PageFormat format, Attributes atts) throws SAXException
  {
    String pageformatName = atts.getValue(PAGEFORMAT_ATT);

    int orientationVal = PageFormat.PORTRAIT;
    String orientation = atts.getValue(ORIENTATION_ATT);
    if (orientation == null)
    {
      orientationVal = PageFormat.PORTRAIT;
    }
    else
    if (orientation.equals(ORIENTATION_LANDSCAPE_VAL))
    {
      orientationVal = PageFormat.LANDSCAPE;
    }
    else if (orientation.equals(ORIENTATION_REVERSE_LANDSCAPE_VAL))
    {
      orientationVal = PageFormat.REVERSE_LANDSCAPE;
    }
    else if (orientation.equals(ORIENTATION_PORTRAIT_VAL))
    {
      orientationVal = PageFormat.PORTRAIT;
    }
    else
      throw new SAXException("Orientation value in REPORT-Tag is invalid.");

    if (pageformatName != null)
    {
      Paper p = PageFormatFactory.getInstance().createPaper(pageformatName);
      if (p == null)
      {
        Log.warn ("Unable to create the requested Paper. " + pageformatName);
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    if (atts.getValue(WIDTH_ATT) != null && atts.getValue(HEIGHT_ATT) != null)
    {
      int[] pageformatData = new int[2];
      pageformatData[0] = ParserUtil.parseInt(atts.getValue(WIDTH_ATT), "No Width set");
      pageformatData[1] = ParserUtil.parseInt(atts.getValue(HEIGHT_ATT), "No Height set");
      Paper p = PageFormatFactory.getInstance().createPaper(pageformatData);
      if (p == null)
      {
        Log.warn ("Unable to create the requested Paper. Paper={" + pageformatData[0] + ", " + pageformatData[1] + "}");
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    Log.warn ("Insufficient Data to create a pageformat: Returned default.");
    return format;
  }

  /**
   * Creates a new group list for the report. The group factory will be the new default handler
   * for SAX Events.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void startGroups(Attributes atts)
      throws SAXException
  {
    GroupFactory groupFactory = handler.createGroupFactory();
    getHandler().setExpectedHandler(groupFactory);
  }

  /**
   * Creates a new function collection for the report. The FunctionFactory will be the new
   * default handler for SAX Events
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void startFunctions(Attributes atts)
      throws SAXException
  {
    FunctionFactory functionFactory = handler.createFunctionFactory();
    getHandler().setExpectedHandler(functionFactory);
  }

  /**
   * Finishes the report generation.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void endReport()
      throws SAXException
  {
    getHandler().setReport(report);
  }

  /**
   * Returns the report under construction.
   *
   * @return the report.
   */
  public JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the current ReportDefinitionContentHandler.
   *
   * @return the report handler.
   */
  public ReportDefinitionContentHandler getHandler()
  {
    return handler;
  }

  /**
   * Returns the current band.
   *
   * @return the current band.
   */
  public Band getCurrentBand()
  {
    return currentBand;
  }

  /**
   * Sets the current band.
   *
   * @param band  the current band.
   */
  public void setCurrentBand(Band band)
  {
    this.currentBand = band;
  }

  /**
   * Sets the current report.
   *
   * @param report the report.
   */
  private void setReport(JFreeReport report)
  {
    this.report = report;
  }

}
