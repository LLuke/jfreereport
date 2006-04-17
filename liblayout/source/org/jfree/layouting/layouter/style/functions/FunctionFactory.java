/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * FunctionFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.functions;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 16.04.2006, 14:15:37
 *
 * @author Thomas Morgner
 */
public class FunctionFactory
{
  public static final String FUNCTIONS_KEY_RANGE = "org.jfree.layouting.functions.";

  private HashMap functions;
  private static FunctionFactory instance;

  public static FunctionFactory getInstance()
  {
    if (instance == null)
    {
      instance = new FunctionFactory();
      instance.registerDefault();
    }
    return instance;
  }

  private FunctionFactory()
  {
    functions = new HashMap();
  }

  public void registerDefault ()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final Iterator keys = config.findPropertyKeys(FUNCTIONS_KEY_RANGE);
    while (keys.hasNext())
    {
      final String key = (String) keys.next();
      final String value = config.getConfigProperty(key);
      final String name = key.substring(FUNCTIONS_KEY_RANGE.length());
      final Object maybeFunction =
              ObjectUtilities.loadAndInstantiate(value, FunctionFactory.class);
      if (maybeFunction instanceof StyleFunction)
      {
        functions.put (name.toLowerCase(), maybeFunction);
      }
    }
  }

  public StyleFunction getFunction (String name)
  {
    final StyleFunction function = (StyleFunction) functions.get(name.toLowerCase());
    if (function == null)
    {
      throw new NullPointerException(name);
    }
    return function;
  }
}
