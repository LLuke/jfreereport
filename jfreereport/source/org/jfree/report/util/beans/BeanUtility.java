/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * BeanUtility.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BeanUtility.java,v 1.5 2005/04/17 21:09:01 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
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

public final class BeanUtility
{

  private static class PropertySpecification
  {
    private String raw;
    private String name;
    private String index;

    public PropertySpecification (final String raw)
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

    public String getRaw ()
    {
      return raw;
    }

    public String getName ()
    {
      return name;
    }

    public String getIndex ()
    {
      return index;
    }

    public String toString ()
    {
      final StringBuffer b = new StringBuffer("PropertySpecification={");
      b.append("raw=");
      b.append(raw);
      b.append("}");
      return b.toString();
    }
  }

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

  public PropertyDescriptor[] getPropertyInfos ()
  {
    return beanInfo.getPropertyDescriptors();
  }

  public Object getProperty (final String name)
          throws BeanException
  {
    return getProperty(new PropertySpecification(name));
  }

  private Object getProperty (final PropertySpecification name)
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
      final Method readMethod = ipd.getIndexedReadMethod();
      if (readMethod == null)
      {
        throw new BeanException("Property is not readable: " + name);
      }
      try
      {
        return readMethod.invoke(bean, new Object[]{new Integer(name.getIndex())});
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
      if (name.getIndex() != null)
      {
        // handle access to array-only properties ..
        try
        {
          //System.out.println(readMethod);
          final Object value = readMethod.invoke(bean, null);
          // we have (possibly) an array.
          if (value == null)
          {
            throw new IndexOutOfBoundsException("No such index, property is null");
          }
          if (value.getClass().isArray() == false)
          {
            throw new BeanException("The property contains no array.");
          }
          final int index = Integer.parseInt(name.getIndex());
          return Array.get(value, index);
        }
        catch(BeanException be)
        {
          throw be;
        }
        catch(IndexOutOfBoundsException iob)
        {
          throw iob;
        }
        catch(Exception e)
        {
          throw new BeanException("Failed to read indexed property.");
        }
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

  public String getPropertyAsString (final String name)
          throws BeanException
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
      throw new BeanException("Unable to handle property of type " + o.getClass()
              .getName());
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
      if (writeMethod != null)
      {
        try
        {
          writeMethod.invoke(bean, new Object[]{new Integer(name.getIndex()), o});
        }
        catch (Exception e)
        {
          throw new BeanException("InvokationError", e);
        }
        // we've done the job ...
        return;
      }
    }

    final Method writeMethod = pd.getWriteMethod();
    if (writeMethod == null)
    {
      throw new BeanException("Property is not writeable: " + name);
    }

    if (name.getIndex() != null)
    {
      // this is a indexed access, but no indexWrite method was found ...
      updateArrayProperty(pd, name, o);
    }
    else
    {
      try
      {
        writeMethod.invoke(bean, new Object[]{o});
      }
      catch (Exception e)
      {
        throw new BeanException("InvokationError", e);
      }
    }
  }

  private void updateArrayProperty (final PropertyDescriptor pd,
                                    final PropertySpecification name,
                                    final Object o)
          throws BeanException
  {
    final Method readMethod = pd.getReadMethod();
    if (readMethod == null)
    {
      throw new BeanException("Property is not readable, cannot perform array update: " + name);
    }
    try
    {
      //System.out.println(readMethod);
      final Object value = readMethod.invoke(bean, null);
      // we have (possibly) an array.
      final int index = Integer.parseInt(name.getIndex());
      final Object array = validateArray(getPropertyType(pd), value, index);
      Array.set(array, index, o);

      final Method writeMethod = pd.getWriteMethod();
      writeMethod.invoke(bean, new Object[]{array});
    }
    catch(BeanException e)
    {
      throw e;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new BeanException("Failed to read property, cannot perform array update: " + name);
    }
  }

  private Object validateArray (final Class propertyType,
                                final Object o, final int size)
          throws BeanException
  {

    if (propertyType.isArray() == false)
    {
      throw new BeanException("The property's value is no array.");
    }

    if (o == null)
    {
      return Array.newInstance(propertyType.getComponentType(), size + 1);
    }

    if (o.getClass().isArray() == false)
    {
      throw new BeanException("The property's value is no array.");
    }

    final int length = Array.getLength(o);
    if (length > size)
    {
      return o;
    }
    // we have to copy the array ..
    final Object retval = Array.newInstance(o.getClass().getComponentType(), size + 1);
    System.arraycopy(o, 0, retval, 0, length);
    return o;
  }

  public void setPropertyAsString (final String name, final String txt)
          throws BeanException
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

    setPropertyAsString(name, getPropertyType(pd), txt);
  }

  public static Class getPropertyType (final PropertyDescriptor pd)
          throws BeanException
  {
    final Class typeFromDescriptor = pd.getPropertyType();
    if (typeFromDescriptor != null)
    {
      return typeFromDescriptor;
    }
    if (pd instanceof IndexedPropertyDescriptor)
    {
      final IndexedPropertyDescriptor idx = (IndexedPropertyDescriptor) pd;
      return idx.getIndexedPropertyType();
    }
    throw new BeanException("Unable to determine the property type.");
  }

  public void setPropertyAsString (final String name, final Class type, final String txt)
          throws BeanException
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
    final ValueConverter vc;
    if (ps.getIndex() != null && type.isArray())
    {
      vc = ConverterRegistry.getInstance().getValueConverter(type.getComponentType());
    }
    else
    {
      vc = ConverterRegistry.getInstance().getValueConverter(type);
    }
    if (vc == null)
    {
      throw new BeanException
              ("Unable to handle '" + type + "' for property '" + name + "'");
    }
    final Object o = vc.toPropertyValue(txt);
    setProperty(ps, o);
  }

  public String[] getProperties ()
          throws BeanException
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
      if (getPropertyType(property).isArray())
      {
        final int max = findMaximumIndex(property);
        for (int idx = 0; idx < max; idx++)
        {
          propertyNames.add(property.getName() + "[" + idx + "]");
        }
      }
      else
      {
        propertyNames.add(property.getName());
      }
    }
    return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
  }

  private int findMaximumIndex (final PropertyDescriptor id)
  {
    final int retval = 0;
    try
    {
      final Object o = getProperty
              (new PropertySpecification(id.getName()));
      return Array.getLength(o);
    }
    catch (Exception e)
    {
      // ignore, we run 'til we encounter an index out of bounds Ex.
    }
    return retval;
  }
}
