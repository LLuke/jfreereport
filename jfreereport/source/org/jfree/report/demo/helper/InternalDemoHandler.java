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
 * InternalDemoHandler.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Treatment.java,v 1.2 2005/01/25 01:13:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.helper;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;

/**
 * A demo handler allows the generic use of demos in the framework.
 * <p>
 * Every demo has a name, a way to create a report and a description in
 * HTML documenting the demo. A demo also provides a presentation component
 * to either show the data or control the demo's appearance.
 *
 * @author Thomas Morgner
 */
public interface InternalDemoHandler extends DemoHandler
{
  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName();

  /**
   * Assigns a demo controler to this demo. It is guaranteed, that a controler
   * is set, before the presentation component is queried or a report is
   * created.
   *
   * @param controler the controler.
   */
  public void setControler (final DemoControler controler);

  /**
   * Returns the demo controler for this demo. The demo controler is supplied
   * by the user of the demo handler.
   *
   * @return the demo controler for this demo handler.
   */
  public DemoControler getControler();

  /**
   * Creates the report. For XML reports, this will most likely call the
   * ReportGenerator, while API reports may use this function to build and return
   * a new, fully initialized report object.
   *
   * @return the fully initialized JFreeReport object.
   * @throws ReportDefinitionException if an error occured preventing the
   * report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException;

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource();

  /**
   * Returns the presentation component for this demo. This component is
   * shown before the real report generation is started. Ususally it contains
   * a JTable with the demo data and/or input components, which allow to configure
   * the report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent();
}
