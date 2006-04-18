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
 * ResourceLookupExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ResourceBundleLookupExpression.java,v 1.1 2006/01/24 19:01:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.ResourceBundle;

import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionUtilities;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 24.01.2006, 19:43:38
 *
 * @author Thomas Morgner
 */
public class TranslateValueExpression extends AbstractExpression
{
  private String field;

  /** the used resource bundle. */
  private String resourceIdentifier;

  public TranslateValueExpression()
  {
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
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
    final ResourceBundle bundle;
    if (resourceIdentifier == null)
    {
      bundle = ExpressionUtilities.getDefaultResourceBundle(this.getRuntime());
    }
    else
    {
      bundle = resourceBundleFactory.getResourceBundle
              (getParentLocale(), resourceIdentifier);
    }

    final Object key = getDataRow().get(getField());
    if (key == null)
    {
      return null;
    }
    return bundle.getObject(String.valueOf(key));
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
  
}
