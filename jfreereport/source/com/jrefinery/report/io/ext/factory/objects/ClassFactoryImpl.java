/**
 * Date: Jan 10, 2003
 * Time: 8:04:30 PM
 *
 * $Id: ClassFactoryImpl.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.Hashtable;

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
    if (od == null) return null;
    return od.getInstance();
  }

  protected void registerClass (Class key, ObjectDescription od)
  {
    classes.put(key, od);
  }
}
