package org.jfree.report;

import java.util.Locale;
import java.util.ResourceBundle;

public class DefaultResourceBundleFactory implements ResourceBundleFactory
{
  private Locale locale;

  public DefaultResourceBundleFactory ()
  {
    this (Locale.getDefault());
  }

  public DefaultResourceBundleFactory (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  public Locale getLocale ()
  {
    return locale;
  }

  public void setLocale (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  public ResourceBundle getResourceBundle (final String key)
  {
    return ResourceBundle.getBundle(key);
  }
}
