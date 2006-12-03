/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
package org.jfree.fonts.registry;

/**
 * Creation-Date: 24.07.2006, 18:36:21
 *
 * @author Thomas Morgner
 */
public class BaselineInfo
{
  public static final int HANGING = 0;
  public static final int MATHEMATICAL = 1;
  public static final int CENTRAL = 2;
  public static final int MIDDLE = 3;
  public static final int ALPHABETIC = 4;
  public static final int IDEOGRAPHIC = 5;

  private double[] baselines;
  private int dominantBaseline;

  public BaselineInfo()
  {
    this.baselines = new double[6];
  }

  public double[] getBaselines()
  {
    return (double[]) baselines.clone();
  }

  public void setBaselines(final double[] baselines)
  {
    if (baselines.length != 6)
    {
      throw new IllegalArgumentException();
    }
    this.baselines = (double[]) baselines.clone();
  }

  public double getBaseline (int indx)
  {
    return baselines[indx];
  }

  public void setBaseline (int idx, double baseline)
  {
    baselines[idx] = baseline;
  }

  public int getDominantBaseline()
  {
    return dominantBaseline;
  }

  public void setDominantBaseline(final int dominantBaseline)
  {
    this.dominantBaseline = dominantBaseline;
  }
}
