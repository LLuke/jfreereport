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
 * ----------------------
 * BandLayoutManager.java
 * ----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutManager.java,v 1.3 2002/12/09 03:56:34 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.targets.pageable.OutputTarget;

import java.awt.geom.Dimension2D;

/**
 * An interface that defines the methods to be supported by a band layout manager.  
 * <p>
 * See the AWT LayoutManager for the idea :)
 *
 * @see StaticLayoutManager
 *
 * @author Thomas Morgner
 */
public interface BandLayoutManager
{
  /**
   * The LayoutManager styleKey. All bands must define their LayoutManager by using
   * this key when using the PageableReportProcessor.
   */
  public static final StyleKey LAYOUTMANAGER = StyleKey.getStyleKey("layoutmanager", 
                                                                    BandLayoutManager.class);

  /**
   * Calculates the preferred layout size for a band.
   *
   * @param b  the band.
   *
   * @return the preferred size.
   */
  public Dimension2D preferredLayoutSize(Band b, Dimension2D containerBounds);

  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b  the band.
   *
   * @return the minimum size. 
   */
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerBounds);

  /**
   * Performs the layout of a band.
   *
   * @param b  the band.
   */
  public void doLayout(Band b);

  /**
   * Sets the output target for the layout manager.
   *
   * @param target  the target.
   */
  public void setOutputTarget (OutputTarget target);

  /**
   * Returns the output target for the layout manager.
   *
   * @return the target.
   */
  public OutputTarget getOutputTarget ();

  /**
   * Clears any cached items used by the layout manager.
   */  
  public void flushLayout();
}
