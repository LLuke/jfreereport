/**
 * Date: Jan 30, 2003
 * Time: 9:44:28 PM
 *
 * $Id: ReportConverterGUI.java,v 1.1 2003/01/30 22:58:28 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.FilesystemFilter;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.StringUtil;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class ReportConverterGUI extends JFrame
{
  private JTextField sourceField;
  private JTextField targetField;
  private JFileChooser fileChooser;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  private class SelectTargetAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectTargetAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectTarget.name"));
      putValue(Action.SHORT_DESCRIPTION, getResources().getString("convertdialog.action.selectTarget.description"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      setTargetFile(performSelectFile(getTargetFile(), true));
    }
  }

  private class SelectSourceAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectSourceAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectSource.name"));
      putValue(Action.SHORT_DESCRIPTION, getResources().getString("convertdialog.action.selectSource.description"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      setSourceFile(performSelectFile(getSourceFile(), false));
    }
  }

  private class ConvertAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ConvertAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.convert.name"));
      putValue(Action.SHORT_DESCRIPTION, getResources().getString("convertdialog.action.convert.description"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      convert();
    }
  }

  /**
   * Constructs a new frame that is initially invisible.
   * <p>
   * This constructor sets the component's locale property to the value
   * returned by <code>JComponent.getDefaultLocale</code>.
   *
   * @see Component#setSize
   * @see Component#setVisible
   */
  public ReportConverterGUI()
  {
    sourceField = new JTextField();
    targetField = new JTextField();
    JButton selectSourceButton = new ActionButton(new SelectSourceAction());
    JButton selectTargetButton = new ActionButton(new SelectTargetAction());
    JButton convertFilesButton = new ActionButton(new ConvertAction());

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel (getResources().getString("convertdialog.sourceFile")), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(sourceField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(selectSourceButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel (getResources().getString("convertdialog.targetFile")), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(targetField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(selectTargetButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(convertFilesButton, gbc);

    setContentPane(contentPane);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FilesystemFilter(new String[]{".xml"}, "XML-Report definitions", true));
    fileChooser.setMultiSelectionEnabled(false);

    setTitle(getResources().getString("convertdialog.title"));
  }

  public static void main (String [] args)
  {
    ReportConverterGUI gui = new ReportConverterGUI();
    gui.addWindowListener(new WindowAdapter(){
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
    gui.pack();
    gui.setVisible(true);
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performTargetValidate(String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("convertdialog.targetIsEmpty"),
                                    getResources().getString("convertdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("convertdialog.targetIsNoFile"),
                                      getResources().getString("convertdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("convertdialog.targetIsNotWritable"),
                                      getResources().getString("convertdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "convertdialog.targetOverwriteConfirmation";
      String key2 = "convertdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{filename}
                                        ),
                                        getResources().getString(key2),
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                                        == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performSourceValidate(String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("convertdialog.sourceIsEmpty"),
                                    getResources().getString("convertdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
    if (f.exists() == false)
    {
      return false;
    }

    if (f.isFile() == false)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("convertdialog.sourceIsNoFile"),
                                    getResources().getString("convertdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (f.canRead() == false)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("convertdialog.sourceIsNotReadable"),
                                    getResources().getString("convertdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  private ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  private void setSourceFile (String file)
  {
    sourceField.setText(file);
  }

  private void setTargetFile (String file)
  {
    targetField.setText(file);
  }

  private String getSourceFile ()
  {
    return sourceField.getText();
  }

  private String getTargetFile ()
  {
    return targetField.getText();
  }

  public boolean convert ()
  {
    if (performSourceValidate(getSourceFile()) &&
        performTargetValidate(getTargetFile()))
    {
      ReportConverter converter = new ReportConverter();
      try
      {
        converter.convertReport(getSourceFile(), getTargetFile());
        return true;
      }
      catch (Exception e)
      {
        Log.error ("Failed to convert.", e);
        return false;
      }
    }
    return false;
  }


  /**
   * selects a file to use as target for the report processing.
   */
  protected String performSelectFile(String filename, boolean appendExt)
  {
    File file = new File(filename);
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    Log.debug ("JFileChooser: " + fileChooser.getCurrentDirectory());
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      if (appendExt)
      {
        if ((StringUtil.endsWithIgnoreCase(selFileName,".xml") == false))
        {
          selFileName = selFileName + ".xml";
        }
      }
      return selFileName;
    }
    return filename;
  }

}
