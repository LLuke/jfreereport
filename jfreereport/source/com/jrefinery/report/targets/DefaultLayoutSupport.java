/**
 * Date: Jan 29, 2003
 * Time: 3:44:05 AM
 *
 * $Id: DefaultLayoutSupport.java,v 1.2 2003/01/29 18:37:11 taqua Exp $
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import com.jrefinery.report.targets.pageable.OutputTargetException;

public class DefaultLayoutSupport implements LayoutSupport
{
  public DefaultLayoutSupport()
  {
  }

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
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws OutputTargetException
  {
    return new DefaultSizeCalculator(font);
  }

  /**
   * Creates and returns a default layout manager for this output target.
   * <p>
   * Note that a new layout manager is created every time this method is called.
   *
   * @return a default layout manager.
   */
  public BandLayoutManager getDefaultLayoutManager()
  {
    BandLayoutManager lm = new StaticLayoutManager();
    lm.setLayoutSupport(this);
    return lm;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return 0;
  }
}
