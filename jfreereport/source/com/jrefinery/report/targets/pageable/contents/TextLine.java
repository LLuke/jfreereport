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
 * TextLine.java
 * ----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: TextLine.java,v 1.1 2002/12/02 17:56:56 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.targets.pageable.SizeCalculator;

import java.awt.geom.Rectangle2D;

public class TextLine implements Content
{
  private SizeCalculator sizeCalc;
  private String content;
  private Rectangle2D bounds;

  public TextLine (SizeCalculator sizeCalc)
  {
    bounds = new Rectangle2D.Float();
    this.sizeCalc = sizeCalc;
    if (sizeCalc.getLineHeight() == 0) throw new IllegalStateException();
  }

  public SizeCalculator getSizeCalculator()
  {
    return sizeCalc;
  }

  public void setContent (String content, Rectangle2D maxBounds)
  {
    if (maxBounds.getX () < 0) throw new IllegalArgumentException();
    if (maxBounds.getY () < 0) throw new IllegalArgumentException();
    if (maxBounds.getWidth () < 0) throw new IllegalArgumentException();
    if (maxBounds.getHeight () < 0) throw new IllegalArgumentException();

    this.content = content;
    Rectangle2D bounds = new Rectangle2D.Float();
    double width = Math.min (maxBounds.getWidth(), getSizeCalculator().getStringWidth(content, 0, content.length()));
    double height = Math.min(maxBounds.getHeight(), getSizeCalculator().getLineHeight());
    bounds.setRect(maxBounds.getX(), maxBounds.getY(), width, height);
    setBounds(bounds);
  }

  // get the content from all content parts
  public String getContent()
  {
    return content;
  }

  // get all contentParts making up that content or null, if this class
  // has no subcontents
  public Content getContentPart(int part)
  {
    return null;
  }

  public int getContentPartCount()
  {
    return 0;
  }

  public Rectangle2D getBounds()
  {
    return bounds.getBounds();
  }

  private void setBounds(Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  public ContentType getContentType()
  {
    return ContentType.Text;
  }

  // this is a single line, so either the content does fit the height, or it doesn't.
  // in that case, return nothing at all.
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
   * @param startPos
   * @param maxWidth
   * @return
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
   * Calculates the StringPos: the string starts at startPos, and it is sure,
   * that the endPos is after the pos calculated for maxWidth.
   * <p>
   * The algorithm used is similiar to the quick-search algorithm, it splits the
   * content in the middle and checks whether the needed position is left or right
   * of the middle. Then the search is repeated within the new search area ...
   *
   * @param startPos
   * @param endPos
   * @param maxWidth
   * @return
   */
  private int calculateWidthPos (final int lineStart, int startPos, int endPos, double maxWidth)
  {
    //Log.debug ("CalcLW: " + startPos + ", " + endPos);
/*
    try
    {
      Thread.sleep(50);
    }
    catch (Exception e)
    {
    }
*/
    if (startPos == endPos)
    {
      return startPos;
    }

    int delta = ((endPos - startPos) / 2) + startPos;
    if (delta == startPos) return startPos;
    if (delta == endPos) return endPos;

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

  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }
}
