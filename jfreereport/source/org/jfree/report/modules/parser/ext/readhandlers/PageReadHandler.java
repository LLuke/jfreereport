/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PageReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageReadHandler.java,v 1.4 2005/03/03 23:00:21 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class PageReadHandler extends AbstractPropertyXmlReadHandler
{

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

  private float x;
  private float y;
  private PageFormat pageFormat;

  public PageReadHandler ()
  {
  }

  public PageFormat getPageFormat ()
  {
    return pageFormat;
  }

  public float getY ()
  {
    return y;
  }

  public float getX ()
  {
    return x;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    handlePageFormat(attrs);
    x = ParserUtil.parseFloat(attrs.getValue("x"), 0);
    y = ParserUtil.parseFloat(attrs.getValue("y"), 0);
  }


  /**
   * Handles the page format.
   *
   * @param atts the attributes.
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  private void handlePageFormat (final Attributes atts)
          throws SAXException
  {
    final JFreeReport report = (JFreeReport)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);

    // grab the default page definition ...
    PageFormat format = report.getPageDefinition().getPageFormat(0);
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
    pageFormat = format;
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
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return null;
  }

  protected void storeComments ()
          throws SAXException
  {
    final Object[] serializedPageFormat = PageFormatFactory.getInstance()
            .resolvePageFormat(pageFormat);
    final CommentHintPath path = new CommentHintPath(serializedPageFormat);
    defaultStoreComments(path);
  }
}
