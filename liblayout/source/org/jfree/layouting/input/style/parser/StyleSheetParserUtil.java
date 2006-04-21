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
 * StyleSheetLoader.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StyleSheetParserUtil.java,v 1.1 2006/04/17 20:54:49 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.parser;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;

/**
 * Creation-Date: 20.11.2005, 17:32:33
 *
 * @author Thomas Morgner
 */
public class StyleSheetParserUtil
{
  private StyleSheetParserUtil()
  {
  }

  /**
   * Parses a single style value for the given key. Returns <code>null</code>,
   * if the key denotes a compound definition, which has no internal
   * representation.
   *
   * @param namespaces
   * @param key
   * @param value
   * @param resourceManager
   * @param baseURL
   * @return
   */
  public static CSSValue parseStyleValue(final Map namespaces,
                                         final StyleKey key,
                                         final String value,
                                         final ResourceManager resourceManager,
                                         final ResourceKey baseURL)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      throw new NullPointerException();
    }

    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

      if (namespaces != null)
      {
        final Iterator entries = namespaces.entrySet().iterator();
        while (entries.hasNext())
        {
          final Map.Entry entry = (Map.Entry) entries.next();
          final String prefix = (String) entry.getKey();
          final String uri = (String) entry.getValue();
          handler.registerNamespace(prefix, uri);
        }
      }
      final InputSource source = new InputSource();
      source.setCharacterStream(new StringReader(value));

      handler.init(source);
      handler.setStyleRule(new CSSStyleRule(null, null));
      parser.setDocumentHandler(handler);
      final LexicalUnit lu = parser.parsePropertyValue(source);
      handler.property(key.getName(), lu, false);
      CSSStyleRule rule = (CSSStyleRule) handler.getStyleRule();

      CSSParserContext.getContext().destroy();

      return rule.getPropertyCSSValue(key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parses a style value. If the style value is a compound key, the corresonding
   * style entries will be added to the style rule.
   *
   * @param namespaces
   * @param key
   * @param value
   * @param resourceManager
   * @param baseURL
   * @return
   */
  public static CSSStyleRule parseStyles(final Map namespaces,
                                         final StyleKey key,
                                         final String value,
                                         final ResourceManager resourceManager,
                                         final ResourceKey baseURL)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      throw new NullPointerException();
    }

    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

      if (namespaces != null)
      {
        final Iterator entries = namespaces.entrySet().iterator();
        while (entries.hasNext())
        {
          final Map.Entry entry = (Map.Entry) entries.next();
          final String prefix = (String) entry.getKey();
          final String uri = (String) entry.getValue();
          handler.registerNamespace(prefix, uri);
        }
      }
      final InputSource source = new InputSource();
      source.setCharacterStream(new StringReader(value));

      handler.init(source);
      handler.setStyleRule(new CSSStyleRule(null, null));
      parser.setDocumentHandler(handler);
      final LexicalUnit lu = parser.parsePropertyValue(source);
      handler.property(key.getName(), lu, false);
      CSSStyleRule rule = (CSSStyleRule) handler.getStyleRule();

      CSSParserContext.getContext().destroy();

      return rule;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
