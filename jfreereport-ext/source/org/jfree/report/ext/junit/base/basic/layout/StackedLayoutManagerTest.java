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
 * StackedLayoutManagerTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StackedLayoutManagerTest.java,v 1.1 2005/08/10 19:30:44 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.layout;

import junit.framework.TestCase;
import org.jfree.report.Band;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.StackedLayoutManager;

public class StackedLayoutManagerTest extends TestCase
{
  public StackedLayoutManagerTest (final String s)
  {
    super(s);
  }

  public void testForEmptyBand ()
  {
    final Band b = new Band();
    b.setLayout(new StackedLayoutManager());
    BandLayoutManagerUtil.doLayout(b, new DefaultLayoutSupport(), 100, 100);
  }
}