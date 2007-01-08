/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Immutable version of the node-layout properties.
 *
 * @author Thomas Morgner
 */
public class ComputedLayoutProperties implements Serializable
{
  // This represents the computed with.
  private RenderLength blockContextWidth;
  // Either AUTO or a valid width.
  private RenderLength computedWidth;

  public ComputedLayoutProperties(final RenderLength blockContextWidth,
                                  final RenderLength computedWidth)
  {
    setBlockContextWidth(blockContextWidth);
    setComputedWidth(computedWidth);
  }

  private void setBlockContextWidth(final RenderLength blockContextWidth)
  {
    if (blockContextWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (blockContextWidth == RenderLength.AUTO)
    {
      this.blockContextWidth = RenderLength.AUTO;
    }
    else if (blockContextWidth.getValue() <= 0)
    {
      this.blockContextWidth = RenderLength.EMPTY;
    }
    else
    {
      this.blockContextWidth = blockContextWidth;
    }
  }

  private void setComputedWidth(final RenderLength computedWidth)
  {
    if (computedWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (computedWidth == RenderLength.AUTO)
    {
      this.computedWidth = RenderLength.AUTO;
    }
    else if (computedWidth.getValue() <= 0)
    {
      this.computedWidth = RenderLength.EMPTY;
    }
    else
    {
      this.computedWidth = computedWidth;
    }
  }

  public RenderLength getBlockContextWidth()
  {
    return blockContextWidth;
  }

  public RenderLength getComputedWidth()
  {
    return computedWidth;
  }
}
