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
