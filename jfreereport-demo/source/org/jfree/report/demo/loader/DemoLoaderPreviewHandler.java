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
 * DemoLoaderPreviewHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.loader;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.util.PreviewHandler;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.swing.preview.PreviewDialog;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 03.12.2006, 14:33:29
 *
 * @author Thomas Morgner
 */
public class DemoLoaderPreviewHandler implements PreviewHandler
{
  public DemoLoaderPreviewHandler()
  {
  }

  public void attemptPreview()
  {
    final ReportJob report = new DefaultReportJob(new JFreeReport());
    final PreviewDialog frame = new PreviewDialog();
    frame.setReportController(new DemoLoader());
    frame.setReportJob(report);
    frame.pack();
    RefineryUtilities.positionFrameRandomly(frame);
    frame.setVisible(true);
    frame.requestFocus();
  }
}
