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
  private boolean hidePageBands;
  private boolean disableRepeatingHeader;

  public HidePageBandForTableExportFunction()
  {
    hidePageBands = true;
  }

  public void reportInitialized(final ReportEvent event)
  {
    final ReportProperties properties = event.getReport().getProperties();
    Object o = properties.get(JFreeReport.REPORT_LAYOUT_SUPPORT);
    boolean isPageable = (o instanceof OutputTarget);

    final ReportDefinition report = event.getReport();
    if (isHidePageBands())
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

  public boolean isHidePageBands()
  {
    return hidePageBands;
  }

  public void setHidePageBands(final boolean hidePageBands)
  {
    this.hidePageBands = hidePageBands;
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
