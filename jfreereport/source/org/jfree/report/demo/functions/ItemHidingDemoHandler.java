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
 * ItemHidingDemoHandler.java
 * -------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ItemHidingDemoHandler.java,v 1.2 2005/09/21 12:00:15 taqua Exp $
 *
 * Changes
 * -------
 * 04-Apr-2003 : Version 1 (DG);
 *
 */

package org.jfree.report.demo.functions;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.groups.ColorAndLetterTableModel;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report where column 3 displays (column 1 / column 2) as a percentage.
 *
 * @author David Gilbert
 */
public class ItemHidingDemoHandler extends AbstractXmlDemoHandler
{
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public ItemHidingDemoHandler ()
  {
    data = new ColorAndLetterTableModel();
  }

  public String getDemoName()
  {
    return "Item Hiding Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("item-hiding.html", ItemHidingDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("item-hiding.xml", ItemHidingDemoHandler.class);
  }


  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final ItemHidingDemoHandler demoHandler = new ItemHidingDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
