/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -------------
 * CardDemo.java
 * -------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CardDemo.java,v 1.5 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package org.jfree.report.demo.cards;

import java.net.URL;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A JFreeReport demo.
 *
 * @author Thomas Morgner.
 */
public class SimpleCardDemoHandler extends AbstractXmlDemoHandler
{
  private TableModel data;

  /**
   * Default constructor.
   */
  public SimpleCardDemoHandler ()
  {
    final CardTableModel model = new CardTableModel();
    model.addCard(new AdminCard("Jared", "Diamond", "NR123123", "login", "secret", new Date()));
    model.addCard(new FreeCard("NR123123", new Date()));
    model.addCard(new PrepaidCard("First Name", "Last Name", "NR123123"));
    model.addCard(new AccountCard("John", "Doe", "NR123123", "login", "secret"));
    model.addCard(new UserCard("Richard", "Helm", "NR123123", "login", "secret", new Date()));
    data = new WrappingTableModel(model, "C1_", "C2_");
  }

  public String getDemoName()
  {
    return "Simple Card printing";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("simple-usercards.html", SimpleCardDemoHandler.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("usercards.xml", SimpleCardDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final SimpleCardDemoHandler handler = new SimpleCardDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
