/**
 * Date: Jan 10, 2003
 * Time: 7:58:07 PM
 *
 * $Id: AbstractObjectDescription.java,v 1.2 2003/01/22 19:38:26 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.Log;

import java.util.Hashtable;
import java.util.Iterator;

public abstract class AbstractObjectDescription implements ObjectDescription, Cloneable
{
  private Class className;
  private Hashtable parameters;
  private Hashtable parameterDefs;

  public AbstractObjectDescription(Class className)
  {
    this.className = className;
    this.parameters = new Hashtable();
    this.parameterDefs = new Hashtable();
  }

  public Class getParameterDefinition (String name)
  {
    return (Class) parameterDefs.get(name);
  }

  public void setParameterDefinition (String name, Class obj)
  {
    if (obj == null)
    {
      parameterDefs.remove(name); 
    }
    else
    {
      parameterDefs.put (name, obj);
    }
  }

  private Class convertPrimitiveClass (Class obj)
  {
    if (obj.isPrimitive() == false) return obj;
    if(obj == Boolean.TYPE)
    {
      return Boolean.class;
    }
    if(obj == Byte.TYPE)
    {
      return Byte.class;
    }
    if (obj == Character.TYPE)
    {
      return Character.class;
    }
    if (obj == Short.TYPE)
    {
      return Short.class;
    }
    if (obj == Integer.TYPE)
    {
      return Integer.class;
    }
    if (obj == Long.TYPE)
    {
      return Long.class;
    }
    if (obj == Float.TYPE)
    {
      return Float.class;
    }
    if (obj == Double.TYPE)
    {
      return Double.class;
    }
    throw new IllegalArgumentException("Class 'void' is not allowed here");
  }

  public void setParameter (String name, Object value)
  {
    if (getParameterDefinition(name) == null)
      throw new IllegalArgumentException("No such Parameter defined: " + name + " in class " + getObjectClass());

    Class parameterClass = convertPrimitiveClass(getParameterDefinition(name));
    if (parameterClass.isAssignableFrom(value.getClass()) == false)
    {
      throw new ClassCastException ("In Object " + getObjectClass() + ": Value is not assignable: " + value.getClass()
                                    + " is not assignable from " + parameterClass);
    }
    parameters.put (name, value);
  }

  public Iterator getParameterNames ()
  {
    return parameterDefs.keySet().iterator();
  }

  public Object getParameter (String name)
  {
    return parameters.get(name);
  }

  public Class getObjectClass()
  {
    return className;
  }

  public ObjectDescription getInstance ()
  {
    try
    {
      AbstractObjectDescription c = (AbstractObjectDescription) super.clone();
      c.parameters = (Hashtable) parameters.clone();
      return c;
    }
    catch (Exception e)
    {
      Log.error ("Should not happen: Clone Error: ", e);
      return null;
    }
  }
}
