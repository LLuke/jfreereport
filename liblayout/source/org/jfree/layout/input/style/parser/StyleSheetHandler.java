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
 * StyleSheetHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Stack;

import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSFontFaceRule;
import org.jfree.layouting.input.style.CSSImportRule;
import org.jfree.layouting.input.style.CSSMediaRule;
import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.util.Log;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

/**
 * Creation-Date: 23.11.2005, 13:06:06
 *
 * @author Thomas Morgner
 */
public class StyleSheetHandler implements DocumentHandler
{
  private StyleKeyRegistry registry;
  private StyleSheet styleSheet;
  private Stack parentRules;
  private CSSDeclarationRule styleRule;

  public StyleSheetHandler(final StyleKeyRegistry registry,
                           final StyleRule parentRule)
  {
    if (registry == null)
    {
      throw new NullPointerException();
    }
    this.registry = registry;
    this.parentRules = new Stack();
    if (parentRule != null)
    {
      parentRules.push(parentRule);
    }
  }

  public CSSDeclarationRule getStyleRule()
  {
    return styleRule;
  }

  public void setStyleRule(final CSSDeclarationRule styleRule)
  {
    this.styleRule = styleRule;
  }

  public StyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public void setStyleSheet(final StyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }

  public void init (InputSource source)
  {
    CSSParserContext.getContext().setStyleKeyRegistry(registry);
    CSSParserContext.getContext().setInputSource(source);
  }

  /**
   * Receive notification of the beginning of a style sheet.
   * <p/>
   * The CSS parser will invoke this method only once, before any other methods
   * in this interface.
   *
   * @param source the input source
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void startDocument(InputSource source) throws CSSException
  {
    init(source);
    if (this.styleSheet == null)
    {
      this.styleSheet = new StyleSheet();
      if (source instanceof URLInputSource)
      {
        URLInputSource u = (URLInputSource) source;
        styleSheet.setHref(u.getUrl());
      }
      else if (source instanceof StringInputSource)
      {
        StringInputSource u = (StringInputSource) source;
        styleSheet.setHref(u.getBaseUrl());
      }
      else
      {
        try
        {
          this.styleSheet.setHref(new URL(source.getURI()));
        }
        catch (MalformedURLException e)
        {
          // ignore ..
        }
      }
    }
  }

  /**
   * Receive notification of the end of a document.
   * <p/>
   * The CSS parser will invoke this method only once, and it will be the last
   * method invoked during the parse. The parser shall not invoke this method
   * until it has either abandoned parsing (because of an unrecoverable error)
   * or reached the end of input.
   *
   * @param source the input source
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void endDocument(InputSource source) throws CSSException
  {
    CSSParserContext.getContext().setStyleKeyRegistry(null);
    CSSParserContext.getContext().setInputSource(null);
  }

  /**
   * Receive notification of a comment. If the comment appears in a declaration
   * (e.g. color: /* comment * / blue;), the parser notifies the comment before
   * the declaration.
   *
   * @param text The comment.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void comment(String text) throws CSSException
  {
    // comments are ignored ..
  }

  /**
   * Receive notification of an unknown rule t-rule not supported by this
   * parser.
   *
   * @param atRule The complete ignored at-rule.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void ignorableAtRule(String atRule) throws CSSException
  {
    Log.debug("Ignorable AT-Rule: " + atRule);
  }

  /**
   * Receive notification of an unknown rule t-rule not supported by this
   * parser.
   *
   * @param prefix <code>null</code> if this is the default namespace
   * @param uri    The URI for this namespace.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void namespaceDeclaration(String prefix, String uri)
          throws CSSException
  {
    Log.debug("Namespace Declaration: " + prefix + " " + uri);
  }

  /**
   * Receive notification of a import statement in the style sheet.
   *
   * @param uri                The URI of the imported style sheet.
   * @param media              The intended destination media for style
   *                           information.
   * @param defaultNamespaceURI The default namespace URI for the imported style
   *                           sheet.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void importStyle(String uri,
                          SACMediaList media,
                          String defaultNamespaceURI) throws CSSException
  {
    Log.debug("Import Style: " + uri + " (" + defaultNamespaceURI + ")");

    final InputSource source = CSSParserContext.getContext().getInputSource();
    // instantiate a new parser and parse the stylesheet.
    try
    {
      final InputSource inputSource = CSSParserContext.getContext().getInputSource();
      URL parentURL = null;
      if (inputSource instanceof URLInputSource)
      {
        URLInputSource urlInputSource = (URLInputSource) inputSource;
        parentURL = urlInputSource.getUrl();
      }
      else if (inputSource != null)
      {
        try
        {
          parentURL = new URL (inputSource.getURI());
        }
        catch(Exception e)
        {
          // ignore it ...
        }
      }
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final CSSImportRule importRule = new CSSImportRule(styleSheet, getParentRule());
      final URL url = new URL(parentURL, uri);
      final StyleSheetHandler handler = new StyleSheetHandler(registry, importRule);
      parser.setDocumentHandler(handler);
      importRule.setHref(url);
      importRule.setStyleSheet(handler.getStyleSheet());
      parser.parseStyleSheet(new URLInputSource(url));
    }
    catch (Exception e)
    {
      Log.warn("Unable to load sub-stylesheet " + uri);
    }

    CSSParserContext.getContext().setStyleKeyRegistry(registry);
    CSSParserContext.getContext().setInputSource(source);

  }

  /**
   * Receive notification of the beginning of a media statement.
   * <p/>
   * The Parser will invoke this method at the beginning of every media
   * statement in the style sheet. there will be a corresponding endMedia()
   * event for every startElement() event.
   *
   * @param media The intended destination media for style information.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void startMedia(SACMediaList media) throws CSSException
  {
    Log.debug("Start Media: " + media);
    // ignore for now ..
    CSSMediaRule rule = new CSSMediaRule(styleSheet, getParentRule());
    parentRules.push(rule);

  }

  /**
   * Receive notification of the end of a media statement.
   *
   * @param media The intended destination media for style information.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void endMedia(SACMediaList media) throws CSSException
  {
    Log.debug("End Media: " + media);
    parentRules.pop();
  }

  /**
   * Receive notification of the beginning of a page statement.
   * <p/>
   * The Parser will invoke this method at the beginning of every page statement
   * in the style sheet. there will be a corresponding endPage() event for every
   * startPage() event.
   *
   * @param name        the name of the page (if any, null otherwise)
   * @param pseudo_page the pseudo page (if any, null otherwise)
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void startPage(String name, String pseudo_page) throws CSSException
  {
    Log.debug("Start Page: " + name + " " + pseudo_page);
    // yes, we have to parse that.
    CSSPageRule rule = new CSSPageRule(styleSheet, getParentRule());
    parentRules.push(rule);
  }

  /**
   * Receive notification of the end of a media statement.
   *
   * @param name       The intended destination medium for style information.
   * @param pseudo_page the pseudo page (if any, null otherwise)
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void endPage(String name, String pseudo_page) throws CSSException
  {
    Log.debug("End Page: " + name + " " + pseudo_page);
    parentRules.pop();
  }

  /**
   * Receive notification of the beginning of a font face statement.
   * <p/>
   * The Parser will invoke this method at the beginning of every font face
   * statement in the style sheet. there will be a corresponding endFontFace()
   * event for every startFontFace() event.
   *
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void startFontFace() throws CSSException
  {
    // font-face events are ignored for now.
    CSSFontFaceRule rule = new CSSFontFaceRule(styleSheet, getParentRule());
    parentRules.push(rule);
  }

  protected StyleRule getParentRule()
  {
    if (parentRules.isEmpty() == false)
    {
      return (StyleRule) parentRules.peek();
    }
    return null;
  }

  /**
   * Receive notification of the end of a font face statement.
   *
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void endFontFace() throws CSSException
  {
    Log.debug("End FontFace");
    parentRules.pop();
  }

  /**
   * Receive notification of the beginning of a rule statement.
   *
   * @param selectors All intended selectors for all declarations.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void startSelector(SelectorList selectors) throws CSSException
  {
    styleRule = new CSSStyleRule(styleSheet, getParentRule());
    Log.debug ("Starting to parse new rule");

  }

  /**
   * Receive notification of the end of a rule statement.
   *
   * @param selectors All intended selectors for all declarations.
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void endSelector(SelectorList selectors) throws CSSException
  {
    if (styleRule.getSize() == 0)
    {
      Log.debug ("Skipping empty rule");
      return;
    }

    int length = selectors.getLength();
    for (int i = 0; i < length; i++)
    {
      final Selector selector = selectors.item(i);
      try
      {
        final CSSStyleRule rule = (CSSStyleRule) styleRule.clone();
        rule.setSelector((CSSSelector) selector);
        styleSheet.addRule(rule);
      }
      catch (CloneNotSupportedException e)
      {
        // should not happen
      }
    }
    Log.debug ("Adding new rule");
  }

  /**
   * Receive notification of a declaration.
   *
   * @param name      the name of the property.
   * @param value     the value of the property. All whitespace are stripped.
   * @param important is this property important ?
   * @throws CSSException Any CSS exception, possibly wrapping another
   *                      exception.
   */
  public void property(String name, LexicalUnit value, boolean important)
          throws CSSException
  {
    CSSValueFactory factory = CSSParserContext.getContext().getValueFactory();
    try
    {
      factory.parseValue(styleRule, name, value, important);
    }
    catch (Exception e)
    {
      // we catch everything.
      Log.warn("Error parsing " + name, e);
    }

  }
}
