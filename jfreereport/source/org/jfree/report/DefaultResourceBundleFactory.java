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
 * $Id: DefaultResourceBundleFactory.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
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
 * A default implementation of the ResourceBundleFactory, that creates resource bundles
 * using the specified locale.
 * <p/>
 * If not defined otherwise, this implementation uses <code>Locale.getDefault()</code> as
 * Locale.
 *
 * @author Thomas Morgner
 */
public class DefaultResourceBundleFactory implements ResourceBundleFactory
{
  /**
   * The locale.
   */
  private Locale locale;

  /**
   * Creates a new DefaultResourceBundleFactory using the system's default locale as
   * factory locale.
   */
  public DefaultResourceBundleFactory ()
  {
    this(Locale.getDefault());
  }

  /**
   * Creates a new DefaultResourceBundleFactory using the specified locale as factory
   * locale.
   *
   * @param locale the Locale instance that should be used when creating ResourceBundles.
   * @throws NullPointerException if the given Locale is null.
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
   * Redefines the locale. The locale given must not be null.
   *
   * @param locale the new locale (never null).
   * @throws NullPointerException if the given locale is null.
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
   * @param key the name of the resourcebundle, never null.
   * @return the created resource bundle
   *
   * @throws NullPointerException if <code>key</code> is
   *                              <code>null</code>
   * @throws java.util.MissingResourceException
   *                              if no resource bundle for the specified base name can be
   *                              found
   * @see ResourceBundle#getBundle(String,Locale)
   */
  public ResourceBundle getResourceBundle (final String key)
  {
    return ResourceBundle.getBundle(key, locale);
  }
}
