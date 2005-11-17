/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * I18nDemo.java
 * -------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: I18nDemo.java,v 1.2 2005/09/21 12:00:17 taqua Exp $
 *
 * Changes
 * -------
 * 04-Apr-2003 : Version 1 (DG);
 *
 */

package org.jfree.report.demo.internationalisation;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report where column 3 displays (column 1 / column 2) as a percentage.
 *
 * @author David Gilbert
 */
public class I18nDemo extends AbstractXmlDemoHandler
{

  /**
   * The data for the report.
   */
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public I18nDemo ()
  {
    this.data = createData();
  }


  public String getDemoName()
  {
    return "Internationalisation Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("i18n.html", I18nDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("i18n.xml", I18nDemo.class);
  }

  /**
   * Creates a sample dataset. <!-- (Used in JUnitTest) -->
   *
   * @return A <code>TableModel</code>.
   */
  public static TableModel createData ()
  {
    final DefaultTableModel data = new DefaultTableModel();
    data.addColumn("Data");
    data.addColumn("A");
    data.addColumn("B");
    data.addColumn("C");
    data.addRow(new Object[]{"data.firstElement", new Double(43.0), new Double(127.5), new Double(10001.999)});
    data.addRow(new Object[]{"data.secondElement", new Double(57.0), new Double(108.5), new Double(-10001.999)});
    data.addRow(new Object[]{"data.thirdElement", new Double(35.0), new Double(164.8), new Double(-999.9999)});
    data.addRow(new Object[]{"data.fourthElement", new Double(86.0), new Double(164.0), new Double(999.9999)});
    data.addRow(new Object[]{"data.lastElement", new Double(12.0), new Double(103.2), new Double(0.999)});
    return data;
  }
}
