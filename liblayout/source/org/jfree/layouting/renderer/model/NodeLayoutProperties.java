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
 * StaticLayoutProperties.java
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
package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.util.Log;

/**
 * A static properties collection. That one is static; once computed it does
 * not change anymore. It does not (under no thinkable circumstances) depend
 * on the given content. It may depend on static content of the parent.
 *
 * @author Thomas Morgner
 */
public class NodeLayoutProperties implements Serializable
{
  private RenderLength blockContextWidth;

  // Either AUTO or a valid width.
  private RenderLength computedWidth;

  private long maximumBoxWidth;
  private long minimumChunkWidth;
  private long metricsAge;

  /**
   * Whether that element will need intrinsic sizing.
   */
  private boolean intrinsic;
  private boolean finished;

  public NodeLayoutProperties()
  {
    computedWidth = RenderLength.AUTO;
  }

  public RenderLength getBlockContextWidth()
  {
    return blockContextWidth;
  }

  public void setBlockContextWidth(final RenderLength blockContextWidth)
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

  public boolean isIntrinsic()
  {
    return intrinsic;
  }

  public void setIntrinsic(final boolean intrinsic)
  {
    this.intrinsic = intrinsic;
  }

  public RenderLength getComputedWidth()
  {
    return computedWidth;
  }

  public void setComputedWidth(final RenderLength computedWidth)
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

  public boolean isFinished()
  {
    return finished;
  }

  public void setFinished(final boolean finished)
  {
    this.finished = finished;
  }


  public String toString()
  {
    return "NodeLayoutProperties{" +
            "finished=" + finished +
            ", blockContextWidth=" + blockContextWidth +
            ", computedWidth=" + computedWidth +
            ", maximumBoxWidth=" + maximumBoxWidth +
            ", minimumChunkWidth=" + minimumChunkWidth +
            ", metricsAge=" + metricsAge +
            ", intrinsic=" + intrinsic +
            '}';
  }

  public long getMaximumBoxWidth()
  {
    return maximumBoxWidth;
  }

  public void setMaximumBoxWidth(final long maximumBoxWidth)
  {
    this.maximumBoxWidth = maximumBoxWidth;
  }

  public long getMinimumChunkWidth()
  {
    return minimumChunkWidth;
  }

  public void setMinimumChunkWidth(final long minimumChunkWidth)
  {
    this.minimumChunkWidth = minimumChunkWidth;
  }

  public long getMetricsAge()
  {
    return metricsAge;
  }

  public void setMetricsAge(final long metricsAge)
  {
    this.metricsAge = metricsAge;
  }
}
