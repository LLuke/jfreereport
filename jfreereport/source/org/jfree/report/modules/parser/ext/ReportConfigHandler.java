/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------
 * ReportConfigHandler.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfigHandler.java,v 1.9 2003/12/04 18:04:06 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.Enumeration;
import java.util.Properties;

import org.jfree.report.JFreeReport;
import org.jfree.report.SimplePageDefinition;
import org.jfree.xml.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A report configuration handler. Handles the report configuration and the
 * definition of the default page format.
 *
 * @author Thomas Morgner.
 * @see ReportConfiguration
 */
public class ReportConfigHandler extends AbstractExtReportParserHandler
{
  /** The 'object-factory' tag name. */
  public static final String OBJECT_FACTORY_TAG = ParserConfigHandler.OBJECT_FACTORY_TAG;

  /** The 'datasource-factory' tag name. */
  public static final String DATASOURCE_FACTORY_TAG = ParserConfigHandler.DATASOURCE_FACTORY_TAG;

  /** The 'template-factory' tag name. */
  public static final String TEMPLATE_FACTORY_TAG = ParserConfigHandler.TEMPLATE_FACTORY_TAG;

  /** The 'default page format' tag name. */
  public static final String DEFAULT_PAGEFORMAT_TAG = "defaultpageformat";

  /** The 'configuration' tag name. */
  public static final String CONFIGURATION_TAG = "configuration";

  /** The 'output-config' tag name. */
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

  /** The current property handler. */
  private PropertyHandler currentPropertyFactory;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ReportConfigHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(DEFAULT_PAGEFORMAT_TAG))
    {
      addComment(createPath(tagName), CommentHandler.OPEN_TAG_COMMENT);
      handlePageFormat(attrs);
    }
    else if (tagName.equals(CONFIGURATION_TAG))
    {
      addComment(createPath(tagName), CommentHandler.OPEN_TAG_COMMENT);
      final CommentHintPath path = createPath(tagName);
      currentPropertyFactory = new PropertyHandler(getReportParser(), CONFIGURATION_TAG, path);
      getParser().pushFactory(currentPropertyFactory);
    }
    else if (tagName.equals(OUTPUT_TARGET_TAG))
    {
      // not yet used ...
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + OUTPUT_TARGET_TAG + ", "
          + DEFAULT_PAGEFORMAT_TAG + ", "
          + CONFIGURATION_TAG);
    }

  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // is not used ... ignore all events ..
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName) throws SAXException
  {
    if (tagName.equals(DEFAULT_PAGEFORMAT_TAG))
    {
      addComment(createPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
      // ignore this event ...
    }
    else if (tagName.equals(OUTPUT_TARGET_TAG))
    {
      // ignore this event ...
    }
    else if (tagName.equals(CONFIGURATION_TAG))
    {
      addComment(createPath(tagName), CommentHandler.CLOSE_TAG_COMMENT);
      // add all properties of the PropertyHandler to the report configuration
      final Properties p = currentPropertyFactory.getProperties();
      final ReportConfiguration rc = getReport().getReportConfiguration();

      final Enumeration pEnum = p.keys();
      while (pEnum.hasMoreElements())
      {
        final String key = (String) pEnum.nextElement();
        final String value = p.getProperty(key);
        if (value != null)
        {
          rc.setConfigProperty(key, value);
        }
      }

    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + OUTPUT_TARGET_TAG + ", "
          + DEFAULT_PAGEFORMAT_TAG + ", "
          + CONFIGURATION_TAG);
    }
  }

  /**
   * Handles the page format.
   *
   * @param atts  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  private void handlePageFormat(final Attributes atts) throws SAXException
  {
    final JFreeReport report = getReport();

    // todo page format changed
    PageFormat format = new PageFormat();//report.getDefaultPageFormat();
    float defTopMargin = (float) format.getImageableY();
    float defBottomMargin = (float) (format.getHeight() - format.getImageableHeight()
        - format.getImageableY());
    float defLeftMargin = (float) format.getImageableX();
    float defRightMargin = (float) (format.getWidth() - format.getImageableWidth()
        - format.getImageableX());

    format = createPageFormat(format, atts);

    defTopMargin = ParserUtil.parseFloat(atts.getValue(TOPMARGIN_ATT), defTopMargin);
    defBottomMargin = ParserUtil.parseFloat(atts.getValue(BOTTOMMARGIN_ATT), defBottomMargin);
    defLeftMargin = ParserUtil.parseFloat(atts.getValue(LEFTMARGIN_ATT), defLeftMargin);
    defRightMargin = ParserUtil.parseFloat(atts.getValue(RIGHTMARGIN_ATT), defRightMargin);

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
        // will not happen..
        Log.debug("Unexpected paper orientation.");
    }

    format.setPaper(p);
    report.setPageDefinition(new SimplePageDefinition (format));
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
  private PageFormat createPageFormat(final PageFormat format, final Attributes atts)
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
          getParser().getLocator());
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
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the current path.
   *
   * @param tagName the name of the new path segment.
   * @return the new comment path.
   */
  private CommentHintPath createPath(final String tagName)
  {
    final CommentHintPath path = new CommentHintPath();
    path.addName(ExtParserModuleInit.REPORT_DEFINITION_TAG);
    path.addName(ExtReportHandler.REPORT_CONFIG_TAG);
    path.addName(tagName);
    return path;
  }

}
