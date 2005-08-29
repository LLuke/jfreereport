package org.jfree.report.demo.helper;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;

/**
 * Creation-Date: 27.08.2005, 19:20:06
 *
 * @author: Thomas Morgner
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
      final JFreeReport report = handler.createReport();

      final PreviewDialog frame = new PreviewDialog(report);
      frame.getBase().setToolbarFloatable(true);
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
