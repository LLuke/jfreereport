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
 * ElementTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementTest.java,v 1.2 2003/07/23 16:06:24 taqua Exp $
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
import org.jfree.report.Element;

public class ElementTest extends TestCase
{
  public static class ElementImpl extends Element
  {
    public ElementImpl()
    {
    }

    /**
     *
     * @return the content-type as string.
     */
    public String getContentType()
    {
      return "Test";
    }
  }

  public ElementTest(final String s)
  {
    super(s);
  }

  public void testElementCreate()
  {
    final Element e = new ElementImpl();
    assertNotNull(e.getDataSource());
    assertNotNull(e.getStyle());
    assertNotNull(e.getName());
    assertTrue(e.isVisible());
    assertNull(e.getParent());
  }

  public void testElementMethods()
  {
    final Element e = new ElementImpl();
    assertTrue(e.isVisible());
    e.setVisible(false);
    assertTrue(e.isVisible() == false);
    e.setVisible(true);
    assertTrue(e.isVisible());

    try
    {
      e.setName(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }

    try
    {
      e.setDataSource(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }
    e.toString();
  }

  public void testSerialize() throws Exception
  {
    final Element e = new ElementImpl();
    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(bo);
    out.writeObject(e);

    final ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()));
    final Element e2 = (Element) oin.readObject();
    assertNotNull(e2); // cannot assert equals, as this is not implemented ...
  }
}
