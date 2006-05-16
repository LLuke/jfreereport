package org.jfree.resourceloader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

/**
 * Creation-Date: 16.05.2006, 15:26:32
 *
 * @author Thomas Morgner
 */
public abstract class AbstractResourceKey implements ResourceKey
{
  private Map parameters;
  private Integer hashCode;

  public AbstractResourceKey(final Map parameters)
  {
    this.parameters = new HashMap();
    final Iterator entries = parameters.entrySet().iterator();
    while (entries.hasNext())
    {
      Map.Entry entry = (Map.Entry) entries.next();
      final Object key = entry.getKey();
      final Object value = entry.getValue();
      if (value instanceof Serializable == false)
      {
        throw new IllegalArgumentException(
                "Value for key " + key + " is not serializable.");
      }
      if (key instanceof LoaderParameterKey)
      {
        this.parameters.put(key, value);
      }
      else if (key instanceof FactoryParameterKey)
      {
        this.parameters.put(key, value);
      }
    }
  }

  public Object getLoaderParameter(LoaderParameterKey key)
  {
    return parameters.get(key);
  }

  public Object getFactoryParameter(FactoryParameterKey key)
  {
    return parameters.get(key);
  }

  /**
   * Creates a unique identifier for this key.
   * <p/>
   * The following statement must be true for all external forms generated by
   * this method: (key1.equals(key2) == key1.toExternalForm().equals(key2.toExternalForm())
   *
   * @return
   */
  public String toExternalForm()
  {
    return null;
  }

  public Map getParameters ()
  {
    return Collections.unmodifiableMap(parameters);
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o instanceof AbstractResourceKey == false)
    {
      return false;
    }

    final AbstractResourceKey that = (AbstractResourceKey) o;

    if (!parameters.equals(that.parameters))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    if (hashCode == null)
    {
      hashCode = new Integer(parameters.hashCode ());
    }
    return hashCode.intValue();
  }
}