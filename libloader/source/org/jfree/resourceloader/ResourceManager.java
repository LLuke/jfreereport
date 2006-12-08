/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 * $Id: ResourceManager.java,v 1.10 2006/12/03 16:41:15 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.resourceloader.cache.NullResourceDataCache;
import org.jfree.resourceloader.cache.NullResourceFactoryCache;
import org.jfree.resourceloader.cache.ResourceDataCache;
import org.jfree.resourceloader.cache.ResourceDataCacheEntry;
import org.jfree.resourceloader.cache.ResourceDataCacheProvider;
import org.jfree.resourceloader.cache.ResourceFactoryCache;
import org.jfree.resourceloader.cache.ResourceFactoryCacheProvider;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The manager takes care about the loaded resources, performs any caching, if
 * needed and is the central instance when dealing with resources.
 *
 * @author Thomas Morgner
 */
public class ResourceManager
{
  private HashMap resourceLoaders;
  private HashMap resourceFactories;
  private ResourceDataCache dataCache;
  private ResourceFactoryCache factoryCache;

  private static final String LOADER_PREFIX =
      "org.jfree.resourceloader.loader.";
  private static final String FACTORY_TYPE_PREFIX =
      "org.jfree.resourceloader.factory.type.";
  public static final String DATA_CACHE_PROVIDER_KEY = "org.jfree.resourceloader.cache.DataCacheProvider";
  public static final String FACTORY_CACHE_PROVIDER_KEY = "org.jfree.resourceloader.cache.FactoryCacheProvider";

  public ResourceManager()
  {
    resourceLoaders = new HashMap();
    resourceFactories = new HashMap();
    dataCache = new NullResourceDataCache();
    factoryCache = new NullResourceFactoryCache();
  }

  public synchronized ResourceKey createKey(Object data)
      throws ResourceKeyCreationException
  {
    if (data instanceof Map)
    {
      return createKey((Map) data);
    }
    final HashMap map = new HashMap();
    map.put(AbstractResourceKey.CONTENT_KEY, data);
    return createKey(map);
  }

  public synchronized ResourceKey createKey(Map data)
      throws ResourceKeyCreationException
  {
    if (data == null)
    {
      throw new NullPointerException("Key data must not be null.");
    }

    final Iterator values = resourceLoaders.values().iterator();
    while (values.hasNext())
    {
      final ResourceLoader loader = (ResourceLoader) values.next();
      if (loader.isSupportedKeyValue(data) == false)
      {
        continue;
      }
      return loader.createKey(data);
    }
    throw new ResourceKeyCreationException
        ("Unable to create key: No loader was able " +
            "to handle the given key data: " + data);
  }

  public ResourceKey deriveKey(ResourceKey parent, Object data)
      throws ResourceKeyCreationException
  {
    if (data instanceof Map)
    {
      return deriveKey(parent, (Map) data);
    }
    final HashMap map = new HashMap();
    map.put(AbstractResourceKey.CONTENT_KEY, data);
    return deriveKey(parent, map);
  }

  public ResourceKey deriveKey(ResourceKey parent, Map data)
      throws ResourceKeyCreationException
  {
    if (data == null)
    {
      throw new NullPointerException("Key data must not be null.");
    }
    if (parent == null)
    {
      return createKey(data);
    }

    // First, try to load the key as absolute value.
    // This assumes, that we have no catch-all implementation.
    final Iterator values = resourceLoaders.values().iterator();
    while (values.hasNext())
    {
      final ResourceLoader loader = (ResourceLoader) values.next();
      if (loader.isSupportedKeyValue(data) == false)
      {
        continue;
      }
      try
      {
        return loader.createKey(data);
      }
      catch (Exception e)
      {
        // OK, did not work out ..
      }
    }

    ResourceLoader loader =
        (ResourceLoader) resourceLoaders.get(parent.getSchema());
    if (loader == null)
    {
      throw new ResourceKeyCreationException
          ("Unable to create key: No such schema.");
    }
    try
    {
      return loader.deriveKey(parent, data);
    }
    catch (ResourceKeyCreationException ex)
    {
      return createKey(data);
    }
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    final ResourceLoader loader = (ResourceLoader)
        resourceLoaders.get(key.getSchema());
    if (loader == null)
    {
      throw new ResourceLoadingException
          ("Unable to create key: No such schema: " + key.getSchema());
    }
    final ResourceDataCacheEntry cached = dataCache.get(key);
    if (cached != null)
    {
      final ResourceData data = cached.getData();
      // check, whether it is valid.
      if (cached.getStoredVersion() < 0)
      {
        // a non versioned entry is always valid. (Maybe this is from a Jar-URL?)
        return data;
      }
      final long version = data.getVersion(this);
      if (version < 0)
      {
        // the system is no longer able to retrieve the version information?
        // (but versioning information must have been available in the past)
        // oh, that's bad. Assume the worst and re-read the data.
        dataCache.remove(data);
      }
      else if (cached.getStoredVersion() == version)
      {
        return data;
      }
      else
      {
        dataCache.remove(data);
      }
    }
    final ResourceData data = loader.load(key);
    return dataCache.put(this, data);
  }

  public Resource createDirectly(Object keyValue, Class target)
      throws ResourceLoadingException,
      ResourceCreationException,
      ResourceKeyCreationException
  {
    final ResourceKey key = createKey(keyValue);
    return create(key, null, target);
  }

  public Resource create(ResourceKey key, ResourceKey context, Class target)
      throws ResourceLoadingException, ResourceCreationException
  {
    if (target == null)
    {
      throw new NullPointerException("Target must not be null");
    }
    if (key == null)
    {
      throw new NullPointerException("Key must not be null.");
    }
    return create(key, context, new Class[]{target});
  }

  public Resource create(ResourceKey key, ResourceKey context)
      throws ResourceLoadingException, ResourceCreationException
  {
    return create(key, context, (Class[]) null);
  }

  public Resource create(ResourceKey key,
                         ResourceKey context,
                         Class[] target)
      throws ResourceLoadingException, ResourceCreationException
  {
    if (key == null)
    {
      throw new NullPointerException("Key must not be null.");
    }
    // ok, we have a handle to the data, and the data is current.
    // Lets check whether we also have a cached result.
    final Resource resource = factoryCache.get(key);
    if (resource != null)
    {
      if (isResourceUnchanged(resource))
      {
        // mama, look i am a good cache manager ...
        return resource;
      }
      else
      {
        // someone evil changed one of the dependent resources ...
        factoryCache.remove(resource);
      }
    }

    // AutoMode ..
    if (target == null)
    {
      final ResourceData data = load(key);

      final Iterator it = resourceFactories.keySet().iterator();
      while (it.hasNext())
      {
        final Object factorykey = it.next();
        final ResourceFactory fact = (ResourceFactory)
            resourceFactories.get(factorykey);
        try
        {
          Resource res = performCreate(data, fact, context);
          if (res != null)
          {
            return res;
          }
        }
        catch (ResourceCreationException rex)
        {
          // ignore it, try the next factory ...
        }
      }
      throw new ResourceCreationException
          ("No known factory was able to handle the given data.");
    }
    else
    {
      ResourceCreationException exception = null;
      final ResourceData data = load(key);
      for (int i = 0; i < target.length; i++)
      {
        Class targetClass = target[i];
        ResourceFactory fact = (ResourceFactory) resourceFactories.get
            (targetClass.getName());
        if (fact == null)
        {
          throw new ResourceCreationException
              ("No factory known for the given target type: " + targetClass);
        }

        try
        {
          return performCreate(data, fact, context);
        }
        catch (ContentNotRecognizedException ce)
        {
          // Ignore it, unless it is the last one.
        }
        catch (ResourceCreationException rex)
        {
          // ignore it, try the next factory ...
          exception = rex;
          if (Log.isDebugEnabled())
          {
            Log.debug("Failed at " + fact.getClass() + ": ", rex);
          }
        }
      }

      if (exception != null)
      {
        throw exception;
      }
      throw new ContentNotRecognizedException
          ("None of the selected factories was able to handle the given data: " + key);
    }
  }

  private Resource performCreate(final ResourceData data,
                                 final ResourceFactory fact,
                                 final ResourceKey context)
      throws ResourceLoadingException, ResourceCreationException
  {
    final Resource created = fact.create(this, data, context);
    factoryCache.put(created);
    return created;
  }

  public boolean isResourceUnchanged(final Resource resource)
      throws ResourceLoadingException
  {
    final ResourceKey[] deps = resource.getDependencies();
    for (int i = 0; i < deps.length; i++)
    {
      final ResourceKey dep = deps[i];
      final long version = resource.getVersion(dep);
      if (version == -1)
      {
        // non-versioning key, ignore it.
        continue;
      }

      final ResourceData data = load(dep);
      if (data.getVersion(this) != version)
      {
        // oh, my bad, an outdated or changed entry.
        // We have to re-read the whole thing.
        return false;
      }
    }
    // all versions have been confirmed to be valid. Nice, we can use the
    // cached product.
    return true;
  }

  public ResourceDataCache getDataCache()
  {
    return dataCache;
  }

  public void setDataCache(final ResourceDataCache dataCache)
  {
    if (dataCache == null)
    {
      throw new NullPointerException();
    }
    this.dataCache = dataCache;
  }

  public ResourceFactoryCache getFactoryCache()
  {
    return factoryCache;
  }

  public void setFactoryCache(final ResourceFactoryCache factoryCache)
  {
    if (factoryCache == null)
    {
      throw new NullPointerException();
    }
    this.factoryCache = factoryCache;
  }

  public void registerDefaults()
  {
    // Create all known resource loaders ...
    registerDefaultLoaders();

    // Register all known factories ...
    registerDefaultFactories();

    // add the caches ..
    registerDataCache();
    registerFactoryCache();
  }

  public void registerDefaultFactories()
  {
    final Configuration config = LibLoaderBoot.getInstance().getGlobalConfig();
    final Iterator itType = config.findPropertyKeys(FACTORY_TYPE_PREFIX);
    while (itType.hasNext())
    {
      final String key = (String) itType.next();
      final String factoryClass = config.getConfigProperty(key);

      final Object maybeFactory = ObjectUtilities.loadAndInstantiate
          (factoryClass, ResourceManager.class, ResourceFactory.class);
      if (maybeFactory instanceof ResourceFactory == false)
      {
        continue;
      }

      final ResourceFactory factory = (ResourceFactory) maybeFactory;
      factory.initializeDefaults();
      registerFactory(factory);
    }
  }

  public void registerDataCache()
  {
    final Configuration config = LibLoaderBoot.getInstance().getGlobalConfig();
    final String dataCacheProviderClass =
        config.getConfigProperty(DATA_CACHE_PROVIDER_KEY);
    if (dataCacheProviderClass == null)
    {
      return;
    }
    final Object maybeDataCacheProvider =
        ObjectUtilities.loadAndInstantiate
            (dataCacheProviderClass, ResourceManager.class, ResourceDataCacheProvider.class);
    if (maybeDataCacheProvider instanceof ResourceDataCacheProvider)
    {
      ResourceDataCacheProvider provider = (ResourceDataCacheProvider) maybeDataCacheProvider;
      try
      {
        final ResourceDataCache cache = provider.createDataCache();
        if (cache != null)
        {
          setDataCache(cache);
        }
      }
      catch (Throwable e)
      {
        // ok, did not work ...
        Log.warn("Failed to set data cache: " + e.getLocalizedMessage());
      }
    }
  }

  public void registerFactoryCache()
  {
    final Configuration config = LibLoaderBoot.getInstance().getGlobalConfig();
    final String cacheProviderClass = config.getConfigProperty
        (FACTORY_CACHE_PROVIDER_KEY);
    if (cacheProviderClass == null)
    {
      return;
    }
    final Object maybeCacheProvider = ObjectUtilities.loadAndInstantiate
        (cacheProviderClass, ResourceManager.class, ResourceFactoryCacheProvider.class);

    if (maybeCacheProvider instanceof ResourceFactoryCacheProvider)
    {
      ResourceFactoryCacheProvider provider = (ResourceFactoryCacheProvider) maybeCacheProvider;
      try
      {
        final ResourceFactoryCache cache = provider.createFactoryCache();
        if (cache != null)
        {
          setFactoryCache(cache);
        }
      }
      catch (Throwable e)
      {
        Log.warn("Failed to set factory cache: " + e.getLocalizedMessage());
      }
    }
  }

  public void registerDefaultLoaders()
  {
    final Configuration config = LibLoaderBoot.getInstance().getGlobalConfig();
    final Iterator it = config.findPropertyKeys(LOADER_PREFIX);
    while (it.hasNext())
    {
      final String key = (String) it.next();
      final String value = config.getConfigProperty(key);
      final Object o =
          ObjectUtilities.loadAndInstantiate(value, ResourceManager.class, ResourceLoader.class);
      if (o instanceof ResourceLoader)
      {
        final ResourceLoader loader = (ResourceLoader) o;
        //Log.debug("Registering loader for " + loader.getSchema());
        registerLoader(loader);
      }
    }
  }

  public void registerLoader(ResourceLoader loader)
  {
    if (loader == null)
    {
      throw new NullPointerException("ResourceLoader must not be null.");
    }
    loader.setResourceManager(this);
    resourceLoaders.put(loader.getSchema(), loader);
  }

  public void registerFactory(ResourceFactory factory)
  {
    if (factory == null)
    {
      throw new NullPointerException("ResourceFactory must not be null.");
    }
    resourceFactories.put(factory.getFactoryType().getName(), factory);
  }
}
