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
 * ------------------------------
 * RectangleTemplate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RectangleTemplate.java,v 1.5 2004/05/07 08:24:41 mungady Exp $
 *
 * Changes
 * -------------------------
 * 12-Jul-2003 : Initial version
 *
 */

package org.jfree.report.filter.templates;

import java.awt.geom.Rectangle2D;

import org.jfree.report.util.geom.StrictBounds;

/**
 * A template to create rectangle elements. The rectangle always has the width and
 * the height of 100 points.
 * <p>
 * This implementation is used to cover the common use of the rectangle shape element.
 * Use the scaling feature of the shape element to adjust the size of the rectangle.
 *
 * @author Thomas Morgner
 */
public class RectangleTemplate extends AbstractTemplate
{
  /**
   * Default Constructor.
   */
  public RectangleTemplate()
  {
  }

  /**
   * Returns the template value, a Rectangle2D.
   *
   * @return a rectangle with a width and height of 100.
   */
  public Object getValue()
  {
    return new StrictBounds(0, 0, 100, 100);
  }
}
