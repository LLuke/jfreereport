/**
 * Date: Jan 10, 2003
 * Time: 5:01:31 PM
 *
 * $Id: StyleKeyFactoryCollector.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.io.Parser;

import java.util.ArrayList;
import java.util.Iterator;

public class StyleKeyFactoryCollector implements StyleKeyFactory
{
  private ArrayList factories;

  public StyleKeyFactoryCollector()
  {
    factories = new ArrayList();
  }

  public void addFactory (StyleKeyFactory factory)
  {
    factories.add (factory);
  }

  public Iterator getFactories ()
  {
    return factories.iterator();
  }

  public StyleKey getStyleKey(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      StyleKey o = fact.getStyleKey(name);
      if (o != null)
        return o;
    }
    return null;
  }

  public Object createBasicObject(StyleKey k, String value)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      StyleKeyFactory fact = (StyleKeyFactory) factories.get(i);
      Object o = fact.createBasicObject(k, value);
      if (o != null)
        return o;
    }
    return null;
  }

  public void init(Parser parser)
  {
  }
}
