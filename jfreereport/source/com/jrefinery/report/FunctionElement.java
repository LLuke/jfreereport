/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * --------------------
 * FunctionElement.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionElement.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner, with modifications by DG (DG);
 * 10-May-2002 : Removed all complex constructors
 * 20-May-2002 : Declared deprecated. This class is no longer used. The ItemFactory produces
 *               TextElements instead which get different filters attached.
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.ReportDataSource;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.FunctionDataSource;
import com.jrefinery.report.filter.DataFilter;

import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * The base class for all elements in JFreeReport that display function values.
 * This class separates the functional part (@see ReportFunction) from the presentation
 * layer.  The report-function given at construction time is used as key for the
 * function collection.
 * @deprecated form this element by stacking it together by using filters
 */
public abstract class FunctionElement extends TextElement
{

  /** The name of the function that this element gets its value from. */
  private FunctionDataSource functionsource;

  /**
   * Constructs a function element using float coordinates.
   * @deprecated form this element by stacking it together by using filters
   */
  protected FunctionElement ()
  {
    functionsource = new FunctionDataSource();
    DataFilter df = getTextFilter ();
    df.setDataSource (functionsource);
  }

  /**
   * Returns the name of the function that this element obtains its value from.
   * @return The function name.
   * @deprecated form this element by stacking it together by using filters
   */
  public String getFunctionName ()
  {
    return functionsource.getFunction();
  }

  /**
   * defines the name of the function that this element obtains its value from.
   * @param function The function name.
   * @deprecated form this element by stacking it together by using filters
   */
  public void setFunctionName (String function)
  {
    functionsource.setFunction(function);
  }

  protected FunctionDataSource getFunctionDataSource ()
  {
    return functionsource;
  }

}
