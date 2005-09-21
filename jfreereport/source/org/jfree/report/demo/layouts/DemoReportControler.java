/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * DemoReportControler.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Treatment.java,v 1.2 2005/01/25 01:13:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.layouts;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.DefaultReportControler;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * The DemoReportControler is a simple report controler implementation which
 * allows to modify two report properties from within the preview frame.
 *
 * @author Thomas Morgner
 */
public class DemoReportControler extends DefaultReportControler
{
  public static final String MESSAGE_ONE_FIELDNAME = "Message1";
  public static final String MESSAGE_TWO_FIELDNAME = "Message2";

  private class UpdateAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public UpdateAction ()
    {
      putValue(Action.NAME, "Update");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final PreviewProxyBase base = getPreviewBase();
      if (base == null)
      {
        return;
      }
      final JFreeReport report = base.getReport();
      report.setProperty(MESSAGE_ONE_FIELDNAME, messageOneField.getText());
      report.setProperty(MESSAGE_TWO_FIELDNAME, messageTwoField.getText());
      try
      {
        base.refresh();
      }
      catch(Exception ex)
      {
        Log.error ("Unable to refresh the report.", ex);
      }
    }
  }

  private JTextArea messageOneField;
  private JTextArea messageTwoField;
  private Action updateAction;

  public DemoReportControler ()
  {
    setLayout(new GridBagLayout());

    final JLabel messageOneLabel = new JLabel ("One:");
    final JLabel messageTwoLabel = new JLabel ("Two:");
    messageOneField = new JTextArea();
    messageOneField.setWrapStyleWord(true);
    messageOneField.setRows(10);
    messageTwoField = new JTextArea();
    messageTwoField.setRows(10);
    messageTwoField.setWrapStyleWord(true);
    updateAction = new UpdateAction();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    add (messageOneLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    add (messageTwoLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    add (new JScrollPane (messageOneField), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    add (new JScrollPane(messageTwoField), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    add(new ActionButton (updateAction));

    setEnabled(false);
    messageOneField.setEnabled(false);
    messageTwoField.setEnabled(false);
    updateAction.setEnabled(false);
  }

  /**
   * Sets whether or not this component is enabled. A component that is enabled may
   * respond to user input, while a component that is not enabled cannot respond to user
   * input.  Some components may alter their visual representation when they are disabled
   * in order to provide feedback to the user that they cannot take input. <p>Note:
   * Disabling a component does not disable it's children.
   * <p/>
   * <p>Note: Disabling a lightweight component does not prevent it from receiving
   * MouseEvents.
   *
   * @param enabled true if this component should be enabled, false otherwise
   * @see java.awt.Component#isEnabled
   * @see java.awt.Component#isLightweight
   */
  public void setEnabled (final boolean enabled)
  {
    super.setEnabled(enabled);
    messageOneField.setEnabled(enabled);
    messageTwoField.setEnabled(enabled);
    updateAction.setEnabled(enabled);
  }

  public void setPreviewBase(final PreviewProxyBase proxyBase)
  {
    super.setPreviewBase(proxyBase);
    final JFreeReport report = proxyBase.getReport();
    messageOneField.setText((String) report.getProperty("Message1"));
    messageTwoField.setText((String) report.getProperty("Message2"));
  }

}
