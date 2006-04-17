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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.tools;

import java.util.HashMap;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.parser.StyleSheetParserUtil;

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
            (namespaces, ContentStyleKeys.CONTENT, "'Test' attr(Grump,url))", null, null);
    System.out.println ("Value: " + value);

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
