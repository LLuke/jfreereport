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
 * ----------------------------------
 * TextContent.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: TextContent.java,v 1.1 2002/12/02 17:56:56 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.targets.pageable.SizeCalculator;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TextContent extends ContentContainer
{
  private SizeCalculator sizeCalculator;

  /**
   * throws ClassCastE if no string
   *
   * Versucht die Optimale Breite & Höhe für den darzustellenden Text zu finden
   */
  public TextContent(String value, Rectangle2D bounds, SizeCalculator ot)
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
        TextParagraph p = new TextParagraph(getSizeCalculator());
        p.setContent((String) paragraphs.get(i), new Rectangle2D.Double(x, y + usedHeight, w, h - usedHeight));
        usedHeight += p.getBounds().getHeight();
        addContentPart(p);
      }
    }
  }

  public SizeCalculator getSizeCalculator()
  {
    return sizeCalculator;
  }

  private List splitContent(String text)
  {
    List lines = new ArrayList();
    try
    {
      BufferedReader reader = new BufferedReader(new StringReader(text));
      String readLine = null;
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
