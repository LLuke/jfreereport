/**
 * Date: Jan 14, 2003
 * Time: 6:59:46 PM
 *
 * $Id$
 */
package com.jrefinery.report.preview;

import javax.swing.Action;
import javax.swing.JMenuBar;
import java.awt.event.ComponentListener;

public interface PreviewProxy
{
  public void pack();
  public void dispose();

  public void addComponentListener(ComponentListener listener);
  public void removeComponentListener (ComponentListener listener);

  public Action createDefaultCloseAction ();
  public void setJMenuBar (JMenuBar bar);
  public void setTitle (String title);
}
