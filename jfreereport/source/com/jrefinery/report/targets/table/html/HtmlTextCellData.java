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
 * ---------------------
 * HtmlTextCellData.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlTextCellData.java,v 1.7 2003/05/02 12:40:41 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import com.jrefinery.report.util.HtmlWriter;
import com.jrefinery.report.util.LineBreakIterator;
import com.jrefinery.report.util.Log;

/**
 * A wrapper for text content within the generated HtmlTable.
 * 
 * @author Thomas Morgner
 */
public class HtmlTextCellData extends HtmlCellData
{
  /** the text content that should be printed within the cell. */
  private String value;

  /**
   * Creates a new HtmlTextCellData for the given content.
   *
   * @param outerBounds the cell bounds.
   * @param value the text content.
   * @param style the style definition for the cell.
   * @param useXHTML a flag indicating whether to use XHTML.
   */
  public HtmlTextCellData(Rectangle2D outerBounds, String value, HtmlCellStyle style, 
                          boolean useXHTML)
  {
    super(outerBounds, style, useXHTML);
    if (value == null) 
    {
      throw new NullPointerException();
    }
    this.value = value;
  }

  /**
   * Writes the (X)HTML-Code to print the Text-Content.
   *
   * @param pout the print writer, which receives the generated HTML-Code.
   * @param filesystem not used.
   */
  public void write(HtmlWriter pout, HtmlFilesystem filesystem)
  {
    try
    {
      printText(pout, value, isUseXHTML());
    }
    catch (IOException ioe)
    {
      // should not happen
      Log.warn ("Unexpected I/O-Error", ioe);
    }
  }

  /**
   * Gets a flag, which indicates whether this cell contains background definitions.
   *
   * @return false, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }


  /**
   * Generates the HTML output for printing the given text.
   *
   * @param pout the target writer
   * @param text the text that should be printed.
   * @param useXHTML true, if XHTML is generated, false otherwise.
   * @throws IOException if printing the text failed.
   */
  public static void printText(HtmlWriter pout, String text, boolean useXHTML)
    throws IOException
  {
    if (text.length() == 0)
    {
      return;
    }

    LineBreakIterator iterator = new LineBreakIterator(text);
    boolean flagStart = true;
    while (iterator.hasNext())
    {
      String readLine = (String) iterator.next();

      if (flagStart == true)
      {
        flagStart = false;
      }
      else
      {
        if (useXHTML)
        {
          pout.println("<br />&nbsp;");
        }
        else
        {
          pout.println("<br>&nbsp;");
        }
      }
      HtmlProducer.getEntityParser().encodeEntities(readLine, pout);
    }
  }

}
