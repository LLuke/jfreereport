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
 * LayoutSupport.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: LayoutSupport.java,v 1.4 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 */
package org.jfree.report.layout;

import org.jfree.report.content.ContentFactory;
import org.jfree.report.style.FontDefinition;

/**
 * The LayoutSupport contains all methods required to estaminate sizes for the
 * content-creation.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.content.Content
 */
public interface LayoutSupport
{

  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font the font.
   * @return the size calculator.
   *
   * @throws SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator (FontDefinition font)
          throws SizeCalculatorException;

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder ();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder ();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalVerticalAlignmentBorder ();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalHorizontalAlignmentBorder ();

  /**
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory ();

  public LayoutManagerCache getCache();
}
