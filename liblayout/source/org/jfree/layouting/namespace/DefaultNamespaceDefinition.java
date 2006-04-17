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
 * DefaultNamespaceDefinition.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.namespace;

import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.util.Log;

/**
 * A default implementation of the NamespaceDefinition interface. This
 * implementation assumes that all elements use the same style and class
 * attributes.
 *
 * @author Thomas Morgner
 */
public class DefaultNamespaceDefinition implements NamespaceDefinition
{
  public static final NamespaceDefinition HTML;
  public static final NamespaceDefinition LIBLAYOUT;
  public static final DefaultNamespaceDefinition XHTML;

  static
  {
    ResourceManager resourceManager = new ResourceManager();
    resourceManager.registerDefaults();
    ResourceKey htmlKey = null;
    try
    {
      htmlKey = resourceManager.createKey
              ("res://org/jfree/layouting/html.css");
    }
    catch (ResourceKeyCreationException e)
    {
      Log.warn ("Unable to create resource manager for HTML namespace");
    }
    HTML = new DefaultNamespaceDefinition
            (Namespaces.HTML_NAMESPACE, htmlKey, "class", "style", "html");
    XHTML = new DefaultNamespaceDefinition
            (Namespaces.XHTML_NAMESPACE, htmlKey, "class", "style", "html");

    ResourceKey libLayoutKey = null;
    try
    {
      libLayoutKey = resourceManager.createKey
              ("res://org/jfree/layouting/liblayout.css");
    }
    catch (ResourceKeyCreationException e)
    {
      Log.warn ("Unable to create resource manager for LibLayout namespace");
    }
    LIBLAYOUT = new DefaultNamespaceDefinition
            (Namespaces.LIBLAYOUT_NAMESPACE, libLayoutKey, "class", "style", "liblayout");
  }

  private String uri;
  private String classAttribute;
  private String styleAttribute;
  private ResourceKey defaultStyleSheet;
  private String preferredPrefix;

  public DefaultNamespaceDefinition(final String uri,
                                    final ResourceKey defaultStyleSheet,
                                    final String classAttribute,
                                    final String styleAttribute,
                                    final String preferredPrefix)
  {
    if (uri == null)
    {
      throw new NullPointerException();
    }
    this.uri = uri;
    this.defaultStyleSheet = defaultStyleSheet;
    this.classAttribute = classAttribute;
    this.styleAttribute = styleAttribute;
    this.preferredPrefix = preferredPrefix;
  }

  public String getURI()
  {
    return uri;
  }

  public String getClassAttribute(String element)
  {
    return classAttribute;
  }

  public String getStyleAttribute(String element)
  {
    return styleAttribute;
  }

  public ResourceKey getDefaultStyleSheetLocation()
  {
    return defaultStyleSheet;
  }

  public String getPreferredPrefix()
  {
    return preferredPrefix;
  }
}
