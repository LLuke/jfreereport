/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * --------------------
 * ExceptionDialog.java
 * --------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExceptionDialog.java,v 1.12 2002/12/02 17:44:50 taqua Exp $
 *
 * Changes
 * -------
 * 30-May-2002 : Initial version
 * 09-Jun-2002 : Documentation
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */

package com.jrefinery.report.util;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The exception dialog is used to display an exception and the exceptions stacktrace to
 * the user.
 *
 * @author Thomas Morgner
 */
public class ExceptionDialog extends JDialog
{
  /** OK action. */
  private class OKAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public OKAction ()
    {
      putValue (NAME, UIManager.getDefaults ().getString ("OptionPane.okButtonText"));
    }

    /**
     * Receives notification that an action event has occurred.
     *
     * @param event  the action event.
     */
    public void actionPerformed (ActionEvent event)
    {
      setVisible (false);
    }
  }

  /**
   * Details action.
   */
  private class DetailsAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public DetailsAction ()
    {
      putValue (NAME, ">>");
    }

    /**
     * Receives notification that an action event has occurred.
     *
     * @param event  the action event.
     */
    public void actionPerformed (ActionEvent event)
    {
      scroller.setVisible (!(scroller.isVisible ()));
      if (scroller.isVisible ())
      {
        putValue (NAME, "<<");
      }
      else
      {
        putValue (NAME, ">>");
      }
      adjustSize ();
    }
  }

  /** A UI component for displaying the stack trace. */
  private JTextArea backtraceArea;

  /** A UI component for displaying the message. */
  private JLabel messageLabel;

  /** The exception. */
  private Exception currentEx;

  /** An action associated with the 'OK' button. */
  private OKAction okAction;

  /** An action associated with the 'Details' button. */
  private DetailsAction detailsAction;

  /** A scroll pane. */
  private JScrollPane scroller;

  /** A filler panel. */
  private JPanel filler;

  /** The default dialog. */
  private static ExceptionDialog defaultDialog;

  /**
   * Creates a new ExceptionDialog.
   */
  public ExceptionDialog ()
  {
    setModal (true);
    messageLabel = new JLabel ();
    backtraceArea = new JTextArea ();

    scroller = new JScrollPane (backtraceArea);
    scroller.setVisible (false);

    JPanel detailPane = new JPanel ();
    detailPane.setLayout (new GridBagLayout ());
    GridBagConstraints gbc = new GridBagConstraints ();
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.gridx = 0;
    gbc.gridy = 0;
    JLabel icon = new JLabel (UIManager.getDefaults ().getIcon ("OptionPane.errorIcon"));
    icon.setBorder (BorderFactory.createEmptyBorder (10, 10, 10, 10));
    detailPane.add (icon, gbc);

    gbc = new GridBagConstraints ();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    detailPane.add (messageLabel);

    gbc = new GridBagConstraints ();
    gbc.anchor = GridBagConstraints.SOUTH;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    detailPane.add (createButtonPane (), gbc);

    filler = new JPanel ();
    filler.setPreferredSize (new Dimension (0, 0));
    filler.setBackground (Color.green);
    gbc = new GridBagConstraints ();
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 5;
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    detailPane.add (filler, gbc);

    gbc = new GridBagConstraints ();
    gbc.anchor = GridBagConstraints.SOUTHWEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    gbc.weighty = 5;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    detailPane.add (scroller, gbc);

    setContentPane (detailPane);
  }

  /**
   * Adjusts the size of the dialog to fit the with of the contained message and stacktrace.
   */
  public void adjustSize ()
  {
    Dimension scSize = scroller.getPreferredSize ();
    Dimension cbase = filler.getPreferredSize ();
    cbase.width = Math.max (scSize.width, cbase.width);
    cbase.height = 0;
    filler.setMinimumSize (cbase);
    pack ();

  }

  /**
   * Initializes the buttonpane.
   *
   * @return a panel containing the 'OK' and 'Details' buttons.
   */
  private JPanel createButtonPane ()
  {
    JPanel buttonPane = new JPanel ();
    buttonPane.setLayout (new FlowLayout (2));
    buttonPane.setBorder (BorderFactory.createEmptyBorder (2, 2, 2, 2));
    okAction = new OKAction ();
    detailsAction = new DetailsAction ();

    JButton ok = new ActionButton (okAction);
    JButton details = new ActionButton (detailsAction);

    FloatingButtonEnabler.getInstance ().addButton (ok);
    FloatingButtonEnabler.getInstance ().addButton (details);

    buttonPane.add (ok);
    buttonPane.add (details);
    return buttonPane;
  }

  /**
   * Sets the message for this exception dialog. The message is displayed on the main page.
   *
   * @param mesg  the message.
   */
  public void setMessage (String mesg)
  {
    messageLabel.setText (mesg);
  }

  /**
   * Returns the message for this exception dialog.   The message is displayed on the main page.
   *
   * @return the message.
   */
  public String getMessage ()
  {
    return messageLabel.getText ();
  }

  /**
   * Sets the exception for this dialog. If no exception is set, the "Detail" button is disabled
   * and the stacktrace text cleared. Else the stacktraces text is read into the detail message
   * area.
   *
   * @param e  the exception.
   */
  public void setException (Exception e)
  {
    currentEx = e;
    if (e == null)
    {
      detailsAction.setEnabled (false);
      backtraceArea.setText ("");
    }
    else
    {
      backtraceArea.setText (readFromException (e));
    }
  }

  /**
   * Reads the stacktrace text from the exception.
   *
   * @param e  the exception.
   *
   * @return the stack trace.
   */
  private String readFromException (Exception e)
  {
    String text = "No backtrace available";
    try
    {
      StringWriter writer = new StringWriter ();
      PrintWriter pwriter = new PrintWriter (writer);
      e.printStackTrace (pwriter);
      text = writer.toString ();
      writer.close ();
    }
    catch (Exception ex)
    {
      Log.info("ExceptionDialog: exception suppressed.");
    }
    return text;
  }

  /**
   * Returns the exception that was the reason for this dialog to show up.
   *
   * @return the exception.
   */
  public Exception getException ()
  {
    return currentEx;
  }

  /**
   * Shows an default dialog with the given message and title and the exceptions stacktrace
   * in the detail area.
   *
   * @param title  the title.
   * @param message  the message.
   * @param e  the exception.
   */
  public static void showExceptionDialog (String title, String message, Exception e)
  {
    if (defaultDialog == null)
    {
      defaultDialog = new ExceptionDialog ();
    }
    if (e != null)
    {
      Log.error("UserError" , e);
    }
    defaultDialog.setTitle (title);
    defaultDialog.setMessage (message);
    defaultDialog.setException (e);
    defaultDialog.adjustSize ();
    defaultDialog.setVisible (true);
  }

}