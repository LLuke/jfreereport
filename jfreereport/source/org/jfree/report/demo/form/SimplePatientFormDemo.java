/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * SimplePatientFormDemo.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimplePatientFormDemo.java,v 1.2 2005/01/25 01:13:55 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 04.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.form;

import java.awt.BorderLayout;
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

import org.jfree.date.DateUtilities;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;

public class SimplePatientFormDemo extends AbstractDemoFrame
{
  private PatientTableModel data;

  public SimplePatientFormDemo ()
  {
    final Patient johnDoe = new Patient("John Doe", "12 Nowhere Road", "Anytown",
            "1234-5678-AB12", "Greedy Health Care Corp.",
            "Symptoms - Weeping and RSI caused by hours of tearing up holiday photos. \n" +
            "Cause - Someone richer, younger and thinner than the patient. \n" +
            "Diagnostics - Broken Heart Disease");
    johnDoe.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "Initial consulting of the doctor", "-", "done"));
    johnDoe.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "X-Ray the patients chest", "-", "done"));
    johnDoe.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "Psychiatrist consulting", "-", "failed"));
    johnDoe.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "2nd X-Ray scan", "-", "done"));
    johnDoe.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "Two Surgeons open the chest and gently mend " +
            "the heart whilst holding their breath.", "anesthetics", "sucess"));
    johnDoe.setLevel("totally healed");

    final Patient kane = new Patient
            ("Kane, (First name not known)", "United States commercial starship Nostromo",
                    "-", "4637-1345-NO123", "Aurora Mining Corp.",
                    "Cause - Face huggers equipped with intelligent alien blood.\n" +
            "Symptoms - Gradual alien metamorphosis and desire to destroy our cities. \n" +
            "Diagnostics - Alien DNA");
    kane.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "Initial consulting of the doctor", "-", "failure"));
    kane.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "X-Ray Scan", "-", "failure"));
    kane.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "mechanically removing the DNA , cleaning of alien elements " +
            "and replacing quickly with clean DNA.", "-", "success"));
    kane.addTreament(new Treatment
            (DateUtilities.createDate(12, 10, 1999),
                    "balanced diet", "-", "failed"));
    kane.setAllergy("fast food");
    kane.setLevel("Alien escaped and killed the patient.");

    data = new PatientTableModel();
    data.addPatient(johnDoe);
    data.addPatient(kane);

    setTitle("Simple Patient Form Demo");
    // as the tablemodel does not fire any change events, we have to initialize it first
    // evil lazy me .. should change that ...
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

    final String d = "This demo creates a form based report for patient data in an hospital.";
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
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    final URL in = getClass().getResource("/org/jfree/report/demo/form/patient.xml");

    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
              MessageFormat.format(getResources().getString("report.definitionnotfound"),
                      new Object[]{in}),
              getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
    }

    final JFreeReport report;
    try
    {
      report = parseReport(in);
      report.setData(this.data);
    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
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

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final SimplePatientFormDemo frame = new SimplePatientFormDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
