/**
 * Date: Jan 10, 2003
 * Time: 4:55:41 PM
 *
 * $Id: AbstractStyleKeyFactory.java,v 1.2 2003/01/22 19:38:26 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.util.Log;

import java.util.Hashtable;

public abstract class AbstractStyleKeyFactory implements StyleKeyFactory
{
  public static final String OBJECT_FACTORY_TAG = "object-factory";

  private Hashtable knownKeys;
  private ClassFactory classFactory;

  public AbstractStyleKeyFactory()
  {
    knownKeys = new Hashtable();
  }

  public ClassFactory getClassFactory()
  {
    return classFactory;
  }

  public void setClassFactory(ClassFactory classFactory)
  {
    if (classFactory == null) throw new NullPointerException();
    this.classFactory = classFactory;
  }

  public void addKey (StyleKey key)
  {
    knownKeys.put (key.getName(), key);
  }

  public StyleKey getStyleKey (String name)
  {
    return (StyleKey) knownKeys.get (name);
  }

  public Object createBasicObject(StyleKey k, String value, Class c)
  {
    if (k == null)
    {
      // no such key registered ...
      return null;
    }

    if (c == null)
      throw new NullPointerException();

    if (classFactory == null)
      throw new NullPointerException("Class "+ getClass());

    ObjectDescription od = classFactory.getDescriptionForClass(c);
    if (od == null) return null;

    od.setParameter("value", value);
    return od.createObject();
  }

  public void init(Parser parser)
  {
    Log.debug ("Init for class : "+ getClass());
    ClassFactory f = (ClassFactory) parser.getConfigurationValue(OBJECT_FACTORY_TAG);
    if (f == null)
      throw new NullPointerException();

    setClassFactory(f);
  }
}
