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
 * ObjectFactoryTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ObjectFactoryTest.java,v 1.2 2003/07/23 16:06:24 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.io.ext;

import java.awt.geom.Line2D;

import junit.framework.TestCase;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;

public class ObjectFactoryTest extends TestCase
{
  public ObjectFactoryTest(final String s)
  {
    super(s);
  }

  public void testObjectQuery()
  {
    final DefaultClassFactory fact = new DefaultClassFactory();
    final ObjectDescription line2DDescr = fact.getDescriptionForClass(Line2D.class);
    assertNotNull(line2DDescr);
    final ObjectDescription od = fact.getSuperClassObjectDescription(Line2D.Float.class, null);
    assertEquals(line2DDescr.getClass(), od.getClass());
  }
}
