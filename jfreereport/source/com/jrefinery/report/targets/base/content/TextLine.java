/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -------------
 * TextLine.java
 * -------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextLine.java,v 1.13 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.base.layout.SizeCalculator;

/**
 * Represents a line of text.
 *
 * @author Thomas Morgner
 */
public class TextLine implements Content
{
  /** The size calculator. */
  private SizeCalculator sizeCalc;

  /** The string. */
  private String content;

  /** The content bounds. */
  private Rectangle2D bounds;

  /** The lineHeight defined for this line. */
  private float lineHeight;

  /**
   * Creates a new line of text.
   *
   * @param sizeCalc  the size calculator.
   * @param lineheight the line height that should be used for this text line.
   */
  public TextLine(final SizeCalculator sizeCalc, final float lineheight)
  {
    bounds = new Rectangle2D.Float();
    this.lineHeight = lineheight;
    this.sizeCalc = sizeCalc;
    if (sizeCalc.getLineHeight() == 0)
    {
      throw new IllegalStateException("This size calculator is not valid");
    }
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  private SizeCalculator getSizeCalculator()
  {
    return sizeCalc;
  }

  /**
   * Returns the height of this text line.
   * @return the height of the line of text.
   */
  public float getHeight()
  {
    return (float) bounds.getHeight();
  }

  /**
   * Returns the content type, in this case
   * {@link com.jrefinery.report.targets.base.content.ContentType#TEXT}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.TEXT;
  }

  /**
   * Sets the content for the line of text.
   *
   * @param content the string for this line of text.
   * @param x the x coordinates for the bounds.
   * @param y the y coordinates for the bounds.
   * @param width the width of the bounds.
   * @param height the height of the bounds.
   */
  public void setContent(final String content, final float x, final float y, float width, float height)
  {
    if (x < 0)
    {
      throw new IllegalArgumentException();
    }
    if (y < 0)
    {
      throw new IllegalArgumentException();
    }
    if (width < 0)
    {
      throw new IllegalArgumentException();
    }
    if (height < 0)
    {
      throw new IllegalArgumentException();
    }

    this.content = content;
    width = Math.min(width, getSizeCalculator().getStringWidth(content, 0, content.length()));
    height = Math.min(height, getSizeCalculator().getLineHeight());
    // apply custom lineheight if greater than the current height ...
    height = Math.max(height, lineHeight);
    bounds.setRect(x, y, width, height);
  }

  /**
   * Returns the text content.
   *
   * @return the text.
   */
  public String getContent()
  {
    return content;
  }

  /**
   * This class does not store sub-content items, so this method always returns zero.
   *
   * @return always zero, a text line is atomic.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * This class does not store sub-content items, so this method always returns <code>null</code>.
   *
   * @param part  ignored.
   *
   * @return <code>null</code>.
   */
  public Content getContentPart(final int part)
  {
    return null;
  }

  /**
   * Returns the bounds of the text.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  /**
   * Returns the content that fits in the specified bounds.
   * <p>
   * This is a single line, so either the content does fit the height, or it doesn't (in that
   * case, return nothing at all).
   *
   * @param bounds  the bounds.
   *
   * @return the content that fits the specified bounds.
   */
  public Content getContentForBounds(final Rectangle2D bounds)
  {
    final Rectangle2D actBounds = this.bounds.createIntersection(bounds);
    if (actBounds.getHeight() < this.bounds.getHeight())
    {
      return null;
    }
    final float frontW = (float) (actBounds.getX() - this.bounds.getX());
    final int frontPos = calcStringLength(0, frontW);
    final int endPos = calcStringLength(frontPos, (float) actBounds.getWidth());

    if (frontPos == endPos)
    {
      // the line would not contain any text ...
      return null;
    }

    final TextLine line = new TextLine(getSizeCalculator(), lineHeight);
    line.setContent(content.substring(frontPos, endPos),
        (float) actBounds.getX(),
        (float) actBounds.getY(),
        (float) actBounds.getWidth(),
        (float) actBounds.getHeight());
    return line;
  }

  /**
   * Calculates the last character that would fit into the given width.
   *
   * @param startPos  the starting position within the string.
   * @param maxWidth  the maximum width (in Java2D units).
   *
   * @return the number of characters that will fit within a certain width.
   */
  private int calcStringLength(final int startPos, final float maxWidth)
  {
    if (maxWidth == 0.0)
    {
      return 0;
    }
    if (content.length() == 0)
    {
      return 0;
    }

    final float lineWidth = getSizeCalculator().getStringWidth(content, startPos, content.length());
    if (lineWidth <= maxWidth)
    {
      return content.length();
    }

    return calculateWidthPos(startPos, startPos, content.length(), maxWidth);
  }

  /**
   * Calculates the StringPos: the string starts at <code>startPos</code>, and it tested,
   * that <code>endPos</code> is after the pos calculated for maxWidth.
   * <p>
   * <code>getStringWidth (content, lineStart, startPos) &lt; maxWidth<br></code>
   * <code>getStringWidth (content, lineStart, endPos) &gt; maxWidth<br></code>
   * <p>
   * The algorithm used is similiar to the quick-search algorithm, it splits the
   * content in the middle and checks whether the needed position is left or right
   * of the middle. Then the search is repeated within the new search area ...
   * <p>
   * This method searches the width position in a substring of the content which
   * starts at lineStart. It is known, that the position is between startPos and
   * endPos, startPos is greater or equal than lineStart and endPos is lower
   * or equal to the position of the last character of the string.
   * <p>
   *
   * @param lineStart  the starting position of the substring that should be calculated.
   * @param startPos  the known lower rangeboundry of the result-range.
   * @param endPos  the known upper boundry of the result range.
   * @param maxWidth  the maximal width in points which limit the string.
   *
   * @return the position, where the string width is nearest or equal to maxWidth..
   */
  private int calculateWidthPos
      (final int lineStart, final int startPos, final int endPos, final float maxWidth)
  {
    if (startPos == endPos)
    {
      return startPos;
    }

    final int delta = ((endPos - startPos) / 2) + startPos;
    if (delta == startPos)
    {
      return startPos;
    }
    if (delta == endPos)
    {
      return endPos;
    }

    final float wDelta = getSizeCalculator().getStringWidth(content, lineStart, delta);
    if (wDelta == maxWidth)
    {
      return delta;
    }
    if (wDelta > maxWidth)
    {
      return calculateWidthPos(lineStart, startPos, delta, maxWidth);
    }
    return calculateWidthPos(lineStart, delta, endPos, maxWidth);
  }

  /**
   * Return the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }

  /**
   * Returns a string representation of this text line.
   *
   * @return a string representation.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append(getClass().getName());
    b.append("={ content=\"");
    b.append(getContent());
    b.append("\", bounds=");
    b.append(getBounds());
    return (b.toString());
  }
}
