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
 * ------------------------------
 * ShapeElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeElementFactory.java,v 1.1 2003/06/10 12:11:54 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.06.2003 : Initial version
 *
 */

package com.jrefinery.report.elementfactory;

import java.awt.Color;
import java.awt.Stroke;

public abstract class ShapeElementFactory extends ElementFactory
{
  private Color color;
  private Stroke stroke;
  private Boolean scale;
  private Boolean keepAspectRatio;
  private Boolean shouldFill;
  private Boolean shouldDraw;

  public ShapeElementFactory()
  {
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public Stroke getStroke()
  {
    return stroke;
  }

  public void setStroke(Stroke stroke)
  {
    this.stroke = stroke;
  }

  public Boolean getScale()
  {
    return scale;
  }

  public void setScale(Boolean scale)
  {
    this.scale = scale;
  }

  public Boolean getKeepAspectRatio()
  {
    return keepAspectRatio;
  }

  public void setKeepAspectRatio(Boolean keepAspectRatio)
  {
    this.keepAspectRatio = keepAspectRatio;
  }

  public Boolean getShouldFill()
  {
    return shouldFill;
  }

  public void setShouldFill(Boolean shouldFill)
  {
    this.shouldFill = shouldFill;
  }

  public Boolean getShouldDraw()
  {
    return shouldDraw;
  }

  public void setShouldDraw(Boolean shouldDraw)
  {
    this.shouldDraw = shouldDraw;
  }
}
