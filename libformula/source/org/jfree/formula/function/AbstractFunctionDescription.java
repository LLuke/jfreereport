/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creation-Date: 04.11.2006, 18:30:33
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFunctionDescription implements FunctionDescription
{
  private String bundleName;

  protected AbstractFunctionDescription(final String bundleName)
  {
    this.bundleName = bundleName;
  }

  public boolean isVolatile()
  {
    return false;
  }

  /**
   * Returns the default value for an optional parameter. If the value returned
   * here is null, then this either means, that the parameter is mandatory or
   * that the default value is computed by the expression itself.
   *
   * @param position
   * @return null.
   */
  public Object getDefaultValue(int position)
  {
    return null;
  }

  public boolean isInfiniteParameterCount()
  {
    return false;
  }

  protected ResourceBundle getBundle(Locale locale)
  {
    return ResourceBundle.getBundle(bundleName, locale);
  }

  public String getDisplayName(Locale locale)
  {
    return getBundle(locale).getString("display-name");
  }

  public String getDescription(Locale locale)
  {
    return getBundle(locale).getString("description");
  }

  public String getParameterDisplayName(int position, Locale locale)
  {
    return getBundle(locale).getString("parameter." + position + ".display-name");
  }

  public String getParameterDescription(int position, Locale locale)
  {
    return getBundle(locale).getString("parameter." + position + ".description");
  }


}
