package org.jfree.report.util.beans;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.report.function.TextFormatExpression;

public final class BeanUtility
{
  private BeanInfo beanInfo;
  private Object bean;
  private HashMap properties;

  public BeanUtility (final Object o)
      throws IntrospectionException
  {
    beanInfo = Introspector.getBeanInfo(o.getClass());
    bean = o;
    properties = new HashMap();

    final PropertyDescriptor[] propertyDescriptors =
        beanInfo.getPropertyDescriptors();
    for (int i = 0; i < propertyDescriptors.length; i++)
    {
      properties.put(propertyDescriptors[i].getName(), propertyDescriptors[i]);
    }
  }

  public PropertyDescriptor[] getPropertyInfos()
  {
    return beanInfo.getPropertyDescriptors();
  }

  public Object getProperty (final String name) throws BeanException
  {
    return getProperty(new PropertySpecification(name));
  }
  
  private Object getProperty (final PropertySpecification name) throws BeanException
  {
    final PropertyDescriptor pd = (PropertyDescriptor) properties.get(name.getName());
    if (pd == null)
    {
      throw new BeanException("No such property:" + name);
    }

    if (pd instanceof IndexedPropertyDescriptor && name.getIndex() != null)
    {
      final IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
      final Method readMethod = ipd.getIndexedReadMethod();
      if (readMethod == null)
      {
        throw new BeanException("Property is not readable: " + name);
      }
      try
      {
        return readMethod.invoke(bean, new Object[]{ new Integer(name.getIndex())});
      }
      catch (Exception e)
      {
        throw new BeanException("InvokationError", e);
      }
    }
    else
    {
      final Method readMethod = pd.getReadMethod();
      if (readMethod == null)
      {
        throw new BeanException("Property is not readable: " + name);
      }
      try
      {
        return readMethod.invoke(bean, null);
      }
      catch (Exception e)
      {
        throw new BeanException("InvokationError", e);
      }
    }
  }

  public String getPropertyAsString (final String name) throws BeanException
  {
    final PropertySpecification ps = new PropertySpecification(name);
    final PropertyDescriptor pd = (PropertyDescriptor) properties.get(ps.getName());
    if (pd == null)
    {
      throw new BeanException("No such property:" + name);
    }
    final Object o = getProperty(ps);
    if (o == null)
    {
      return null;
    }

    final ValueConverter vc =
        ConverterRegistry.getInstance().getValueConverter(o.getClass());
    if (vc == null)
    {
      throw new BeanException("Unable to handle property of type " + o.getClass().getName());
    }
    return vc.toAttributeValue(o);
  }

  public void setProperty (final String name, final Object o)
      throws BeanException
  {
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }
    setProperty(new PropertySpecification(name), o);
  }

  private void setProperty (final PropertySpecification name, final Object o)
      throws BeanException
  {
    final PropertyDescriptor pd = (PropertyDescriptor) properties.get(name.getName());
    if (pd == null)
    {
      throw new BeanException("No such property:" + name);
    }
    if (pd instanceof IndexedPropertyDescriptor && name.getIndex() != null)
    {
      final IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
      final Method writeMethod = ipd.getIndexedWriteMethod();
      if (writeMethod == null)
      {
        throw new BeanException("Property is not writeable: " + name);
      }
      try
      {
        writeMethod.invoke(bean, new Object[]{ new Integer(name.getIndex()), o });
      }
      catch (Exception e)
      {
        throw new BeanException("InvokationError", e);
      }
    }
    else
    {
      final Method writeMethod = pd.getWriteMethod();
      if (writeMethod == null)
      {
        throw new BeanException("Property is not writeable: " + name);
      }
      try
      {
        writeMethod.invoke(bean, new Object[]{ o });
      }
      catch (Exception e)
      {
        throw new BeanException("InvokationError", e);
      }
    }
  }

  public void setPropertyAsString (final String name, final String txt) throws BeanException
  {
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }
    if (txt == null)
    {
      throw new NullPointerException("Text must not be null");
    }
    final PropertySpecification ps = new PropertySpecification(name);
    final PropertyDescriptor pd = (PropertyDescriptor) properties.get(ps.getName());
    if (pd == null)
    {
      throw new BeanException("No such property:" + name);
    }

    setPropertyAsString(name, pd.getPropertyType(), txt);
  }


  public void setPropertyAsString (final String name, final Class type, final String txt) throws BeanException
  {
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }
    if (type == null)
    {
      throw new NullPointerException("Type must not be null");
    }
    if (txt == null)
    {
      throw new NullPointerException("Text must not be null");
    }
    final PropertySpecification ps = new PropertySpecification(name);
    final ValueConverter vc =
        ConverterRegistry.getInstance().getValueConverter(type);
    if (vc == null)
    {
      throw new BeanException
              ("Unable to handle '" + type + "' for property '" + name + "'");
    }
    final Object o = vc.toPropertyValue(txt);
    setProperty(ps, o);
  }

  private class PropertySpecification
  {
    private String raw;
    private String name;
    private String index;

    public PropertySpecification(final String raw)
    {
      this.raw = raw;
      this.name = getNormalizedName(raw);
      this.index = getIndex(raw);
    }

    private String getNormalizedName (final String property)
    {
      final int idx = property.indexOf('[');
      if (idx < 0)
      {
        return property;
      }
      return property.substring(0, idx);
    }

    private String getIndex (final String property)
    {
      final int idx = property.indexOf('[');
      if (idx < 0)
      {
        return null;
      }
      final int end = property.indexOf(']', idx + 1);
      if (end < 0)
      {
        return null;
      }
      return property.substring(idx + 1, end);
    }

    public String getRaw()
    {
      return raw;
    }

    public String getName()
    {
      return name;
    }

    public String getIndex()
    {
      return index;
    }

    public String toString()
    {
      final StringBuffer b = new StringBuffer("PropertySpecification={");
      b.append("raw=");
      b.append(raw);
      b.append("}");
      return b.toString();
    }
  }


  public String[] getProperties ()
  {
    final ArrayList propertyNames = new ArrayList();
    final PropertyDescriptor[] pd = getPropertyInfos();
    for (int i = 0; i < pd.length; i++)
    {
      final PropertyDescriptor property = pd[i];
      if (property.isHidden())
      {
        continue;
      }
      if (property.getReadMethod() == null ||
          property.getWriteMethod() == null)
      {
        // it will make no sense to write a property now, that
        // we can't read in later...
        continue;
      }
      if (property.getPropertyType().isArray())
      {
        final int max = findMaximumIndex(property);
        for (int idx = 0; idx < max; idx++)
        {
          propertyNames.add (property.getName() + "[" + idx + "]");
        }
      }
      else
      {
        propertyNames.add (property.getName());
      }
    }
    return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
  }

  private int findMaximumIndex(final PropertyDescriptor id)
  {
    final int retval = 0;
    try
    {
      final Object o = getProperty
          (new PropertySpecification(id.getName()));
      return Array.getLength(o);
    }
    catch(Exception e)
    {
      // ignore, we run 'til we encounter an index out of bounds Ex.
    }
    return retval;
  }

  public static void main (String[] args)
          throws IntrospectionException, BeanException
  {
    TextFormatExpression te = new TextFormatExpression();
    BeanUtility bu = new BeanUtility(te);
    bu.setPropertyAsString("field[0]", "Hello");
  }
}
