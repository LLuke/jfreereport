/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultCloseHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.jfree.report.demo.JFreeReportDemoBoot;

/**
 * A Window close handler that either closes the frame, or if the frame is
 * not embedded, shuts down the VM.
 *
 * @author Thomas Morgner
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
    if (JFreeReportDemoBoot.getInstance().getGlobalConfig().getConfigProperty
            (AbstractDemoFrame.EMBEDDED_KEY, "false").equals("false"))
    {
      System.exit(0);
    }
    else
    {
      event.getWindow().setVisible(false);
    }
  }
}
