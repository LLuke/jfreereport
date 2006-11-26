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
 * CSSParserContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CSSParserContext.java,v 1.3 2006/07/11 13:29:47 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser;

import java.util.Map;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.resourceloader.ResourceKey;

/**
 * Creation-Date: 25.11.2005, 17:47:10
 *
 * @author Thomas Morgner
 */
public class CSSParserContext
{
  private static class ThreadContextVar extends ThreadLocal
  {
    public ThreadContextVar()
    {
    }

    public Object initialValue()
    {
      return new CSSParserContext();
    }
  }

  private static ThreadContextVar contextVar = new ThreadContextVar();

  private StyleKeyRegistry styleKeyRegistry;
  private CSSValueFactory valueFactory;
  private ResourceKey source;
  private Map namespaces;
  private String defaultNamespace;

  public static CSSParserContext getContext()
  {
    return (CSSParserContext) contextVar.get();
  }

  private CSSParserContext()
  {
  }

  public void setStyleKeyRegistry(final StyleKeyRegistry styleKeyRegistry)
  {
    if (styleKeyRegistry == null)
    {
      this.styleKeyRegistry = null;
      this.valueFactory = null;
    }
    else
    {
      this.styleKeyRegistry = styleKeyRegistry;
      this.valueFactory = new CSSValueFactory(styleKeyRegistry);
    }
  }

  public StyleKeyRegistry getStyleKeyRegistry()
  {
    return styleKeyRegistry;
  }

  public CSSValueFactory getValueFactory()
  {
    return valueFactory;
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public void setSource(final ResourceKey source)
  {
    this.source = source;
  }

  public Map getNamespaces()
  {
    return namespaces;
  }

  public void setNamespaces(final Map namespaces)
  {
    this.namespaces = namespaces;
  }

  public String getDefaultNamespace()
  {
    return defaultNamespace;
  }

  public void setDefaultNamespace(final String defaultNamespace)
  {
    this.defaultNamespace = defaultNamespace;
  }

  public void destroy()
  {
    this.defaultNamespace = null;
    this.namespaces = null;
    this.source = null;
    this.styleKeyRegistry = null;
    this.valueFactory = null;
  }
}
