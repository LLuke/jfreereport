/**
 * Date: Feb 2, 2003
 * Time: 5:26:50 PM
 *
 * $Id$
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;

import javax.swing.KeyStroke;
import javax.swing.Icon;

public interface ExportPlugin
{
  public boolean performExport (JFreeReport report);
  public String getDisplayName();
  public String getShortDescription();
  public Icon getSmallIcon();
  public Icon getLargeIcon();
  public KeyStroke getAcceleratorKey();
  public Integer getMnemonicKey();
  public boolean isSeparated ();
  public boolean isAddToToolbar();
}
