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
 * $Id: FunctionElement.java,v 1.1.1.1 2002/04/25 17:02:23 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner, with modifications by DG (DG);
 * 10-May-2002 : Removed all complex constructors
 */

package com.jrefinery.report;

import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

/**
 * The base class for all elements in JFreeReport that display function values.
 * This class separates the functional part (@see ReportFunction) from the presentation
 * layer.  The report-function given at construction time is used as key for the
 * function collection.
 */
public abstract class FunctionElement extends TextElement
{

  /** The name of the function that this element gets its value from. */
  private String functionName;

  /** The current value of the function. */
  private Object value;

  /**
   * Constructs a function element using float coordinates.
   */
  protected FunctionElement ()
  {
  }

  /**
   * Returns the name of the function that this element obtains its value from.
   * @return The function name.
   */
  public String getFunctionName ()
  {
    return this.functionName;
  }

  /**
   * defines the name of the function that this element obtains its value from.
   * @param function The function name.
   */
  public void setFunctionName (String function)
  {
    if (function == null)
      throw new NullPointerException ("Function must not be null");

    this.functionName = function;
  }

  /**
   * Returns the value for the element.
   */
  public Object getValue ()
  {
    return this.value;
  }

  /**
   * Sets the current value of the element.
   * @param value The new value for the element;
   */
  public void setValue (Object value)
  {
    this.value = value;
  }

  /**
   * Returns a string representing the formatted data.
   * @return A formatted version of the data value.
   */
  public String getFormattedText ()
  {
    return String.valueOf (getValue ());
  }
}
