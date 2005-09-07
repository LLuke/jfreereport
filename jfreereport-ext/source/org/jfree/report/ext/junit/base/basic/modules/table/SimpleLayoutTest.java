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
 * SimpleLayoutTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimpleLayoutTest.java,v 1.3 2005/01/31 17:16:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 10.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules.table;

import java.net.URL;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.Band;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.ext.junit.base.functionality.FunctionalityTestLib;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class SimpleLayoutTest extends TestCase
{
  public SimpleLayoutTest()
  {
  }

  public SimpleLayoutTest(String s)
  {
    super(s);
  }

  public void testSimpleLayout () throws Exception
  {
    final FunctionalityTestLib.ReportTest test =
            new FunctionalityTestLib.ReportTest
                    ("org/jfree/report/demo/report5.xml", new DefaultTableModel());

    final URL url = ObjectUtilities.getResource
      (test.getReportDefinition(), SimpleLayoutTest.class);
    assertNotNull("Failed to locate " + test.getReportDefinition(), url);

    Log.debug("Processing: " + url);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
    report.setData(test.getReportTableModel());

    FunctionalityTestLib.createStreamHTML(report);
  }

}
