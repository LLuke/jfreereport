/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TextContent.java,v 1.18 2005/09/07 14:23:49 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.content;

import java.util.ArrayList;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.util.LineBreakIterator;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.util.Log;

/**
 * A container for text content. The content will be split into paragraphs.
 *
 * @author Thomas Morgner
 * @see TextParagraph
 */
public class TextContent extends ContentContainer
{
  /** A configuration key to enable some additional checks. */
  public static final String DEBUG_TEXTCONTENT_KEY =
          "org.jfree.report.content.DebugTextContent";

  /**
   * A size calculator.
   */
  private final SizeCalculator sizeCalculator;

  /**
   * Creates a new container for text. The line height can be used to extend the height of
   * a single text line. It can not be used to narrow down the text line.
   *
   * @param value           the text.
   * @param lineHeight      the height of a text line
   * @param bounds          the bounds.
   * @param ot              the size calculator.
   * @param reservedLiteral the text that should be appended if the text does not fit into
   *                        the bounds
   * @param trimTextContent defines, whether to remove whitespaces from the start and end
   *                        of an line.
   */
  public TextContent (final String value, final long lineHeight,
                      final StrictBounds bounds, final SizeCalculator ot,
                      final String reservedLiteral, final boolean trimTextContent)
  {
    super(bounds);
    this.sizeCalculator = ot;

    final long x = bounds.getX();
    final long y = bounds.getY();
    final long w = bounds.getWidth();
    final long h = bounds.getHeight();

    if (w <= 0)
    {
      return;
    }

    final LineBreakIterator iterator = new LineBreakIterator(value);
    if (iterator.hasNext() == false)
    {
      return;
    }
    final String firstLine = (String) iterator.next();
    if (iterator.hasNext())
    {
      final ArrayList paragraphs = new ArrayList();
      paragraphs.add(firstLine);
      while (iterator.hasNext())
      {
        paragraphs.add(iterator.next());
      }

      long usedHeight = 0;
      for (int i = 0; i < paragraphs.size(); i++)
      {
        final TextParagraph p = new TextParagraph
                (getSizeCalculator(), lineHeight, reservedLiteral, trimTextContent);
        p.setContent((String) paragraphs.get(i), x, y + usedHeight, w, h - usedHeight);
        usedHeight += p.getBounds().getHeight();
        addContentPart(p);
      }

      if (usedHeight == 0 &&
              JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
              (DEBUG_TEXTCONTENT_KEY, "false").equals("true"))
      {
        Log.warn("Empty TextContent created. This might indicate an invalid font size definition.");
      }
    }
    else
    {
      // the simplified, one paragraph version ..
      final TextParagraph p = new TextParagraph
              (getSizeCalculator(), lineHeight, reservedLiteral, trimTextContent);
      p.setContent(firstLine, x, y, w, h);
      addContentPart(p);
    }
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  private SizeCalculator getSizeCalculator ()
  {
    return sizeCalculator;
  }
}
