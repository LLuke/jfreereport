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
 * $Id: TextContent.java,v 1.10 2003/03/18 17:14:41 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.pageable.operations.TextOperationModule;
import com.jrefinery.report.util.LineBreakIterator;
import com.jrefinery.report.util.Log;

/**
 * A container for text content. The content will be split into paragraphs.
 *
 * @see TextParagraph
 *
 * @author Thomas Morgner
 */
public class TextContent extends ContentContainer
{
  /** A size calculator. */
  private SizeCalculator sizeCalculator;

  /**
   * Creates a new container for text. The line height can be used to extend the height
   * of a single text line. It can not be used to narrow down the text line. 
   *
   * @param value  the text.
   * @param lineHeight the height of a text line
   * @param bounds  the bounds.
   * @param ot  the size calculator.
   */
  public TextContent(String value, float lineHeight, Rectangle2D bounds, SizeCalculator ot)
  {
    super (bounds);
    this.sizeCalculator = ot;

    float x = (float) bounds.getX();
    float y = (float) bounds.getY();
    float w = (float) bounds.getWidth();
    float h = (float) bounds.getHeight();
    float usedHeight = 0;

    if (w != 0)
    {
      List paragraphs = splitContent(value);
      for (int i = 0; i < paragraphs.size(); i++)
      {
        TextParagraph p = new TextParagraph(getSizeCalculator(), lineHeight);
        p.setContent((String) paragraphs.get(i),
                      new Rectangle2D.Float(x, y + usedHeight, w, h - usedHeight));
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

    // check if empty content ... this case is easy ...
    if (text.length() == 0)
      return lines;

    LineBreakIterator iterator = new LineBreakIterator(text);
    int oldPos = 0;
    int pos = iterator.nextWithEnd();
    while (pos != LineBreakIterator.DONE)
    {
      lines.add(text.substring(oldPos, pos));
      oldPos = pos;
      pos = iterator.nextWithEnd();
    }
    return lines;
  }

}
