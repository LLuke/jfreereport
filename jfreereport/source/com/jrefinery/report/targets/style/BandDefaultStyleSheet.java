/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * --------------------------
 * BandDefaultStyleSheet.java
 * --------------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandDefaultStyleSheet.java,v 1.16 2003/06/19 18:44:11 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadoc comments (DG);
 *
 */

package com.jrefinery.report.targets.style;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.base.bandlayout.StaticLayoutManager;
import org.jfree.ui.FloatDimension;

/**
 * A default band style sheet. This StyleSheet defines the default attribute
 * values for all Bands.
 * 
 * @author Thomas Morgner
 */
public class BandDefaultStyleSheet extends BandStyleSheet
{
  /** A shared default style-sheet. */
  private static BandDefaultStyleSheet defaultStyle;
  /** a flag indicating the read-only state of this style sheet. */
  private boolean locked;

  /**
   * Creates a new default style sheet.
   */
  protected BandDefaultStyleSheet()
  {
    super("GlobalBand");
    setStyleProperty(MINIMUMSIZE, new FloatDimension(0, 0));
    setStyleProperty(MAXIMUMSIZE, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));
    setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, new Point2D.Float(0, 0));
    setStyleProperty(BOUNDS, new Rectangle2D.Float());
    setStyleProperty(PAGEBREAK_AFTER, Boolean.FALSE);
    setStyleProperty(PAGEBREAK_BEFORE, Boolean.FALSE);
    setStyleProperty(DISPLAY_ON_FIRSTPAGE, Boolean.TRUE);
    setStyleProperty(DISPLAY_ON_LASTPAGE, Boolean.TRUE);
    setLocked(true);
  }

  /**
   * Gets the locked state of this stylesheet. After the first initialization the
   * stylesheet gets locked, so that it could not be changed anymore.
   *
   * @return true, if this stylesheet is readonly.
   */
  protected boolean isLocked()
  {
    return locked;
  }

  /**
   * Defines the locked-state for this stylesheet.
   *
   * @param locked true, if the stylesheet is locked and read-only, false otherwise.
   */
  protected void setLocked(boolean locked)
  {
    this.locked = locked;
  }

  /**
   * Returns the default band style sheet.
   *
   * @return the style-sheet.
   */
  public static final BandDefaultStyleSheet getBandDefaultStyle ()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new BandDefaultStyleSheet();
    }
    return defaultStyle;
  }

  /**
   * Sets a style property (or removes the style if the value is <code>null</code>).
   *
   * @param key  the style key (<code>null</code> not permitted).
   * @param value  the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException if the value cannot be assigned with the given key.
   * @throws UnsupportedOperationException as this style sheet is read only.
   */
  public void setStyleProperty(StyleKey key, Object value)
  {
    if (isLocked())
    {
      throw new UnsupportedOperationException("This stylesheet is readonly");
    }
    else
    {
      super.setStyleProperty(key, value);
    }
  }

  /**
   * Clones the style-sheet. The assigned parent style sheets are not cloned.
   * The stylesheets are not assigned to the contained stylesheet collection,
   * you have to reassign them manually ...
   *
   * @return the clone.
   */
  public ElementStyleSheet getCopy()
  {
    return this;
  }

  /**
   * Assigns a new stylesheet collection to this stylesheet.
   * <p>
   * Default StyleSheets are shared among all reports, as they are unmodifyable.
   *
   * @param styleSheetCollection
   * @throws IllegalArgumentException
   */
  public final void registerStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
  }

  public final void unregisterStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
  }

  public boolean isGlobalDefault()
  {
    return true;
  }
}
