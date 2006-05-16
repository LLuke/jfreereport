package org.jfree.resourceloader.factory.property;

import java.util.Properties;
import java.io.IOException;

import org.jfree.resourceloader.ResourceFactory;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.SimpleResource;

public class PropertiesResourceFactory
  implements ResourceFactory
{
  public PropertiesResourceFactory ()
  {
  }

  public Resource create (final ResourceManager manager,
                          final ResourceData data,
                          final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final Properties properties = new Properties();
      properties.load(data.getResourceAsStream(manager));
      return new SimpleResource (data.getKey(), properties, data.getVersion(manager));
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to load the properties file.",e);
    }
  }

  public Class getFactoryType ()
  {
    return Properties.class;
  }

  public void initializeDefaults ()
  {
    // none required ...
  }
}
