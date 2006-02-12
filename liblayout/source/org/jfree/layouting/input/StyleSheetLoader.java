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
 * StyleSheetLoader.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: StyleSheetLoader.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.11.2005 : Initial version
 */
package org.jfree.layouting.input;

import java.net.URL;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.parser.CSSParserFactory;
import org.jfree.layouting.input.style.parser.StyleSheetHandler;
import org.jfree.layouting.input.style.parser.URLInputSource;
import org.jfree.layouting.input.style.parser.StringInputSource;
import org.jfree.util.ObjectUtilities;
import org.w3c.css.sac.Parser;

/**
 * Creation-Date: 20.11.2005, 17:32:33
 *
 * @author Thomas Morgner
 */
public class StyleSheetLoader
{
  private static StyleSheetLoader instance;
  private StyleSheet initialValues;
  private StyleSheet defaultStyleSheet;
  private boolean errorDefaultStyleSheet;
  private boolean errorInitialValues;


  public static StyleSheetLoader getInstance()
  {
    if (instance == null)
    {
      instance = new StyleSheetLoader();
    }
    return instance;
  }

  private StyleSheetLoader()
  {
  }

  public synchronized StyleSheet getDefaultStyleSheet()
  {
    if (errorDefaultStyleSheet)
    {
      return null;
    }

    if (defaultStyleSheet == null)
    {
      final URL defaultStyleURL = ObjectUtilities.getResource
              ("org/jfree/layouting/html.css", StyleSheetLoader.class);
      defaultStyleSheet = parseStyleSheet(defaultStyleURL);
      errorDefaultStyleSheet = (defaultStyleSheet == null);
    }
    return defaultStyleSheet;
  }

  public StyleSheet parseStyleSheet (URL url)
  {
    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler(
              StyleKeyRegistry.getRegistry(), null);
      parser.setDocumentHandler(handler);
      parser.parseStyleSheet(new URLInputSource(url));
      return handler.getStyleSheet();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public CSSDeclarationRule parseStyleText(String text, URL url)
  {
    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
      final StyleSheetHandler handler = new StyleSheetHandler
              (StyleKeyRegistry.getRegistry(), null);
      final StringInputSource source = new StringInputSource(text, url);
      handler.init(source);
      handler.setStyleRule(new CSSStyleRule(null, null));
      parser.setDocumentHandler(handler);
      parser.parseStyleDeclaration(source);
      return handler.getStyleRule();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public boolean isErrorDefaultStyleSheet()
  {
    return errorDefaultStyleSheet;
  }

  public synchronized StyleSheet getInitialValuesSheet()
  {
    if (errorInitialValues)
    {
      return null;
    }

    if (initialValues == null)
    {
      final URL defaultStyleURL = ObjectUtilities.getResource
              ("org/jfree/layouting/initial.css", StyleSheetLoader.class);

      initialValues = parseStyleSheet(defaultStyleURL);
      errorInitialValues = (initialValues == null);
    }
    return initialValues;
  }

  public boolean isErrorInitialValues()
  {
    return errorInitialValues;
  }
}
