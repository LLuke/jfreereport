/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * HorizontalLineTemplate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HorizontalLineTemplate.java,v 1.2 2003/08/18 21:36:39 taqua Exp $
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
public class HorizontalLineTemplate extends AbstractTemplate
{
  /**
   * Default Constructor.
   */
  public HorizontalLineTemplate()
  {
  }

  /**
   * Returns the template value, an horizontal line.
   * 
   * @return a horizontal line with a width of 100. 
   */
  public Object getValue()
  {
    return new Line2D.Float(0,0, 100, 0);
  }
}
