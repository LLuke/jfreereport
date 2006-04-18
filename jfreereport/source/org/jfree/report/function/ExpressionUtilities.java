/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ExpressionUtilities.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ExpressionUtilities.java,v 1.1 2006/01/24 19:01:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creation-Date: 24.01.2006, 19:11:14
 *
 * @author Thomas Morgner
 */
public class ExpressionUtilities
{
  private ExpressionUtilities()
  {
  }

  public static ResourceBundle getDefaultResourceBundle
          (final ExpressionRuntime expression)
  {
    final Locale locale = expression.getDeclaringParent().getLocale();
    final String resourceBundleName =
            expression.getConfiguration().getConfigProperty
            ("org.jfree.report.ResourceBundle");
    return expression.getResourceBundleFactory().getResourceBundle
            (locale, resourceBundleName);
  }
}
