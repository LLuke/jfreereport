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
 * $Id: SimplePatientFormDemo.java,v 1.6 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 04.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.form;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.date.DateUtilities;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

public class SimplePatientFormDemo extends AbstractXmlDemoHandler
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

  }

  public String getDemoName()
  {
    return "Patient Form Demo";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("form.html", SimplePatientFormDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(this.data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("patient.xml", SimplePatientFormDemo.class);
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

    final SimplePatientFormDemo handler = new SimplePatientFormDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
