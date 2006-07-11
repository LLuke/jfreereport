/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultDocumentContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultDocumentContext.java,v 1.3 2006/04/21 17:29:37 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.page.PagePolicy;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.jfree.layouting.layouter.i18n.DefaultLocalizationContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.namespace.DefaultNamespaceCollection;
import org.jfree.layouting.namespace.DefaultNamespaceDefinition;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.HashNMap;
import org.jfree.util.Log;

/**
 * Creation-Date: 08.12.2005, 20:17:07
 *
 * @author Thomas Morgner
 */
public class DefaultDocumentContext extends DefaultDocumentMetaNode
        implements DocumentContext
{

  private static final LayoutElement[] EMPTY_ELEMENT_ARRAY =
          new LayoutElement[0];

  private ArrayList metaNodes;
  private HashNMap pendingContent;

  private HashMap counterStyles;
  private HashMap counterPolicy;
  private HashMap stringPolicy;

  private DefaultNamespaceCollection namespaceCollection;
  private int quoteLevel;

  public DefaultDocumentContext()
  {
    metaNodes = new ArrayList();

    pendingContent = new HashNMap();
    counterStyles = new HashMap();
    counterPolicy = new HashMap();
    stringPolicy = new HashMap();

    setMetaAttribute(DATE_ATTR, new Date());
    setMetaAttribute(LOCALIZATION_ATTR, new DefaultLocalizationContext());
  }

  public PagePolicy getStringPolicy(String name)
  {
    return (PagePolicy) stringPolicy.get(name);
  }

  public void setStringPolicy(String name, PagePolicy policy)
  {
    stringPolicy.put(name, policy);
  }

  public PagePolicy getCounterPolicy(String name)
  {
    return (PagePolicy) counterPolicy.get(name);
  }

  public void setCounterPolicy(String name, PagePolicy policy)
  {
    counterPolicy.put(name, policy);
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

  public void addPendingContent(String name, LayoutElement element)
  {
    pendingContent.add(name, element);
  }

  public void clearPendingContent(String name)
  {
    pendingContent.removeAll(name);
  }

  public LayoutElement[] getPendingContent(String name)
  {
    return (LayoutElement[])
            pendingContent.toArray(name, EMPTY_ELEMENT_ARRAY);
  }

  public ResourceManager getResourceManager()
  {
    final Object o = getMetaAttribute(RESOURCE_MANAGER_ATTR);
    if (o instanceof ResourceManager == false)
    {
      ResourceManager value = new ResourceManager();
      value.registerDefaults();
      setMetaAttribute(RESOURCE_MANAGER_ATTR, value);
      return value;
    }
    return (ResourceManager) o;
  }

  public void setCounterStyle(String counterName, CounterStyle style)
  {
    counterStyles.put(counterName, style);
  }

  public CounterStyle getCounterStyle(String counterName)
  {
    CounterStyle style = (CounterStyle) counterStyles.get(counterName);
    if (style == null)
    {
      return new DecimalCounterStyle();
    }
    return style;
  }

  /**
   * This method is called once after the input-feed received all the document
   * meta-data.
   */
  public void initialize()
  {
    namespaceCollection = new DefaultNamespaceCollection();

    Log.debug ("Initializing document context");

    NamespaceDefinition[] defaults = Namespaces.createFromConfig
            (LibLayoutBoot.getInstance().getGlobalConfig(),
                    "org.jfree.layouting.namespaces.", getResourceManager());
    for (int i = 0; i < defaults.length; i++)
    {
      final NamespaceDefinition definition = defaults[i];
      namespaceCollection.addDefinition(definition);
    }

    for (int i = 0; i < metaNodes.size(); i++)
    {
      final DocumentMetaNode metaNode = (DocumentMetaNode) metaNodes.get(i);
      final Object nodeType = metaNode.getMetaAttribute("type");
      if ("namespace".equals(nodeType) == false)
      {
        continue;
      }
      final Object def = metaNode.getMetaAttribute("definition");
      if (def instanceof NamespaceDefinition)
      {
        namespaceCollection.addDefinition((NamespaceDefinition) def);
        continue;
      }

      final String uri = (String) metaNode.getMetaAttribute("uri");
      final String prefix = (String) metaNode.getMetaAttribute("prefix");
      final String classAttr = (String) metaNode.getMetaAttribute(
              "class-attribute");
      final String styleAttr = (String) metaNode.getMetaAttribute(
              "style-attribute");
      final ResourceKey styleSheetLocation =
              (ResourceKey) metaNode.getMetaAttribute("default-stylesheet");
      namespaceCollection.addDefinition(new DefaultNamespaceDefinition
              (uri, styleSheetLocation, classAttr, styleAttr, prefix));
    }
  }

  public NamespaceCollection getNamespaces()
  {
    return namespaceCollection;
  }

  public int getQuoteLevel()
  {
    return quoteLevel;
  }

  public void openQuote()
  {
    quoteLevel += 1;
  }

  public void closeQuote()
  {
    quoteLevel -= 1;
    if (quoteLevel < 0)
    {
      quoteLevel = 0;
    }
  }
}