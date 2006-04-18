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
 * TranslateStaticValueExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.04.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.ResourceBundle;
import java.util.Locale;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionUtilities;
import org.jfree.report.i18n.ResourceBundleFactory;

/**
 * Creation-Date: 24.01.2006, 19:43:38
 *
 * @author Thomas Morgner
 */
public class TranslateStaticValueExpression extends AbstractExpression
{
  private String resourceKey;

  /** the used resource bundle. */
  private String resourceIdentifier;

  public TranslateStaticValueExpression()
  {
  }

  public String getResourceKey()
  {
    return resourceKey;
  }

  public void setResourceKey(final String resourceKey)
  {
    this.resourceKey = resourceKey;
  }

  public String getResourceIdentifier()
  {
    return resourceIdentifier;
  }

  public void setResourceIdentifier(final String resourceIdentifier)
  {
    this.resourceIdentifier = resourceIdentifier;
  }


  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue() throws DataSourceException
  {
    final ResourceBundleFactory resourceBundleFactory = getResourceBundleFactory();
    final Locale locale = getParentLocale();
    final ResourceBundle bundle;
    if (resourceIdentifier == null)
    {
      bundle = ExpressionUtilities.getDefaultResourceBundle(this.getRuntime());
    }
    else
    {
      bundle = resourceBundleFactory.getResourceBundle(locale, resourceIdentifier);
    }

    if (resourceKey == null)
    {
      return null;
    }
    return bundle.getObject(resourceKey);
  }
}
