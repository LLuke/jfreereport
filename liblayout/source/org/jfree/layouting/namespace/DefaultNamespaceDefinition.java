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
 * $Id: DefaultNamespaceDefinition.java,v 1.1 2006/04/17 21:06:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.namespace;

import org.jfree.resourceloader.ResourceKey;

/**
 * A default implementation of the NamespaceDefinition interface. This
 * implementation assumes that all elements use the same style and class
 * attributes.
 *
 * @author Thomas Morgner
 */
public class DefaultNamespaceDefinition implements NamespaceDefinition
{
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
