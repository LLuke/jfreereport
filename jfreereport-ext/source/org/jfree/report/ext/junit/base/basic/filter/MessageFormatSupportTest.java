/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * MessageFormatSupportTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MessageFormatSupportTest.java,v 1.2 2005/09/20 16:58:22 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.filter;

import junit.framework.TestCase;
import org.jfree.report.filter.MessageFormatSupport;
import org.jfree.report.util.CSVTokenizer;

public class MessageFormatSupportTest extends TestCase
{
  public MessageFormatSupportTest (String s)
  {
    super(s);
  }

  public void testQuotedExample ()
  {
    final String example = "$(\"customer.firstName\") $(\"customer.lastName\")";
    final MessageFormatSupport support = new MessageFormatSupport();
    support.setFormatString(example);
    assertEquals("CompiledFormat", "{0} {1}", support.getCompiledFormat());
  }

  public void testCSVTokenizer ()
  {
    final String example = "\"Test\"";
    CSVTokenizer tokenizer = new CSVTokenizer(example, ",", "\"");
    assertTrue("Tokenizer has at least one element", tokenizer.hasMoreTokens());
    assertEquals(tokenizer.nextToken(), "Test");
  }

  public void testComplexReplacement ()
  {
    MessageFormatSupport support = new MessageFormatSupport();
    support.setFormatString("$(null,number,integer), $(dummy), $(null,date), $(null,number,integer)");
    SimpleDataRow sdr = new SimpleDataRow();
    sdr.add("null", null);
    sdr.add("dummy", "Content");

    String text = support.performFormat(sdr);
    assertEquals("Expected content w/o nullString", "null, Content, null, null", text);
    System.out.println(text);

    support.setNullString("-");
    String ntext = support.performFormat(sdr);
    assertEquals("Expected content w nullString", "-, Content, -, -",ntext);
    System.out.println(ntext);
  }
}
