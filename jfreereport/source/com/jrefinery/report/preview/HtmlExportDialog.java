
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
 * ------------------
 * ExcelExportDialog.java
 * ------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG);
                     based on PDFSaveDialog by Thomas Morgner, David Gilbert (for Simba Management Limited) and contributors
 * Contributor(s):
 *
 * $Id: ExcelExportDialog.java,v 1.1 2003/01/14 21:10:08 taqua Exp $
 *
 * Changes
 * --------
 * 02-Jan-2002 : Initial version
 */

package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.table.excel.ExcelProcessor;
import com.jrefinery.report.targets.table.html.HtmlProcessor;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.ui.ExtensionFileFilter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * The ExcelExportDialog is used to perform the printing of a report into an Excel file.
 * report.
 * <p>
 * The main method to call the dialog is ExcelExportDialog.exportToExcel(). Given a report and a pageformat,
 * the dialog is shown and if the user approved the dialog, the excel file  is saved using the settings
 * made in the dialog.
 *
 * @author Heiko Evermann
 */
public class HtmlExportDialog extends JDialog
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

  /** Author text field. */
  private JTextField txAuthor;

  /** Title text field. */
  private JTextField txTitle;

  /** Confirmed flag. */
  private boolean confirmed;

  /** Confirm button. */
  private JButton btnConfirm;

  /** Cancel button. */
  private JButton btnCancel;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";


  /**
   * Creates a new Excel save dialog.
   *
   * @param owner  the dialog owner.
   */
  public HtmlExportDialog(Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new PDF Excel dialog.
   *
   * @param owner  the dialog owner.
   */
  public HtmlExportDialog(Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel save dialog.  The created dialog is modal.
   */
  public HtmlExportDialog()
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
    setTitle(getResources().getString("excelexportdialog.dialogtitle"));
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

    JLabel lblFileName = new JLabel(getResources().getString("htmlexportdialog.filename"));
    JLabel lblAuthor = new JLabel(getResources().getString("htmlexportdialog.author"));
    JLabel lblTitel = new JLabel(getResources().getString("htmlexportdialog.title"));
    JButton btnSelect = new ActionButton(getActionSelectFile());

    txAuthor = new JTextField();
    txFilename = new JTextField();
    txTitle = new JTextField();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(lblFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblTitel, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblAuthor, gbc);

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
    contentPane.add(txTitle, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(txAuthor, gbc);

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
   * Returns the filename of the exceö file.
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
   * Returns the title of the excel file.
   *
   * @return the title
   */
  public String getExcelTitle()
  {
    return txTitle.getText();
  }

  /**
   * Defines the title of the excel file.
   *
   * @param title the title
   */
  public void setExcelTitle(String title)
  {
    this.txTitle.setText(title);
  }

  /**
   * @return the name of the author of this report.
   */
  public String getAuthor()
  {
    return txAuthor.getText();
  }

  /**
   * Defines the Author of the report. Any freeform text is valid. This defaults to the value of
   * the systemProperty "user.name".
   *
   * @param author the name of the author.
   */
  public void setAuthor(String author)
  {
    this.txAuthor.setText(author);
  }

  /**
   * @return true, if the dialog has been confirmed and the excel file should be saved, false otherwise.
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
   * clears all selections, input fields and set the selected encryption level to none.
   */
  public void clear()
  {
    txAuthor.setText(System.getProperty("user.name"));
    txFilename.setText("");
    txTitle.setText("");
  }

  /**
   * selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    JFileChooser fileChooser = new JFileChooser();
    ExtensionFileFilter filter = new ExtensionFileFilter("Html Documents", ".html");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setSelectedFile(new File(getFilename()));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (selFileName.toUpperCase().endsWith(".HTML") == false)
      {
        selFileName = selFileName + ".html";
      }
      setFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
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
                                    getResources().getString("htmlexportdialog.targetIsEmpty"),
                                    getResources().getString("htmlexportdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("htmlexportdialog.targetIsNoFile"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("htmlexportdialog.targetIsNotWritable"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "htmlexportdialog.targetOverwriteConfirmation";
      String key2 = "htmlexportdialog.targetOverwriteTitle";
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
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into a PDF-File.
   *
   * @param report  the report being processed.
   *
   * @return true or false.
   */
  public boolean exportToHtml(JFreeReport report)
  {
    initFromConfiguration(report.getReportConfiguration());
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }
    return writeHtml(report);
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public boolean writeHtml(JFreeReport report)
  {
    OutputStream out = null;
    try
    {

      out = new BufferedOutputStream(new FileOutputStream(new File(getFilename())));
      HtmlProcessor target = new HtmlProcessor(report);
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
        out.close();
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
}
