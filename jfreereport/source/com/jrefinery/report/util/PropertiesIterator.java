/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 20.08.2002
 * Time: 21:51:14
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.util;

import java.util.Iterator;
import java.util.Properties;

public class PropertiesIterator implements Iterator
{
  private Properties properties;
  private String prefix;
  private int count;

  public PropertiesIterator(Properties properties)
  {
    this (properties, null);
  }

  public PropertiesIterator(Properties properties, String prefix)
  {
    if (properties == null) throw new NullPointerException();
    this.properties = properties;
    this.prefix = prefix;
    this.count = 0;
  }

  public boolean hasNext()
  {
    System.out.println ("Has no next: " +getNextKey());
    System.out.println ("Props: " +properties);
    return properties.containsKey(getNextKey());
  }

  private String getNextKey ()
  {
    if (prefix == null)
    {
      return String.valueOf(count);
    }
    return prefix + String.valueOf(count);
  }

  public Object next()
  {
    String value = properties.getProperty(getNextKey());
    count++;
    return value;
  }

  public void remove()
  {
    // is ignored
  }
}
