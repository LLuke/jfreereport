/**
 * Date: Jan 29, 2003
 * Time: 3:37:10 AM
 *
 * $Id$
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

}
