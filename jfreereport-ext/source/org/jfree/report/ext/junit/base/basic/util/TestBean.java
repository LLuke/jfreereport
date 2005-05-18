/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TestBean.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class TestBean
{
  private String simpleString;
  private int simpleInt;
  private boolean simpleBool;
  private double simpleDouble;
  private Color simpleColor;
  private ArrayList fullyIndexed;
  private String[] arrayOnly;
  private ArrayList indexOnly;

  public TestBean ()
  {
    indexOnly = new ArrayList();
    fullyIndexed = new ArrayList();
  }

  public String[] getArrayOnly ()
  {
    return arrayOnly;
  }

  public void setArrayOnly (final String[] arrayOnly)
  {
    this.arrayOnly = arrayOnly;
  }

  public String[] getFullyIndexed ()
  {
    return (String[]) fullyIndexed.toArray(new String [fullyIndexed.size()]);
  }

  public void setFullyIndexed (final String[] fullyIndexed)
  {
    this.fullyIndexed.clear();
    if (fullyIndexed != null)
    {
      this.fullyIndexed.addAll(Arrays.asList(fullyIndexed));
    }
  }

  public String getFullyIndexed (final int idx)
  {
    return (String) fullyIndexed.get(idx);
  }

  public void setFullyIndexed (final int idx, final String indexOnly)
  {
    if (this.fullyIndexed.size() == idx)
    {
      this.fullyIndexed.add(indexOnly);
    }
    else
    {
      this.fullyIndexed.set(idx, indexOnly);
    }
  }

  public String getIndexOnly (final int idx)
  {
    return (String) indexOnly.get(idx);
  }

  public void setIndexOnly (final int idx, final String indexOnly)
  {
    if (this.indexOnly.size() == idx)
    {
      this.indexOnly.add(indexOnly);
    }
    else
    {
      this.indexOnly.set(idx, indexOnly);
    }
  }



  public boolean isSimpleBool ()
  {
    return simpleBool;
  }

  public void setSimpleBool (final boolean simpleBool)
  {
    this.simpleBool = simpleBool;
  }

  public Color getSimpleColor ()
  {
    return simpleColor;
  }

  public void setSimpleColor (final Color simpleColor)
  {
    this.simpleColor = simpleColor;
  }

  public double getSimpleDouble ()
  {
    return simpleDouble;
  }

  public void setSimpleDouble (final double simpleDouble)
  {
    this.simpleDouble = simpleDouble;
  }

  public int getSimpleInt ()
  {
    return simpleInt;
  }

  public void setSimpleInt (final int simpleInt)
  {
    this.simpleInt = simpleInt;
  }

  public String getSimpleString ()
  {
    return simpleString;
  }

  public void setSimpleString (final String simpleString)
  {
    this.simpleString = simpleString;
  }
}
