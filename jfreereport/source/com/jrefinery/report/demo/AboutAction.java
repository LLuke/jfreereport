/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * AboutAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: AboutAction.java,v 1.3 2002/05/16 13:05:35 jaosch Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.preview.PreviewFrame;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class AboutAction extends AbstractAction implements Runnable
{

  private JFreeReportDemo demo;

  /**
   * Constructs a new action.
   */
  public AboutAction (JFreeReportDemo demo, ResourceBundle resources)
  {

    this.demo = demo;

    String name = resources.getString ("action.about.name");
    this.putValue (Action.NAME, name);

    String description = resources.getString ("action.about.description");
    this.putValue (Action.SHORT_DESCRIPTION, description);

    Integer mnemonic = (Integer) resources.getObject ("action.about.mnemonic");
    this.putValue (Action.MNEMONIC_KEY, mnemonic);

    ImageIcon icon16 = (ImageIcon) resources.getObject("action.about.small-icon");
    this.putValue (Action.SMALL_ICON, icon16);

    ImageIcon icon24 = (ImageIcon) resources.getObject("action.about.icon");
    this.putValue ("ICON24", icon24);

    this.putValue (Action.ACTION_COMMAND_KEY, JFreeReportDemo.ABOUT_COMMAND);

  }

  public void actionPerformed (ActionEvent e)
  {
    SwingUtilities.invokeLater (this);
  }

  public void run ()
  {
    demo.displayAbout ();
  }
}
