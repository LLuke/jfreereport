/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------------
 * ExcelExportDialog.java
 * ----------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG, based on PDFSaveDialog);
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelExportDialog.java,v 1.14 2005/03/01 10:09:39 taqua Exp $
 *
 * Changes
 * --------
 * 02-Jan-2003 : Initial version
 * 25-Feb-2003 : Added missing Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.xls;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.AbstractExportDialog;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.xls.ExcelProcessor;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.FilesystemFilter;
import org.jfree.ui.action.ActionButton;

/**
 * A dialog that is used to prepare the printing of a report into an Excel file.
 * <p/>
 * The main method to call the dialog is {@link org.jfree.report.modules.gui.xls.ExcelExportDialog#performQueryForExport(JFreeReport)}.
 * Given a report, the dialog is shown and if the user approved the dialog, the excel file
 * is saved using the settings made in the dialog.
 *
 * @author Heiko Evermann
 */
public class ExcelExportDialog extends AbstractExportDialog
{
  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ConfirmAction extends AbstractConfirmAction
  {
    /**
     * Default constructor.
     */
    public ConfirmAction (ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("excelexportdialog.confirm"));
    }
  }

  /**
   * Internal action class to cancel the report processing.
   */
  private class CancelAction extends AbstractCancelAction
  {
    /**
     * Default constructor.
     */
    public CancelAction (ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("excelexportdialog.cancel"));
    }
  }

  /**
   * Internal action class to select a target file.
   */
  private class ActionSelectFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectFile (ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("excelexportdialog.selectFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      performSelectFile();
    }
  }

  /**
   * Select file action.
   */
  private Action actionSelectFile;

  /**
   * Filename text field.
   */
  private JTextField txFilename;

  /**
   * The strict layout check-box.
   */
  private JCheckBox cbStrictLayout;

  /**
   * A file chooser.
   */
  private JFileChooser fileChooser;

  /**
   * Creates a new Excel save dialog.
   *
   * @param owner the dialog owner.
   */
  public ExcelExportDialog (final Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel dialog.
   *
   * @param owner the dialog owner.
   */
  public ExcelExportDialog (final Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel save dialog.  The created dialog is modal.
   */
  public ExcelExportDialog ()
  {
    initConstructor();
  }

  protected String getConfigurationSuffix ()
  {
    return "_xlsexport";
  }

  /**
   * Initialisation.
   */
  private void initConstructor ()
  {
    actionSelectFile = new ActionSelectFile(getResources());

    setCancelAction(new CancelAction(getResources()));
    setConfirmAction(new ConfirmAction(getResources()));

    setTitle(getResources().getString("excelexportdialog.dialogtitle"));
    initialize();
    clear();
  }

  /**
   * Returns a single instance of the file selection action.
   *
   * @return the action.
   */
  private Action getActionSelectFile ()
  {
    return actionSelectFile;
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize ()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblFileName = new JLabel(getResources().getString("excelexportdialog.filename"));
    final JButton btnSelect = new ActionButton(getActionSelectFile());

    txFilename = new JTextField();
    cbStrictLayout = new JCheckBox(getResources().getString("excelexportdialog.strict-layout"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 5);
    contentPane.add(lblFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbStrictLayout, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    contentPane.add(btnSelect, gbc);

    final ActionButton btnCancel = new ActionButton(getCancelAction());
    final ActionButton btnConfirm = new ActionButton(getConfirmAction());
    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1,2,5,5));
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    getRootPane().setDefaultButton(btnConfirm);

    buttonPanel.registerKeyboardAction(getConfirmAction(), KeyStroke.getKeyStroke('\n'),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 10, 0);
    contentPane.add(buttonPanel, gbc);


    final JPanel contentWithStatus = new JPanel();
    contentWithStatus.setLayout(new BorderLayout());
    contentWithStatus.add(contentPane, BorderLayout.CENTER);
    contentWithStatus.add(getStatusBar(), BorderLayout.SOUTH);

    setContentPane(contentWithStatus);

    getFormValidator().registerButton(cbStrictLayout);
    getFormValidator().registerTextField(txFilename);
  }

  /**
   * Returns the filename of the excel file.
   *
   * @return the name of the file where to save the excel file.
   */
  public String getFilename ()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the excel file.
   *
   * @param filename the filename of the excel file
   */
  public void setFilename (final String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Returns the setting of the 'strict layout' check-box.
   *
   * @return A boolean.
   */
  public boolean isStrictLayout ()
  {
    return cbStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict-layout' check-box.
   *
   * @param strictLayout the new setting.
   */
  public void setStrictLayout (final boolean strictLayout)
  {
    cbStrictLayout.setSelected(strictLayout);
  }

  /**
   * Clears all selections and input fields.
   */
  public void clear ()
  {
    txFilename.setText("");
    cbStrictLayout.setSelected(false);
  }

  /**
   * Returns the user input of this dialog as properties collection.
   *
   * @return the user input.
   */
  public Properties getDialogContents ()
  {
    final Properties p = new Properties();
    p.setProperty("filename", getFilename());
    p.setProperty("strict-layout", String.valueOf(isStrictLayout()));
    return p;
  }

  /**
   * Restores the user input from a properties collection.
   *
   * @param p the user input.
   */
  public void setDialogContents (final Properties p)
  {
    setFilename(p.getProperty("filename", getFilename()));
    setStrictLayout(StringUtil.parseBoolean(p.getProperty("strict-layout"), isStrictLayout()));
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile ()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter(".xls", "Excel Documents");
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final File file = new File(getFilename());
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtil.endsWithIgnoreCase(selFileName, ".xls") == false)
      {
        selFileName = selFileName + ".xls";
      }
      setFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it
   * is also checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidate ()
  {
    getStatusBar().clear();

    final String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
              getResources().getString("excelexportdialog.targetIsEmpty"));
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("excelexportdialog.targetIsNoFile"));
        return false;
      }
      if (f.canWrite() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("excelexportdialog.targetIsNotWritable"));
        return false;
      }
      final String message = MessageFormat.format(getResources().getString
              ("excelexportdialog.targetExistsWarning"),
              new Object[]{filename});
      getStatusBar().setStatus(JStatusBar.TYPE_WARNING,
              getResources().getString(message));
    }
    return true;
  }

  protected boolean performConfirm ()
  {
    final String key1 = "excelexportdialog.targetOverwriteConfirmation";
    final String key2 = "excelexportdialog.targetOverwriteTitle";
    if (JOptionPane.showConfirmDialog(this,
            MessageFormat.format(getResources().getString(key1),
                    new Object[]{getFilename()}),
            getResources().getString(key2),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
            == JOptionPane.NO_OPTION)
    {
      return false;
    }
    return true;
  }

  /**
   * Initialises the Excel export dialog from the settings in the report configuration.
   *
   * @param config the report configuration.
   */
  public void initFromConfiguration (final ReportConfiguration config)
  {
    final String strict = config.getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX +
            TableProcessor.STRICT_LAYOUT,
                    config.getConfigProperty(TableProcessor.STRICT_LAYOUT,
                            TableProcessor.STRICT_LAYOUT_DEFAULT));
    setStrictLayout(strict.equals("true"));
  }

  /**
   * Stores the input from the dialog into the report configuration of the report.
   *
   * @param config the report configuration that should receive the new settings.
   */
  public void storeToConfiguration (final ReportConfiguration config)
  {
    config.setConfigProperty(ExcelProcessor.CONFIGURATION_PREFIX +
        TableProcessor.STRICT_LAYOUT, String.valueOf(isStrictLayout()));
  }

  protected String getResourceBaseName ()
  {
    return ExcelExportPlugin.BASE_RESOURCE_CLASS;
  }


  public static void main (final String[] args)
  {
    final ExcelExportDialog dialog = new ExcelExportDialog();
    dialog.setModal(true);
    dialog.pack();
    dialog.performQueryForExport(new JFreeReport());
    System.exit(0);
  }
}
