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
 * -------------------
 * LayoutSupport.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutSupport.java,v 1.1 2003/02/07 22:40:40 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 */
package com.jrefinery.report.targets.base.layout;

import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.FontDefinition;

/**
 * The LayoutSupport contains all methods required to estaminate sizes for the
 * content-creation.
 *
 * @see com.jrefinery.report.targets.base.content.Content
 * @see com.jrefinery.report.targets.pageable.operations.OperationModule
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
   * @throws com.jrefinery.report.targets.pageable.OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws SizeCalculatorException;


  /**
   * Returns the default layout manager.
   *
   * @return the default layout manager.
   */
  public BandLayoutManager getDefaultLayoutManager ();

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
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory ();
}
