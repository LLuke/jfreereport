/**
 * Date: Jan 12, 2003
 * Time: 5:55:41 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.elements;

import com.jrefinery.report.Element;

import java.util.Hashtable;

public class AbstractElementFactory implements ElementFactory
{
  private Hashtable elements;

  public AbstractElementFactory()
  {
    elements = new Hashtable();
  }

  public void registerElement (Element e)
  {
    registerElement(e.getContentType(), e);
  }

  public void registerElement (String type, Element e)
  {
    elements.put(type, e);
  }

  public Element getElementForType(String type)
  {
    Element e = (Element) elements.get(type);
    if (e == null) return null;
    try
    {
      return (Element) e.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }
}
