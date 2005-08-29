package org.jfree.report.demo.layouts;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.DefaultReportControler;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * Creation-Date: 28.08.2005, 18:39:25
 *
 * @author: Thomas Morgner
 */
public class DemoReportControler extends DefaultReportControler
{
  public static final String MESSAGE_ONE_FIELDNAME = "MessageOne";
  public static final String MESSAGE_TWO_FIELDNAME = "MessageTwo";

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
    messageOneField.setRows(10);
    messageTwoField = new JTextArea();
    messageTwoField.setRows(10);
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
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add (new JScrollPane (messageOneField), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
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

}
