/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TableExportVerificationTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.ext.junit.base.basic.modules.table.tableverify.VerifyPattern;
import org.jfree.report.ext.junit.base.basic.modules.table.tableverify.VerifyTableProcessor;

// This tests, whether the cells of the table are created correctly
// it checks the global structure of the table, not the contents.
// formating and content checks have to be added later...
public class TableExportVerificationTest extends TestCase
{

  public TableExportVerificationTest()
  {
  }

  public TableExportVerificationTest(String s)
  {
    super(s);
  }

  public void testCreateContent () throws Exception
  {
    TableVerifyDefinition definition = new TableVerifyDefinition();
    JFreeReport report = definition.createReport();
    report.setData(new SampleData1());
    VerifyPattern pattern = createVerifyExport(report);
    assertNotNull(pattern);
    assertEquals(definition.createPattern(), pattern);
  }

  public static VerifyPattern createVerifyExport(final JFreeReport report)
      throws Exception
  {
    final VerifyTableProcessor pr = new VerifyTableProcessor(report);
    pr.setContent(new VerifyPattern());
    pr.setStrictLayout(false);
    pr.processReport();
    return pr.getContent();
  }


}
