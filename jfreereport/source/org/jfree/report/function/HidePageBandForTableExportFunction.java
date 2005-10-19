package org.jfree.report.function;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDefinition;
import org.jfree.report.Group;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.util.ReportProperties;

/**
 * Hides the page header and footer if the export type is not pageable.
 * Repeated groupheaders can be disabled.
 *
 * @author Thomas Morgner
 */
public class HidePageBandForTableExportFunction extends AbstractFunction
{
  private boolean hidePageHeader;
  private boolean disableRepeatingHeader;

  public HidePageBandForTableExportFunction()
  {
    hidePageHeader = true;
  }

  public void reportInitialized(final ReportEvent event)
  {
    final ReportProperties properties = event.getReport().getProperties();
    Object o = properties.get(JFreeReport.REPORT_LAYOUT_SUPPORT);
    boolean isPageable = (o instanceof OutputTarget);

    final ReportDefinition report = event.getReport();
    if (isHidePageHeader())
    {
      report.getPageHeader().setVisible(isPageable);
      report.getPageFooter().setVisible(isPageable);
    }
    if (isDisableRepeatingHeader())
    {
      final int gc = report.getGroupCount();
      for (int i = 0; i < gc; i++)
      {
        final Group g = report.getGroup(i);
        if (g.getHeader().isRepeat())
        {
          g.getHeader().setRepeat(isPageable);
        }
      }
    }
  }

  public boolean isHidePageHeader()
  {
    return hidePageHeader;
  }

  public void setHidePageHeader(final boolean hidePageHeader)
  {
    this.hidePageHeader = hidePageHeader;
  }

  public boolean isDisableRepeatingHeader()
  {
    return disableRepeatingHeader;
  }

  public void setDisableRepeatingHeader(final boolean disableRepeatingHeader)
  {
    this.disableRepeatingHeader = disableRepeatingHeader;
  }

  public Object getValue()
  {
    return null;
  }
}
