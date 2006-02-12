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
 * DocumentStyleLoader.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DocumentStyleLoader.java,v 1.1 2006/02/12 21:49:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 08.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.layouting.input.StyleSheetLoader;
import org.jfree.layouting.input.style.CSSCounterRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.parser.CSSParserFactory;
import org.jfree.layouting.input.style.parser.StringInputSource;
import org.jfree.layouting.input.style.parser.StyleSheetHandler;
import org.jfree.layouting.input.style.parser.URLInputSource;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.DocumentMetaNode;
import org.jfree.util.Log;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

/**
 * Creation-Date: 08.12.2005, 21:33:59
 *
 * @author Thomas Morgner
 */
public class DocumentStyleLoader
{
  private ArrayList activeStyleRules;
  private URL baseUrl;
  private DocumentContext documentContext;
  private StyleSheet defaultStyleSheet;

  public DocumentStyleLoader(final DocumentContext context)
  {
    this.activeStyleRules = new ArrayList();
    this.documentContext = context;
    this.baseUrl = documentContext.getBaseURL();

    this.defaultStyleSheet = StyleSheetLoader.getInstance()
            .getDefaultStyleSheet();
    if (StyleSheetLoader.getInstance().isErrorDefaultStyleSheet())
    {
      Log.error(
              "There was an unexpected error loading the default stylesheet.");
    }

  }

  public CSSStyleRule[] parseDocument ()
  {
    if (defaultStyleSheet != null)
    {
      addStyleRules(defaultStyleSheet);
    }

    // ok, no known styles. We have to go the long way...
    int count = documentContext.getMetaNodeCount();
    for (int i = 0; i < count; i++)
    {
      DocumentMetaNode node = documentContext.getMetaNode(i);
      // check, whether this is a style node
      final Object typeAttr = node.getMetaAttribute("type");
      if ("style".equals(typeAttr))
      {
        handleStyleNode(node);
      }
      else if ("link".equals(typeAttr))
      {
        handleLinkNode(node);
      }
    }

    // todo: Perform a less destructive sorting.
    // We should preserve the order of the elements with the same weight.
    Collections.sort(activeStyleRules, new CSSStyleRuleComparator());
    return (CSSStyleRule[]) activeStyleRules.toArray
            (new CSSStyleRule[activeStyleRules.size()]);
  }

  private void handleStyleNode(final DocumentMetaNode node) {// do some inline parsing
    // (Same as the <style> element of HTML)
    final Object styleText = node.getMetaAttribute("#pcdata");
    if (styleText == null)
    {
      return;
    }
    StringInputSource inputSource = new StringInputSource
            (String.valueOf(styleText), baseUrl);
    StyleSheet styleSheet = parseStyleSheet(inputSource);
    if (styleSheet == null)
    {
      return;
    }
    addStyleRules(styleSheet);
  }

  private void handleLinkNode(final DocumentMetaNode node) {// do some external parsing
    // (Same as the <link> element of HTML)
    try
    {
      final Object href = node.getMetaAttribute("href");
      final URLInputSource inputSource;
      if (href instanceof URL)
      {
        inputSource = new URLInputSource((URL) href);
      }
      else if (href instanceof String)
      {
        URL url = new URL (baseUrl, (String) href);
        inputSource = new URLInputSource(url);
      }
      else
      {
        return;
      }
      StyleSheet styleSheet = parseStyleSheet(inputSource);
      inputSource.close();
      if (styleSheet == null)
      {
        return;
      }
      addStyleRules(styleSheet);
    }
    catch(IOException ioe)
    {
      // ignore everything
      Log.warn ("Failed to parse stylesheet.", ioe);
    }
  }


  private void addStyleRules (StyleSheet styleSheet)
  {
    int rc = styleSheet.getRuleCount();
    for (int i = 0; i < rc; i++)
    {
      final StyleRule rule = styleSheet.getRule(i);
      if (rule instanceof CSSCounterRule)
      {
        // todo: Add the counter definition to the document context
      }

      if (rule instanceof CSSStyleRule)
      {
        final CSSStyleRule drule = (CSSStyleRule) rule;
        activeStyleRules.add(drule);
      }
    }
  }

  private StyleSheet parseStyleSheet(InputSource inputSource)
  {
    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (StyleKeyRegistry.getRegistry(), null);
      parser.setDocumentHandler(handler);
      parser.parseStyleSheet(inputSource);
      return handler.getStyleSheet();
    }
    catch(Exception e)
    {
      // ignore everything
      Log.warn ("Failed to parse stylesheet.", e);
      return null;
    }
  }

  public ArrayList getActiveStyles()
  {
    return activeStyleRules;
  }
}
