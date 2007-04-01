/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://reporting.pentaho.org/libloader/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader.factory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.jfree.resourceloader.LibLoaderBoot;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ContentNotRecognizedException;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;

/**
 * Creation-Date: 05.04.2006, 16:58:57
 *
 * @author Thomas Morgner
 */
public abstract class AbstractResourceFactory implements ResourceFactory
{

  private static class ResourceFactoryEntry implements Comparable
  {
    private FactoryModule module;
    private int weight;

    public ResourceFactoryEntry(final FactoryModule module, final int weight)
    {
      this.module = module;
      this.weight = weight;
    }

    public FactoryModule getModule()
    {
      return module;
    }

    public int getWeight()
    {
      return weight;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.<p>
     *
     * "Note: this class has a natural ordering that is inconsistent with
     * equals."
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it from
     *                            being compared to this Object.
     */
    public int compareTo(final Object o)
    {
      final ResourceFactoryEntry entry = (ResourceFactoryEntry) o;
      // the order is intentionally reveresed, so that a sorted set has the best
      // fit as first entry.
      return entry.weight - weight;
    }
  }

  /** The available factory methods. */
  private HashSet factoryModules;
  /** Which type of objects do we create here? */
  private Class factoryType;

  protected AbstractResourceFactory(final Class factoryType)
  {
    if (factoryType == null)
    {
      throw new NullPointerException();
    }
    this.factoryType = factoryType;
    this.factoryModules = new HashSet();
  }

  public Class getFactoryType()
  {
    return factoryType;
  }

  public void initializeDefaults ()
  {
    final String type = getFactoryType().getName();
    final String prefix = CONFIG_PREFIX + type;
    final Configuration config = LibLoaderBoot.getInstance().getGlobalConfig();
    final Iterator itType = config.findPropertyKeys(prefix);
    while (itType.hasNext())
    {
      final String key = (String) itType.next();
      final String modClass = config.getConfigProperty(key);
      final Object maybeFactory = ObjectUtilities.loadAndInstantiate
              (modClass, AbstractResourceFactory.class, FactoryModule.class);
      if (maybeFactory instanceof FactoryModule == false)
      {
        continue;
      }
      registerModule((FactoryModule) maybeFactory);
    }
  }

  public synchronized boolean registerModule(final String className)
  {
    try
    {
      final Class c = ObjectUtilities.getClassLoader
              (getClass()).loadClass(className);
      registerModule((FactoryModule) c.newInstance());
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

  public synchronized void registerModule(final FactoryModule module)
  {
    if (factoryModules.contains(module))
    {
      return;
    }
    factoryModules.add(module);
  }

  /**
   *
   * @param data
   * @param context
   * @return
   * @throws ResourceCreationException
   * @throws ResourceLoadingException
   */
  public synchronized Resource create(final ResourceManager manager,
                                      final ResourceData data,
                                      final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    final TreeSet sortedEntries = new TreeSet();
    final Iterator factoryModulesIt = factoryModules.iterator();
    while (factoryModulesIt.hasNext())
    {
      final FactoryModule mod = (FactoryModule) factoryModulesIt.next();
      int weight = mod.canHandleResource(manager, data);
      if (weight >= 0)
      {
        sortedEntries.add(new ResourceFactoryEntry(mod, weight));
      }
    }

    final Iterator it = sortedEntries.iterator();
    while (it.hasNext())
    {
      final ResourceFactoryEntry entry = (ResourceFactoryEntry) it.next();
      try
      {
        return entry.getModule().create(manager, data, context);
      }
      catch(Exception ex)
      {
        // ok, that one failed, try the next one ...
      }
    }
    throw new ContentNotRecognizedException
            ("No valid handler for the given content.");
  }
}
