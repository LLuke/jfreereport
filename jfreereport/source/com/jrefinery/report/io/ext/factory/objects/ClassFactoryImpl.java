/**
 * Date: Jan 10, 2003
 * Time: 8:04:30 PM
 *
 * $Id: ClassFactoryImpl.java,v 1.1 2003/01/22 19:45:28 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.util.Log;

import java.util.Hashtable;
import java.util.Enumeration;

public abstract class ClassFactoryImpl implements ClassFactory
{
  private Hashtable classes;

  public ClassFactoryImpl()
  {
    classes = new Hashtable();
  }

  public ObjectDescription getDescriptionForClass (Class c)
  {
    ObjectDescription od = (ObjectDescription) classes.get(c);
    if (od == null)
    {
      return null;
    }
    return od.getInstance();
  }

  public ObjectDescription getSuperClassObjectDescription (Class d)
  {
    Enumeration enum = classes.keys();
    while (enum.hasMoreElements())
    {
      Class keyClass = (Class) enum.nextElement();
      if (keyClass.isAssignableFrom(d))
      {
//        Log.debug (keyClass + " is assignable " + d);
        return (ObjectDescription) classes.get (keyClass);
      }
      else
      {
//        Log.debug (keyClass + " is not assignable " + d);
      }
    }
    return null;
  }

  protected void registerClass (Class key, ObjectDescription od)
  {
    classes.put(key, od);
  }
}
