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
 * PeopleReportAPIDemoHandler.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PeopleReportAPIDemoHandler.java,v 1.2 2005/09/21 12:00:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.onetomany;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * A demo handler that shows how to define the one-to-many reports using the
 * API.
 *
 * @author Thomas Morgner
 */
public class PeopleReportAPIDemoHandler extends AbstractDemoHandler
{
  private PeopleReportTableModel tableModel;

  public PeopleReportAPIDemoHandler()
  {
    tableModel = new PeopleReportTableModel();
  }

  public String getDemoName()
  {
    return "One-To-Many-Elements Reports Demo (API-Version)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final PeopleReportDefinition reportCreator = new PeopleReportDefinition();
    final JFreeReport report = reportCreator.getReport();
    report.setData(tableModel);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("people-api.html", PeopleReportAPIDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

}
