/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------
 * FloatDimension.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FloatDimension.java,v 1.11 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 23.04.2003 : Initial version
 */
package com.jrefinery.report.targets;

/**
 * A float based implementation of the java.awt.geom.Dimension2D class.
 * This class remains here for compatibility, as this implementation moved
 * into the JCommon library.
 *
 * @author Thomas Morgner
 * @deprecated this class moved into the JCommon library and is now located
 * in the package com.jrefinery.ui.
 */
public class FloatDimension extends org.jfree.ui.FloatDimension
{
  /**
   * Creates a new dimension object with width and height set to zero.
   */
  public FloatDimension()
  {
  }

  /**
   * Creates a new dimension that is a copy of another dimension.
   *
   * @param fd  the dimension to copy.
   */
  public FloatDimension(final FloatDimension fd)
  {
    super(fd);
  }

  /**
   * Creates a new dimension.
   *
   * @param width  the width.
   * @param height  the height.
   */
  public FloatDimension(final float width, final float height)
  {
    super(width, height);
  }
}
