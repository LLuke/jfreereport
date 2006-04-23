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
 * CSSParseTest.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: CSSParseTest.java,v 1.2 2006/04/17 20:51:20 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.tools;

import java.util.HashMap;
import java.io.StringReader;
import java.io.IOException;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.parser.StyleSheetParserUtil;
import org.w3c.flute.parser.Parser;
import org.w3c.css.sac.InputSource;

/**
 * Creation-Date: 23.11.2005, 13:00:17
 *
 * @author Thomas Morgner
 */
public class CSSParseTest
{
  public static void main(String[] args)
  {
    LibLayoutBoot.getInstance().start();
    final HashMap namespaces = new HashMap();
    namespaces.put("xml", "balh");

    Object value = StyleSheetParserUtil.parseStyleValue
            (namespaces, ContentStyleKeys.CONTENT, "attr(xml|Grump,url))", null, null);
    System.out.println ("Value: " + value);

    final InputSource source = new InputSource();
    source.setCharacterStream(new StringReader("A|A"));

    Parser p = new Parser();
    try
    {
      System.out.println (p.parseNamespaceToken(source));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    final String selector = "html|tag[Test|Attr]";
    source.setCharacterStream(new StringReader(selector));
    try
    {
      Object o = p.parseSelectors(source);
      System.out.println(o);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
//    Log.error ("Start..." + StyleKeyRegistry.getRegistry().getKeyCount());
//
//    StyleSheetLoader.getInstance().getInitialValuesSheet();
//    StyleSheetLoader.getInstance().getDefaultStyleSheet();
//
//    Log.debug ("---------------------------------------------------------");
//    final URL defaultStyleURL = ObjectUtilities.getResource
//            ("org/jfree/layouting/test.css", StyleSheetLoader.class);
//
//    try
//    {
//      final Parser parser = CSSParserFactory.getInstance().createCSSParser();
//      final StyleSheetHandler handler = new StyleSheetHandler(
//              StyleKeyRegistry.getRegistry(), null);
//      parser.setDocumentHandler(handler);
//      parser.parseStyleSheet(new URLInputSource(defaultStyleURL));
//      StyleSheet s  = handler.getStyleSheet();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//
//    CSSDeclarationRule r = StyleSheetLoader.getInstance().parseStyleText
//            ("color:#ffffff;", defaultStyleURL);
//    r.isReadOnly();
  }
}
