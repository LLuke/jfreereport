/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id: DefaultFunctionRegistry.java,v 1.7 2007/01/26 22:11:51 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jfree.util.Configuration;
import org.jfree.util.HashNMap;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.11.2006, 12:48:32
 *
 * @author Thomas Morgner
 */
public class DefaultFunctionRegistry implements FunctionRegistry
{
  private static final String FUNCTIONS_PREFIX = "org.jfree.formula.functions.";

  private FunctionCategory[] categories;
  private HashNMap categoryFunctions;
  private HashMap functions;
  private HashMap functionMetaData;

  public DefaultFunctionRegistry()
  {
    categoryFunctions = new HashNMap();
    functionMetaData = new HashMap();
    functions = new HashMap();
    categories = new FunctionCategory[0];
  }

  public FunctionCategory[] getCategories()
  {
    return (FunctionCategory[]) categories.clone();
  }

  public Function[] getFunctions()
  {
    final String[] fnClasses = (String[]) functions.values().toArray
        (new String[functions.size()]);
    final ArrayList functions = new ArrayList(fnClasses.length);
    for (int i = 0; i < fnClasses.length; i++)
    {
      final String aClass = fnClasses[i];
      final Function function = (Function) ObjectUtilities.loadAndInstantiate
          (aClass, DefaultFunctionRegistry.class, Function.class);
      if (function == null)
      {
        Log.debug ("There is no such function: " + aClass);
      }
      else
      {
        functions.add(function);
      }
    }
    return (Function[]) functions.toArray(new Function[functions.size()]);
  }

  public String[] getFunctionNames()
  {
    return (String[]) functions.keySet().toArray(new String[functions.size()]);
  }

  public String[] getFunctionNamesByCategory(FunctionCategory category)
  {
    return (String[]) categoryFunctions.toArray(category, new String[0]);
  }

  public Function[] getFunctionsByCategory(FunctionCategory category)
  {
    final String[] fnNames = (String[]) categoryFunctions.toArray
        (category, new String[0]);
    final ArrayList functions = new ArrayList(fnNames.length);
    for (int i = 0; i < fnNames.length; i++)
    {
      final String aName = fnNames[i];
      final Function function = createFunction(aName);
      if (function != null)
      {
        functions.add(function);
      }
    }
    return (Function[]) functions.toArray(new Function[functions.size()]);
  }

  public Function createFunction(String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    final String functionClass = (String) functions.get(name.toUpperCase());
    final Function function = (Function) ObjectUtilities.loadAndInstantiate
        (functionClass, DefaultFunctionRegistry.class, Function.class);
    if (function == null)
    {
      Log.debug ("There is no such function: " + name);
    }
    return function;
  }

  public FunctionDescription getMetaData(String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    return (FunctionDescription) functionMetaData.get(name.toUpperCase());
  }

  public void initialize(final Configuration configuration)
  {
    final Iterator functionKeys =
        configuration.findPropertyKeys(FUNCTIONS_PREFIX);
    final HashSet categories = new HashSet();


    while (functionKeys.hasNext())
    {
      final String classKey = (String) functionKeys.next();
      if (classKey.endsWith(".class") == false)
      {
        continue;
      }

      final String className = configuration.getConfigProperty(classKey);
      if (className.length() == 0)
      {
        continue;
      }
      final Object fn = ObjectUtilities.loadAndInstantiate
          (className, DefaultFunctionRegistry.class, Function.class);
      if (fn instanceof Function == false)
      {
        continue;
      }

      final Function function = (Function) fn;

      final int endIndex = classKey.length() - ".class".length();
      final String descrKey = classKey.substring(0, endIndex) + ".description";
      final Object descr = ObjectUtilities.loadAndInstantiate
          (descrKey, DefaultFunctionRegistry.class, FunctionDescription.class);

      final FunctionDescription description;
      if (descr instanceof FunctionDescription == false)
      {
        description = new DefaultFunctionDescription(function.getCanonicalName());
      }
      else
      {
        description = (FunctionDescription) descr;
      }

      final FunctionCategory cat = description.getCategory();
      categoryFunctions.add(cat, function.getCanonicalName());
      functionMetaData.put (function.getCanonicalName(), description);
      functions.put(function.getCanonicalName(), className);
      categories.add(cat);
    }

    this.categories = (FunctionCategory[]) categories.toArray
        (new FunctionCategory[categories.size()]);
  }

}
