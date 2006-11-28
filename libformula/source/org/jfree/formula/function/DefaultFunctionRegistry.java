/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * ------------
 * DefaultFunctionRegistry.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultFunctionRegistry.java,v 1.3 2006/11/20 21:05:30 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jfree.formula.function.math.SumFunction;
import org.jfree.formula.function.math.SumFunctionDescription;
import org.jfree.formula.function.userdefined.DefaultFunctionDescription;
import org.jfree.util.Configuration;
import org.jfree.util.HashNMap;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;

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
    return (Function[]) functions.values().toArray
        (new Function[functions.size()]);
  }

  public Function[] getFunctionsByCategory(FunctionCategory category)
  {
    return (Function[]) categoryFunctions.toArray
        (category, new Function[0]);
  }

  public Function createFunction(String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    final String functionClass = (String) functions.get(name.toUpperCase());
    final Function function = (Function) ObjectUtilities.loadAndInstantiate
        (functionClass, DefaultFunctionRegistry.class);
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
          (className, DefaultFunctionRegistry.class);
      if (fn instanceof Function == false)
      {
        continue;
      }

      final Function function = (Function) fn;

      final int endIndex = classKey.length() - ".class".length();
      final String descrKey = classKey.substring(0, endIndex) + ".description";
      final Object descr = ObjectUtilities.loadAndInstantiate
          (descrKey, DefaultFunctionRegistry.class);

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
      categoryFunctions.add(cat, className);
      functionMetaData.put (function.getCanonicalName(), description);
      functions.put(function.getCanonicalName(), className);
      categories.add(cat);
    }

    this.categories = (FunctionCategory[]) categories.toArray
        (new FunctionCategory[categories.size()]);
  }

}
