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
 * -----------------------------
 * ElementDefaultStyleSheet.java
 * -----------------------------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ElementDefaultStyleSheet.java,v 1.4 2004/05/07 08:14:24 mungady Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadoc comments (DG);
 *
 */

package org.jfree.report.style;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jfree.report.ElementAlignment;
import org.jfree.ui.FloatDimension;

/**
 * The default element style sheet. This style sheet defines default attribute
 * values for all elements.
 * <p>
 * The default valignment was modified to top.
 * 
 * @author Thomas Morgner
 */
public class ElementDefaultStyleSheet extends ElementStyleSheet
{
  /** The default paint. */
  public static final Color DEFAULT_PAINT = Color.black;

  /** The default font. */
  public static final FontDefinition DEFAULT_FONT_DEFINITION = new FontDefinition("Serif", 10);

  /** A shared default style-sheet. */
  private static ElementDefaultStyleSheet defaultStyle;
  /** a flag indicating the read-only state of this style sheet. */
  private boolean locked;

  /**
   * Creates a new style sheet.
   */
  protected ElementDefaultStyleSheet(final String name)
  {
    super(name);
    setStyleProperty(MINIMUMSIZE, new FloatDimension(0, 0));
    setStyleProperty(MAXIMUMSIZE, new FloatDimension(Short.MAX_VALUE, Short.MAX_VALUE));
    setStyleProperty(BOUNDS, new Rectangle2D.Float());
    setStyleProperty(PAINT, DEFAULT_PAINT);
    setFontDefinitionProperty(DEFAULT_FONT_DEFINITION);
    setStyleProperty(VALIGNMENT, ElementAlignment.TOP);
    setStyleProperty(ALIGNMENT, ElementAlignment.LEFT);
    setStyleProperty(VISIBLE, Boolean.TRUE);
    setStyleProperty(LINEHEIGHT, new Float(0));
    setStyleProperty(ELEMENT_LAYOUT_CACHEABLE, Boolean.TRUE);
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
  protected void setLocked(final boolean locked)
  {
    this.locked = locked;
  }

  /**
   * Returns the default element style sheet.
   *
   * @return the style-sheet.
   */
  public static final ElementDefaultStyleSheet getDefaultStyle()
  {
    if (defaultStyle == null)
    {
      defaultStyle = new ElementDefaultStyleSheet("GlobalElement");
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
  public void setStyleProperty(final StyleKey key, final Object value)
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
   * Returns true, if this stylesheet is one of the global default stylesheets.
   * Global default stylesheets are unmodifiable and shared among all element stylesheets.
   *
   * @return always true.
   */
  public boolean isGlobalDefault()
  {
    return true;
  }

  protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
  {
    throw new UnsupportedOperationException
            ("Cannot add other stylesheets as parent to the global default stylesheet");
  }
}
