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
 * GroupTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupTest.java,v 1.1 2003/06/01 20:43:37 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.basic;

import com.jrefinery.report.Group;
import junit.framework.TestCase;

public class GroupTest extends TestCase
{
  public GroupTest(String s)
  {
    super(s);
  }

  public void testCreate() throws Exception
  {
    Group g1 = new Group();
    assertNotNull(g1.clone());
    assertNotNull(g1.getFields());
    assertNotNull(g1.getFooter());
    assertNotNull(g1.getHeader());
    assertNotNull(g1.getName());
    assertNotNull(g1.toString());
  }

  public void testEquals()
  {
    Group g1 = new Group();
    g1.setName("");
    Group g2 = new Group();
    g2.setName("");
    assertTrue(g1.equals(g2));
    assertTrue(g1.compareTo(g2) == 0);

    g1.addField("Field");
    g2.addField("Field");
    assertTrue(g1.equals(g2));
    assertTrue(g1.compareTo(g2) == 0);

    g2.addField("Field");
    assertTrue(g1.equals(g2));
    assertTrue(g1.compareTo(g2) == 0);

    g2.addField("Field2");
    assertTrue(g1.equals(g2) == false);
    assertTrue(g1.compareTo(g2) == -1);
  }

  public void testMethods()
  {
    Group g = new Group();
    try
    {
      g.setName(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }
    try
    {
      g.setHeader(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }
    try
    {
      g.setFooter(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }
    try
    {
      g.addField(null);
      fail();
    }
    catch (NullPointerException npe)
    {
    }

  }
}
