/**
 * Date: Jan 10, 2003
 * Time: 8:04:30 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import java.util.Hashtable;

public abstract class ClassFactory
{
  private Hashtable classes;

  public ClassFactory()
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
