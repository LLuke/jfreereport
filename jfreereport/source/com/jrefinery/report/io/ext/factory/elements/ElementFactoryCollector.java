/**
 * Date: Jan 12, 2003
 * Time: 5:53:06 PM
 *
 * $Id: ElementFactoryCollector.java,v 1.2 2003/01/13 19:00:49 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.Element;

import java.util.ArrayList;
import java.util.Iterator;

public class ElementFactoryCollector implements ElementFactory
{
  private ArrayList factories;

  public ElementFactoryCollector()
  {
    factories = new ArrayList();
  }

  public void addFactory (ElementFactory factory)
  {
    factories.add (factory);
  }

  public Iterator getFactories ()
  {
    return factories.iterator();
  }
  
  public Element getElementForType(String type)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      ElementFactory fact = (ElementFactory) factories.get(i);
      Element element = fact.getElementForType(type);
      if (element != null) return element;
    }
    return null;
  }
}
