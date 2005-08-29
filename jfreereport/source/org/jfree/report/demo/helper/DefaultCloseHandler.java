package org.jfree.report.demo.helper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.jfree.report.util.ReportConfiguration;

/**
 * Window close handler.
 */
public class DefaultCloseHandler extends WindowAdapter
{
  public DefaultCloseHandler ()
  {
  }

  /**
   * Handles the window closing event.
   *
   * @param event the window event.
   */
  public void windowClosing (final WindowEvent event)
  {
    if (ReportConfiguration.getGlobalConfig().getConfigProperty
            ("org.jfree.report.demo.Embedded", "false").equals("false"))
    {
      System.exit(0);
    }
    else
    {
      event.getWindow().setVisible(false);
    }
  }
}
