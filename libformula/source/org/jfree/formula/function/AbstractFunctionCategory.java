/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Creation-Date: 05.11.2006, 14:31:22
 *
 * @author Thomas Morgner
 */
public class AbstractFunctionCategory implements FunctionCategory
{
  private String bundleName;

  protected AbstractFunctionCategory(final String bundleName)
  {
    this.bundleName = bundleName;
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

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    return !(o == null || getClass() != o.getClass());
  }

  public int hashCode()
  {
    return getClass().hashCode();
  }
}
