/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------------
 * OutputTarget.java
 * -----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: OutputTarget.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : Introduced drawImage and drawMultiLine
 * 10-May-2002 : Documentation
 * 16-May-2002 : Interface of drawShape changed
 * 
 */

package com.jrefinery.report;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.print.PageFormat;

/**
 * An interface that defines the methods that must be supported by a report output target.
 * JFreeReport currently implements two targets:  one for Graphics2D (screen and printer) and
 * one for Acrobat PDF files.
 */
public interface OutputTarget
{

  /**
   * Opens the target.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author);

  /**
   * Closes the target.
   */
  public void close ();

  /**
   * Returns the page format for the target.
   *
   * @return The page format.
   */
  public PageFormat getPageFormat ();

  /**
   * Sets the page format for the target.
   *
   * @param format The page format.
   */
  public void setPageFormat (PageFormat format);

  /**
   * Returns the coordinate of the left edge of the page.
   *
   * @return The left edge of the page.
   */
  public float getPageX ();

  /**
   * Returns the coordinate of the top edge of the page.
   *
   * @return The top edge of the page.
   */
  public float getPageY ();

  /**
   * Returns the page width in points (1/72 inch).
   *
   * @return The page width.
   */
  public float getPageWidth ();

  /**
   * Returns the page height in points (1/72 inch).
   *
   * @return The page height.
   */
  public float getPageHeight ();

  /**
   * Returns the left edge of the printable area of the page.  The units are points.
   *
   * @return The left edge of the printable area of the page.
   */
  public float getUsableX ();

  /**
   * Returns the top edge of the printable area of the page.  The units are points.
   *
   * @return The top edge of the printable area of the page.
   */
  public float getUsableY ();

  /**
   * Returns the width (in points) of the printable area of the page.
   *
   * @return The width of the printable area of the page.
   */
  public float getUsableWidth ();

  /**
   * Returns the height (in points) of the printable area of the page.
   *
   * @return The height of the printable area of the page.
   */
  public float getUsableHeight ();

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont (Font font);

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  public void setPaint (Paint paint);

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   * @param x1 The x-coordinate for the upper left corner.
   * @param y1 The y-coordinate for the upper left corner.
   * @param x2 The x-coordinate for the lower right corner.
   * @param y2 The y-coordinate for the lower right corner.
   * @param alignment The horizontal alignment.
   */
  public void drawString (String text,
                          float x1, float y1, float x2, float y2,
                          int alignment);

  /**
   * Draws a shape relative to the specified coordinates.
   *
   * @param shape The shape to draw.
   * @param x The x coordinate.
   * @param y The y coordinate.
   */
  public void drawShape (ShapeElement shape, float x, float y);


  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param shape The shape to draw.
   * @param x The x coordinate.
   * @param y The y coordinate.
   */
  public void drawImage (ImageReference image, float x, float y);

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text). The text is split at the end of the line and continued in the next line.
   *
   * @param text The text.
   * @param x1 The x-coordinate for the upper left corner.
   * @param y1 The y-coordinate for the upper left corner.
   * @param x2 The x-coordinate for the lower right corner.
   * @param y2 The y-coordinate for the lower right corner.
   * @param alignment The horizontal alignment.
   */
  public void drawMultiLineText (String mytext, float x, float y, float w, float h, int align);

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   */
  public void endPage ();
}