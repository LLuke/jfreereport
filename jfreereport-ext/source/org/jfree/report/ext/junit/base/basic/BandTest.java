/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * BandTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandTest.java,v 1.2 2003/07/23 16:06:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.Element;

public class BandTest extends TestCase
{
  public BandTest(final String s)
  {
    super(s);
  }

  public void testBandCreate()
  {
    final Band b = new Band();
    assertNotNull(b.getContentType());
    assertNotNull(b.getDataSource());
    assertNotNull(b.getStyle());
    assertNotNull(b.getName());
    assertTrue(b.isVisible());
    assertNull(b.getParent());
    assertNotNull(b.getLayout());
    assertNotNull(b.getElementArray());
    assertTrue(b.getElementCount() == 0);
    assertNotNull(b.getElements());
    assertNotNull(b.getBandDefaults());
  }

  public void testBandMethods()
  {
    final Band b = new Band();
    assertTrue(b.isVisible());
    b.setVisible(false);
    assertTrue(b.isVisible() == false);
    b.setVisible(true);
    assertTrue(b.isVisible());

    try
    {
      b.setName(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }

    try
    {
      b.setDataSource(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }

    b.toString();
  }

  public void testAddElement()
  {
    final Band b = new Band();
    assertTrue(b.getElementCount() == 0);
    b.addElement(0, new ElementTest.ElementImpl());
    assertTrue(b.getElementCount() == 1);
    b.addElement(new ElementTest.ElementImpl());
    assertTrue(b.getElementCount() == 2);
    b.addElement(0, new ElementTest.ElementImpl());
    assertTrue(b.getElementCount() == 3);
    b.addElement(2, new ElementTest.ElementImpl());
    assertTrue(b.getElementCount() == 4);
    try
    {
      b.addElement(5, new ElementTest.ElementImpl());
      fail();
    }
    catch (IllegalArgumentException iob)
    {
    }
    try
    {
      b.addElement(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }
    try
    {
      b.addElement(b);
      fail();
    }
    catch (IllegalArgumentException ia)
    {
    }

    try
    {
      final Band b1 = new Band();
      final Band b2 = new Band();
      final Band b3 = new Band();
      b1.addElement(b2);
      b2.addElement(b3);
      b3.addElement(b1);
      fail();
    }
    catch (IllegalArgumentException ia)
    {
    }

  }

  public void testSerialize() throws Exception
  {
    final Band e = new Band();
    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(bo);
    out.writeObject(e);

    final ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()));
    final Element e2 = (Element) oin.readObject();
    assertNotNull(e2); // cannot assert equals, as this is not implemented.
  }

}
