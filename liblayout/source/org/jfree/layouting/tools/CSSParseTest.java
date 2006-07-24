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
 * $Id: CSSParseTest.java,v 1.6 2006/07/20 17:50:52 taqua Exp $
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
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
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
            (namespaces, LineStyleKeys.VERTICAL_ALIGN,
                    "baseline", null, null);
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
  }
}
