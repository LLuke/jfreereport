/**
 * Date: Feb 2, 2003
 * Time: 5:26:50 PM
 *
 * $Id: ExportPlugin.java,v 1.1 2003/02/02 22:47:39 taqua Exp $
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;

import javax.swing.Icon;
import javax.swing.KeyStroke;

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
