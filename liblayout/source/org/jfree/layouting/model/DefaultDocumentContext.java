/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * DefaultDocumentContext.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DefaultDocumentContext.java,v 1.1 2006/02/12 21:43:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 08.12.2005 : Initial version
 */
package org.jfree.layouting.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import org.jfree.util.HashNMap;
import org.jfree.loader.ContentCache;
import org.jfree.layouting.layouter.loader.GenericResourceFactory;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.i18n.DefaultLocalizationContext;

/**
 * Creation-Date: 08.12.2005, 20:17:07
 *
 * @author Thomas Morgner
 */
public class DefaultDocumentContext extends DefaultDocumentMetaNode
        implements DocumentContext
{
  public static final LayoutElement[] EMPTY_ELEMENT_ARRAY = new LayoutElement[0];
  private static final String BASE_URL_ATTR = "base-url";
  private static final String DATE_ATTR = "date";
  private static final String LOCALIZATION_ATTR = "localization-context";

  private ArrayList metaNodes;
  private HashNMap pendingContent;
  private HashMap strings;
  private GenericResourceFactory resourceFactory;
  private HashMap counterStyles;

  public DefaultDocumentContext()
  {
    metaNodes = new ArrayList();
    strings = new HashMap();
    pendingContent = new HashNMap();
    resourceFactory = new GenericResourceFactory();
    counterStyles = new HashMap();
    setMetaAttribute(DATE_ATTR, new Date());
    setMetaAttribute(LOCALIZATION_ATTR, new DefaultLocalizationContext());
  }

  public void addMetaNode(DocumentMetaNode node)
  {
    if (node == null)
    {
      throw new NullPointerException();
    }
    if (node instanceof DocumentContext)
    {
      throw new IllegalArgumentException();
    }
    metaNodes.add(node);
  }

  public void removeMetaNode(DocumentMetaNode node)
  {
    metaNodes.remove(node);
  }

  public DocumentMetaNode getMetaNode(int index)
  {
    return (DocumentMetaNode) metaNodes.get(index);
  }

  public int getMetaNodeCount()
  {
    return metaNodes.size();
  }

  public Date getDate()
  {
    Object o = getMetaAttribute(DATE_ATTR);
    if (o instanceof Date == false)
    {
      return null;
    }
    return (Date) o;
  }

  public URL getBaseURL()
  {
    Object o = getMetaAttribute(BASE_URL_ATTR);
    if (o instanceof URL == false)
    {
      return null;
    }
    return (URL) o;
  }

  public LocalizationContext getLocalizationContext()
  {
    Object o = getMetaAttribute(LOCALIZATION_ATTR);
    if (o instanceof LocalizationContext == false)
    {
      DefaultLocalizationContext value = new DefaultLocalizationContext();
      setMetaAttribute(LOCALIZATION_ATTR, value);
      return value;
    }
    return (LocalizationContext) o;
  }

  public void addPendingContent (String name, LayoutElement element)
  {
    pendingContent.add(name, element);
  }

  public void clearPendingContent (String name)
  {
    pendingContent.removeAll(name);
  }

  public LayoutElement[] getPendingContent (String name)
  {
    return (LayoutElement[])
            pendingContent.toArray(name, EMPTY_ELEMENT_ARRAY);
  }

  public String getString (String name)
  {
    return (String) strings.get(name);
  }

  public void setString (String name, String value)
  {
    if (value == null)
    {
      strings.remove(name);
    }
    else
    {
      strings.put (name, value);
    }
  }

  public ContentCache getResourceLoader ()
  {
    return resourceFactory;
  }

  public void setCounterStyle (String counterName, CounterStyle style)
  {
    counterStyles.put (counterName, style);
  }

  public CounterStyle getCounterStyle (String counterName)
  {
    CounterStyle style = (CounterStyle) counterStyles.get(counterName);
    if (style == null)
    {
      return new DecimalCounterStyle();
    }
    return style;
  }
}
