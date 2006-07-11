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
 * $Id: FunctionFactory.java,v 1.1 2006/04/17 21:01:50 taqua Exp $
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
import org.jfree.layouting.layouter.style.functions.values.StyleValueFunction;
import org.jfree.layouting.layouter.style.functions.content.ContentFunction;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;

/**
 * Creation-Date: 16.04.2006, 14:15:37
 *
 * @author Thomas Morgner
 */
public class FunctionFactory
{
  public static final String VALUE_FUNCTIONS_KEY_RANGE =
          "org.jfree.layouting.functions.values.";
  public static final String CONTENT_FUNCTIONS_KEY_RANGE =
          "org.jfree.layouting.functions.content.";

  private HashMap styleFunctions;
  private HashMap contentFunctions;
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
    styleFunctions = new HashMap();
    contentFunctions = new HashMap();
  }

  public void registerDefault ()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final Iterator valueKeys = config.findPropertyKeys(VALUE_FUNCTIONS_KEY_RANGE);
    while (valueKeys.hasNext())
    {
      final String key = (String) valueKeys.next();
      final String value = config.getConfigProperty(key);
      final String name = key.substring(VALUE_FUNCTIONS_KEY_RANGE.length());
      final Object maybeFunction =
              ObjectUtilities.loadAndInstantiate(value, FunctionFactory.class);
      if (maybeFunction instanceof StyleValueFunction)
      {
        styleFunctions.put (name.toLowerCase(), maybeFunction);
      }
    }

    final Iterator contentKeys = config.findPropertyKeys(CONTENT_FUNCTIONS_KEY_RANGE);
    while (contentKeys.hasNext())
    {
      final String key = (String) contentKeys.next();
      final String value = config.getConfigProperty(key);
      final String name = key.substring(CONTENT_FUNCTIONS_KEY_RANGE.length());
      final Object maybeFunction =
              ObjectUtilities.loadAndInstantiate(value, FunctionFactory.class);
      if (maybeFunction instanceof ContentFunction)
      {
        contentFunctions.put (name.toLowerCase(), maybeFunction);
      }
    }

  }

  public StyleValueFunction getStyleFunction (String name)
  {
    final StyleValueFunction function = (StyleValueFunction) styleFunctions.get(name.toLowerCase());
    if (function == null)
    {
      Log.warn ("Unrecognized style function encountered: " + name);
    }
    // todo: Check for null values in all callers ..
    return function;
  }

  public ContentFunction getContentFunction (String name)
  {
    final ContentFunction function = (ContentFunction) contentFunctions.get(name.toLowerCase());
    if (function == null)
    {
      Log.warn ("Unrecognized content function encountered: " + name);
    }
    // todo: Check for null values in all callers ..
    return function;
  }
}
