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
 * ------------------------------
 * ElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementFactory.java,v 1.1 2003/06/10 12:11:54 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-Jun-2003 : Initial version
 *
 */

package com.jrefinery.report.elementfactory;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import com.jrefinery.report.Element;

public abstract class ElementFactory
{
  private String name;
  private Dimension2D minimumSize;
  private Dimension2D maximumSize;
  private Dimension2D preferredSize;
  private Point2D absolutePosition;
  private Boolean dynamicHeight;

  public ElementFactory()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Dimension2D getMinimumSize()
  {
    return minimumSize;
  }

  public void setMinimumSize(Dimension2D minimumSize)
  {
    this.minimumSize = minimumSize;
  }

  public Dimension2D getMaximumSize()
  {
    return maximumSize;
  }

  public void setMaximumSize(Dimension2D maximumSize)
  {
    this.maximumSize = maximumSize;
  }

  public Dimension2D getPreferredSize()
  {
    return preferredSize;
  }

  public void setPreferredSize(Dimension2D preferredSize)
  {
    this.preferredSize = preferredSize;
  }

  public Point2D getAbsolutePosition()
  {
    return absolutePosition;
  }

  public void setAbsolutePosition(Point2D absolutePosition)
  {
    this.absolutePosition = absolutePosition;
  }

  public Boolean getDynamicHeight()
  {
    return dynamicHeight;
  }

  public void setDynamicHeight(Boolean dynamicHeight)
  {
    this.dynamicHeight = dynamicHeight;
  }

  public abstract Element createElement ();
}
