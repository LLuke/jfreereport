package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.JFreeReport;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.common.ConfigurationReadHandler;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * &lt;!ELEMENT report   (configuration?, reportheader?, reportfooter?, pageheader?,
 * pagefooter?, watermark?, groups?, items?, functions?)&gt;
 * &lt;!ATTLIST report
 *   width          CDATA           #IMPLIED
 *   height         CDATA           #IMPLIED
 *   name           CDATA           #IMPLIED
 *   pageformat     %pageFormats;   #IMPLIED
 *   orientation    (%orientations;) "portrait"
 *   leftmargin     CDATA           #IMPLIED
 *   rightmargin    CDATA           #IMPLIED
 *   topmargin      CDATA           #IMPLIED
 *   bottommargin   CDATA           #IMPLIED
 * &gt;
 * </pre>
 */
public class JFreeReportReadHandler extends AbstractPropertyXmlReadHandler
{
  public static final String REPORT_KEY = ReportParser.HELPER_OBJ_REPORT_NAME;

  /**
   * Literal text for an XML report element.
   */
  public static final String REPORT_TAG = "report";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NAME_ATT = "name";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEFORMAT_ATT = "pageformat";

  /**
   * Literal text for an XML attribute.
   */
  public static final String LEFTMARGIN_ATT = "leftmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String RIGHTMARGIN_ATT = "rightmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String TOPMARGIN_ATT = "topmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String BOTTOMMARGIN_ATT = "bottommargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String WIDTH_ATT = "width";

  /**
   * Literal text for an XML attribute.
   */
  public static final String HEIGHT_ATT = "height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_ATT = "orientation";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_PORTRAIT_VAL = "portrait";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_LANDSCAPE_VAL = "landscape";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_REVERSE_LANDSCAPE_VAL = "reverselandscape";

  private JFreeReport report;

  public JFreeReportReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final Object maybeReport = getRootHandler().getHelperObject(REPORT_KEY);
    final JFreeReport report;
    if (maybeReport instanceof JFreeReport == false)
    {
      // replace it ..
      report = new JFreeReport();
    }
    else
    {
      report = (JFreeReport) maybeReport;
    }

    final boolean include =
            ObjectUtilities.equal("true", getRootHandler().getHelperObject("include-parsing"));
    if (include == false)
    {
      final String name = attrs.getValue(NAME_ATT);
      if (name != null)
      {
        report.setName(name);
      }

      PageFormat format = report.getPageDefinition().getPageFormat(0);
      float defTopMargin = (float) format.getImageableY();
      float defBottomMargin = (float) (format.getHeight() - format.getImageableHeight()
              - format.getImageableY());
      float defLeftMargin = (float) format.getImageableX();
      float defRightMargin = (float) (format.getWidth() - format.getImageableWidth()
              - format.getImageableX());

      format = createPageFormat(format, attrs);

      defTopMargin = ParserUtil.parseFloat(attrs.getValue(TOPMARGIN_ATT), defTopMargin);
      defBottomMargin = ParserUtil.parseFloat(attrs.getValue(BOTTOMMARGIN_ATT), defBottomMargin);
      defLeftMargin = ParserUtil.parseFloat(attrs.getValue(LEFTMARGIN_ATT), defLeftMargin);
      defRightMargin = ParserUtil.parseFloat(attrs.getValue(RIGHTMARGIN_ATT), defRightMargin);

      final Paper p = format.getPaper();
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
        default:
          throw new IllegalStateException("Unexpected paper orientation.");
      }

      format.setPaper(p);
      report.setPageDefinition(new SimplePageDefinition(format));
    }
    getRootHandler().setHelperObject(REPORT_KEY, report);
    this.report = report;
  }


  /**
   * Creates the pageFormat by using the given Attributes. If an PageFormat name is given,
   * the named PageFormat is used and the parameters width and height are ignored. If no
   * name is defined, height and width attributes are used to create the pageformat. The
   * attributes define the dimension of the PageFormat in points, where the printing
   * resolution is defined at 72 pixels per inch.
   *
   * @param format the page format.
   * @param atts   the element attributes.
   * @return the page format.
   *
   * @throws SAXException if there is an error parsing the report.
   */
  private PageFormat createPageFormat (final PageFormat format, final Attributes atts)
          throws SAXException
  {
    final String pageformatName = atts.getValue(PAGEFORMAT_ATT);

    final int orientationVal;
    final String orientation = atts.getValue(ORIENTATION_ATT);
    if (orientation == null)
    {
      orientationVal = PageFormat.PORTRAIT;
    }
    else if (orientation.equals(ORIENTATION_LANDSCAPE_VAL))
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
      throw new ParseException("Orientation value in REPORT-Tag is invalid.",
              getRootHandler().getLocator());
    }
    if (pageformatName != null)
    {
      final Paper p = PageFormatFactory.getInstance().createPaper(pageformatName);
      if (p == null)
      {
        Log.warn("Unable to create the requested Paper. " + pageformatName);
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    if (atts.getValue(WIDTH_ATT) != null && atts.getValue(HEIGHT_ATT) != null)
    {
      final int[] pageformatData = new int[2];
      pageformatData[0] = ParserUtil.parseInt(atts.getValue(WIDTH_ATT), "No Width set");
      pageformatData[1] = ParserUtil.parseInt(atts.getValue(HEIGHT_ATT), "No Height set");
      final Paper p = PageFormatFactory.getInstance().createPaper(pageformatData);
      if (p == null)
      {
        Log.warn("Unable to create the requested Paper. Paper={" + pageformatData[0] + ", "
                + pageformatData[1] + "}");
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    Log.info("Insufficient Data to create a pageformat: Returned default.");
    return format;
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("configuration"))
    {
      return new ConfigurationReadHandler(report.getReportConfiguration());
    }
    else if (tagName.equals("reportheader"))
    {
      return new RootLevelBandReadHandler(report.getReportHeader());
    }
    else if (tagName.equals("reportfooter"))
    {
      return new RootLevelBandReadHandler(report.getReportFooter());
    }
    else if (tagName.equals("pageheader"))
    {
      return new RootLevelBandReadHandler(report.getPageHeader());
    }
    else if (tagName.equals("pagefooter"))
    {
      return new RootLevelBandReadHandler(report.getPageFooter());
    }
    else if (tagName.equals("watermark"))
    {
      return new RootLevelBandReadHandler(report.getWatermark());
    }
    else if (tagName.equals("groups"))
    {
      return new GroupsReadHandler(report.getGroups());
    }
    else if (tagName.equals("items"))
    {
      return new RootLevelBandReadHandler(report.getItemBand());
    }
    else if (tagName.equals("functions"))
    {
      return new FunctionsReadHandler(report);
    }
    else if (tagName.equals("include"))
    {
      return new IncludeReadHandler();
    }
    else if (tagName.equals("parser-config"))
    {
      return new ParserConfigurationReadHandler();
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return report;
  }

  protected void storeComments ()
          throws SAXException
  {
    final CommentHintPath hintPath = new CommentHintPath(report);
    defaultStoreComments(hintPath);
  }
}
