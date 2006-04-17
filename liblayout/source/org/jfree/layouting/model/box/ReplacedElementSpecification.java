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
 * ReplacedElementSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.model.box;

import java.awt.Shape;

import org.jfree.layouting.input.style.keys.box.Fit;

public class ReplacedElementSpecification
{
  /** Replaced content treatment */
  private Shape crop;
  private Fit fit;
  private long fitPositionTop;
  private long fitPositionLeft;

  public ReplacedElementSpecification ()
  {
  }

  public Shape getCrop ()
  {
    return crop;
  }

  public void setCrop (Shape crop)
  {
    this.crop = crop;
  }

  public Fit getFit ()
  {
    return fit;
  }

  public void setFit (Fit fit)
  {
    this.fit = fit;
  }

  public long getFitPositionLeft ()
  {
    return fitPositionLeft;
  }

  public void setFitPositionLeft (long fitPositionLeft)
  {
    this.fitPositionLeft = fitPositionLeft;
  }

  public long getFitPositionTop ()
  {
    return fitPositionTop;
  }

  public void setFitPositionTop (long fitPositionTop)
  {
    this.fitPositionTop = fitPositionTop;
  }


}
