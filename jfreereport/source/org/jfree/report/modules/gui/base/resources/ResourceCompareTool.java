/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ------------------------
 * ResourceCompareTool.java
 * ------------------------
 *
 * $Id: ResourceCompareTool.java,v 1.2 2003/08/18 18:27:59 taqua Exp $
 *
 * Changes
 * -------
 * 03-Feb-2003 : Initial version
 *
 */
package org.jfree.report.modules.gui.base.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The ResourceCompareTool provides simple reporting capabilities to compare a
 * localized resource bundle with the default resource bundle.
 * <p>
 * This report contains all keys, which are undefined in the localisation, which
 * are defined with an incompabile object value or which are not defined in the
 * default resource bundle, but defined in the localized version.
 * <p>
 * Using the automated comparing will help to make translations more easier.
 *
 * @author Thomas Morgner
 */
public final class ResourceCompareTool
{
  /** 
   * Default Constructor.
   */
  private ResourceCompareTool()
  {
  }
  
  /**
   * Loads the resource bundle for the given locale.
   *
   * @param s the locale specification as ISO country code.
   * @param base the base name of the resource bundle.
   * @return the loaded resources or null, if there is no such translation.
   */
  private static JFreeReportResources loadLocale(final String base, final String s)
  {
    try
    {
      String className;
      if (s != null)
      {
        className = base + "_" + s;
      }
      else
      {
        className = base;
      }
      final Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
      return (JFreeReportResources) c.newInstance();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Executes the test for the given localisation. This compares that all defined values
   * are valid and that no extra keys are defined. It also prints some status information
   * that may be helpfull to verify the integrity of the translation. 
   * 
   * @param base the base name of the resource bundle.
   * @param locale the locale that should be tested.
   */
  public static void executeTest (String base, String locale)
  {
    final JFreeReportResources resources = loadLocale(base, null);
    final JFreeReportResources compare = loadLocale(base, locale);

    if (compare == null)
    {
      System.err.println("The locale " + locale + " is not implemented.");
      System.exit(1);
    }

    final Object[][] contentsRes = resources.getContents();
    final Object[][] contentsComp = compare.getContents();
    if (compare.getContents() == null)
    {
      throw new IllegalArgumentException("The given localisation is not a valid implementation");
    }
    final Hashtable baseContentTable = new Hashtable();
    final Hashtable compContentTable = new Hashtable();

    for (int i = 0; i < contentsRes.length; i++)
    {
      final String name = (String) contentsRes[i][0];
      final Object value = contentsRes[i][1];
      baseContentTable.put(name, value);
    }
    for (int i = 0; i < contentsComp.length; i++)
    {
      final String name = (String) contentsComp[i][0];
      final Object value = contentsComp[i][1];
      compContentTable.put(name, value);
    }

    final ArrayList notImplemented = new ArrayList();
    final ArrayList wrongType = new ArrayList();
    final ArrayList invalidKey = new ArrayList();

    Enumeration enum = baseContentTable.keys();
    while (enum.hasMoreElements())
    {
      final String key = (String) enum.nextElement();
      final Object valueBase = baseContentTable.get(key);
      final Object valueComp = compContentTable.get(key);
      if (valueComp == null)
      {
        notImplemented.add(key);
      }
      else if (valueBase.getClass().isAssignableFrom(valueComp.getClass()) == false)
      {
        wrongType.add(key);
      }
    }

    enum = compContentTable.keys();
    while (enum.hasMoreElements())
    {
      final String key = (String) enum.nextElement();
      final Object valueBase = baseContentTable.get(key);
      if (valueBase == null)
      {
        invalidKey.add(key);
      }
    }

    Collections.sort(wrongType);
    Collections.sort(invalidKey);
    Collections.sort(notImplemented);

    System.out.println("The following keys return values, which are not of the same baseclass as "
        + "the original key.");
    for (int i = 0; i < wrongType.size(); i++)
    {
      System.out.println(wrongType.get(i));
    }
    System.out.println("---------------------------------------");
    System.out.println("  " + wrongType.size() + " elements listed ");
    System.out.println("---------------------------------------\n\n");

    System.out.println("The following keys are not implemented by the localisation.");
    System.out.println("This does not always indicate an error, if the key does not need to be "
        + "translated.");
    for (int i = 0; i < notImplemented.size(); i++)
    {
      System.out.println(notImplemented.get(i));
    }
    System.out.println("---------------------------------------");
    System.out.println("  " + notImplemented.size() + " elements listed ");
    System.out.println("---------------------------------------\n\n");

    System.out.println("The following are invalid. These keys are not implemented by the base "
        + "class.");
    for (int i = 0; i < invalidKey.size(); i++)
    {
      System.out.println(invalidKey.get(i));
    }
    System.out.println("---------------------------------------");
    System.out.println("  " + invalidKey.size() + " elements listed ");
    System.out.println("---------------------------------------\n\n");

    System.exit(0);
  }

  /**
   * Starts the resource comparing process.
   *
   * @param args a string array with only one element, the ISO code of the localisation.
   */
  public static void main(final String[] args)
  {
    if (args.length != 2)
    {
      throw new IllegalArgumentException("Need locale identifier as argument.");
    }

    executeTest(args[0], args[1]);
  }
}
