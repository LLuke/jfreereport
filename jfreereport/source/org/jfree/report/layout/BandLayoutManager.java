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
 * ----------------------
 * BandLayoutManager.java
 * ----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandLayoutManager.java,v 1.9 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Checkstyle fixes (DG);
 *
 */

package org.jfree.report.layout;

import java.awt.geom.Dimension2D;

import org.jfree.report.Band;
import org.jfree.report.style.StyleKey;

/**
 * An interface that defines the methods to be supported by a band layout manager.
 * <p>
 * See the AWT LayoutManager for the idea :)
 *
 * @see org.jfree.report.layout.StaticLayoutManager
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
   * @param containerDims the bounds of the surrounding container.
   *
   * @return the preferred size.
   */
  public Dimension2D preferredLayoutSize(Band b, Dimension2D containerDims);

  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b  the band.
   * @param containerDims the bounds of the surrounding container.
   *
   * @return the minimum size.
   */
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerDims);

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
  public void setLayoutSupport(LayoutSupport target);

  /**
   * Returns the output target for the layout manager.
   *
   * @return the target.
   */
  public LayoutSupport getLayoutSupport();

  /**
   * Clears any cached items used by the layout manager. Invalidates the layout.
   *
   * @param container  the container.
   */
  public void invalidateLayout(Band container);
}
