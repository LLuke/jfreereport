/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ----------------
 * TextContent.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextContent.java,v 1.4 2003/02/05 13:25:29 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A container for text content.
 *
 * @author Thomas Morgner
 */
public class TextContent extends ContentContainer
{
  /** A size calculator. */
  private SizeCalculator sizeCalculator;

  /**
   * Creates a new container for text.
   *
   * @param value  the text.
   * @param bounds  the bounds.
   * @param ot  the size calculator.
   *
   * Versucht die Optimale Breite & H�he f�r den darzustellenden Text zu finden
   */
  public TextContent(String value, float lineHeight, Rectangle2D bounds, SizeCalculator ot)
  {
    super (bounds);
    this.sizeCalculator = ot;

    double x = bounds.getX();
    double y = bounds.getY();
    double w = bounds.getWidth();
    double h = bounds.getHeight();
    double usedHeight = 0;

    if (w != 0)
    {
      List paragraphs = splitContent(value);
      for (int i = 0; i < paragraphs.size(); i++)
      {
        TextParagraph p = new TextParagraph(getSizeCalculator(), lineHeight);
        p.setContent((String) paragraphs.get(i),
                      new Rectangle2D.Double(x, y + usedHeight, w, h - usedHeight));
        usedHeight += p.getBounds().getHeight();
        addContentPart(p);
      }
    }
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  public SizeCalculator getSizeCalculator()
  {
    return sizeCalculator;
  }

  /**
   * Returns the supplied text as a list of lines/paragraphs.
   *
   * @param text  the text.
   *
   * @return a list of lines/paragraphs.
   */
  private List splitContent(String text)
  {
    List lines = new ArrayList();
    try
    {
      BufferedReader reader = new BufferedReader(new StringReader(text));
      String readLine;
      while ((readLine = reader.readLine()) != null)
      {
        lines.add(readLine);
      }
      reader.close();
    }
    catch (IOException ioe)
    {
      Log.info("This will not happen.", ioe);
    }
    return lines;
  }

}
