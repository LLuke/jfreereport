/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * A CSS stylesheet. Unlike the W3C stylesheet classes, this class is a minimal
 * set of attributes, designed with usablity and performance in mind.
 * <p/>
 * Stylesheets are resolved by looking at the elements. For the sake of
 * simplicity, stylesheet objects itself do not hold references to their parent
 * stylesheets.
 * <p/>
 * The W3C media list is omited - this library assumes the visual/print media.
 * The media would have been specified in the document anyway, so we do not
 * care.
 * <p/>
 * This class is a union of the W3C CSSStyleSheet and the CSSStyleRuleList. It
 * makes no sense to separate them in this context.
 *
 * @author Thomas Morgner
 */
public class StyleSheet
{
  private boolean readOnly;
  private ResourceKey source;
  private ResourceManager resourceManager;
  private ArrayList rules;
  private ArrayList styleSheets;
  private HashMap namespaces;
  private transient Map roNamespaces;

  public StyleSheet()
  {
    this.rules = new ArrayList();
    this.styleSheets = new ArrayList();
    this.namespaces = new HashMap();
  }

  public synchronized boolean isReadOnly()
  {
    return readOnly;
  }

  protected synchronized void setReadOnly(final boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public synchronized void setSource(final ResourceKey href)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    this.source = href;
  }

  public void setResourceManager(final ResourceManager resourceManager)
  {
    this.resourceManager = resourceManager;
  }

  public ResourceManager getResourceManager()
  {
    if (resourceManager == null)
    {
      resourceManager = new ResourceManager();
      resourceManager.registerDefaults();
    }
    return resourceManager;
  }

  public synchronized void addRule(final StyleRule rule)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.add(rule);
  }

  public synchronized void insertRule(final int index, final StyleRule rule)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.add(index, rule);
  }

  public synchronized void deleteRule(final int index)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.remove(index);
  }

  public synchronized int getRuleCount()
  {
    return rules.size();
  }

  public synchronized StyleRule getRule(int index)
  {
    return (StyleRule) rules.get(index);
  }

  public synchronized void addStyleSheet (StyleSheet styleSheet)
  {
    styleSheets.add(styleSheet);
  }

  public synchronized int getStyleSheetCount ()
  {
    return styleSheets.size();
  }

  public synchronized StyleSheet getStyleSheet (int index)
  {
    return (StyleSheet) styleSheets.get(index);
  }

  public synchronized void removeStyleSheet (StyleSheet styleSheet)
  {
    styleSheets.remove(styleSheet);
  }

  public synchronized void addNamespace (String prefix, String uri)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    if (prefix == null)
    {
      throw new NullPointerException();
    }
    if (uri == null)
    {
      throw new NullPointerException();
    }
    namespaces.put(prefix, uri);
    roNamespaces = null;
  }

  public synchronized String getNamespaceURI (String prefix)
  {
    return (String) namespaces.get(prefix);
  }

  public synchronized String[] getNamespacePrefixes()
  {
    return (String[]) namespaces.keySet().toArray
            (new String[namespaces.size()]);
  }

  public synchronized Map getNamespaces()
  {
    if (roNamespaces == null)
    {
      roNamespaces = Collections.unmodifiableMap(namespaces);
    }
    return roNamespaces;
  }
}
