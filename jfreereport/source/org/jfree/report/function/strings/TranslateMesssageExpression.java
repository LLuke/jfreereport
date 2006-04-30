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
 * ResourceMesssageFormatExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TranslateMesssageExpression.java,v 1.1 2006/04/18 11:45:15 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.function.ExpressionUtilities;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.report.util.MessageFormatSupport;
import org.jfree.util.ObjectUtilities;

/**
 * This does the same as the ResourceMessageFormatFilter.
 *
 * @author Thomas Morgner
 */
public class TranslateMesssageExpression extends AbstractExpression
{
  /** The report definition registered to this connectable. */
  private transient String formatString;
  private transient Locale locale;

  private String formatKey;
  private String nullValue;

  /** the used resource bundle. */
  private String resourceIdentifier;
  private MessageFormatSupport messageFormatSupport;

  public TranslateMesssageExpression()
  {
    messageFormatSupport = new MessageFormatSupport();
  }

  public String getFormatKey()
  {
    return formatKey;
  }

  public void setFormatKey(final String formatKey)
  {
    this.formatKey = formatKey;
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
    updateMessageFormat();
    messageFormatSupport.setNullString(nullValue);
    return messageFormatSupport.performFormat(getDataRow());
  }

  private void updateMessageFormat()
  {
    final ResourceBundle bundle;
    final Locale newLocale = getParentLocale();
    if (resourceIdentifier == null)
    {
      bundle = ExpressionUtilities.getDefaultResourceBundle(this.getRuntime());
    }
    else
    {
      final ResourceBundleFactory resourceBundleFactory = getResourceBundleFactory();
      bundle = resourceBundleFactory.getResourceBundle(newLocale,
              resourceIdentifier);
    }

    // update the format string, if neccessary ...
    final String newFormatString = bundle.getString(formatKey);
    if (ObjectUtilities.equal(newFormatString, formatString) == false)
    {
      messageFormatSupport.setFormatString(newFormatString);
      formatString = newFormatString;
    }

    // update the locale, if neccessary ...
    if (ObjectUtilities.equal(newLocale, locale) == false)
    {
      messageFormatSupport.setLocale(newLocale);
      locale = newLocale;
    }
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    updateMessageFormat();
    info.setDependendFields(messageFormatSupport.getFields());
  }

  public String getNullValue()
  {
    return nullValue;
  }

  public void setNullValue(final String nullValue)
  {
    this.nullValue = nullValue;
  }
}
