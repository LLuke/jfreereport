/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * NumberFormatFilter.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.filter;

import java.text.NumberFormat;
import java.text.Format;

public class NumberFormatFilter extends FormatFilter
{
  public NumberFormatFilter ()
  {
    setFormatter(NumberFormat.getInstance());
  }

  public void setNumberFormat (NumberFormat nf)
  {
    setFormatter(nf);
  }

  public NumberFormat getNumberFormat ()
  {
    return (NumberFormat) getFormatter();
  }

  public void setFormatter (Format f)
  {
    super.setFormatter((NumberFormat) f);
  }

  public void setGroupingUsed(boolean newValue)
  {
    getNumberFormat().setGroupingUsed(newValue);
  }

  public boolean isGroupingUsed()
  {
    return getNumberFormat().isGroupingUsed();
  }

  public void setMaximumFractionDigits(int newValue)
  {
    getNumberFormat().setMaximumFractionDigits(newValue);
  }

  public int getMaximumFractionDigits()
  {
    return getNumberFormat().getMaximumFractionDigits();
  }

  public void setMaximumIntegerDigits(int newValue)
  {
    getNumberFormat().setMaximumFractionDigits(newValue);
  }

  public int getMaximumIntegerDigits()
  {
    return getNumberFormat().getMaximumFractionDigits();
  }

  public void setMinimumFractionDigits(int newValue)
  {
    getNumberFormat().setMaximumFractionDigits(newValue);
  }

  public int getMinimumFractionDigits()
  {
    return getNumberFormat().getMaximumFractionDigits();
  }

  public void setMinimumIntegerDigits(int newValue)
  {
    getNumberFormat().setMaximumFractionDigits(newValue);
  }

  public int getMinimumIntegerDigits()
  {
    return getNumberFormat().getMaximumFractionDigits();
  }
}
