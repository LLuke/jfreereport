/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ParserUtil;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.PropertyHandler;
import com.jrefinery.report.util.PageFormatFactory;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.Properties;
import java.util.Enumeration;

public class ReportConfigHandler implements ReportDefinitionHandler
{
  public static final String DEFAULT_PAGEFORMAT_TAG = "defaultpageformat";
  public static final String CONFIGURATION_TAG = "configuration";
  public static final String OUTPUT_TARGET_TAG = "output-config";

  /** Literal text for an XML attribute. */
  public static final String PAGEFORMAT_ATT = "pageformat";

  /** Literal text for an XML attribute. */
  public static final String LEFTMARGIN_ATT = "leftmargin";

  /** Literal text for an XML attribute. */
  public static final String RIGHTMARGIN_ATT = "rightmargin";

  /** Literal text for an XML attribute. */
  public static final String TOPMARGIN_ATT = "topmargin";

  /** Literal text for an XML attribute. */
  public static final String BOTTOMMARGIN_ATT = "bottommargin";

  /** Literal text for an XML attribute. */
  public static final String WIDTH_ATT = "width";

  /** Literal text for an XML attribute. */
  public static final String HEIGHT_ATT = "height";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_ATT = "orientation";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_PORTRAIT_VAL = "portrait";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_LANDSCAPE_VAL = "landscape";

  /** Literal text for an XML attribute. */
  public static final String ORIENTATION_REVERSE_LANDSCAPE_VAL = "reverselandscape";


  private Parser parser;
  private String finishTag;
  private PropertyHandler currentPropertyFactory;

  public ReportConfigHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(DEFAULT_PAGEFORMAT_TAG))
    {
      handlePageFormat(attrs);
    }
    else if (tagName.equals(CONFIGURATION_TAG))
    {
      currentPropertyFactory = new PropertyHandler(getParser(), CONFIGURATION_TAG);
      getParser().pushFactory(currentPropertyFactory);
    }
    else if (tagName.equals(OUTPUT_TARGET_TAG))
    {
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              OUTPUT_TARGET_TAG + ", " +
                              DEFAULT_PAGEFORMAT_TAG + ", " +
                              CONFIGURATION_TAG);
    }

  }

  public void characters(char ch[], int start, int length)
  {
    // is not used ... ignore all events ..
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(DEFAULT_PAGEFORMAT_TAG))
    {
      // ignore this event ...
    }
    else if (tagName.equals(OUTPUT_TARGET_TAG))
    {
      // ignore this event ...
    }
    else if (tagName.equals(CONFIGURATION_TAG))
    {
      // add all properties of the PropertyHandler to the report configuration
      Properties p = currentPropertyFactory.getProperties();
      ReportConfiguration rc = getReport().getReportConfiguration();

      Enumeration pEnum = p.keys();
      while (pEnum.hasMoreElements())
      {
        String key = (String) pEnum.nextElement();
        String value = p.getProperty(key);
        if (value != null)
        {
          rc.setConfigProperty(key, value);
        }
      }

    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              OUTPUT_TARGET_TAG + ", " +
                              DEFAULT_PAGEFORMAT_TAG + ", " +
                              CONFIGURATION_TAG);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  public void setParser(Parser parser)
  {
    this.parser = parser;
  }

  private JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(InitialReportHandler.REPORT_DEFINITION_TAG);
  }

  private void handlePageFormat (Attributes atts) throws SAXException
  {
    JFreeReport report = getReport();

    PageFormat format = report.getDefaultPageFormat();
    double defTopMargin = format.getImageableY();
    double defBottomMargin = format.getHeight() - format.getImageableHeight()
                                                - format.getImageableY();
    double defLeftMargin = format.getImageableX();
    double defRightMargin = format.getWidth() - format.getImageableWidth()
                                              - format.getImageableX();

    format = createPageFormat(format, atts);

    defTopMargin = ParserUtil.parseDouble(atts.getValue(TOPMARGIN_ATT), defTopMargin);
    defBottomMargin = ParserUtil.parseDouble(atts.getValue(BOTTOMMARGIN_ATT), defBottomMargin);
    defLeftMargin = ParserUtil.parseDouble(atts.getValue(LEFTMARGIN_ATT), defLeftMargin);
    defRightMargin = ParserUtil.parseDouble(atts.getValue(RIGHTMARGIN_ATT), defRightMargin);

    Paper p = format.getPaper();
    switch (format.getOrientation())
    {
      case PageFormat.PORTRAIT:
        PageFormatFactory.getInstance().setBorders(p, defTopMargin, defLeftMargin,
                                                      defBottomMargin, defRightMargin);
        break;
      case PageFormat.LANDSCAPE:
        // right, top, left, bottom
        PageFormatFactory.getInstance().setBorders(p, defRightMargin, defTopMargin,
                                                   defLeftMargin, defBottomMargin);
        break;
      case PageFormat.REVERSE_LANDSCAPE:
        PageFormatFactory.getInstance().setBorders(p, defLeftMargin, defBottomMargin,
                                                   defRightMargin, defTopMargin);
        break;
    }

    format.setPaper(p);
    report.setDefaultPageFormat(format);
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
   * @throws org.xml.sax.SAXException if there is an error parsing the report.
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
    {
      throw new SAXException("Orientation value in REPORT-Tag is invalid.");
    }
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
        Log.warn ("Unable to create the requested Paper. Paper={" + pageformatData[0] + ", "
                  + pageformatData[1] + "}");
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    Log.warn ("Insufficient Data to create a pageformat: Returned default.");
    return format;
  }


}
