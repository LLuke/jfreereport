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
 * PeopleReportDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PeopleReportDemo.java,v 1.1 2005/05/06 15:03:09 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.onetomany;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;

public class PeopleReportDemo extends AbstractDemoFrame
{

  private class PreviewAPIAction extends AbstractActionDowngrade
  {
    public PreviewAPIAction ()
    {
      putValue(Action.NAME, "Preview Report defined by API");
    }

    public void actionPerformed (final ActionEvent event)
    {
      attemptAPIPreview();
    }
  }

  /**
   * The data for the report.
   */
  private final TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public PeopleReportDemo (final String title)
  {
    setTitle(title);
    this.data = createJoinedTableModel();
    setJMenuBar(createMenuBar());
    setContentPane(createContent());
  }

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  public JMenuBar createMenuBar ()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem previewItem = new ActionMenuItem(getPreviewAction());
    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    fileMenu.add(previewItem);
    fileMenu.add(new ActionMenuItem(new PreviewAPIAction()));
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);
    return mb;
  }

  /**
   * Creates the content for the application frame.
   *
   * @return a panel containing the basic user interface.
   */
  public JPanel createContent ()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    final String d = "This demo shows how to build reports with one-to-many relations.";
    final JTextArea textArea = new JTextArea(d);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
    final JScrollPane scroll = new JScrollPane(textArea);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    final JTable table = new JTable(this.data);
    final JScrollPane scrollPane = new JScrollPane(table);

    final JButton previewButton = new ActionButton(getPreviewAction());

    content.add(scroll, BorderLayout.NORTH);
    content.add(scrollPane);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;

  }

  protected JFreeReport parseReport ()
  {
    final URL in = getClass().getResource("/org/jfree/report/demo/onetomany/joined-report.xml");

    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
              MessageFormat.format(getResources().getString("report.definitionnotfound"),
                      new Object[]{in}),
              getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
      return null;
    }
    try
    {
      final JFreeReport report = parseReport(in);
      report.setData(this.data);
      return report;
    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
      return null;
    }
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptAPIPreview ()
  {
    final PeopleReportByAPI reportCreator = new PeopleReportByAPI();
    final JFreeReport report = reportCreator.getReport();
    report.setData(this.data);
    try
    {
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setToolbarFloatable(true);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportProcessingException rpe)
    {
      showExceptionDialog("report.previewfailure", rpe);
    }
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    final JFreeReport report = parseReport();
    if (report == null)
    {
      return;
    }
    try
    {
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setToolbarFloatable(true);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportProcessingException rpe)
    {
      showExceptionDialog("report.previewfailure", rpe);
    }
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL the template location.
   * @return a report.
   */
  private JFreeReport parseReport (final URL templateURL)
  {

    JFreeReport result = null;
    final ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
      result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      Log.error("Failed to parse the report definition", e);
    }
    return result;

  }

  private TableModel createJoinedTableModel ()
  {
    final String[] columnnames = new String[] {
      "person.name", "person.address",
      "recordType",
      "activitylog.Time", "activitylog.Task",
      "office.Name", "office.Annotations",
      "lunch.Meal", "lunch.Rating"
    };

    final Object[][] data = new Object[][] {
      {
        "Michael B. DK", "street 1, town",
        "activitylog",
        "Monday 10:00 - 12:00",  "lunchbreak (Pizza)",
        null, null,
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "activitylog",
        "Monday 12:00 - 13:00",  "some work",
        null, null,
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "activitylog",
        "Monday 13:00 - 15:00",  "surfed the internet",
        null, null,
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "office",
        null, null,
        "Goofy", "loves to hide all pencils",
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "office",
        null, null,
        "Dagobert D.", "greedy bastard, would sell soul for payrise",
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "office",
        null, null,
        "Donald D.", "keep away from phone!!!",
        null, null
      },
      {
        "Michael B. DK", "street 1, town",
        "lunch",
        null, null,
        null, null,
        "Pizza", "A+"
      },
      {
        "Michael B. DK", "street 1, town",
        "lunch",
        null, null,
        null, null,
        "Toast", "F- (Am I in jail, or why do they serve dried bread and water?"
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Monday 10:00 - 11:00",  "Meeting with major. Got assignment for headhunt.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Monday 11:00 - 13:00",  "Meeting with bank director. Got assignment for headhunt.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Monday 13:00 - 17:00",  "Meeting with shop keepers. Got assignment for headhunt.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Thursday 10:00 - 11:00",  "Negotiate with Daltons. We'll share bounty 50:50.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Thursday 12:00 - 12:05",  "Great showdown to earn some easy money.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "activitylog",
        "Thursday 14:00 - 17:00",  "Daltons break free from jail. They stole my share.",
        null, null,
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "office",
        null, null,
        "Joe", "smart one, beware!",
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "office",
        null, null,
        "William", "does what Joe says ..",
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "office",
        null, null,
        "Jack", "does what Joe says ..",
        null, null
      },
      {
        "Lucky Luke", "Daisytown, Saloon",
        "office",
        null, null,
        "Avrell", "Stupid head.",
        null, null
      },
    };

    final DefaultTableModel model = new DefaultTableModel(data, columnnames);
    return model;
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final PeopleReportDemo frame = new PeopleReportDemo("One-To-Many-Elements Reports Demo");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
