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
 * -----------------------
 * ReportConfigWriter.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfigWriter.java,v 1.11 2005/02/23 21:05:56 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;

import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.util.Log;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.CommentHandler;
import org.jfree.xml.writer.AttributeList;

/**
 * A report configuration writer.
 *
 * @author Thomas Morgner.
 */
public class ReportConfigWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The comment hint path for all report config related parser hints.
   */
  private static final CommentHintPath REPORT_CONFIG_HINT_PATH =
          new CommentHintPath(new String[]
          {REPORT_DEFINITION_TAG, REPORT_CONFIG_TAG});

  /**
   * The comment hint path for all report config related parser hints.
   */
  private static final CommentHintPath CONFIGURATION_HINT_PATH =
          new CommentHintPath(new String[]
          {REPORT_DEFINITION_TAG,
           REPORT_CONFIG_TAG,
           CONFIGURATION_TAG
          });

  /**
   * The comment hint path for all page format definition related parser hints.
   */
  private static final CommentHintPath DEFAULT_PAGEFORMAT_HINT_PATH =
          new CommentHintPath(new String[]
          {REPORT_DEFINITION_TAG,
           REPORT_CONFIG_TAG,
           DEFAULT_PAGEFORMAT_TAG
          });

  /**
   * A report configuration writer.
   *
   * @param reportWriter the report writer.
   * @param indentLevel  the current indention level.
   */
  public ReportConfigWriter (final ReportWriter reportWriter, final int indentLevel)
  {
    super(reportWriter, indentLevel);
  }

  /**
   * Writes the report configuration element.
   *
   * @param writer the writer.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void write (final Writer writer)
          throws IOException
  {
    writeComment(writer, REPORT_CONFIG_HINT_PATH, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, REPORT_CONFIG_TAG);

    writeComment(writer, DEFAULT_PAGEFORMAT_HINT_PATH, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, DEFAULT_PAGEFORMAT_TAG,
            buildPageFormatProperties(), CLOSE);
    final ReportConfiguration config = getReport().getReportConfiguration();
    final Enumeration properties = config.getConfigProperties();
    if (properties.hasMoreElements())
    {
      writeComment(writer, CONFIGURATION_HINT_PATH, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(writer, CONFIGURATION_TAG);
      while (properties.hasMoreElements())
      {
        final String key = (String) properties.nextElement();
        final String value = config.getConfigProperty(key);
        if (value != null)
        {
          final CommentHintPath path = CONFIGURATION_HINT_PATH.getInstance();
          path.addName(key);
          writeComment(writer, path, CommentHandler.OPEN_TAG_COMMENT);
          writeTag(writer, "property", "name", key, OPEN);
          writer.write(normalize(value));
          writeCloseTag(writer, "property");
        }
      }
      writeComment(writer, CONFIGURATION_HINT_PATH, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, CONFIGURATION_TAG);
    }

    writeComment(writer, REPORT_CONFIG_HINT_PATH, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, REPORT_CONFIG_TAG);
    writer.write(getLineSeparator());
  }

  /**
   * Compiles a collection of page format properties.
   *
   * @return The properties.
   */
  private AttributeList buildPageFormatProperties ()
  {
    final AttributeList retval = new AttributeList();
    final PageFormat fmt = getReport().getPageDefinition().getPageFormat(0);
    final int[] borders = getBorders(fmt.getPaper());

    if (fmt.getOrientation() == PageFormat.LANDSCAPE)
    {
      retval.setAttribute(ORIENTATION_ATT,
              ORIENTATION_LANDSCAPE_VAL);
      retval.setAttribute(TOPMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setAttribute(LEFTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setAttribute(BOTTOMMARGIN_ATT,
              String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(RIGHTMARGIN_ATT,
              String.valueOf(borders[BOTTOM_BORDER]));
    }
    else if (fmt.getOrientation() == PageFormat.PORTRAIT)
    {
      retval.setAttribute(ORIENTATION_ATT,
              ORIENTATION_PORTRAIT_VAL);
      retval.setAttribute(TOPMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setAttribute(LEFTMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(BOTTOMMARGIN_ATT,
              String.valueOf(borders[BOTTOM_BORDER]));
      retval.setAttribute(RIGHTMARGIN_ATT,
              String.valueOf(borders[RIGHT_BORDER]));
    }
    else
    {
      retval.setAttribute(ORIENTATION_ATT,
              ORIENTATION_REVERSE_LANDSCAPE_VAL);
      retval.setAttribute(TOPMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(LEFTMARGIN_ATT,
              String.valueOf(borders[BOTTOM_BORDER]));
      retval.setAttribute(BOTTOMMARGIN_ATT,
              String.valueOf(borders[RIGHT_BORDER]));
      retval.setAttribute(RIGHTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
    }

    final int w = (int) fmt.getPaper().getWidth();
    final int h = (int) fmt.getPaper().getHeight();

    final String pageDefinition = lookupPageDefinition(w, h);
    if (pageDefinition != null)
    {
      retval.setAttribute(PAGEFORMAT_ATT, pageDefinition);
    }
    else
    {
      retval.setAttribute(WIDTH_ATT, String.valueOf(w));
      retval.setAttribute(HEIGHT_ATT, String.valueOf(h));
    }
    return retval;
  }

  /**
   * A constant for the top border.
   */
  private static final int TOP_BORDER = 0;

  /**
   * A constant for the left border.
   */
  private static final int LEFT_BORDER = 1;

  /**
   * A constant for the bottom border.
   */
  private static final int BOTTOM_BORDER = 2;

  /**
   * A constant for the right border.
   */
  private static final int RIGHT_BORDER = 3;

  /**
   * Returns the borders for the given paper.
   *
   * @param p the paper.
   * @return The borders.
   */
  private int[] getBorders (final Paper p)
  {
    final int[] retval = new int[4];

    retval[0] = (int) p.getImageableY();
    retval[1] = (int) p.getImageableX();
    retval[2] = (int) (p.getHeight() - (p.getImageableY() + p.getImageableHeight()));
    retval[3] = (int) (p.getWidth() - (p.getImageableX() + p.getImageableWidth()));
    return retval;
  }

  /**
   * Finds the page definition from the {@link org.jfree.report.util.PageFormatFactory}
   * class that matches the specified width and height.
   *
   * @param w the width.
   * @param h the height.
   * @return The page definition name.
   */
  public String lookupPageDefinition (final int w, final int h)
  {
    try
    {
      final Field[] fields = PageFormatFactory.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))
        {
          final Object o = f.get(PageFormatFactory.getInstance());
          if (o instanceof int[])
          {
            final int[] pageDef = (int[]) o;
            if (pageDef[0] == w && pageDef[1] == h)
            {
              return f.getName();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.info("Unable to translate the page size", e);
    }
    return null;
  }
}
