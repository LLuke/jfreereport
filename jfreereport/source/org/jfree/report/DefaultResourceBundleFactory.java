/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * DefaultResourceBundleFactory.java
 * ------------------------------
 * (C)opyright 2005, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DefaultLogModule.java,v 1.4 2005/01/24 23:57:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01-Jan-2005 : Initial version
 *
 */
package org.jfree.report;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A default implementation of the ResourceBundleFactory, that creates
 * resource bundles using the specified locale.
 * <p>
 * If not defined otherwise, this implementation uses <code>Locale.getDefault()</code>
 *
 * @author Thomas Morgner
 */
public class DefaultResourceBundleFactory implements ResourceBundleFactory
{
  /** The locale. */
  private Locale locale;

  /**
   * Creates a new DefaultResourceBundleFactory using the system's default
   * locale as factory locale.
   */
  public DefaultResourceBundleFactory ()
  {
    this (Locale.getDefault());
  }

  /**
   * Creates a new DefaultResourceBundleFactory using the specified
   * locale as factory locale.
   */
  public DefaultResourceBundleFactory (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  /**
   * Returns the locale that will be used to create the resource bundle.
   *
   * @return the locale.
   */
  public Locale getLocale ()
  {
    return locale;
  }

  /**
   * Defines the locale.
   * @param locale the new locale (never null).
   */
  public void setLocale (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  /**
   * Creates a resource bundle named by the given key and using the factory's defined
   * locale.
   *
   * @param key the name of the resourcebundle
   * @return the created resource bundle
   * @see ResourceBundle#getBundle(String,Locale)
   */ 
  public ResourceBundle getResourceBundle (final String key)
  {
    return ResourceBundle.getBundle(key, locale);
  }
}
