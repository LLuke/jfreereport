/**
 * Date: Jan 21, 2003
 * Time: 7:48:58 PM
 *
 * $Id: CSVExportDialog.java,v 1.3 2003/02/02 23:43:50 taqua Exp $
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.csv.CSVProcessor;
import com.jrefinery.report.targets.table.csv.CSVTableProcessor;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.LengthLimitingDocument;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.ui.ExtensionFileFilter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class CSVExportDialog extends JDialog implements ExportPlugin
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
      putValue(Action.NAME, getResources().getString("csvexportdialog.confirm"));
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
    public void actionPerformed(ActionEvent e)
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
      putValue(Action.NAME, getResources().getString("csvexportdialog.selectFile"));
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

  /** Filename text field. */
  private JTextField txFilename;

  private JComboBox cbEncoding;
  private EncodingComboBoxModel encodingModel;
  private JCheckBox cbxStrictLayout;

  private JRadioButton rbSeparatorTab;
  private JRadioButton rbSeparatorColon;
  private JRadioButton rbSeparatorSemicolon;
  private JRadioButton rbSeparatorOther;
  private JTextField txSeparatorOther;

  private JRadioButton rbExportData;
  private JRadioButton rbExportPrintedElements;

  private JFileChooser fileChooser;

  /** Confirmed flag. */
  private boolean confirmed;

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
  public CSVExportDialog(Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new PDF Excel dialog.
   *
   * @param owner  the dialog owner.
   */
  public CSVExportDialog(Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new Excel save dialog.  The created dialog is modal.
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
      public void windowClosing(WindowEvent e)
      {
        new ActionCancel().actionPerformed(null);
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
   * Initializes the Swing components of this dialog.
   */
  private void initialize()
  {
    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new ExtensionFileFilter("Comma Separated Value files", ".csv"));
    fileChooser.setMultiSelectionEnabled(false);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    JLabel lblFileName = new JLabel(getResources().getString("csvexportdialog.filename"));
    JLabel lblEncoding = new JLabel(getResources().getString("csvexportdialog.encoding"));
    JButton btnSelect = new ActionButton(new ActionSelectFile());
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

    JPanel p = new JPanel (new GridLayout());
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
    JButton btnCancel = new ActionButton(new ActionCancel());
    JButton btnConfirm = new ActionButton(new ActionConfirm());
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(new ActionConfirm(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),
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

  private JPanel createExportTypePanel ()
  {
    // separator panel
    JPanel exportTypePanel = new JPanel();
    exportTypePanel.setLayout(new GridBagLayout());

    TitledBorder tb = new TitledBorder(getResources().getString("csvexportdialog.exporttype"));
    exportTypePanel.setBorder(tb);

    rbExportData = new JRadioButton(getResources().getString("csvexportdialog.export.data"));
    rbExportPrintedElements = new JRadioButton(getResources().getString("csvexportdialog.export.printed_elements"));

    ButtonGroup btg = new ButtonGroup();
    btg.add (rbExportData);
    btg.add (rbExportPrintedElements);

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

  private JPanel createSeparatorPanel ()
  {
    // separator panel
    JPanel separatorPanel = new JPanel();
    separatorPanel.setLayout(new GridBagLayout());

    TitledBorder tb = new TitledBorder(getResources().getString("csvexportdialog.separatorchar"));
    separatorPanel.setBorder(tb);

    rbSeparatorTab = new JRadioButton(getResources().getString("csvexportdialog.separator.tab"));
    rbSeparatorColon = new JRadioButton(getResources().getString("csvexportdialog.separator.colon"));
    rbSeparatorSemicolon = new JRadioButton(getResources().getString("csvexportdialog.separator.semicolon"));
    rbSeparatorOther = new JRadioButton(getResources().getString("csvexportdialog.separator.other"));

    ButtonGroup btg = new ButtonGroup();
    btg.add (rbSeparatorTab);
    btg.add (rbSeparatorColon);
    btg.add (rbSeparatorSemicolon);
    btg.add (rbSeparatorOther);

    Action selectAction = new ActionSelectSeparator();
    rbSeparatorTab.addActionListener(selectAction);
    rbSeparatorColon.addActionListener(selectAction);
    rbSeparatorSemicolon.addActionListener(selectAction);
    rbSeparatorOther.addActionListener(selectAction);

    LengthLimitingDocument ldoc = new LengthLimitingDocument(1);
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

    JPanel otherPanel = new JPanel();
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
    txFilename.setText("");
    cbEncoding.setSelectedIndex(encodingModel.indexOf(System.getProperty ("file.encoding", "Cp1251")));
    rbExportPrintedElements.setSelected(true);
    rbSeparatorColon.setSelected(true);
    cbxStrictLayout.setSelected(false);
    performSeparatorSelection();
  }

  public String getSeparatorString ()
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

  public void setSeparatorString (String s)
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

  public String getEncoding ()
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

  public void setEncoding (String encoding)
  {
    cbEncoding.setSelectedIndex(encodingModel.indexOf(encoding));
  }

  public boolean isExportRawData ()
  {
    return rbExportData.isSelected();
  }

  public void setExportRawData (boolean b)
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
   * selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    fileChooser.setSelectedFile(new File(getFilename()));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile();
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
                                    getResources().getString("csvexportdialog.targetIsEmpty"),
                                    getResources().getString("csvexportdialog.errorTitle"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
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
                                      getResources().getString("csvexportdialog.targetIsNotWritable"),
                                      getResources().getString("csvexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "csvexportdialog.targetOverwriteConfirmation";
      String key2 = "csvexportdialog.targetOverwriteTitle";
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
  public boolean performExport(JFreeReport report)
  {
    initFromConfiguration(report.getReportConfiguration());
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }
    if (isExportRawData())
    {
      return writeRawCSV(report);
    }
    else
    {
      return writeLayoutedCSV (report);
    }
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report  the report.
   *
   * @return true or false.
   */
  public boolean writeLayoutedCSV(JFreeReport report)
  {
    Writer out = null;
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(
                  new File(getFilename())), getEncoding()));
      CSVTableProcessor target = new CSVTableProcessor(report, getSeparatorString());
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
        out.close();
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
  public boolean writeRawCSV(JFreeReport report)
  {
    Writer out = null;
    try
    {

      out = new BufferedWriter(
          new OutputStreamWriter(
              new FileOutputStream(
                  new File(getFilename())), getEncoding()));
      CSVProcessor target = new CSVProcessor(report, getSeparatorString());
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
    setSeparatorString(config.getConfigProperty(CSVProcessor.CSV_SEPARATOR, ","));
  }

  public static void main (String [] args)
  {
    JDialog d = new CSVExportDialog();
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

  private void performSeparatorSelection()
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

  public String getDisplayName()
  {
    return resources.getString ("action.export-to-csv.name");
  }

  public String getShortDescription()
  {
    return resources.getString ("action.export-to-csv.description");
  }

  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject ("action.export-to-csv.small-icon");
  }

  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject ("action.export-to-csv.icon");
  }

  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject ("action.export-to-csv.accelerator");
  }

  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject ("action.export-to-csv.mnemonic");
  }

  public boolean isSeparated()
  {
    return false;
  }

  public boolean isAddToToolbar()
  {
    return false;
  }

  public boolean isStrictLayout()
  {
    return cbxStrictLayout.isSelected();
  }

  public void setStrictLayout(boolean strictLayout)
  {
    cbxStrictLayout.setSelected(strictLayout);
  }


}
