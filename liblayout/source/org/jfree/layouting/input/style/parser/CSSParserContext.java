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
 * CSSParserContext.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSParserContext.java,v 1.1 2006/02/12 21:57:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 25.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.w3c.css.sac.InputSource;

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
  private InputSource inputSource;
  private URL url;

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

  public InputSource getInputSource()
  {
    return inputSource;
  }

  public void setInputSource(final InputSource inputSource)
  {
    this.inputSource = inputSource;
  }

  public URL getURL()
  {
    if (inputSource == null)
    {
      return null;
    }

    final String uri = inputSource.getURI();
    if (uri == null)
    {
      return null;
    }

    try
    {
      return new URL(uri);
    }
    catch (MalformedURLException e)
    {
      return null;
    }
  }
}
