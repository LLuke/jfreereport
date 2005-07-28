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
 * MultiReportDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MultiReportDemo.java,v 1.3 2005/05/20 16:06:43 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.multireport;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
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
import org.jfree.report.modules.misc.tablemodel.JoiningTableModel;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ElementDefinitionException;

/**
 * The MultiReportDemo combines data from multiple table models
 * into one single report.
 * <p>
 * For a detailed explaination of the demo have a look at the
 * file <a href="multireport.html">file</a>'.
 *
 * @author Thomas Morgner
 */
public class MultiReportDemo extends AbstractDemoFrame
{

  /**
   * The data for the report.
   */
  private final TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public MultiReportDemo (final String title)
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

    final String d = "This demo shows how to join two tablemodels in one report.";
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

  /**
   * Parses the report.
   *
   * @return the report.
   */
  protected JFreeReport parseReport ()
          throws IOException, ElementDefinitionException
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/multireport/joined-report.xml", MultiReportDemo.class);

    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
              MessageFormat.format(getResources().getString("report.definitionnotfound"),
                      new Object[]{in}),
              getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
      return null;
    }

    final ReportGenerator generator = ReportGenerator.getInstance();
    final JFreeReport report = generator.parseReport(in);
    report.setData(this.data);
    return report;
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    try
    {
      final JFreeReport report = parseReport();
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
    catch (IOException e)
    {
      showExceptionDialog("report.definitionfailure", e);
    }
    catch (ElementDefinitionException e)
    {
      showExceptionDialog("report.definitionfailure", e);
    }
  }

  private TableModel createFruitTableModel ()
  {
    final String[] names = new String[]{"Id Number", "Cat", "Fruit"};
    final Object[][] data = new Object[][]{
      { "I1", "A", "Apple"},
      { "I2", "A", "Orange"},
      { "I3", "B", "Water melon"},
      { "I4", "B", "Strawberry"},
    };
    return new DefaultTableModel(data, names);
  }

  private TableModel createColorTableModel ()
  {
    final String[] names = new String[]{"Number", "Group", "Color"};
    final Object[][] data = new Object[][]{
      { new Integer(1), "X", "Red"},
      { new Integer(2), "X", "Green"},
      { new Integer(3), "Y", "Yellow"},
      { new Integer(4), "Y", "Blue"},
      { new Integer(5), "Z", "Orange"},
      { new Integer(6), "Z", "White"},
    };
    return new DefaultTableModel(data, names);
  }

  private TableModel createJoinedTableModel ()
  {
    final JoiningTableModel jtm = new JoiningTableModel();
    jtm.addTableModel("Color", createColorTableModel());
    jtm.addTableModel("Fruit", createFruitTableModel());
    return jtm;
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final MultiReportDemo frame = new MultiReportDemo("Multiple Reports Demo");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
