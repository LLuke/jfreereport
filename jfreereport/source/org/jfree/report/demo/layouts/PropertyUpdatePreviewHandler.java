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
 * PropertyUpdatePreviewHandler.java
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
package org.jfree.report.demo.layouts;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.demo.helper.InternalDemoHandler;
import org.jfree.report.demo.helper.PreviewHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;

/**
 * A helper class to make this demo accessible from the DemoFrontend.
 */
public class PropertyUpdatePreviewHandler implements PreviewHandler
{
  private InternalDemoHandler handler;

  public PropertyUpdatePreviewHandler(final InternalDemoHandler handler)
  {
    this.handler = handler;
  }

  public void attemptPreview()
  {
    try
    {
      final JFreeReport report = handler.createReport();

      final PreviewDialog frame = new PreviewDialog(report);
      frame.getBase().setToolbarFloatable(true);
      frame.getBase().setReportControler(new DemoReportControler());
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportDefinitionException e)
    {
      Log.error("Unable to create the report; report definition contained errors.", e);
      AbstractDemoFrame.showExceptionDialog("report.definitionfailure", e);
    }
    catch (ReportProcessingException rpe)
    {
      Log.error("Unable to procress the report");
      AbstractDemoFrame.showExceptionDialog("report.previewfailure", rpe);
    }
  }
}
