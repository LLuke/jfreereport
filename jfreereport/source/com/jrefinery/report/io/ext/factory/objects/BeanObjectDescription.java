/**
 * Date: Jan 10, 2003
 * Time: 8:17:01 PM
 *
 * $Id: BeanObjectDescription.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

public class BeanObjectDescription extends AbstractObjectDescription
{
  public BeanObjectDescription(Class className)
  {
    super(className);
    // now create some method descriptions ..

    Method[] methods = getObjectClass().getMethods();
    for (int i = 0; i < methods.length; i++)
    {
      Method m = methods[i];
      if (Modifier.isPublic(m.getModifiers()) == false)
      {
        //Log.debug ("Is not Public: " + m);
        continue;
      }
      if (Modifier.isStatic(m.getModifiers()) == true)
      {
        //Log.debug ("Is Static: " + m);
        continue;
      }
      if (m.getParameterTypes().length != 1)
      {
        //Log.debug ("Wrong Parameters: " + m);
        continue;
      }

      if (m.getName().startsWith("set"))
      {
        setParameterDefinition(getPropertyName(m.getName()),
                               m.getParameterTypes()[0]);
      }
      else
      {
        //Log.debug ("Wrong Name: " + m);
      }
    }
  }

  public Object createObject()
  {
    try
    {
      Object o = getObjectClass().newInstance();
      // now add the various parameters ...

      Iterator it = getParameterNames();
      while (it.hasNext())
      {
        String name = (String) it.next();
        Method method = findSetMethod(name);
        Object parameterValue = getParameter(name);
        if (parameterValue == null)
        {
          // Log.debug ("Parameter: " + name + " is null");
        }
        else
        {
          method.invoke(o, new Object[]{parameterValue});
        }
      }
      return o;
    }
    catch (Exception e)
    {
      Log.error("Unable to invoke bean method", e);
    }
    return null;
  }

  private Method findSetMethod(String parameterName)
      throws NoSuchMethodException
  {
    return getObjectClass().getMethod(getSetterName(parameterName),
                                      new Class[]{getParameterDefinition(parameterName)});
  }

  private Method findGetMethod(String parameterName)
      throws NoSuchMethodException
  {
    return getObjectClass().getMethod(getGetterName(parameterName),
                                      new Class[0]);
  }

  private String getSetterName(String parameterName)
  {
    if (parameterName.length() == 0)
      return "set";

    StringBuffer b = new StringBuffer();
    b.append("set");
    b.append(Character.toUpperCase(parameterName.charAt(0)));
    if (parameterName.length() > 1)
    {
      b.append(parameterName.substring(1));
    }
    return b.toString();
  }

  private String getGetterName(String parameterName)
  {
    if (parameterName.length() == 0)
      return "get";

    StringBuffer b = new StringBuffer();
    b.append("get");
    b.append(Character.toUpperCase(parameterName.charAt(0)));
    if (parameterName.length() > 1)
    {
      b.append(parameterName.substring(1));
    }
    return b.toString();
  }

  private String getPropertyName(String methodName)
  {
    if (methodName.length() < 3) throw new IllegalArgumentException();
    if (methodName.length() == 3)
      return "";

    StringBuffer b = new StringBuffer();
    b.append(Character.toLowerCase(methodName.charAt(3)));
    if (methodName.length() > 4)
    {
      b.append(methodName.substring(4));
    }
    return b.toString();
  }

  public static void main(String[] args)
  {
    BeanObjectDescription o = new BeanObjectDescription(FloatDimension.class);
    o.setParameter("height", new Float(100));
    o.setParameter("width", new Float(100));
    System.out.println(o.createObject());
  }

  public void setParameterFromObject(Object o)
      throws ObjectFactoryException
  {
    Class c = getObjectClass();
    if (c.isInstance(o) == false)
    {
      throw new ObjectFactoryException("Object is no instance of " + c);
    }

    Iterator it = getParameterNames();
    while (it.hasNext())
    {
      String propertyName = (String) it.next();

      try
      {
        Method method = findGetMethod(propertyName);
        Object retval = method.invoke(o, new Object[]{});
        if (retval != null)
        {
          setParameter(propertyName, retval);
        }
      }
      catch (Exception e)
      {
        Log.debug("Exception on method invokation.", e);
      }

    }
  }
}
