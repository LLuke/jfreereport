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
package org.jfree.layouting.input.style.parser;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;

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
   * @param selector
   * @param resourceManager
   * @param baseURL
   * @return the parsed selector or null
   */
  public static SelectorList parseSelector(final Map namespaces,
                                       final String selector,
                                       final ResourceManager resourceManager,
                                       final ResourceKey baseURL)
  {
    if (selector == null)
    {
      throw new NullPointerException();
    }

    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

      setupNamespaces(namespaces, handler);

      final InputSource source = new InputSource();
      source.setCharacterStream(new StringReader(selector));

      handler.init(source);
      handler.setStyleRule(new CSSStyleRule(null, null));
      parser.setDocumentHandler(handler);

      final SelectorList selectorList = parser.parseSelectors(source);
      CSSParserContext.getContext().destroy();
      return selectorList;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  private static void setupNamespaces (Map namespaces, StyleSheetHandler handler)
  {
    if (namespaces == null)
    {
      return;
    }

    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final String prefix = (String) entry.getKey();
      final String uri = (String) entry.getValue();
      handler.registerNamespace(prefix, uri);
    }
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

      setupNamespaces(namespaces, handler);
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
    return parseStyles(namespaces, key.getName(), value, resourceManager, baseURL);
  }

  public static CSSDeclarationRule parseStyleRule(final Map namespaces,
                                            final String styleText,
                                            final ResourceManager resourceManager,
                                            final ResourceKey baseURL,
                                            final CSSDeclarationRule baseRule)
  {
    if (styleText == null)
    {
      throw new NullPointerException("Name is null");
    }

    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

      setupNamespaces(namespaces, handler);
      final InputSource source = new InputSource();
      source.setCharacterStream(new StringReader(styleText));

      handler.init(source);
      handler.setStyleRule(baseRule);
      parser.setDocumentHandler(handler);
      parser.parseStyleDeclaration(source);
      CSSDeclarationRule rule = handler.getStyleRule();
      CSSParserContext.getContext().destroy();

      return rule;
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
   * @param name
   * @param value
   * @param resourceManager
   * @param baseURL
   * @return
   */
  public static CSSStyleRule parseStyles(final Map namespaces,
                                         final String name,
                                         final String value,
                                         final ResourceManager resourceManager,
                                         final ResourceKey baseURL)
  {
    return parseStyles(namespaces, name, value, resourceManager,
            baseURL, new CSSStyleRule(null, null));
  }


  public static CSSStyleRule parseStyles(final Map namespaces,
                                         final String name,
                                         final String value,
                                         final ResourceManager resourceManager,
                                         final ResourceKey baseURL,
                                         final CSSDeclarationRule baserule)
  {
    if (name == null)
    {
      throw new NullPointerException("Name is null");
    }
    if (value == null)
    {
      throw new NullPointerException("Value is null");
    }

    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

      setupNamespaces(namespaces, handler);
      final InputSource source = new InputSource();
      source.setCharacterStream(new StringReader(value));

      handler.init(source);
      handler.setStyleRule(baserule);
      parser.setDocumentHandler(handler);
      final LexicalUnit lu = parser.parsePropertyValue(source);
      handler.property(name, lu, false);
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

  public static String[] parseNamespaceIdent (String attrName)
  {
    final String name;
    final String namespace;
    final StringTokenizer strtok = new StringTokenizer(attrName, "|");
    final CSSParserContext context = CSSParserContext.getContext();
    // explicitly undefined is different from default namespace..
    // With that construct I definitly violate the standard, but
    // most stylesheets are not yet written with namespaces in mind
    // (and most tools dont support namespaces in CSS).
    //
    // by acknowledging the explicit rule but redefining the rule where
    // no namespace syntax is used at all, I create compatiblity. Still,
    // if the stylesheet does not carry a @namespace rule, this is the same
    // as if the namespace was omited.
    if (strtok.countTokens() == 2)
    {
      String tkNamespace = strtok.nextToken();
      if (tkNamespace.length() == 0)
      {
        namespace = null;
      }
      else if (tkNamespace.equals("*"))
      {
        namespace = "*";
      }
      else
      {
        namespace = (String)
                context.getNamespaces().get(tkNamespace);
      }
      name = strtok.nextToken();
    }
    else
    {
      name = strtok.nextToken();
      namespace = context.getDefaultNamespace();
    }
    return new String[]{namespace, name};
  }
}
