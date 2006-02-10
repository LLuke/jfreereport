/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------------
 * SizeCalculator.java
 * -------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SizeCalculator.java,v 1.4 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 * 08-Feb-2002 : more Docs.
 */

package org.jfree.report.layout;

/**
 * The interface for an class that is able to calculate the width of a given string, and
 * the height of a line of text.  The calculations rely on state information (e.g. font
 * size, graphics device, etc) maintained by the calculator.
 * <p/>
 * Every {@link org.jfree.report.layout.LayoutSupport} can create an instance of a class
 * that implements this interface, via the {@link org.jfree.report.layout.LayoutSupport#createTextSizeCalculator}
 * method.
 *
 * @author Thomas Morgner
 */
public interface SizeCalculator
{
  public static final String USE_MAX_CHAR_SIZE = "org.jfree.report.layout.fontrenderer.UseMaxCharBounds";
  public static final String CLIP_TEXT = "org.jfree.report.layout.fontrenderer.ClipText";
  
  /**
   * Calculates the width of a <code>String<code> in the current <code>Graphics</code>
   * context.
   *
   * @param text         the text.
   * @param lineStartPos the start position of the substring to be measured.
   * @param endPos       the position of the last character to be measured.
   * @return the width of the string in Java2D units.
   */
  public float getStringWidth (String text, int lineStartPos, int endPos);

  /**
   * Returns the line height.  This includes the font's ascent, descent and leading.
   *
   * @return the line height.
   */
  public float getLineHeight ();

}
