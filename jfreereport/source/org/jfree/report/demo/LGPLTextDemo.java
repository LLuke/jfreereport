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
 * LGPLTextDemo.java
 * -------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: LGPLTextDemo.java,v 1.6 2005/01/24 23:58:32 taqua Exp $
 *
 * Changes
 * -------
 * 29-Nov-2002 : Version 1 (DG);
 *
 */

package org.jfree.report.demo;

import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ElementDefinitionException;

/**
 * A simple JFreeReport demonstration.  The generated report contains the complete text of
 * the LGPL.
 *
 * @author Thomas Morgner
 */
public class LGPLTextDemo extends SimpleDemoFrame
{

  /**
   * Constructs the demo application.
   */
  public LGPLTextDemo ()
  {
    init();
  }

  protected JFreeReport createReport ()
          throws ElementDefinitionException, IOException
  {
    return loadReport("/org/jfree/report/demo/lgpl.xml");
  }

  protected TableModel getData ()
  {
    return new DefaultTableModel();
  }

  protected String getResourcePrefix ()
  {
    return "demo.lgpl";
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

    final LGPLTextDemo frame = new LGPLTextDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}