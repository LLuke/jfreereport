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
 * GroupListTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupListTest.java,v 1.5 2005/01/31 17:16:31 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import junit.framework.TestCase;
import org.jfree.report.Group;
import org.jfree.report.GroupList;

public class GroupListTest extends TestCase
{
  public GroupListTest(final String s)
  {
    super(s);
  }

  public void testCreate()
          throws CloneNotSupportedException
  {
    final GroupList gl = new GroupList();
    gl.clear();
    assertNotNull(gl.clone());
    assertNotNull(gl.iterator());
    assertNotNull(gl.toString());
  }

  public void testMethods()
  {
    final GroupList gl = new GroupList();
    try
    {
      gl.add(null);
    }
    catch (NullPointerException npe)
    {
      // expected, ignored
    }

    final Group g1 = new Group();
    gl.add(g1);
    gl.add(g1);
    assertTrue(gl.size() == 1); // the old instance gets removed and replaced by the new group
    gl.add(new Group());
    assertTrue(gl.size() == 1); // the old instance gets removed and replaced by the new group

    final Group g2 = new Group();
    g2.addField("Test");

    final Group g3 = new Group();
    g3.addField("Failure");

    // group g2 and g3 are unreleated, g2 is no child or parent of g3
    gl.add(g2);
    try
    {
      gl.add(g3);
      fail();
    }
    catch (IllegalArgumentException iea)
    {
      // expected, ignored
    }
    assertEquals(2, gl.size());
  }
}
