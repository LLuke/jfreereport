/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: ReportConfigWriter.java,v 1.8 2003/05/30 16:57:51 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Properties;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.ReportConfigHandler;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.PageFormatFactory;
import com.jrefinery.report.util.ReportConfiguration;

/**
 * A report configuration writer.
 * 
 * @author Thomas Morgner.
 */
public class ReportConfigWriter extends AbstractXMLDefinitionWriter
{
  /**
   * A report configuration writer.
   * 
   * @param reportWriter  the report writer.
   * @param indentLevel the current indention level.
   */
  public ReportConfigWriter(ReportWriter reportWriter, int indentLevel)
  {
    super(reportWriter, indentLevel);
  }

  /**
   * Writes the report configuration element.
   * 
   * @param writer  the writer.
   * 
   * @throws IOException if there is an I/O problem.
   */
  public void write(Writer writer) throws IOException
  {
    writeTag(writer, ExtReportHandler.REPORT_CONFIG_TAG);
    writeTag(writer, ReportConfigHandler.DEFAULT_PAGEFORMAT_TAG,
             buildPageFormatProperties(), CLOSE);
    writeTag(writer, ReportConfigHandler.CONFIGURATION_TAG);
    ReportConfiguration config = getReport().getReportConfiguration();
    Enumeration enum = config.getConfigProperties();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      String value = config.getConfigProperty(key);
      if (value != null)
      {
        writeTag(writer, "property", "name", key, OPEN);
        writer.write(normalize(value));
        writeCloseTag(writer, "property");
      }
    }

    writeCloseTag(writer, ReportConfigHandler.CONFIGURATION_TAG);
    writeCloseTag(writer, ExtReportHandler.REPORT_CONFIG_TAG);
  }

  /**
   * Compiles a collection of page format properties.
   * 
   * @return The properties.
   */
  private Properties buildPageFormatProperties ()
  {
    Properties retval = new Properties();
    PageFormat fmt = getReport().getDefaultPageFormat();
    int[] borders = getBorders(fmt.getPaper());

    if (fmt.getOrientation() == PageFormat.LANDSCAPE)
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, 
                         ReportConfigHandler.ORIENTATION_LANDSCAPE_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, 
                         String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, 
                         String.valueOf(borders[BOTTOM_BORDER]));
    }
    else if (fmt.getOrientation() == PageFormat.PORTRAIT)
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, 
                         ReportConfigHandler.ORIENTATION_PORTRAIT_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, 
                         String.valueOf(borders[BOTTOM_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, 
                         String.valueOf(borders[RIGHT_BORDER]));
    }
    else
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, 
                         ReportConfigHandler.ORIENTATION_REVERSE_LANDSCAPE_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, 
                         String.valueOf(borders[BOTTOM_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, 
                         String.valueOf(borders[RIGHT_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
    }

    int w = (int) fmt.getPaper().getWidth();
    int h = (int) fmt.getPaper().getHeight();

    String pageDefinition = lookupPageDefinition(w, h);
    if (pageDefinition != null)
    {
      retval.setProperty(ReportConfigHandler.PAGEFORMAT_ATT, pageDefinition);
    }
    else
    {
      retval.setProperty(ReportConfigHandler.WIDTH_ATT, String.valueOf(w));
      retval.setProperty(ReportConfigHandler.HEIGHT_ATT, String.valueOf(h));
    }
    return retval;
  }

  /** A constant for the top border. */
  private static final int TOP_BORDER = 0;
  
  /** A constant for the left border. */
  private static final int LEFT_BORDER = 1;
  
  /** A constant for the bottom border. */
  private static final int BOTTOM_BORDER = 2;
  
  /** A constant for the right border. */
  private static final int RIGHT_BORDER = 3;

  /**
   * Returns the borders for the given paper.
   * 
   * @param p  the paper.
   * 
   * @return The borders.
   */
  private int[] getBorders (Paper p)
  {
    int [] retval = new int[4];

    retval[0] = (int) p.getImageableY();
    retval[1] = (int) p.getImageableX();
    retval[2] = (int) (p.getHeight() - (p.getImageableY() + p.getImageableHeight()));
    retval[3] = (int) (p.getWidth() - (p.getImageableX() + p.getImageableWidth()));
    return retval;
  }

  /**
   * Finds the page definition from the {@link PageFormatFactory} class that matches the
   * specified width and height.
   * 
   * @param w  the width.
   * @param h  the height.
   * 
   * @return The page definition name.
   */
  public String lookupPageDefinition (int w, int h)
  {
    try
    {
      Field[] fields = PageFormatFactory.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))
        {
          Object o = f.get(PageFormatFactory.getInstance());
          if (o instanceof int[])
          {
            int[] pageDef = (int[]) o;
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
      Log.info ("Unable to translate the page size", e);
    }
    return null;
  }
}
