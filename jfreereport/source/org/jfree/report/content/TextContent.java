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
 * $Id: TextContent.java,v 1.13 2005/02/23 21:04:37 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.content;

import java.util.ArrayList;
import java.util.List;

import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.util.LineBreakIterator;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.geom.StrictBounds;

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
    long usedHeight = 0;

    if (w != 0)
    {
      final List paragraphs = splitContent(value);
      for (int i = 0; i < paragraphs.size(); i++)
      {
        final TextParagraph p = new TextParagraph
                (getSizeCalculator(), lineHeight, reservedLiteral, trimTextContent);
        p.setContent((String) paragraphs.get(i), x, y + usedHeight, w, h - usedHeight);
        usedHeight += p.getBounds().getHeight();
        addContentPart(p);
      }

      if (usedHeight == 0 &&
              ReportConfiguration.getGlobalConfig().getConfigProperty
              (DEBUG_TEXTCONTENT_KEY, "false").equals("true"))
      {
        Log.warn("Empty TextContent created. This might indicate an invalid font size definition.");
      }
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

  /**
   * Returns the supplied text as a list of lines/paragraphs.
   *
   * @param text the text.
   * @return a list of lines/paragraphs.
   */
  private List splitContent (final String text)
  {
    final List lines = new ArrayList();

    // check if empty content ... this case is easy ...
    if (text.length() == 0)
    {
      return lines;
    }

    final LineBreakIterator iterator = new LineBreakIterator(text);
    while (iterator.hasNext())
    {
      lines.add(iterator.next());
    }
    return lines;
  }

}
