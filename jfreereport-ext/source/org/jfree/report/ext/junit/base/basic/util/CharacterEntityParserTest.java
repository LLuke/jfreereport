/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * CharacterEntityParserTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CharacterEntityParserTest.java,v 1.4 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.util;

import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.ReportConfiguration;
import junit.framework.TestCase;

public class CharacterEntityParserTest extends TestCase
{
  public void testEncode () throws Exception
  {
    final String testNative = "Test is a הצü<&> test";
    final String testEncoded = "Test is a &auml;&ouml;&uuml;&lt;&amp;&gt; test";
    final CharacterEntityParser ep = CharacterEntityParser.createHTMLEntityParser();
    //Log.debug (ep.decodeEntities(testEncoded));
    assertEquals(testNative, ep.decodeEntities(testEncoded));
    assertEquals(testEncoded, ep.encodeEntities(testNative));
  }
}
