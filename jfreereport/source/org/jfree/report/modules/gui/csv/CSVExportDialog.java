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
 * --------------------
 * CSVExportDialog.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportDialog.java,v 1.16 2005/09/07 14:25:10 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.csv;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.AbstractExportDialog;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.modules.output.csv.CSVProcessor;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.csv.CSVTableProcessor;
import org.jfree.report.util.EncodingSupport;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.LengthLimitingDocument;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Configuration;

/**
 * A dialog for exporting a report to CSV format.
 *
 * @author Thomas Morgner.
 */
public class CSVExportDialog extends AbstractExportDialog
{
  /**
   * The 'CSV encoding' property key.
   */
  public static final String CSV_OUTPUT_ENCODING
          = "org.jfree.report.modules.gui.csv.Encoding";
  /**
   * A default value of the 'CSV encoding' property key.
   */
  public static final String CSV_OUTPUT_ENCODING_DEFAULT =
          EncodingSupport.getPlatformDefaultEncoding();

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionSelectSeparator extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectSeparator ()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      performSeparatorSelection();
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
    public ActionSelectFile (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("csvexportdialog.selectFile"));
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

  private class RawExportSelectionChangeListener implements ChangeListener
  {
    public RawExportSelectionChangeListener ()
    {
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged (final ChangeEvent e)
    {
      updateRawExportSelection();
    }
  }

  private class CancelAction extends AbstractCancelAction
  {
    public CancelAction (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("csvexportdialog.cancel"));
    }
  }

  private class ConfirmAction extends AbstractConfirmAction
  {
    public ConfirmAction (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("csvexportdialog.confirm"));
    }
  }

  /**
   * Filename text field.
   */
  private JTextField txFilename;

  /**
   * The encoding combo-box.
   */
  private JComboBox cbEncoding;

  /**
   * The encoding model.
   */
  private EncodingComboBoxModel encodingModel;

  /**
   * The strict layout check-box.
   */
  private JCheckBox cbxStrictLayout;

  /**
   * The columnnames-as-first-row layout check-box.
   */
  private JCheckBox cbxColumnNamesAsFirstRow;

  /**
   * A radio button for tab separators.
   */
  private JRadioButton rbSeparatorTab;

  /**
   * A radio button for colon separators.
   */
  private JRadioButton rbSeparatorColon;

  /**
   * A radio button for semi-colon separators.
   */
  private JRadioButton rbSeparatorSemicolon;

  /**
   * A radio button for other separators.
   */
  private JRadioButton rbSeparatorOther;

  /**
   * A text field for the 'other' separator.
   */
  private JTextField txSeparatorOther;

  /**
   * A radio button for exporting data only.
   */
  private JRadioButton rbExportData;

  /**
   * A radio button for exporting all printed elements.
   */
  private JRadioButton rbExportPrintedElements;

  /**
   * A file chooser.
   */
  private JFileChooser fileChooser;

  private static final String COMMA_SEPARATOR = ",";
  private static final String SEMICOLON_SEPARATOR = ";";
  private static final String TAB_SEPARATOR = "\t";
  private static final String CSV_FILE_EXTENSION = ".csv";


  /**
   * Creates a new CSV export dialog.
   *
   * @param owner the dialog owner.
   */
  public CSVExportDialog (final Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new CSV export dialog.
   *
   * @param owner the dialog owner.
   */
  public CSVExportDialog (final Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new CSV export dialog.  The created dialog is modal.
   */
  public CSVExportDialog ()
  {
    initConstructor();
  }

  /**
   * Initialisation.
   */
  private void initConstructor ()
  {
    setCancelAction(new CancelAction(getResources()));
    setConfirmAction(new ConfirmAction(getResources()));
    setTitle(getResources().getString("csvexportdialog.dialogtitle"));
    initialize();
    clear();
    getFormValidator().setEnabled(true);
  }

  protected String getResourceBaseName ()
  {
    return CSVExportPlugin.BASE_RESOURCE_CLASS;
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize ()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblFileName = new JLabel(getResources().getString("csvexportdialog.filename"));
    final JLabel lblEncoding = new JLabel(getResources().getString("csvexportdialog.encoding"));
    final JButton btnSelect = new ActionButton(new ActionSelectFile(getResources()));
    cbxStrictLayout = new JCheckBox(getResources().getString("csvexportdialog.strict-layout"));

    txFilename = new JTextField();
    encodingModel = EncodingComboBoxModel.createDefaultModel();
    encodingModel.sort();
    cbEncoding = new JComboBox(encodingModel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 5);
    contentPane.add(lblFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 5);
    contentPane.add(lblEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(3, 1, 5, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.insets = new Insets(1, 5, 5, 1);
    contentPane.add(btnSelect, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(5, 1, 1, 1);
    contentPane.add(cbEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbxStrictLayout, gbc);

    final JPanel p = new JPanel(new GridLayout(2,1,5,5));
    p.add(createSeparatorPanel());
    p.add(createExportTypePanel());

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(10, 1, 1, 1);
    contentPane.add(p, gbc);

    // button panel
    final JButton btnCancel = new ActionButton(getCancelAction());
    final JButton btnConfirm = new ActionButton(getConfirmAction());
    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1,2,5,5));
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    getRootPane().setDefaultButton(btnConfirm);
    buttonPanel.registerKeyboardAction(getConfirmAction(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 10, 0);
    gbc.anchor = GridBagConstraints.EAST;
    contentPane.add(buttonPanel, gbc);


    final JPanel contentWithStatus = new JPanel();
    contentWithStatus.setLayout(new BorderLayout());
    contentWithStatus.add(contentPane, BorderLayout.CENTER);
    contentWithStatus.add(getStatusBar(), BorderLayout.SOUTH);

    setContentPane(contentWithStatus);

    getFormValidator().registerButton(cbxStrictLayout);
    getFormValidator().registerTextField(txFilename);
    getFormValidator().registerComboBox(cbEncoding);
  }

  /**
   * Creates a panel for the export type.
   *
   * @return The panel.
   */
  private JPanel createExportTypePanel ()
  {
    // separator panel
    final JPanel exportTypePanel = new JPanel();
    exportTypePanel.setLayout(new GridBagLayout());

    final TitledBorder tb =
            new TitledBorder(getResources().getString("csvexportdialog.exporttype"));
    exportTypePanel.setBorder(tb);

    rbExportData = new JRadioButton(getResources().getString("csvexportdialog.export.data"));
    rbExportPrintedElements = new JRadioButton(getResources().getString("csvexportdialog.export.printed_elements"));
    cbxColumnNamesAsFirstRow = new JCheckBox(getResources().getString("cvsexportdialog.export.columnnames"));
    rbExportData.addChangeListener(new RawExportSelectionChangeListener());


    getFormValidator().registerButton(rbExportData);
    getFormValidator().registerButton(rbExportPrintedElements);
    getFormValidator().registerButton(cbxColumnNamesAsFirstRow);

    final ButtonGroup btg = new ButtonGroup();
    btg.add(rbExportData);
    btg.add(rbExportPrintedElements);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.gridwidth = 2;
    exportTypePanel.add(rbExportPrintedElements, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.gridwidth = 2;
    exportTypePanel.add(rbExportData, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(1, 20, 1, 1);
    exportTypePanel.add(cbxColumnNamesAsFirstRow, gbc);

    return exportTypePanel;
  }

  /**
   * Creates a separator panel.
   *
   * @return The panel.
   */
  private JPanel createSeparatorPanel ()
  {
    // separator panel
    final JPanel separatorPanel = new JPanel();
    separatorPanel.setLayout(new GridBagLayout());

    final TitledBorder tb =
            new TitledBorder(getResources().getString("csvexportdialog.separatorchar"));
    separatorPanel.setBorder(tb);

    rbSeparatorTab = new JRadioButton(getResources().getString("csvexportdialog.separator.tab"));
    rbSeparatorColon = new JRadioButton(getResources().getString("csvexportdialog.separator.colon"));
    rbSeparatorSemicolon = new JRadioButton(getResources().getString("csvexportdialog.separator.semicolon"));
    rbSeparatorOther = new JRadioButton(getResources().getString("csvexportdialog.separator.other"));

    getFormValidator().registerButton(rbSeparatorColon);
    getFormValidator().registerButton(rbSeparatorOther);
    getFormValidator().registerButton(rbSeparatorSemicolon);
    getFormValidator().registerButton(rbSeparatorTab);

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
    getFormValidator().registerTextField(txSeparatorOther);


    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorTab, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorColon, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorSemicolon, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(rbSeparatorOther, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    separatorPanel.add(txSeparatorOther, gbc);

    return separatorPanel;
  }

  /**
   * Returns the export file name.
   *
   * @return The file name.
   */
  public String getFilename ()
  {
    return txFilename.getText();
  }

  /**
   * Sets the export file name.
   *
   * @param filename the file name.
   */
  public void setFilename (final String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Clears all selections, input fields and sets the selected encryption level to none.
   */
  public void clear ()
  {
    txFilename.setText("");
    cbEncoding.setSelectedIndex(encodingModel.indexOf
            (EncodingSupport.getPlatformDefaultEncoding()));
    rbExportPrintedElements.setSelected(true);
    rbSeparatorColon.setSelected(true);
    cbxStrictLayout.setSelected(false);
    cbxColumnNamesAsFirstRow.setSelected(false);
    cbxColumnNamesAsFirstRow.setEnabled(false);
    performSeparatorSelection();
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
    p.setProperty("encoding", getEncoding());
    p.setProperty("separator-string", getSeparatorString());
    p.setProperty("strict-layout", String.valueOf(isStrictLayout()));
    p.setProperty("export-raw-data", String.valueOf(isExportRawData()));
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
    setEncoding(p.getProperty("encoding", getEncoding()));
    setSeparatorString(p.getProperty("separator-string", getSeparatorString()));
    setStrictLayout(StringUtil.parseBoolean(p.getProperty("strict-layout"), isStrictLayout()));
    setExportRawData(StringUtil.parseBoolean(p.getProperty("export-raw-data"), isExportRawData()));
  }

  /**
   * Returns the separator string, which is controlled by the selection of radio buttons.
   *
   * @return The separator string.
   */
  public String getSeparatorString ()
  {
    if (rbSeparatorColon.isSelected())
    {
      return COMMA_SEPARATOR;
    }
    if (rbSeparatorSemicolon.isSelected())
    {
      return SEMICOLON_SEPARATOR;
    }
    if (rbSeparatorTab.isSelected())
    {
      return TAB_SEPARATOR;
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
   * @param s the separator.
   */
  public void setSeparatorString (final String s)
  {
    if (s == null)
    {
      rbSeparatorOther.setSelected(true);
      txSeparatorOther.setText("");
    }
    else if (s.equals(COMMA_SEPARATOR))
    {
      rbSeparatorColon.setSelected(true);
    }
    else if (s.equals(SEMICOLON_SEPARATOR))
    {
      rbSeparatorSemicolon.setSelected(true);
    }
    else if (s.equals(TAB_SEPARATOR))
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
  public String getEncoding ()
  {
    if (cbEncoding.getSelectedIndex() == -1)
    {
      return EncodingSupport.getPlatformDefaultEncoding();
    }
    else
    {
      return encodingModel.getEncoding(cbEncoding.getSelectedIndex());
    }
  }

  /**
   * Sets the encoding.
   *
   * @param encoding the encoding.
   */
  public void setEncoding (final String encoding)
  {
    cbEncoding.setSelectedIndex(encodingModel.indexOf(encoding));
  }

  /**
   * Returns <code>true</code> if the user selected to export raw data only, and
   * <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public boolean isExportRawData ()
  {
    return rbExportData.isSelected();
  }

  /**
   * Sets a flag that controls whether raw data is exported.
   *
   * @param b the new flag value.
   */
  public void setExportRawData (final boolean b)
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
  protected void performSelectFile ()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      fileChooser.addChoosableFileFilter(new ExtensionFileFilter
              (getResources().getString("csvexportdialog.csv-file-description"), CSV_FILE_EXTENSION));
      fileChooser.setMultiSelectionEnabled(false);
    }

    fileChooser.setSelectedFile(new File(getFilename()));
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on csv
      if (StringUtil.endsWithIgnoreCase(selFileName, CSV_FILE_EXTENSION) == false)
      {
        selFileName = selFileName + CSV_FILE_EXTENSION;
      }
      setFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it
   * is also checked for validity.
   *
   * @return <code>true</code> if the input is valid, <code>false</code> otherwise
   */
  protected boolean performValidate ()
  {
    getStatusBar().clear();

    final String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
              getResources().getString("csvexportdialog.targetIsEmpty"));
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("csvexportdialog.targetIsNoFile"));
        return false;
      }
      if (f.canWrite() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("csvexportdialog.targetIsNotWritable"));
        return false;
      }

      final String message = MessageFormat.format(getResources().getString
              ("csvexportdialog.targetExistsWarning"),
              new Object[]{filename});
      getStatusBar().setStatus(JStatusBar.TYPE_WARNING, message);

    }
    return true;
  }

  protected boolean performConfirm ()
  {
    final File f = new File(getFilename());
    if (f.exists())
    {
      final String key1 = "csvexportdialog.targetOverwriteConfirmation";
      final String key2 = "csvexportdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
              MessageFormat.format(getResources().getString(key1),
                      new Object[]{getFilename()}),
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
   * Initialises the CSV export dialog from the settings in the report configuration.
   *
   * @param config the report configuration.
   */
  public void initFromConfiguration (final Configuration config)
  {
    // the CSV separator has two sources, either the data CSV or the
    // table CSV. As we have only one input field for that property,
    // we use a cascading schema to resolve this. The data oriented
    // separator is preferred ...
    final String tableCSVSeparator = config.getConfigProperty
            (CSVTableProcessor.CONFIGURATION_PREFIX + "." +
                    CSVTableProcessor.SEPARATOR_KEY, COMMA_SEPARATOR);
    setSeparatorString(config.getConfigProperty
            (CSVProcessor.CSV_SEPARATOR, tableCSVSeparator));

    final String strict = config.getConfigProperty
            (CSVTableProcessor.CONFIGURATION_PREFIX + "." + CSVTableProcessor.STRICT_LAYOUT,
                    config.getConfigProperty(TableProcessor.STRICT_LAYOUT, TableProcessor.STRICT_LAYOUT_DEFAULT));
    setStrictLayout(strict.equals("true"));

    final String colNames = config.getConfigProperty (CSVProcessor.CSV_DATAROWNAME, "false");
    setColumnNamesAsFirstRow(colNames.equals("true"));

    final String encoding = config.getConfigProperty
            (CSV_OUTPUT_ENCODING, CSV_OUTPUT_ENCODING_DEFAULT);
    encodingModel.ensureEncodingAvailable(encoding);
    setEncoding(encoding);
  }

  /**
   * Stores the input from the dialog into the report configuration of the report.
   *
   * @param config the report configuration that should receive the new settings.
   */
  public void storeToConfiguration (final ModifiableConfiguration config)
  {
    config.setConfigProperty(CSVProcessor.CSV_SEPARATOR, getSeparatorString());
    config.setConfigProperty(CSVTableProcessor.CONFIGURATION_PREFIX + "." +
            CSVTableProcessor.SEPARATOR_KEY, getSeparatorString());
    config.setConfigProperty(CSVTableProcessor.CONFIGURATION_PREFIX + "." +
            CSVTableProcessor.STRICT_LAYOUT, String.valueOf(isStrictLayout()));
    config.setConfigProperty(CSVProcessor.CSV_DATAROWNAME,
            String.valueOf(isColumnNamesAsFirstRow()));
    config.setConfigProperty(CSV_OUTPUT_ENCODING, getEncoding());
  }


  /**
   * Enables or disables the 'other' separator text field.
   */
  protected void performSeparatorSelection ()
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

  public boolean isColumnNamesAsFirstRow ()
  {
    return  cbxColumnNamesAsFirstRow.isSelected();
  }

  public void setColumnNamesAsFirstRow (final boolean colsAsFirstRow)
  {
    cbxColumnNamesAsFirstRow.setSelected(colsAsFirstRow);
  }

  /**
   * Returns the current setting of the 'strict layout' combo-box.
   *
   * @return A boolean.
   */
  public boolean isStrictLayout ()
  {
    return cbxStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict layout' combo-box setting.
   *
   * @param strictLayout the new setting.
   */
  public void setStrictLayout (final boolean strictLayout)
  {
    cbxStrictLayout.setSelected(strictLayout);
  }

  protected String getConfigurationSuffix ()
  {
    return "_csvexport";
  }

  protected void updateRawExportSelection()
  {
    cbxColumnNamesAsFirstRow.setEnabled(rbExportData.isSelected());
  }

  public static void main (final String[] args)
  {
    final CSVExportDialog dialog = new CSVExportDialog();
    dialog.setModal(true);
    dialog.pack();
    dialog.performQueryForExport(new JFreeReport());
    System.exit(0);
  }
}
