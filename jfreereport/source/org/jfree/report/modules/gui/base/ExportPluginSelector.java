package org.jfree.report.modules.gui.base;

import org.jfree.report.util.ReportConfiguration;

/**
 * Creation-Date: 05.09.2005, 19:52:04
 *
 * @author: Thomas Morgner
 */
public interface ExportPluginSelector
{
  public boolean isPluginValid(final PreviewProxy proxy,
                               final ReportConfiguration config);
  public Class getPluginClass ();
  public String getPreference ();


}
