/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -----------------------
 * ReportConverterGUI.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConverterGUI.java,v 1.14 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FilesystemFilter;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.StringUtil;

/**
 * A utility application for converting XML report files from the old format to the
 * new format.
 *
 * @author Thomas Morgner.
 */
public class ReportConverterGUI extends JFrame
{
  /** The source field. */
  private JTextField sourceField;

  /** The target field. */
  private JTextField targetField;

  /** A file chooser. */
  private JFileChooser fileChooser;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  /**
   * An action for selecting the target.
   */
  private class SelectTargetAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectTargetAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectTarget.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.selectTarget.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setTargetFile(performSelectFile(getTargetFile(), true));
    }
  }

  /**
   * An action for selecting the source file.
   */
  private class SelectSourceAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectSourceAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectSource.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.selectSource.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSourceFile(performSelectFile(getSourceFile(), false));
    }
  }

  /**
   * An action for converting an XML report definition from the old format to the new.
   */
  private class ConvertAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ConvertAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.convert.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.convert.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      convert();
    }
  }

  /**
   * Default constructor.
   */
  public ReportConverterGUI()
  {
    sourceField = new JTextField();
    targetField = new JTextField();
    final JButton selectSourceButton = new ActionButton(new SelectSourceAction());
    final JButton selectTargetButton = new ActionButton(new SelectTargetAction());
    final JButton convertFilesButton = new ActionButton(new ConvertAction());

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.sourceFile")), gbc);

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
    contentPane.add(new JLabel(getResources().getString("convertdialog.targetFile")), gbc);

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
    fileChooser.addChoosableFileFilter(new FilesystemFilter(new String[]{".xml"},
        "XML-Report definitions", true));
    fileChooser.setMultiSelectionEnabled(false);

    setTitle(getResources().getString("convertdialog.title"));
  }

  /**
   * Starting point for the utility application.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    final ReportConverterGUI gui = new ReportConverterGUI();
    gui.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(final WindowEvent e)
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
   * @param filename  the file name.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performTargetValidate(final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.targetIsEmpty"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
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
      final String key1 = "convertdialog.targetOverwriteConfirmation";
      final String key2 = "convertdialog.targetOverwriteTitle";
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
   * @param filename  the file name.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performSourceValidate(final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.sourceIsEmpty"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
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
   * Retrieves the resources for the frame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return The resources.
   */
  private ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  /**
   * Sets the source file.
   *
   * @param file  the file name.
   */
  private void setSourceFile(final String file)
  {
    sourceField.setText(file);
  }

  /**
   * Sets the target file.
   *
   * @param file  the file name.
   */
  private void setTargetFile(final String file)
  {
    targetField.setText(file);
  }

  /**
   * Returns the source file name.
   *
   * @return The name.
   */
  private String getSourceFile()
  {
    return sourceField.getText();
  }

  /**
   * Returns the name of the target file.
   *
   * @return The name.
   */
  private String getTargetFile()
  {
    return targetField.getText();
  }

  /**
   * Performs the conversion, returning <code>true</code> if the conversion is successful, and
   * <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public boolean convert()
  {

    if (performSourceValidate(getSourceFile()) == false)
    {
      JOptionPane.showMessageDialog(this, "Validating the source file input failed",
          "Check the source file", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (performTargetValidate(getTargetFile()) == false)
    {
      JOptionPane.showMessageDialog(this, "Validating the source file input failed",
          "Check the source file", JOptionPane.WARNING_MESSAGE);
      return false;
    }

    final ReportConverter converter = new ReportConverter();
    try
    {
      Log.debug("Converting report ...");
      converter.convertReport(getSourceFile(), getTargetFile(), "UTF-16");
      return true;
    }
    catch (Exception e)
    {
      Log.error("Failed to convert.", e);
      ExceptionDialog.showExceptionDialog
          ("Failed to convert.", "Error while converting the reports.", e);
      return false;
    }
  }

  /**
   * Selects a file to use.
   *
   * @param filename  the current selection.
   * @param appendExt  append an extension?
   *
   * @return The file name.
   */
  protected String performSelectFile(final String filename, final boolean appendExt)
  {
    final File file = new File(filename);
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      if (appendExt)
      {
        if ((StringUtil.endsWithIgnoreCase(selFileName, ".xml") == false))
        {
          selFileName = selFileName + ".xml";
        }
      }
      return selFileName;
    }
    return filename;
  }

}
