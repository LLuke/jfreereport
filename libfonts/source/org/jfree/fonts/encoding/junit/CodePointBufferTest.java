/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * CodePointBufferTest.java
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
package org.jfree.fonts.encoding.junit;

import java.util.Arrays;

import junit.framework.TestCase;
import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.CodePointStream;

/**
 * Creation-Date: 23.04.2006, 17:28:07
 *
 * @author Thomas Morgner
 */
public class CodePointBufferTest extends TestCase
{
  public CodePointBufferTest()
  {
  }

  public CodePointBufferTest(String string)
  {
    super(string);
  }

  public void testWrite ()
  {
    CodePointBuffer buffer = new CodePointBuffer(0);
    CodePointStream cps = new CodePointStream(buffer, 10);
    cps.put(10);
    cps.put(11);
    cps.put(12);
    cps.put(13);

    cps.put(new int[]{20, 21, 22, 23, 24, 25});
    cps.close();

    assertEquals("Buffer-Cursor: ", 10, buffer.getCursor());
  }
}
