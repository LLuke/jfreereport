/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * AbstractObjectDescription.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.Log;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * An abstract base class for object descriptions.
 * 
 * @author Thomas Morgner.
 */
public abstract class AbstractObjectDescription implements ObjectDescription, Cloneable
{
  /** The class. */
  private Class className;
  
  /** Storage for parameters. */
  private Hashtable parameters;
  
  /** Storage for parameter definitions. */
  private Hashtable parameterDefs;

  /**
   * Creates a new object description.
   * 
   * @param className  the class.
   */
  public AbstractObjectDescription(Class className)
  {
    this.className = className;
    this.parameters = new Hashtable();
    this.parameterDefs = new Hashtable();
  }

  /**
   * Returns a parameter class.
   * 
   * @param name  the parameter definition.
   * 
   * @return The class.
   */
  public Class getParameterDefinition (String name)
  {
    return (Class) parameterDefs.get(name);
  }

  /**
   * Sets the class for a parameter.
   * 
   * @param name  the parameter name.
   * @param obj  the parameter class.
   */
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

  /**
   * Converts primitives to corresponding object class.
   * 
   * @param obj  the class.
   * 
   * @return The class.
   */
  private Class convertPrimitiveClass (Class obj)
  {
    if (obj.isPrimitive() == false) 
    {
      return obj;
    }
    if (obj == Boolean.TYPE)
    {
      return Boolean.class;
    }
    if (obj == Byte.TYPE)
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

  /**
   * Sets a parameter.
   * 
   * @param name  the name.
   * @param value  the value.
   */
  public void setParameter (String name, Object value)
  {
    if (getParameterDefinition(name) == null)
    {
      throw new IllegalArgumentException("No such Parameter defined: " + name 
                                         + " in class " + getObjectClass());
    }
    Class parameterClass = convertPrimitiveClass(getParameterDefinition(name));
    if (parameterClass.isAssignableFrom(value.getClass()) == false)
    {
      throw new ClassCastException ("In Object " + getObjectClass() 
                                    + ": Value is not assignable: " + value.getClass()
                                    + " is not assignable from " + parameterClass);
    }
    parameters.put (name, value);
  }

  /**
   * Returns an iterator for the parameter names.
   * 
   * @return The iterator.
   */
  public Iterator getParameterNames ()
  {
    return parameterDefs.keySet().iterator();
  }

  /**
   * Returns a parameter value.
   * 
   * @param name  the parameter name.
   * 
   * @return The parameter value.
   */
  public Object getParameter (String name)
  {
    return parameters.get(name);
  }

  /**
   * Returns the class for the object.
   * 
   * @return The class.
   */
  public Class getObjectClass()
  {
    return className;
  }

  /**
   * Returns a cloned instance of the object description.
   *
   * @return A cloned instance.
   */
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
