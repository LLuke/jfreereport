/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * AbstractResolverFactory.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ResolverFactory.java,v 1.1 2006/02/12 21:49:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.12.2005, 14:40:17
 *
 * @author Thomas Morgner
 */
public class ResolverFactory
{
  private static final String AUTO_PREFIX = "org.jfree.layouting.resolver.auto.";
  private static final String COMPUTED_PREFIX = "org.jfree.layouting.resolver.computed.";
  private static final String PERCENTAGE_PREFIX = "org.jfree.layouting.resolver.percentages.";

  private static ResolverFactory factory;

  public static synchronized ResolverFactory getInstance()
  {
    if (factory == null)
    {
      factory = new ResolverFactory();
      factory.registerDefaults();
    }
    return factory;
  }

  private ResolveHandlerModule[] handlers;
  private StyleKeyRegistry registry;

  private ResolverFactory()
  {
    handlers = null;
    registry = StyleKeyRegistry.getRegistry();
  }

  public void registerDefaults()
  {
    ArrayList handlerList = new ArrayList();

    final HashMap autoHandlers = loadModules(AUTO_PREFIX);
    final HashMap compHandlers = loadModules(COMPUTED_PREFIX);
    final HashMap percHandlers = loadModules(PERCENTAGE_PREFIX);
    final HashSet keys = new HashSet();
    keys.addAll(autoHandlers.keySet());
    keys.addAll(compHandlers.keySet());
    keys.addAll(percHandlers.keySet());

    for (Iterator iterator = keys.iterator(); iterator.hasNext();)
    {
      final StyleKey key = (StyleKey) iterator.next();
      final ResolveHandler autoHandler = (ResolveHandler) autoHandlers.get(key);
      final ResolveHandler compHandler = (ResolveHandler) compHandlers.get(key);
      final ResolveHandler percHandler = (ResolveHandler) percHandlers.get(key);
      handlerList.add(new ResolveHandlerModule(key, autoHandler, compHandler,
              percHandler));
    }

    handlers = (ResolveHandlerModule[]) handlerList.toArray
            (new ResolveHandlerModule[handlerList.size()]);
    ResolveHandlerSorter.sort(handlers);
    Log.debug ("Registered " + handlers.length + " modules.");
  }

  private HashMap loadModules(final String configPrefix)
  {
    final HashMap handlers = new HashMap();
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    Iterator sit = config.findPropertyKeys(configPrefix);
    int length = configPrefix.length();
    while (sit.hasNext())
    {
      final String configkey = (String) sit.next();
      final String name = configkey.substring(length).toLowerCase();
      final StyleKey key = registry.findKeyByName(name);
      if (key == null)
      {
        Log.warn("Invalid stylekey for resolver: " + name);
        continue;
      }

      final String c = config.getConfigProperty(configkey);
      ResolveHandler module = (ResolveHandler)
              ObjectUtilities.loadAndInstantiate(c, ResolverFactory.class);
      if (module != null)
      {
        handlers.put(key, module);
      }
      else
      {
        Log.warn("Invalid resolver implementation: " + c);
      }
    }
    return handlers;
  }

  public void performResolve(LayoutProcess process,
                             final LayoutElement node,
                             final LayoutStyle style)
  {
    for (int i = 0; i < handlers.length; i++)
    {
      final ResolveHandlerModule handler = handlers[i];

      final ResolveHandler autoValueHandler = handler.getAutoValueHandler();
      if (autoValueHandler != null)
      {
        final StyleKey key = handler.getKey();
        CSSValue value = style.getValue(key);
        if (value instanceof CSSAutoValue)
        {
          autoValueHandler.resolve(process, node, style, key);
        }
      }
      final ResolveHandler compValueHandler = handler.getComputedValueHandler();
      if (compValueHandler != null)
      {
        compValueHandler.resolve(process, node, style, handler.getKey());
      }
      final ResolveHandler percValueHandler = handler
              .getPercentagesValueHandler();
      if (percValueHandler != null)
      {
        percValueHandler.resolve(process, node, style, handler.getKey());
      }
    }
  }
}