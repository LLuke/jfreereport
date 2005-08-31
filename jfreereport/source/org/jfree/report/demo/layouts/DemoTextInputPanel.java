package org.jfree.report.demo.layouts;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Creation-Date: 28.08.2005, 21:20:11
 *
 * @author: Thomas Morgner
 */
public class DemoTextInputPanel extends JPanel
{
  private JTextArea messageOneField;
  private JTextArea messageTwoField;

  public DemoTextInputPanel()
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
    add (new JScrollPane(messageOneField), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    add (new JScrollPane(messageTwoField), gbc);
  }

  public String getMessageOne ()
  {
    return messageOneField.getText();
  }

  public String getMessageTwo ()
  {
    return messageTwoField.getText();
  }
}
