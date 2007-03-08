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
 * $Id: ResourceManager.java,v 1.14 2007/03/02 17:08:11 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.resourceloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
  private static final Set failedModules = new HashSet();
  private ArrayList resourceLoaders;
  private ArrayList resourceFactories;
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
    resourceLoaders = new ArrayList();
    resourceFactories = new ArrayList();
    dataCache = new NullResourceDataCache();
    factoryCache = new NullResourceFactoryCache();
  }

  public synchronized ResourceKey createKey(Object data)
      throws ResourceKeyCreationException
  {
    return createKey(data, null);
  }

  public synchronized ResourceKey createKey(Object data, Map parameters)
      throws ResourceKeyCreationException
  {
    if (data == null)
    {
      throw new NullPointerException("Key data must not be null.");
    }

    final Iterator values = resourceLoaders.iterator();
    while (values.hasNext())
    {
      final ResourceLoader loader = (ResourceLoader) values.next();
      try
      {
        final ResourceKey key = loader.createKey(data, parameters);
        if (key != null)
        {
          return key;
        }
      }
      catch(ResourceKeyCreationException rkce)
      {
        // ignore it.
      }
    }

    throw new ResourceKeyCreationException
        ("Unable to create key: No loader was able " +
            "to handle the given key data: " + data);
  }

  /**
   * Since LibLoader 0.3.0 only hierarchical keys can be derived. For that, the
   * deriving path must be given as String.
   *
   * @param parent
   * @param path
   * @return
   */
  public ResourceKey deriveKey(ResourceKey parent, String path)
      throws ResourceKeyCreationException
  {
    return deriveKey(parent, path, null);
  }

  public ResourceKey deriveKey(ResourceKey parent, String path, Map parameters)
      throws ResourceKeyCreationException
  {
    if (path == null)
    {
      throw new NullPointerException("Key data must not be null.");
    }
    if (parent == null)
    {
      return createKey(path, parameters);
    }

    // First, try to load the key as absolute value.
    // This assumes, that we have no catch-all implementation.
    for (int i = 0; i < resourceLoaders.size(); i++)
    {
      final ResourceLoader loader = (ResourceLoader) resourceLoaders.get(i);
      final ResourceKey key = loader.createKey(path, parameters);
      if (key != null)
      {
        return key;
      }
    }

    ResourceKeyCreationException rce = null;
    for (int i = 0; i < resourceLoaders.size(); i++)
    {
      final ResourceLoader loader = (ResourceLoader) resourceLoaders.get(i);
      if (loader.isSupportedKey(parent) == false)
      {
        continue;
      }
      try
      {
        final ResourceKey key = loader.deriveKey(parent, path, parameters);
        if (key != null)
        {
          return key;
        }
      }
      catch(ResourceKeyCreationException rcke)
      {
        rce = rcke;
      }
    }

    if (rce != null)
    {
      throw rce;
    }
    throw new ResourceKeyCreationException
        ("Unable to create key: No such schema or the key was not recognized.");
  }

  private ResourceLoader findBySchema(ResourceKey key)
  {
    for (int i = 0; i < resourceLoaders.size(); i++)
    {
      ResourceLoader loader = (ResourceLoader) resourceLoaders.get(i);
      if (loader.isSupportedKey(key))
      {
        return loader;
      }
    }
    return null;
  }


  public URL toURL(final ResourceKey key)
  {
    ResourceLoader loader = findBySchema(key);
    if (loader == null)
    {
      return null;
    }
    return loader.toURL(key);
  }

  public ResourceData load(ResourceKey key) throws ResourceLoadingException
  {
    final ResourceLoader loader = findBySchema(key);
    if (loader == null)
    {
      throw new ResourceLoadingException
          ("Invalid key: No resource-loader registered for schema: " + key.getSchema());
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
      return autoCreateResource(key, context);
    }

    ResourceCreationException exception = null;
    final ResourceData data = load(key);
    for (int i = 0; i < resourceFactories.size(); i++)
    {
      final ResourceFactory fact =
          (ResourceFactory) resourceFactories.get(i);
      if (isSupportedTarget(target, fact) == false)
      {
//        throw new ResourceCreationException
//            ("No factory known for the given target type: " + targetClass);
        // try the next factory instead ..
        continue;
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

  private boolean isSupportedTarget(Class[] target, ResourceFactory fact)
  {
    for (int j = 0; j < target.length; j++)
    {
      final Class aClass = target[j];
      if (fact.getFactoryType().isAssignableFrom(aClass))
      {
        return true;
      }
    }
    return false;
  }

  private Resource autoCreateResource(final ResourceKey key,
                                      final ResourceKey context)
      throws ResourceLoadingException, ResourceCreationException
  {
    final ResourceData data = load(key);

    final Iterator it = resourceFactories.iterator();
    while (it.hasNext())
    {
      final ResourceFactory fact = (ResourceFactory) it.next();
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

  private Resource performCreate(final ResourceData data,
                                 final ResourceFactory fact,
                                 final ResourceKey context)
      throws ResourceLoadingException, ResourceCreationException
  {
    final Resource created = fact.create(this, data, context);
    factoryCache.put(created);
    return created;
  }

  private boolean isResourceUnchanged(final Resource resource)
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
        synchronized (failedModules)
        {
          if (failedModules.contains(dataCacheProviderClass) == false)
          {
            Log.warn("Failed to create data cache: " + e.getLocalizedMessage());
            failedModules.add(dataCacheProviderClass);
          }
        }
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
        synchronized (failedModules)
        {
          if (failedModules.contains(cacheProviderClass) == false)
          {
            Log.warn("Failed to create factory cache: " + e.getLocalizedMessage());
            failedModules.add(cacheProviderClass);
          }
        }
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
    resourceLoaders.add(loader);
  }

  public void registerFactory(ResourceFactory factory)
  {
    if (factory == null)
    {
      throw new NullPointerException("ResourceFactory must not be null.");
    }
    resourceFactories.add(factory);
  }

}
