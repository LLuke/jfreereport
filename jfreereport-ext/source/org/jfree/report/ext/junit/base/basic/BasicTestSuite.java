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
 * BasicTestSuite.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicTestSuite.java,v 1.3 2003/09/09 10:27:57 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jfree.report.ext.junit.base.basic.content.ContentTestSuite;
import org.jfree.report.ext.junit.base.basic.layout.LayoutTestSuite;
import org.jfree.report.ext.junit.base.basic.preview.PreviewTestSuite;
import org.jfree.report.ext.junit.base.basic.style.StyleTestSuite;
import org.jfree.report.ext.junit.base.basic.util.UtilTestSuite;
import org.jfree.report.ext.junit.base.basic.modules.ModuleTestSuite;

public class BasicTestSuite extends TestSuite
{
  public BasicTestSuite(final String s)
  {
    super(s);
    addTestSuite(BandTest.class);
    addTestSuite(ElementTest.class);
    addTestSuite(DataRowBackendTest.class);
    addTestSuite(DataRowBackendPreviewTest.class);
    addTestSuite(GroupTest.class);
    addTestSuite(GroupListTest.class);
    addTestSuite(JFreeReportTest.class);
    addTest(new ContentTestSuite(ContentTestSuite.class.getName()));
    addTest(new LayoutTestSuite(LayoutTestSuite.class.getName()));
    addTest(new UtilTestSuite(UtilTestSuite.class.getName()));
    addTest(new StyleTestSuite(StyleTestSuite.class.getName()));
    addTest(new PreviewTestSuite(PreviewTestSuite.class.getName()));
    addTest(new ModuleTestSuite(ModuleTestSuite.class.getName()));
  }


  public static Test suite()
  {
    return new BasicTestSuite(BasicTestSuite.class.getName());
  }
}
