/**
 * Date: Jan 12, 2003
 * Time: 5:53:06 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.Element;
import com.jrefinery.report.io.ext.factory.elements.ElementFactory;

import java.util.ArrayList;

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
