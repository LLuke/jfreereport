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
 * Spacing.java
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
package org.jfree.layouting.renderer.text;

/**
 * Additional character spacing. This has a minimum, optimum and maximum. If the
 * optimum is less than the minimum the optimum is set to the minimum. If the
 * optimum is greater than the maximum the optimum is set to the maximum value.
 * <p/>
 * Spacing is given in absolute values, the unit is micro-points.
 *
 * @author Thomas Morgner
 */
public class Spacing
{
  public static final Spacing EMPTY_SPACING = new Spacing(0, 0, 0);

  private int minimum;
  private int maximum;
  private int optimum;

  public Spacing(final int minimum, final int optimum, final int maximum)
  {
    if (maximum < minimum)
    {
      this.minimum = minimum;
      this.maximum = maximum;
    }
    else
    {
      this.minimum = minimum;
      this.maximum = maximum;
    }
    if (optimum < this.minimum)
    {
      this.optimum = this.minimum;
    }
    else if (optimum > this.maximum)
    {
      this.optimum = this.maximum;
    }
    else
    {
      this.optimum = optimum;
    }
  }

  public int getMinimum()
  {
    return minimum;
  }

  public int getMaximum()
  {
    return maximum;
  }

  public int getOptimum()
  {
    return optimum;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final Spacing spacing = (Spacing) o;

    if (maximum != spacing.maximum)
    {
      return false;
    }
    if (minimum != spacing.minimum)
    {
      return false;
    }
    if (optimum != spacing.optimum)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = minimum;
    result = 29 * result + maximum;
    result = 29 * result + optimum;
    return result;
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer();
    b.append(getClass().getName());
    b.append("={minimum=");
    b.append(minimum);
    b.append(", optimum=");
    b.append(optimum);
    b.append(", maximum=");
    b.append(maximum);
    b.append("}");
    return b.toString();
  }
}