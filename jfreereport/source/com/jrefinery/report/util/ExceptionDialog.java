/**
 *
 *  Date: 30.05.2002
 *  ExceptionDialog.java
 *  ------------------------------
 *  30.05.2002 : ...
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

public class ExceptionDialog extends JDialog
{
  private class OKAction extends AbstractAction
  {
    public OKAction ()
    {
      putValue (NAME, UIManager.getDefaults().getString("OptionPane.okButtonText"));
    }

    public void actionPerformed (ActionEvent event)
    {
      setVisible (false);
    }
  }

  private class DetailsAction extends AbstractAction
  {
    public DetailsAction ()
    {
      putValue (NAME, UIManager.getDefaults().getString("Details"));
    }

    public void actionPerformed (ActionEvent event)
    {
      scroller.setVisible (!(scroller.isVisible ()));
      adjustSize ();
    }
  }

  private JTextArea backtraceArea;
  private JLabel messageLabel;
  private Exception currentEx;
  private OKAction okAction;
  private DetailsAction detailsAction;
  private JScrollPane scroller;
  private JPanel filler;
  private static ExceptionDialog defaultDialog;

  public ExceptionDialog ()
  {
    setModal (true);
    messageLabel = new JLabel ();
    backtraceArea = new JTextArea ();

    scroller = new JScrollPane (backtraceArea);
    scroller.setVisible (false);

    JPanel detailPane = new JPanel ();
    detailPane.setLayout (new GridBagLayout ());
    detailPane.setBorder (BorderFactory.createEtchedBorder ());
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

  public void adjustSize ()
  {
    Dimension scSize = scroller.getPreferredSize ();
    Dimension cbase = filler.getPreferredSize ();
    cbase.width = Math.max (scSize.width, cbase.width);
    cbase.height = 0;
    filler.setMinimumSize (cbase);
    pack ();

  }

  private JPanel createButtonPane ()
  {
    JPanel buttonPane = new JPanel ();
    buttonPane.setLayout (new FlowLayout (2));
    buttonPane.setBorder (BorderFactory.createEmptyBorder (2, 2, 2, 2));
    okAction = new OKAction ();
    detailsAction = new DetailsAction ();

    buttonPane.add (new JButton (okAction));
    buttonPane.add (new JButton (detailsAction));
    return buttonPane;
  }

  public void setMessage (String mesg)
  {
    messageLabel.setText (mesg);
  }

  public String getMessage ()
  {
    return messageLabel.getText ();
  }

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
    }
    return text;
  }

  public Exception getException ()
  {
    return currentEx;
  }

  public static void showExceptionDialog (String title, String message, Exception e)
  {
    if (defaultDialog == null)
    {
      defaultDialog = new ExceptionDialog ();
    }
    defaultDialog.setTitle(title);
    defaultDialog.setMessage (message);
    defaultDialog.setException (e);
    defaultDialog.adjustSize ();
    defaultDialog.setVisible (true);
  }

  public static void main (String[] args)
  {
    try
    {
      showExceptionDialog ("Exception", "Message", new NullPointerException());
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
    System.exit (0);
  }
}