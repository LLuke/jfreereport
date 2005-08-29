package org.jfree.report.demo.helper;

import javax.swing.Action;

import org.jfree.report.modules.gui.base.components.JStatusBar;

/**
 * Creation-Date: 27.08.2005, 15:06:15
 *
 * @author: Thomas Morgner
 */
public interface DemoControler
{
  public JStatusBar getStatusBar ();
  public Action getExportAction ();
}
