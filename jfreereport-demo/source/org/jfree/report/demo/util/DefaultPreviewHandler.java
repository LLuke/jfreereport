/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * DefaultPreviewHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import org.jfree.report.modules.gui.swing.preview.PreviewDialog;
import org.jfree.report.flow.ReportJob;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;

/**
 * The DefaultPreviewHandler creates a PreviewDialog for the current report.
 *
 * @author Thomas Morgner
 */
public class DefaultPreviewHandler implements PreviewHandler
{
  private InternalDemoHandler handler;

  public DefaultPreviewHandler(final InternalDemoHandler handler)
  {
    this.handler = handler;
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  public void attemptPreview()
  {

    try
    {
      final ReportJob report = handler.createReport();

      final PreviewDialog frame = new PreviewDialog();
      frame.setReportJob(report);
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
  }
}
