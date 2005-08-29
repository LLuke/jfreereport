/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * CSVReaderDemo.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Mimil;
 *
 * $Id: CSVReaderDemo.java,v 1.6 2005/05/20 16:06:20 taqua Exp $
 *
 * $Log: CSVReaderDemo.java,v $
 * Revision 1.6  2005/05/20 16:06:20  taqua
 * More on the classloader topic: Changed resource paths to global paths;
 * Fixed the i18n issues (parser, demo, resource bundle lookup)
 *
 * Revision 1.5  2005/05/18 18:38:26  taqua
 * Changed the classloader handling. Now the used classloader is configurable
 * using JCommon.
 *
 * Revision 1.4  2005/03/03 21:50:38  taqua
 * Resolved some warnings for Eclipse configured with paranoid compile settings.
 *
 * Revision 1.3  2005/02/23 21:04:37  taqua
 * More build process fixes - ready for JDK 1.2.2 now
 *
 * Revision 1.2  2005/02/23 19:31:42  taqua
 * First part of the ANT build update.
 *
 * Revision 1.1  2005/02/19 18:37:07  taqua
 * CSVTableModel classes moved into modules/misc/tablemodel
 *
 * Revision 1.3  2005/01/31 17:16:19  taqua
 * Module and JUnit updates for 0.8.5
 *
 * Revision 1.2  2004/08/07 17:45:47  mimil
 * Some JavaDocs
 *
 * Revision 1.1  2004/08/07 14:35:14  mimil
 * Initial version
 *
 */

package org.jfree.report.demo.csv;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractDemoHandler;
import org.jfree.report.demo.helper.DemoControler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.modules.misc.tablemodel.CSVTableModelProducer;
import org.jfree.util.ObjectUtilities;

/**
 * Demo that show how to use <code>CSVTableModelProducer</code> to generate
 * <code>TableModel</code> for JFreeReport input data.
 *
 * @see CSVTableModelProducer
 */
public class CSVReaderDemo extends AbstractDemoHandler
{
  private CSVUserInputPanel inputPanel;

  /**
   * Creates the demo workspace.
   */
  public CSVReaderDemo ()
  {
  }

  public String getDemoName()
  {
    return "Generic Report Generation Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    return null;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("generic-demo.html", CSVReaderDemo.class);
  }

  public synchronized void setControler(final DemoControler controler)
  {
    super.setControler(controler);
    inputPanel = null;
  }

  public synchronized JComponent getPresentationComponent()
  {
    if (inputPanel == null)
    {
      inputPanel = new CSVUserInputPanel(getControler());
    }
    return inputPanel;
  }
}
