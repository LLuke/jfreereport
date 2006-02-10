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
 * HtmlTableCellStyleTest.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.02.2006 : Initial version
 */
package org.jfree.report.ext.junit.base.basic.modules.table;

import java.awt.Color;

import junit.framework.TestCase;
import org.jfree.report.modules.output.table.html.HtmlTableCellStyle;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.ElementAlignment;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.content.EmptyContent;

/**
 * Creation-Date: 09.02.2006, 16:49:24
 *
 * @author Thomas Morgner
 */
public class HtmlTableCellStyleTest extends TestCase
{
  public HtmlTableCellStyleTest(String string)
  {
    super(string);
  }

  public void testTableBackground ()
  {
    TableCellBackground tcb1 =
            new TableCellBackground
                    (EmptyContent.getDefaultEmptyContent(),
                            ElementDefaultStyleSheet.getDefaultStyle(),
                            Color.black);
    TableCellBackground tcb1a =
            new TableCellBackground
                    (EmptyContent.getDefaultEmptyContent(),
                            ElementDefaultStyleSheet.getDefaultStyle(),
                            Color.black);

    assertTrue("Equal Objects: ", tcb1a.equals(tcb1));
    assertTrue("Equal Objects: ", tcb1.equals(tcb1a));
  }

  public void testEqualsHtmlTableCellStyle ()
  {
    HtmlTableCellStyle s1 = new HtmlTableCellStyle(null, ElementAlignment.TOP);
    HtmlTableCellStyle s2 = new HtmlTableCellStyle(null, ElementAlignment.TOP);
    HtmlTableCellStyle s3 = new HtmlTableCellStyle(null, ElementAlignment.MIDDLE);

    assertTrue("Equal Objects: ", s1.equals(s2));
    assertTrue("Equal Objects: ", s2.equals(s1));
    assertFalse("Equal Objects: ", s2.equals(s3));
    assertFalse("Equal Objects: ", s3.equals(s2));
  }
}
