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
 * ------------------------------
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-May-2004 : Initial version
 *
 */

package org.jfree.layout.peer;

import org.jfree.layout.style.FontDefinition;
import org.jfree.layout.peer.view.ViewFactory;

/**
 * The LayoutSupport contains all methods required to estaminate sizes for the
 * content-creation.
 *
 * @author Thomas Morgner
 */
public interface LayoutSupport
{

  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font)
      throws SizeCalculatorException;

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder();

  /**
   * Returns the next horizontal content break. These breaks must be handled
   * specially, as unlucky dividing at these breaks might result in a loss of
   * data.
   *
   * @param source the current source coordinate
   * @return the the next break from there, or -1 if there are no more breaks
   */
  public float getNextHorizontalBreak (float source);

  /**
   * Returns the next vertical content break. These breaks must be handled
   * specially, as unlucky dividing at these breaks might result in a loss of
   * data.
   *
   * @param source the current source coordinate
   * @return the the next break from there, or -1 if there are no more breaks
   */
  public float getNextVerticalBreak (float source);

  /**
   * The page width.
   *
   * @return the width of the page
   */
  public float getWidth ();

  public float getHeight ();

  public ViewFactory getViewFactory();
}
