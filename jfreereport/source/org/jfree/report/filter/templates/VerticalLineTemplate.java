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
 * VerticalLineTemplate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: VerticalLineTemplate.java,v 1.4 2003/08/25 14:29:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12-Jul-2003 : Initial version
 *
 */

package org.jfree.report.filter.templates;

import java.awt.geom.Line2D;

/**
 * Defines a horizontal line template. The line always has the width of 100 points.
 * This implementation is used to cover the common use of the line shape element.
 * Use the scaling feature of the shape element to adjust the size of the line.
 *
 * @author Thomas Morgner
 */
public class VerticalLineTemplate extends AbstractTemplate
{
  /**
   * Default Constructor.
   */
  public VerticalLineTemplate()
  {
  }

  /**
   * Returns the template value, a vertical line.
   *
   * @return a vertical line with a height of 100.
   */
  public Object getValue()
  {
    return new Line2D.Float(0, 0, 0, 100);
  }
}
