/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BorderSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BorderSpecification.java,v 1.2 2006/04/17 20:51:17 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import org.jfree.layouting.layouter.context.BorderDefinition;

/**
 * Creation-Date: 14.12.2005, 23:59:13
 *
 * @author Thomas Morgner
 */
public class BorderSpecification
{
  private BorderDefinition topDefinition;
  private BorderDefinition bottomDefinition;
  private BorderDefinition leftDefinition;
  private BorderDefinition rightDefinition;
  private BorderDefinition breakDefinition;

  private long topLeftHorizontalRadius;
  private long topLeftVerticalRadius;
  private long bottomLeftHorizontalRadius;
  private long bottomLeftVerticalRadius;
  private long topRightHorizontalRadius;
  private long topRightVerticalRadius;
  private long bottomRightHorizontalRadius;
  private long bottomRightVerticalRadius;

  public BorderSpecification()
  {
    topDefinition = new BorderDefinition();
    leftDefinition = new BorderDefinition();
    bottomDefinition = new BorderDefinition();
    rightDefinition = new BorderDefinition();
    breakDefinition = new BorderDefinition();
  }

  public long getTopLeftHorizontalRadius()
  {
    return topLeftHorizontalRadius;
  }

  public void setTopLeftHorizontalRadius(final long topLeftHorizontalRadius)
  {
    this.topLeftHorizontalRadius = topLeftHorizontalRadius;
  }

  public long getTopLeftVerticalRadius()
  {
    return topLeftVerticalRadius;
  }

  public void setTopLeftVerticalRadius(final long topLeftVerticalRadius)
  {
    this.topLeftVerticalRadius = topLeftVerticalRadius;
  }

  public long getBottomLeftHorizontalRadius()
  {
    return bottomLeftHorizontalRadius;
  }

  public void setBottomLeftHorizontalRadius(final long bottomLeftHorizontalRadius)
  {
    this.bottomLeftHorizontalRadius = bottomLeftHorizontalRadius;
  }

  public long getBottomLeftVerticalRadius()
  {
    return bottomLeftVerticalRadius;
  }

  public void setBottomLeftVerticalRadius(final long bottomLeftVerticalRadius)
  {
    this.bottomLeftVerticalRadius = bottomLeftVerticalRadius;
  }

  public long getTopRightHorizontalRadius()
  {
    return topRightHorizontalRadius;
  }

  public void setTopRightHorizontalRadius(final long topRightHorizontalRadius)
  {
    this.topRightHorizontalRadius = topRightHorizontalRadius;
  }

  public long getTopRightVerticalRadius()
  {
    return topRightVerticalRadius;
  }

  public void setTopRightVerticalRadius(final long topRightVerticalRadius)
  {
    this.topRightVerticalRadius = topRightVerticalRadius;
  }

  public long getBottomRightHorizontalRadius()
  {
    return bottomRightHorizontalRadius;
  }

  public void setBottomRightHorizontalRadius(final long bottomRightHorizontalRadius)
  {
    this.bottomRightHorizontalRadius = bottomRightHorizontalRadius;
  }

  public long getBottomRightVerticalRadius()
  {
    return bottomRightVerticalRadius;
  }

  public void setBottomRightVerticalRadius(final long bottomRightVerticalRadius)
  {
    this.bottomRightVerticalRadius = bottomRightVerticalRadius;
  }

  public BorderDefinition getTopDefinition()
  {
    return topDefinition;
  }

  public BorderDefinition getBottomDefinition()
  {
    return bottomDefinition;
  }

  public BorderDefinition getLeftDefinition()
  {
    return leftDefinition;
  }

  public BorderDefinition getRightDefinition()
  {
    return rightDefinition;
  }

  public BorderDefinition getBreakDefinition()
  {
    return breakDefinition;
  }
}
