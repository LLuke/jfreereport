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
 * --------------------
 * CSVExportDialog.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportDialog.java,v 1.2 2003/08/18 18:27:59 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.csv;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.gui.base.components.LengthLimitingDocument;
import org.jfree.report.modules.gui.csv.resources.CSVExportResources;
import org.jfree.report.modules.output.csv.CSVProcessor;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.ExtensionFileFilter;

/**
 * A dialog for exporting a report to CSV format.
 *
 * @author Thomas Morgner.
 */
public class CSVExportDialog extends JDialog
{
  /** The 'CSV encoding' property key. */
  public static final String CSV_OUTPUT_ENCODING
      = "org.jfree.report.modules.gui.csv.Encoding";
  /** A default value of the 'CSV encoding' property key. */
  public static final String CSV_OUTPUT_ENCODING_DEFAULT = 
    ReportConfiguration.getPlatformDefaultEncoding();


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
      putValue(Action.NAME, getResources().getString("csvexportdialog.confirm"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      if (performValidate())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionSelectSeparator extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectSeparator()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performSeparatorSelection();
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
      putValue(Action.NAME, getResources().getString("csvexportdialog.cancel"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
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
      putValue(Action.NAME, getResources().getString("csvexportdialog.selectFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performSelectFile();
    }
  }

  /** Filename text field. */
  private JTextField txFilename;

  /** The encoding combo-box. */
  private JComboBox cbEncoding;

  /** The encoding model. */
  private EncodingComboBoxModel encodingModel;

  /** The strict layout check-box. */
  private JCheckBox cbxStrictLayout;

  /** A radio button for tab separators. */
  private JRadioButton rbSeparatorTab;

  /** A radio button for colon separators. */
  private JRadioButton rbSeparatorColon;

  /** A radio button for semi-colon separators. */
  private JRadioButton rbSeparatorSemicolon;

  /** A radio button for other separators. */
  private JRadioButton rbSeparatorOther;

  /** A text field for the 'other' separator. */
  private JTextField txSeparatorOther;

  /** A radio button for exporting data only. */
  private JRadioButton rbExportData;

  /** A radio button for exporting all printed elements. */
  private JRadioButton rbExportPrintedElements;

  /** A file chooser. */
  private JFileChooser fileChooser;

  /** Confirmed flag. */
  private boolean confirmed;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      CSVExportResources.class.getName();

  /**
   * Creates a new CSV export dialog.
   *
   * @param owner  the dialog owner.
   */
  public CSVExportDialog(final Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new CSV export dialog.
   *
   * @param owner  the dialog owner.
   */
  public CSVExportDialog(final Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new CSV export dialog.  The created dialog is modal.
   */
  public CSVExportDialog()
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
    setTitle(getResources().getString("csvexportdialog.dialogtitle"));
    initialize();
    clear();

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(final WindowEvent e)
      {
        new ActionCancel().actionPerformed(null);
      }
    }
    );
  }

  /**
   * Retrieves the resources for this dialog. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  protected ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblFileName = new JLabel(getResources().getString("csvexportdialog.filename"));
    final JLabel lblEncoding = new JLabel(getResources().getString("csvexportdialog.encoding"));
    final JButton btnSelect = new ActionButton(new ActionSelectFile());
    cbxStrictLayout = new JCheckBox(getResources().getString("csvexportdialog.strict-layout"));

    txFilename = new JTextField();
    encodingModel = EncodingComboBoxModel.createDefaultModel();
    encodingModel.sort();
    cbEncoding = new JComboBox(encodingModel);

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
    contentPane.add(lblEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbxStrictLayout, gbc);

    final JPanel p = new JPanel(new GridLayout());
    p.add(createSeparatorPanel());
    p.add(createExportTypePanel());

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(10, 1, 1, 1);
    contentPane.add(p, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    contentPane.add(btnSelect, gbc);

    // button panel
    final JButton btnCancel = new ActionButton(new ActionCancel());
    final JButton btnConfirm = new ActionButton(new ActionConfirm());
    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(new ActionConfirm(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(buttonPanel, gbc);
    setContentPane(contentPane);
  }

  /**
   * Creates a panel for the export type.
   *
   * @return The panel.
   */
  private JPanel createExportTypePanel()
  {
    // separator panel
    final JPanel exportTypePanel = new JPanel();
    exportTypePanel.setLayout(new GridBagLayout());

    final TitledBorder tb = 
      new TitledBorder(getResources().getString("csvexportdialog.exporttype"));
    exportTypePanel.setBorder(tb);

    rbExportData = new JRadioButton(getResources().getString("csvexportdialog.export.data"));
    rbExportPrintedElements = new JRadioButton(
        getResources().getString("csvexportdialog.export.printed_elements"));

    final ButtonGroup btg = new ButtonGroup();
    btg.add(rbExportData);
    btg.add(rbExportPrintedElements);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    exportTypePanel.add(rbExportData, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    exportTypePanel.add(rbExportPrintedElements, gbc);

    return exportTypePanel;
  }

  /**
   * Creates a separator panel.
   *
   * @return The panel.
   */
  private JPanel createSeparatorPanel()
  {
    // separator panel
    final JPanel separatorPanel = new JPanel();
    separatorPanel.setLayout(new GridBagLayout());

    final TitledBorder tb = 
      new TitledBorder(getResources().getString("csvexportdialog.separatorchar"));
    separatorPanel.setBorder(tb);

    rbSeparatorTab = new JRadioButton(getResources().getString("csvexportdialog.separator.tab"));
    rbSeparatorColon = new JRadioButton(
        getResources().getString("csvexportdialog.separator.colon"));
    rbSeparatorSemicolon = new JRadioButton(
        getResources().getString("csvexportdialog.separator.semicolon"));
    rbSeparatorOther = new JRadioButton(
        getResources().getString("csvexportdialog.separator.other"));

    final ButtonGroup btg = new ButtonGroup();
    btg.add(rbSeparatorTab);
    btg.add(rbSeparatorColon);
    btg.add(rbSeparatorSemicolon);
    btg.add(rbSeparatorOther);

    final Action selectAction = new ActionSelectSeparator();
    rbSeparatorTab.addActionListener(selectAction);
    rbSeparatorColon.addActionListener(selectAction);
    rbSeparatorSemicolon.addActionListener(selectAction);
    rbSeparatorOther.addActionListener(selectAction);

    final LengthLimitingDocument ldoc = new LengthLimitingDocument(1);
    txSeparatorOther = new JTextField();
    txSeparatorOther.setDocument(ldoc);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorTab, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorColon, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorSemicolon, gbc);

    final JPanel otherPanel = new JPanel();
    otherPanel.setLayout(new GridBagLayout());

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    otherPanel.add(rbSeparatorOther, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    otherPanel.add(txSeparatorOther, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(otherPanel, gbc);

    return separatorPanel;
  }

  /**
   * Returns the export file name.
   *
   * @return The file name.
   */
  public String getFilename()
  {
    return txFilename.getText();
  }

  /**
   * Sets the export file name.
   *
   * @param filename  the file name.
   */
  public void setFilename(final String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Returns <code>true</code> if the user confirmed the selection, and <code>false</code>
   * otherwise.  The file should only be saved if the result is <code>true</code>.
   *
   * @return  A boolean.
   */
  public boolean isConfirmed()
  {
    return confirmed;
  }

  /**
   * Defines whether this dialog has been finished using the 'OK' or the 'Cancel' option.
   *
   * @param confirmed set to <code>true</code>, if OK was pressed, <code>false</code> otherwise
   */
  protected void setConfirmed(final boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  /**
   * Clears all selections, input fields and sets the selected encryption level to none.
   */
  public void clear()
  {
    txFilename.setText("");
    cbEncoding.setSelectedIndex(
        encodingModel.indexOf(System.getProperty("file.encoding", "Cp1251")));
    rbExportPrintedElements.setSelected(true);
    rbSeparatorColon.setSelected(true);
    cbxStrictLayout.setSelected(false);
    performSeparatorSelection();
  }

  /**
   * Returns the separator string, which is controlled by the selection of radio buttons.
   *
   * @return The separator string.
   */
  public String getSeparatorString()
  {
    if (rbSeparatorColon.isSelected())
    {
      return ",";
    }
    if (rbSeparatorSemicolon.isSelected())
    {
      return ";";
    }
    if (rbSeparatorTab.isSelected())
    {
      return "\t";
    }
    if (rbSeparatorOther.isSelected())
    {
      return txSeparatorOther.getText();
    }
    return "";
  }

  /**
   * Sets the separator string.
   *
   * @param s  the separator.
   */
  public void setSeparatorString(final String s)
  {
    if (s == null)
    {
      rbSeparatorOther.setSelected(true);
      txSeparatorOther.setText("");
    }
    else if (s.equals(","))
    {
      rbSeparatorColon.setSelected(true);
    }
    else if (s.equals(";"))
    {
      rbSeparatorSemicolon.setSelected(true);
    }
    else if (s.equals("\t"))
    {
      rbSeparatorTab.setSelected(true);
    }
    else
    {
      rbSeparatorOther.setSelected(true);
      txSeparatorOther.setText(s);
    }
    performSeparatorSelection();
  }

  /**
   * Returns the encoding.
   *
   * @return The encoding.
   */
  public String getEncoding()
  {
    if (cbEncoding.getSelectedIndex() == -1)
    {
      return System.getProperty("file.encoding");
    }
    else
    {
      return encodingModel.getEncoding(cbEncoding.getSelectedIndex());
    }
  }

  /**
   * Sets the encoding.
   *
   * @param encoding  the encoding.
   */
  public void setEncoding(final String encoding)
  {
    cbEncoding.setSelectedIndex(encodingModel.indexOf(encoding));
  }

  /**
   * Returns <code>true</code> if the user selected to export raw data only, and <code>false</code>
   * otherwise.
   *
   * @return A boolean.
   */
  public boolean isExportRawData()
  {
    return rbExportData.isSelected();
  }

  /**
   * Sets a flag that controls whether raw data is exported.
   *
   * @param b  the new flag value.
   */
  public void setExportRawData(final boolean b)
  {
    if (b == true)
    {
      rbExportData.setSelected(true);
    }
    else
    {
      rbExportPrintedElements.setSelected(true);
    }
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      fileChooser.addChoosableFileFilter(
          new ExtensionFileFilter
              (getResources().getString("csvexportdialog.csv-file-description"), ".csv"));
      fileChooser.setMultiSelectionEnabled(false);
    }

    fileChooser.setSelectedFile(new File(getFilename()));
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on csv
      if (StringUtil.endsWithIgnoreCase(selFileName, ".csv") == false)
      {
        selFileName = selFileName + ".csv";
      }
      setFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return <code>true</code> if the input is valid, <code>false</code> otherwise
   */
  public boolean performValidate()
  {
    final String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("csvexportdialog.targetIsEmpty"),
          getResources().getString("csvexportdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString("csvexportdialog.targetIsNoFile"),
            getResources().getString("csvexportdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString(
                "csvexportdialog.targetIsNotWritable"),
            getResources().getString("csvexportdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      final String key1 = "csvexportdialog.targetOverwriteConfirmation";
      final String key2 = "csvexportdialog.targetOverwriteTitle";
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
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into a CSV file.
   *
   * @param report  the report being processed.
   *
   * @return true or false.
   */
  public boolean performExport(final JFreeReport report)
  {
    initFromConfiguration(report.getReportConfiguration());
    setModal(true);
    setVisible(true);
    if (isConfirmed() == false)
    {
      return true;
    }
    if (isExportRawData())
    {
      return writeRawCSV(report);
    }
    else
    {
      return writeLayoutedCSV(report);
    }
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public boolean writeLayoutedCSV(final JFreeReport report)
  {
    Writer out = null;
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(
                  new File(getFilename())), getEncoding()));
      final CSVTableProcessor target = new CSVTableProcessor(report, getSeparatorString());
      target.setWriter(out);
      target.setStrictLayout(isStrictLayout());
      target.processReport();
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
   * Saves a report to CSV format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public boolean writeRawCSV(final JFreeReport report)
  {
    Writer out = null;
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(
                  new File(getFilename())), getEncoding()));
      final CSVProcessor target = new CSVProcessor(report, getSeparatorString());
      target.setWriter(out);
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
  private void showExceptionDialog(final String localisationBase, final Exception e)
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
   * Initialises the CSV export dialog from the settings in the report configuration.
   *
   * @param config  the report configuration.
   */
  public void initFromConfiguration(final ReportConfiguration config)
  {
    setSeparatorString(config.getConfigProperty(CSVProcessor.CSV_SEPARATOR, ","));
    encodingModel.ensureEncodingAvailable(getCSVTargetEncoding(config));
    setEncoding(getCSVTargetEncoding(config));
  }

  /**
   * Enables or disables the 'other' separator text field.
   */
  protected void performSeparatorSelection()
  {
    if (rbSeparatorOther.isSelected())
    {
      txSeparatorOther.setEnabled(true);
    }
    else
    {
      txSeparatorOther.setEnabled(false);
    }
  }

  /**
   * Returns the current setting of the 'strict layout' combo-box.
   *
   * @return A boolean.
   */
  public boolean isStrictLayout()
  {
    return cbxStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict layout' combo-box setting.
   *
   * @param strictLayout  the new setting.
   */
  public void setStrictLayout(final boolean strictLayout)
  {
    cbxStrictLayout.setSelected(strictLayout);
  }

  /**
   * This method exists for debugging purposes.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    final JDialog d = new CSVExportDialog();
    d.pack();
    d.addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(final WindowEvent e)
      {
        System.exit(0);
      }
    });
    d.setVisible(true);
  }

  /**
   * Returns the CSV encoding property value.
   * 
   * @param config the report configuration from where to read the values.
   * @return the CSV encoding property value.
   */
  public String getCSVTargetEncoding(ReportConfiguration config)
  {
    return config.getConfigProperty(CSV_OUTPUT_ENCODING, CSV_OUTPUT_ENCODING_DEFAULT);
  }

  /**
   * Sets the CSV encoding property value.
   *
   * @param config the report configuration from where to read the values.
   * @param targetEncoding  the new encoding.
   */
  public void setCSVTargetEncoding(ReportConfiguration config, final String targetEncoding)
  {
    config.setConfigProperty(CSV_OUTPUT_ENCODING, targetEncoding);
  }

}
