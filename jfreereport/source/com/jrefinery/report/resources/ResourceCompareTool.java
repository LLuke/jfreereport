/**
 * Date: Feb 3, 2003
 * Time: 8:41:37 PM
 *
 * $Id: ResourceCompareTool.java,v 1.1 2003/02/03 20:34:12 taqua Exp $
 */
package com.jrefinery.report.resources;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collections;

public class ResourceCompareTool
{
  private static JFreeReportResources loadLocale (String s)
  {
    try
    {
      Class c = Class.forName(JFreeReportResources.class.getName() + "_" + s);
      return (JFreeReportResources) c.newInstance();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public static void main (String [] args)
  {
    if (args.length != 1)
    {
      throw new IllegalArgumentException("Need locale identifier as argument.");
    }

    JFreeReportResources resources = new JFreeReportResources();
    JFreeReportResources compare = loadLocale(args[0]);
    if (compare == null)
    {
      System.err.println("The locale " + args[0] + " is not implemented.");
    }

    Object[][] contentsRes = resources.getContents();
    Object[][] contentsComp = compare.getContents();
    if (compare.getContents() == null)
      throw new IllegalArgumentException("The given localisation is not a valid implementation");

    Hashtable baseContentTable = new Hashtable();
    Hashtable compContentTable = new Hashtable();

    for (int i = 0; i < contentsRes.length; i++)
    {
      String name = (String) contentsRes[i][0];
      Object value = contentsRes[i][1];
      baseContentTable.put (name, value);
    }
    for (int i = 0; i < contentsComp.length; i++)
    {
      String name = (String) contentsComp[i][0];
      Object value = contentsComp[i][1];
      compContentTable.put (name, value);
    }

    ArrayList notImplemented = new ArrayList();
    ArrayList wrongType = new ArrayList();
    ArrayList invalidKey = new ArrayList();

    Enumeration enum = baseContentTable.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      Object valueBase = baseContentTable.get(key);
      Object valueComp = compContentTable.get(key);
      if (valueComp == null)
      {
        notImplemented.add (key);
      }
      else if (valueBase.getClass().isAssignableFrom(valueComp.getClass()) == false)
      {
        wrongType.add(key);
      }
    }

    enum = compContentTable.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      Object valueBase = baseContentTable.get(key);
      if (valueBase == null)
      {
        invalidKey.add(key);
      }
    }

    Collections.sort(wrongType);
    Collections.sort(invalidKey);
    Collections.sort(notImplemented);

    System.out.println ("The following keys return values, which are not of the same baseclass as the original key.");
    for (int i = 0; i < wrongType.size(); i++)
    {
      System.out.println(wrongType.get(i));
    }
    System.out.println ("---------------------------------------");
    System.out.println ("  " + wrongType.size() + " elements listed ");
    System.out.println ("---------------------------------------\n\n");

    System.out.println ("The following keys are not implemented by the localisation.");
    System.out.println ("This does not always indicate an error, if the key does not need to be translated.");
    for (int i = 0; i < notImplemented.size(); i++)
    {
      System.out.println(notImplemented.get(i));
    }
    System.out.println ("---------------------------------------");
    System.out.println ("  " + notImplemented.size() + " elements listed ");
    System.out.println ("---------------------------------------\n\n");

    System.out.println ("The following are invalid. These keys are not implemented by the base class.");
    for (int i = 0; i < invalidKey.size(); i++)
    {
      System.out.println(invalidKey.get(i));
    }
    System.out.println ("---------------------------------------");
    System.out.println ("  " + invalidKey.size() + " elements listed ");
    System.out.println ("---------------------------------------\n\n");

    System.exit(0);
  }
}
