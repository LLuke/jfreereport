/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * BugLibrary.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 12.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.bugs;

import java.net.URL;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;

public class BugLibrary
{
  public static JFreeReport parse(final String urlname, final TableModel data)
  {
    try
    {
      final URL in = Object.class.getResource(urlname);
      if (in == null)
      {
        Log.debug("ERROR: xml file not found.");
        return null;
      }
      else
      {
        Log.debug ("Found: " + in);
      }
      final ReportGenerator gen = ReportGenerator.getInstance();
      JFreeReport report1 = null;
      try
      {
        report1 = gen.parseReport(in, in);
      }
      catch (Exception ioe)
      {
        Log.debug("ERROR 1: report definition failure.", ioe);
        return null;
      }

      if (report1 == null)
      {
        Log.debug("ERROR 2: the report is null.");
        return null;
      }
      report1.setData(data);
      return report1;
    }
    catch (Exception e)
    {
      Log.debug("ERROR 3: " , e);
      return null;
    }
  }

  public static boolean previewDialog (JFreeReport report)
  {
    try
    {
      final PreviewDialog frame1 = new PreviewDialog(report);
      frame1.pack();
      frame1.setModal(true);
      RefineryUtilities.positionFrameRandomly(frame1);
      frame1.setVisible(true);
      frame1.requestFocus();
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }


}
