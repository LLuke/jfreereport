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
 * -------------
 * TextLine.java
 * -------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextLine.java,v 1.4 2002/12/06 20:34:13 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.targets.pageable.SizeCalculator;

import java.awt.geom.Rectangle2D;

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

  /**
   * Creates a new line of text. 
   *
   * @param sizeCalc  the size calculator.
   */
  public TextLine (SizeCalculator sizeCalc)
  {
    bounds = new Rectangle2D.Float();
    this.sizeCalc = sizeCalc;
    if (sizeCalc.getLineHeight() == 0)
    {
      throw new IllegalStateException();
    }
  }

  /**
   * Returns the size calculator.
   *
   * @return the size calculator.
   */
  public SizeCalculator getSizeCalculator()
  {
    return sizeCalc;
  }

  /**
   * Sets the content for the line of text.
   *
   * @param content  the new string.
   * @param maxBounds  the maximum bounds for the string.
   */
  public void setContent (String content, Rectangle2D maxBounds)
  {
    if (maxBounds.getX () < 0) 
    {
      throw new IllegalArgumentException();
    }
    if (maxBounds.getY () < 0) 
    {
      throw new IllegalArgumentException();
    }
    if (maxBounds.getWidth () < 0) 
    {
      throw new IllegalArgumentException();
    }
    if (maxBounds.getHeight () < 0) 
    {
      throw new IllegalArgumentException();
    }

    this.content = content;
    Rectangle2D bounds = new Rectangle2D.Float();
    double width = Math.min (maxBounds.getWidth(), 
                             getSizeCalculator().getStringWidth(content, 0, content.length()));
    double height = Math.min(maxBounds.getHeight(), getSizeCalculator().getLineHeight());
    bounds.setRect(maxBounds.getX(), maxBounds.getY(), width, height);
    setBounds(bounds);
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
   * This class does not store subcontent items, so this method always returns null.
   *
   * @param part  ignored.
   *
   * @return null.
   */
  public Content getContentPart(int part)
  {
    return null;
  }

  /**
   * This class does not store subcontent items, so this method always returns zero.
   *
   * @return zero.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * Returns the bounds of the text.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds();
  }

  /**
   * Sets the bounds of the string.
   *
   * @param bounds  the bounds.
   */
  private void setBounds(Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  /**
   * Returns the content type, in this case ContentType.TEXT.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.TEXT;
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
  public Content getContentForBounds(Rectangle2D bounds)
  {
    Rectangle2D myBounds = getBounds();
    Rectangle2D actBounds = myBounds.createIntersection(bounds);
    if (actBounds.getHeight() < myBounds.getHeight())
    {
      return null;
    }
    //double frontX = myBounds.getX();
    double frontW = actBounds.getX() - myBounds.getX();

    int frontPos = calcStringLength (0, frontW);
    int endPos = calcStringLength(frontPos, actBounds.getWidth());

    if (frontPos == endPos)
    {
      return null;
    }

    TextLine line = new TextLine(getSizeCalculator());
    line.setContent(content.substring(frontPos, endPos), actBounds);
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
  private int calcStringLength (int startPos, double maxWidth)
  {
    if (content.length() == 0)
    {
      return 0;
    }

    float lineWidth = getSizeCalculator().getStringWidth(content, startPos, content.length());
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
  private int calculateWidthPos (final int lineStart, int startPos, int endPos, double maxWidth)
  {
    if (startPos == endPos)
    {
      return startPos;
    }

    int delta = ((endPos - startPos) / 2) + startPos;
    if (delta == startPos) 
    {
      return startPos;
    }
    if (delta == endPos) 
    {
      return endPos;
    }

    double wDelta = getSizeCalculator().getStringWidth(content, lineStart, delta);
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

  public String toString ()
  {
    return (getClass().getName() + "={ content=\"" + getContent() + "\", bounds=" + getBounds()); 
  }
}
