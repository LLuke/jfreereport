/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------
 * ExcelExportDialog.java
 * ----------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG, based on PDFSaveDialog);
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited);
 * 
 * $Id: ExcelExportDialog.java,v 1.12 2003/06/13 22:54:00 taqua Exp $
 *
 * Changes
 * --------
 * 02-Jan-2003 : Initial version
 * 25-Feb-2003 : Added missing Javadocs (DG);
 * 
 */

package com.jrefinery.report.preview;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FilesystemFilter;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.StringUtil;

/**
 * A dialog that is used to perform the printing of a report into an Excel file.
 * <p>
 * The main method to call the dialog is {@link ExcelExportDialog#performExport}. Given a report
 * and a pageformat, the dialog is shown and if the user approved the dialog, the excel file 
 * is saved using the settings made in the dialog.
 *
 * @author Heiko Evermann
 */
public class ExcelExportDialog extends JDialog
{
  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionConfirm extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionConfirm()
    {
      putValue(Action.NAME, getResources().getString("excelexportdialog.confirm"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      if (performValidate())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  /**
   * Internal action class to cancel the report processing.
   */
  private class ActionCancel extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionCancel()
    {
      putValue(Action.NAME, getResources().getString("excelexportdialog.cancel"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      setConfirmed(false);
      setVisible(false);
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
    public ActionSelectFile()
    {
      putValue(Action.NAME, getResources().getString("excelexportdialog.selectFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      performSelectFile();
    }
  }

  /** Confirm action. */
  private Action actionConfirm;

  /** Cancel action. */
  private Action actionCancel;

  /** Select file action. */
  private Action actionSelectFile;

  /** Filename text field. */
  private JTextField txFilename;

  /** The strict layout check-box. */
  private JCheckBox cbStrictLayout;

  /** Confirmed flag. */
  private boolean confirmed;

  /** Confirm button. */
  private JButton btnConfirm;

  /** Cancel button. */
  private JButton btnCancel;

  /** Localised resources. */
  private ResourceBundle resources;

  /** A file chooser. */
  private JFileChooser fileChooser;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  /**
   * Creates a new Excel save dialog.
   *
   * @param owner  the dialog owner.
   */
  public ExcelExportDialog(Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel dialog.
   *
   * @param owner  the dialog owner.
   */
  public ExcelExportDialog(Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel save dialog.  The created dialog is modal.
   */
  public ExcelExportDialog()
  {
    initConstructor();
  }

  /**
   * Initialisation.
   */
  private void initConstructor()
  {
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    setTitle(resources.getString("excelexportdialog.dialogtitle"));
    initialize();
    clear();

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        getActionCancel().actionPerformed(null);
      }
    }
    );
  }

  /**
   * Returns the resource bundle used for that dialog.
   *
   * @return the resource bundle
   */
  public ResourceBundle getResources()
  {
    return resources;
  }

  /**
   * Returns a single instance of the file selection action.
   *
   * @return the action.
   */
  private Action getActionSelectFile()
  {
    if (actionSelectFile == null)
    {
      actionSelectFile = new ActionSelectFile();
    }
    return actionSelectFile;
  }

  /**
   * Returns a single instance of the dialog confirm action.
   *
   * @return the action.
   */
  private Action getActionConfirm()
  {
    if (actionConfirm == null)
    {
      actionConfirm = new ActionConfirm();
    }
    return actionConfirm;
  }

  /**
   * Returns a single instance of the dialog cancel action.
   *
   * @return the action.
   */
  private Action getActionCancel()
  {
    if (actionCancel == null)
    {
      actionCancel = new ActionCancel();
    }
    return actionCancel;
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize()
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    JLabel lblFileName = new JLabel(getResources().getString("excelexportdialog.filename"));
    JButton btnSelect = new ActionButton(getActionSelectFile());

    txFilename = new JTextField();
    cbStrictLayout = new JCheckBox(getResources().getString("excelexportdialog.strict-layout"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
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

    btnCancel = new ActionButton(getActionCancel());
    btnConfirm = new ActionButton(getActionConfirm());
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(getActionConfirm(), KeyStroke.getKeyStroke('\n'),
                                       JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(buttonPanel, gbc);

    setContentPane(contentPane);
  }

  /**
   * Returns the filename of the excel file.
   *
   * @return the name of the file where to save the excel file.
   */
  public String getFilename()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the excel file.
   *
   * @param filename the filename of the excel file
   */
  public void setFilename(String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Gets the confirmation state of the dialog. A confirmed dialog has no invalid
   * settings and the user confirmed any resource conflicts.
   *
   * @return true, if the dialog has been confirmed and the excel file should be saved, 
   * false otherwise.
   */
  public boolean isConfirmed()
  {
    return confirmed;
  }

  /**
   * Defines whether this dialog has been finished using the 'OK' or the 'Cancel' option.
   *
   * @param confirmed set to true, if OK was pressed, false otherwise
   */
  protected void setConfirmed(boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  /**
   * Returns the setting of the 'strict layout' check-box.
   * 
   * @return A boolean.
   */
  public boolean isStrictLayout()
  {
    return cbStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict-layout' check-box. 
   * 
   * @param strictLayout  the new setting.
   */
  public void setStrictLayout(boolean strictLayout)
  {
    cbStrictLayout.setSelected(strictLayout);
  }

  /**
   * Clears all selections and input fields.
   */
  public void clear()
  {
    txFilename.setText("");
    cbStrictLayout.setSelected(false);
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      FilesystemFilter filter = new FilesystemFilter("Excel Documents", ".xls");
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    File file = new File(getFilename());
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile();
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
   * Validates the contents of the dialog's input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidate()
  {
    String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("excelexportdialog.targetIsEmpty"),
                                    getResources().getString("excelexportdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("excelexportdialog.targetIsNoFile"),
                                      getResources().getString("excelexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString(
                                          "excelexportdialog.targetIsNotWritable"),
                                      getResources().getString("excelexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "excelexportdialog.targetOverwriteConfirmation";
      String key2 = "excelexportdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{getFilename()}
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
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * Excel file.
   *
   * @param report  the report being processed.
   *
   * @return true or false.
   */
  public boolean performExport(JFreeReport report)
  {
    initFromConfiguration(report.getReportConfiguration());
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }
    return writeExcel(report);
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public boolean writeExcel(JFreeReport report)
  {
    OutputStream out = null;
    try
    {

      out = new BufferedOutputStream(new FileOutputStream(new File(getFilename())));
      ExcelProcessor target = new ExcelProcessor(report);
      target.setStrictLayout(isStrictLayout());
      target.setOutputStream(out);
      target.processReport();
      out.close();
      return true;
    }
    catch (Exception re)
    {
      showExceptionDialog("error.processingfailed", re);
      return false;
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        showExceptionDialog("error.savefailed", e);
      }
    }
  }


  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
   *
   * @param localisationBase  the resource key prefix.
   * @param e  the exception.
   */
  private void showExceptionDialog(String localisationBase, Exception e)
  {
    ExceptionDialog.showExceptionDialog(
        getResources().getString(localisationBase + ".title"),
        MessageFormat.format(
            getResources().getString(localisationBase + ".message"),
            new Object[]{e.getLocalizedMessage()}
        ),
        e);
  }

  /**
   * Initialises the Excel export dialog from the settings in the report configuration.
   *
   * @param config  the report configuration.
   */
  public void initFromConfiguration(ReportConfiguration config)
  {
    // nothing to initialize so far. We have much less options than in "save to PDF"
  }


  /**
   * For debugging.
   * 
   * @param args  ignored.
   */
  public static void main (String [] args)
  {
    JDialog d = new ExcelExportDialog();
    d.pack();
    d.addWindowListener(new WindowAdapter(){
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
    d.setVisible(true);
  }

}
