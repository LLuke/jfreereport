/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *                   leonlyong;
 *
 * $Id: ReportFactory.java,v 1.7 2003/11/05 17:31:53 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 30-Aug-2002 : PageFormat definition added (thanks "leonlyong")
 */

package org.jfree.report.modules.parser.simple;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.IncludeParser;
import org.jfree.report.modules.parser.base.IncludeParserFrontend;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportRootHandler;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A handler for the SAX events generated by the top level <REPORT> element in the JFreeReport
 * XML report definition file.
 *
 * @author Thomas Morgner
 */
public class ReportFactory extends AbstractReportDefinitionHandler
    implements ReportDefinitionTags, ReportRootHandler
{
  /** The current text. */
  private StringBuffer currentText;

  /** The current property. */
  private String currentProperty;

  /** The encoding. */
  private String currentEncoding;

  /** A character entity parser. */
  private CharacterEntityParser entityParser;

  /**
   * DefaultConstructor.
   */
  public ReportFactory()
  {
  }

  /**
   * Constructs a new handler.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public void init(final ReportParser parser, final String finishTag)
  {
    super.init(parser, finishTag);
    entityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * A SAX event indicating that an element start tag has been read.
   *
   * @param tagName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a parsing exception.
   */
  public void startElement(final String tagName,
                           final Attributes atts) throws SAXException
  {
    if (tagName.equals(REPORT_TAG))
    {
      startReport(atts);
    }
    else if (tagName.equals(REPORT_HEADER_TAG)
        || tagName.equals(REPORT_FOOTER_TAG)
        || tagName.equals(PAGE_HEADER_TAG)
        || tagName.equals(PAGE_FOOTER_TAG)
        || tagName.equals(ITEMS_TAG))
    {
      // Forward the event to the newly created
      final BandFactory bandFactory = new BandFactory(getReportParser(), tagName);
      getParser().pushFactory(bandFactory);
      bandFactory.startElement(tagName, atts);
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      startGroups(atts);
    }
    else if (tagName.equals(CONFIGURATION_TAG))
    {
      // do nothing
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      startFunctions(atts);
    }
    else if (tagName.equals(PROPERTY_TAG))
    {
      startProperty(atts);
    }
    else if (tagName.equals("include"))
    {
      handleIncludeParsing(atts);
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected one of <report>, <groups>, <items>, <functions>");
    }
  }

  /**
   * Performs the include element. This invokes a new parser run and waits until that
   * run is finished.
   *
   * @param atts the attribute set for the include tag.
   * @throws SAXException if there is an error parsing the included fragment.
   */
  private void handleIncludeParsing(final Attributes atts) throws SAXException
  {
    final String file = atts.getValue("src");
    if (file == null)
    {
      throw new SAXException("Required attribute 'src' is missing.");
    }

    try
    {
      final URL target = new URL(getContentBase(), file);
      final IncludeParserFrontend parserFrontend = new IncludeParserFrontend(getParser());
      parserFrontend.parse(target);
    }
    catch (IOException e)
    {
      throw new SAXException("Failure while including external report definition.", e);
    }
  }

  /**
   * Starts a new property entry within the report configuration section.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is an error parsing the XML.
   */
  private void startProperty(final Attributes atts)
      throws SAXException
  {
    currentProperty = atts.getValue(NAME_ATT);
    currentEncoding = atts.getValue(PROPERTY_ENCODING_ATT);
    if (currentEncoding == null)
    {
      currentEncoding = PROPERTY_ENCODING_TEXT;
    }
    currentText = new StringBuffer();
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  the character array.
   * @param start  the first character index.
   * @param length  the length (number of valid characters).
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // accumulate the characters in case the text is split into several chunks...
    if (this.currentText != null)
    {
      this.currentText.append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * A SAX event indicating that an element end tag has been read.
   *
   * @param qName  the element name.
   *
   * @throws SAXException if there is a parsing problem.
   */
  public void endElement(final String qName) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
    if (elementName.equals(REPORT_TAG))
    {
      // do nothing
    }
    else if (elementName.equals(CONFIGURATION_TAG))
    {
      // do nothing
    }
    else if (elementName.equals("include"))
    {
      // ignored ...
      // @todo add include ...
    }
    else if (elementName.equals(PROPERTY_TAG))
    {
      endProperty();
    }
    else if (elementName.equals(REPORT_HEADER_TAG)
        || elementName.equals(REPORT_FOOTER_TAG)
        || elementName.equals(PAGE_HEADER_TAG)
        || elementName.equals(PAGE_FOOTER_TAG)
        || elementName.equals(FUNCTIONS_TAG)
        || elementName.equals(GROUPS_TAG)
        || elementName.equals(ITEMS_TAG))
    {
      // ignore ...
    }
    else
    {
      Log.error("Expected </report>");
      throw new SAXException("Expected report end tag");
    }
  }

  /**
   * Ends the definition of a single property entry.
   *
   * @throws SAXException if there is a problem parsing the element.
   */
  private void endProperty()
      throws SAXException
  {
    getReport().getReportConfiguration()
        .setConfigProperty(currentProperty, entityParser.decodeEntities(currentText.toString()));
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
  private void startReport(final Attributes atts)
      throws SAXException
  {
    if (isIncludedParsing())
    {
      // do nothing if this is called from an other parser ...
      return;
    }



    final JFreeReport report = new JFreeReport();
    final String name = atts.getValue(NAME_ATT);
    if (name != null)
    {
      report.setName(name);
    }

    PageFormat format = report.getDefaultPageFormat();
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
        throw new IllegalStateException("Unexpected paper orientation.");
    }

    format.setPaper(p);
    report.setDefaultPageFormat(format);

    //PageFormatFactory.logPageFormat(format);
    getParser().setHelperObject(ReportParser.HELPER_OBJ_REPORT_NAME, report);
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
      throw new ParseException("Orientation value in REPORT-Tag is invalid.", getLocator());
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
   * Creates a new group list for the report. The group factory will be the new default handler
   * for SAX Events.
   *
   * @param atts  the element attributes.
   */
  private void startGroups(final Attributes atts)
  {
    getParser().pushFactory(new GroupFactory(getReportParser(), GROUPS_TAG));
  }

  /**
   * Creates a new function collection for the report. The FunctionFactory will be the new
   * default handler for SAX Events
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   */
  private void startFunctions(final Attributes atts)
      throws SAXException
  {
    getParser().pushFactory(new FunctionFactory(getReportParser(), FUNCTIONS_TAG));
  }

  /**
   * Returns true, if this is a report definition fragment, which is included from
   * an other report.
   *
   * @return true, if this is an included report definition fragment, false otherwise.
   */
  private boolean isIncludedParsing()
  {
    return getParser().getConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "false").equals("true");
  }
}
