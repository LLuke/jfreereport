package org.jfree.report.function;

import org.jfree.report.Group;
import org.jfree.report.ReportDefinition;
import org.jfree.report.event.ReportEvent;

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
    boolean isTable = getRuntime().getExportDescriptor().startsWith("table");

    final ReportDefinition report = event.getReport();
    if (isHidePageBands())
    {
      report.getPageHeader().setVisible(isTable == false);
      report.getPageFooter().setVisible(isTable == false);
    }
    if (isDisableRepeatingHeader())
    {
      final int gc = report.getGroupCount();
      for (int i = 0; i < gc; i++)
      {
        final Group g = report.getGroup(i);
        if (g.getHeader().isRepeat())
        {
          g.getHeader().setRepeat(isTable == false);
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
