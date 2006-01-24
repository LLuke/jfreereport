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
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function.strings;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.filter.MessageFormatSupport;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * This does the same as the ResourceMessageFormatFilter.
 *
 * @author Thomas Morgner
 */
public class ResourceMesssageFormatExpression extends AbstractExpression
{
  /** The report definition registered to this connectable. */
  private transient String formatString;
  private transient Locale locale;

  private String formatKey;

  /** the used resource bundle. */
  private String resourceIdentifier;
  private MessageFormatSupport messageFormatSupport;

  public ResourceMesssageFormatExpression()
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
  public Object getValue()
  {
    final ResourceBundleFactory resourceBundleFactory = getResourceBundleFactory();
    final ResourceBundle bundle;
    if (resourceIdentifier == null)
    {
      bundle = ExpressionUtilities.getDefaultResourceBundle(this);
    }
    else
    {
       bundle = resourceBundleFactory.getResourceBundle(resourceIdentifier);
    }

    // update the format string, if neccessary ...
    final String newFormatString = bundle.getString(formatKey);
    if (ObjectUtilities.equal(newFormatString, formatString) == false)
    {
      messageFormatSupport.setFormatString(newFormatString);
      formatString = newFormatString;
    }
    // update the locale, if neccessary ...
    final Locale newLocale = resourceBundleFactory.getLocale();
    if (ObjectUtilities.equal(newLocale, locale) == false)
    {
      messageFormatSupport.setLocale(resourceBundleFactory.getLocale());
      locale = newLocale;
    }

    return messageFormatSupport.performFormat(getDataRow());
  }
}
