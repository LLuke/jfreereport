/**
 * Date: Jan 29, 2003
 * Time: 3:37:10 AM
 *
 * $Id: LayoutSupport.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.SizeCalculator;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.pageable.OutputTargetException;

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
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws OutputTargetException;


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
}
