package org.jfree.report.demo;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;

public class TriggerComplexPageFooterFunction extends AbstractFunction
{

  /**
   * Receives notification that report generation has completed, the report footer was
   * printed, no more output is done. This is a helper event to shut down the output
   * service.
   *
   * @param event The event.
   */
  public void reportDone (final ReportEvent event)
  {
    event.getReport().getPageFooter().getElement("page-footer-content").setVisible(false);
    event.getReport().getPageFooter().getElement("fake-report-footer").setVisible(true);
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    event.getReport().getPageFooter().getElement("page-footer-content").setVisible(true);
    event.getReport().getPageFooter().getElement("fake-report-footer").setVisible(false);
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return null;
  }
}
