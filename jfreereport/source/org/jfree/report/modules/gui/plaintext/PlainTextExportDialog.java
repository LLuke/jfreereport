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
 * --------------------------
 * PlainTextExportDialog.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportDialog.java,v 1.8.4.9 2004/12/13 20:23:54 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.plaintext;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.misc.configstore.base.ConfigStoreException;
import org.jfree.report.modules.output.pageable.plaintext.AbstractEpsonPrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.IBMCompatiblePrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecification;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecificationManager;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionRadioButton;

/**
 * A dialog that is used to export reports to plain text.
 *
 * @author Thomas Morgner.
 */
public class PlainTextExportDialog extends JDialog
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
      putValue(Action.NAME, getResources().getString("plain-text-exportdialog.confirm"));
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
  private class ActionCancel extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionCancel()
    {
      putValue(Action.NAME, getResources().getString("plain-text-exportdialog.cancel"));
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
   * An action to select a file.
   */
  private class ActionSelectFile extends AbstractFileSelectionAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectFile()
    {
      super (PlainTextExportDialog.this);
      putValue(NAME, getResources().getString("plain-text-exportdialog.selectFile"));
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "Text Files";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".txt";
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      final File selectedFile = performSelectFile
              (new File (getFilename()), JFileChooser.SAVE_DIALOG, true);
      if (selectedFile != null)
      {
        setFilename(selectedFile.getPath());
      }
    }
  }

  /**
   * An action to select a plain printer.
   */
  private class ActionSelectPrinter extends AbstractAction
  {
    private int printer;

    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectPrinter(final int printer)
    {
      putValue(NAME, getResources().getString(PRINTER_NAMES[printer]));
      this.printer = printer;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSelectedPrinter(printer);
    }
  }

  private class SelectEpsonModelAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public SelectEpsonModelAction ()
    {
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      updateEpsonEncoding();
    }
  }

  /** Plain text output. */
  public static final int TYPE_PLAIN_OUTPUT = 0;

  /** Epson printer output. */
  public static final int TYPE_EPSON_OUTPUT = 1;

  /** IBM printer output. */
  public static final int TYPE_IBM_OUTPUT = 2;

  private static final String[] PRINTER_NAMES = new String[] {
    "plain-text-exportdialog.printer.plain",
    "plain-text-exportdialog.printer.epson",
    "plain-text-exportdialog.printer.ibm"
  };

  /** 6 lines per inch. */
  public static final Integer LPI_6 = new Integer(6);

  /** 10 lines per inch. */
  public static final Integer LPI_10 = new Integer(10);

  /** 10 characters per inch. */
  public static final Integer CPI_10 = new Integer(10);

  /** 12 characters per inch. */
  public static final Integer CPI_12 = new Integer(12);

  /** 15 characters per inch. */
  public static final Integer CPI_15 = new Integer(15);

  /** 17 characters per inch. */
  public static final Integer CPI_17 = new Integer(17);

  /** 20 characters per inch. */
  public static final Integer CPI_20 = new Integer(20);

  /** Confirmed flag. */
  private boolean confirmed;

  /** Localised resources. */
  private ResourceBundle resources;

  /** A combo-box for selecting the encoding. */
  private EncodingSelector encodingSelector;

  /** A radio button for selecting plain printer commands. */
  private JRadioButton rbPlainPrinterCommandSet;

  /** A radio button for selecting Epson printer commands. */
  private JRadioButton rbEpsonPrinterCommandSet;

  /** A radio button for selecting IBM printer commands. */
  private JRadioButton rbIBMPrinterCommandSet;

  /** The filename text field. */
  private JTextField txFilename;

  /** A combo-box for selecting lines per inch. */
  private JComboBox cbLinesPerInch;

  /** A combo-box for selecting characters per inch. */
  private JComboBox cbCharsPerInch;

  private JComboBox cbEpsonPrinterType;

  private KeyedComboBoxModel epsonPrinters;

  /**
   * Creates a non-modal dialog without a title and without
   * a specified Frame owner.  A shared, hidden frame will be
   * set as the owner of the Dialog.
   */
  public PlainTextExportDialog()
  {
    init();
  }

  /**
   * Creates a non-modal dialog without a title with the
   * specifed Frame as its owner.
   *
   * @param owner the Frame from which the dialog is displayed
   */
  public PlainTextExportDialog(final Frame owner)
  {
    super(owner);
    init();
  }

  /**
   * Creates a non-modal dialog without a title with the
   * specifed Dialog as its owner.
   *
   * @param owner the Dialog from which the dialog is displayed
   */
  public PlainTextExportDialog(final Dialog owner)
  {
    super(owner);
    init();
  }

  /**
   * Initialise the dialog.
   */
  private void init()
  {
    setModal(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setTitle(getResources().getString("plain-text-exportdialog.dialogtitle"));

    epsonPrinters = loadEpsonPrinters();
    cbEpsonPrinterType = new JComboBox(epsonPrinters);
    cbEpsonPrinterType.setAction(new SelectEpsonModelAction());

    final Integer[] lpiModel = {
      LPI_6,
      LPI_10
    };

    final Integer[] cpiModel = {
      CPI_10,
      CPI_12,
      CPI_15,
      CPI_17,
      CPI_20
    };

    cbLinesPerInch = new JComboBox(new DefaultComboBoxModel(lpiModel));
    cbCharsPerInch = new JComboBox(new DefaultComboBoxModel(cpiModel));

    rbPlainPrinterCommandSet = new ActionRadioButton (new ActionSelectPrinter(TYPE_PLAIN_OUTPUT));
    rbEpsonPrinterCommandSet = new ActionRadioButton(new ActionSelectPrinter(TYPE_EPSON_OUTPUT));
    rbIBMPrinterCommandSet = new ActionRadioButton(new ActionSelectPrinter(TYPE_IBM_OUTPUT));

    txFilename = new JTextField();
    encodingSelector = new EncodingSelector();

    final ButtonGroup bg = new ButtonGroup();
    bg.add(rbPlainPrinterCommandSet);
    bg.add(rbEpsonPrinterCommandSet);
    bg.add(rbIBMPrinterCommandSet);

    final JPanel rootPane = new JPanel ();
    rootPane.setLayout(new BorderLayout());

    rootPane.add(createButtonPanel(), BorderLayout.SOUTH);
    rootPane.add(createContentPane(), BorderLayout.CENTER);
    setContentPane(rootPane);

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

  private KeyedComboBoxModel loadEpsonPrinters ()
  {
    final KeyedComboBoxModel epsonPrinters = new KeyedComboBoxModel();
    final PrinterSpecificationManager specManager =
            AbstractEpsonPrinterDriver.getPrinterSpecificationManager();
    final String[] printerNames =
            specManager.getPrinterNames();
    Arrays.sort(printerNames);
    for (int i = 0; i < printerNames.length; i++)
    {
      final PrinterSpecification pspec = specManager.getPrinter(printerNames[i]);
      epsonPrinters.add(pspec, pspec.getDisplayName());
    }
    return epsonPrinters;
  }

  /**
   * Creates the content pane for the export dialog.
   *
   * @return the created content pane.
   */
  private JComponent createContentPane()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblPrinterSelect = new JLabel(
        getResources().getString("plain-text-exportdialog.printer"));
    final JLabel lblFileName
        = new JLabel(getResources().getString("plain-text-exportdialog.filename"));
    final JLabel lblEncoding
        = new JLabel(getResources().getString("plain-text-exportdialog.encoding"));
    final JButton btnSelect = new ActionButton(new ActionSelectFile());

    final JLabel lblCharsPerInch = new JLabel(
        getResources().getString("plain-text-exportdialog.chars-per-inch"));
    final JLabel lblLinesPerInch = new JLabel(
        getResources().getString("plain-text-exportdialog.lines-per-inch"));
    final JLabel lblFontSettings = new JLabel(
        getResources().getString("plain-text-exportdialog.font-settings"));

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
    contentPane.add(lblPrinterSelect, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbPlainPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbIBMPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbEpsonPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(cbEpsonPrinterType, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(encodingSelector, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblFontSettings, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 2;
    gbc.gridy = 5;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblCharsPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 2;
    gbc.gridy = 6;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblLinesPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbCharsPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbLinesPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 4;
    gbc.gridy = 0;
    contentPane.add(btnSelect, gbc);

    return contentPane;
  }

  /**
   * Creates the button panel for the export dialog.
   *
   * @return the created button panel
   */
  private JPanel createButtonPanel()
  {
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

    final JPanel buttonCarrier = new JPanel();
    buttonCarrier.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonCarrier.add(buttonPanel);
    return buttonCarrier;
  }

  private void updateEpsonEncoding ()
  {
    final PrinterSpecification spec = (PrinterSpecification)
            epsonPrinters.getSelectedKey();
    if (spec == null)
    {
      Log.debug ("Invalid selection: " + spec);
      encodingSelector.setEncodings
              (AbstractEpsonPrinterDriver.getPrinterSpecificationManager().
              getGenericPrinter());
    }
    else
    {
      encodingSelector.setEncodings(spec);
    }
  }

  /**
   * Sets the selected printer.
   *
   * @param type  the type.
   */
  public void setSelectedPrinter(final int type)
  {
    final String oldEncoding = getEncoding();
    if (type == TYPE_EPSON_OUTPUT)
    {
      rbEpsonPrinterCommandSet.setSelected(true);
      cbEpsonPrinterType.setEnabled(true);
      updateEpsonEncoding();
    }
    else if (type == TYPE_IBM_OUTPUT)
    {
      rbIBMPrinterCommandSet.setSelected(true);
      cbEpsonPrinterType.setEnabled(false);
      encodingSelector.setEncodings(new IBMCompatiblePrinterDriver.GenericIBMPrinterSpecification());
    }
    else if (type == TYPE_PLAIN_OUTPUT)
    {
      rbPlainPrinterCommandSet.setSelected(true);
      cbEpsonPrinterType.setEnabled(false);
      encodingSelector.setEncodings(new EncodingSelector.GenericPrinterSpecification());
    }
    else
    {
      throw new IllegalArgumentException();
    }
    if (oldEncoding != null)
    {
      setEncoding(oldEncoding);
    }
  }

  /**
   * Returns the selected printer.
   *
   * @return The printer type.
   */
  public int getSelectedPrinter()
  {
    if (rbPlainPrinterCommandSet.isSelected())
    {
      return TYPE_PLAIN_OUTPUT;
    }
    if (rbEpsonPrinterCommandSet.isSelected())
    {
      return TYPE_EPSON_OUTPUT;
    }
    return TYPE_IBM_OUTPUT;
  }

  /**
   * Returns the filename.
   *
   * @return the name of the file where to save the file.
   */
  public String getFilename()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the file.
   *
   * @param filename the filename of the file
   */
  public void setFilename(final String filename)
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
  protected void setConfirmed(final boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  /**
   * clears all selections, input fields and set the selected encryption level to none.
   */
  public void clear()
  {
    txFilename.setText("");
    setSelectedPrinter(TYPE_PLAIN_OUTPUT);
    cbEpsonPrinterType.setEnabled(false);
    cbEpsonPrinterType.setSelectedItem(AbstractEpsonPrinterDriver.getDefaultPrinter());
    cbCharsPerInch.setSelectedItem(CPI_10);
    cbLinesPerInch.setSelectedItem(LPI_6);
    setEncoding(System.getProperty("file.encoding", "Cp1251"));
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
    if (getSelectedPrinterModel() != null)
    {
      p.setProperty("printer-model", getSelectedPrinterModel());
    }

    p.setProperty("chars-per-inch", String.valueOf(getCharsPerInch()));
    p.setProperty("lines-per-inch", String.valueOf(getLinesPerInch()));
    p.setProperty("selected-printer", String.valueOf(getSelectedPrinter()));
    return p;
  }

  /**
   * Restores the user input from a properties collection.
   *
   * @param p the user input.
   */
  public void setDialogContents (final Properties p)
  {
    setSelectedPrinter(StringUtil.parseInt(p.getProperty("selected-printer"), getSelectedPrinter()));

    setCharsPerInch(StringUtil.parseInt(p.getProperty("chars-per-inch"), getCharsPerInch()));
    setLinesPerInch(StringUtil.parseInt(p.getProperty("lines-per-inch"), getLinesPerInch()));

    setEncoding(p.getProperty("encoding", getEncoding()));
    setFilename(p.getProperty("filename", getFilename()));
    setSelectedPrinterModel(p.getProperty("printer-model", getSelectedPrinterModel()));
  }

  /**
   * Returns the lines-per-inch setting.
   *
   * @return The lines-per-inch setting.
   */
  public int getLinesPerInch()
  {
    final Integer i = (Integer) cbLinesPerInch.getSelectedItem();
    if (i == null)
    {
      return LPI_6.intValue();
    }
    return i.intValue();
  }

  /**
   * Sets the lines per inch.
   *
   * @param lpi  the lines per inch.
   */
  public void setLinesPerInch(final int lpi)
  {
    final Integer lpiObj = new Integer (lpi);
    final ComboBoxModel model = cbLinesPerInch.getModel();
    for (int i = 0; i < model.getSize(); i++)
    {
      if (lpiObj.equals(model.getElementAt(i)))
      {
        cbLinesPerInch.setSelectedIndex(i);
        return;
      }
    }
    throw new IllegalArgumentException("There is no such LPI: " + lpi);
  }

  /**
   * Returns the characters-per-inch setting.
   *
   * @return The characters-per-inch setting.
   */
  public int getCharsPerInch()
  {
    final Integer i = (Integer) cbCharsPerInch.getSelectedItem();
    if (i == null)
    {
      return CPI_10.intValue();
    }
    return i.intValue();
  }

  /**
   * Sets the characters per inch.
   *
   * @param cpi  the characters per inch.
   */
  public void setCharsPerInch(final int cpi)
  {
    final Integer cpiObj = new Integer (cpi);
    final ComboBoxModel model = cbCharsPerInch.getModel();
    for (int i = 0; i < model.getSize(); i++)
    {
      if (cpiObj.equals(model.getElementAt(i)))
      {
        cbCharsPerInch.setSelectedIndex(i);
        return;
      }
    }
    throw new IllegalArgumentException("There is no such CPI: " + cpi);
  }

  /**
   * Returns the encoding.
   *
   * @return The encoding.
   */
  public String getEncoding()
  {
    return encodingSelector.getSelectedEncoding();
  }

  /**
   * Sets the encoding.
   *
   * @param encoding  the encoding.
   */
  public void setEncoding(final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException("Encoding must not be null");
    }
    encodingSelector.setSelectedEncoding(encoding);
  }

  /**
   * Initialises the PDF save dialog from the settings in the report configuration.
   *
   * @param config  the report configuration.
   */
  public void initFromConfiguration(final ReportConfiguration config)
  {
    setEncoding(config.getConfigProperty
        (PlainTextOutputTarget.TEXT_OUTPUT_ENCODING,
            PlainTextOutputTarget.TEXT_OUTPUT_ENCODING_DEFAULT));
    config.getConfigProperty
            (AbstractEpsonPrinterDriver.EPSON_PRINTER_TYPE, getSelectedPrinterModel());

    try
    {
      setLinesPerInch(StringUtil.parseInt(config.getConfigProperty
          (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.LINES_PER_INCH, "6"),
          getLinesPerInch()));
    }
    catch (IllegalArgumentException e)
    {
      // ignore
    }
    try
    {
      setCharsPerInch(StringUtil.parseInt(config.getConfigProperty
          (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.CHARS_PER_INCH, "10"),
          getCharsPerInch()));
    }
    catch (IllegalArgumentException e)
    {
      // ignore
    }
  }

  /**
   * Stores the input from the dialog into the report configuration of the 
   * report.
   * 
   * @param config the report configuration that should receive the new
   * settings.
   */
  public void storeToConfiguration(final ReportConfiguration config)
  {
    config.setConfigProperty(PlainTextOutputTarget.TEXT_OUTPUT_ENCODING,
        getEncoding());
    config.setConfigProperty(PlainTextOutputTarget.CONFIGURATION_PREFIX +
        PlainTextOutputTarget.CHARS_PER_INCH, String.valueOf(getCharsPerInch()));
    config.setConfigProperty(PlainTextOutputTarget.CONFIGURATION_PREFIX +
        PlainTextOutputTarget.LINES_PER_INCH, String.valueOf(getLinesPerInch()));
    config.setConfigProperty
            (AbstractEpsonPrinterDriver.EPSON_PRINTER_TYPE, getSelectedPrinterModel());
  }

  /**
   * Opens the dialog to query all necessary input from the user.
   * This will not start the processing, as this is done elsewhere.
   * 
   * @param report the report that should be processed.
   * @return true, if the processing should continue, false otherwise.
   */
  public boolean performQueryForExport(final JFreeReport report)
  {
    initFromConfiguration(report.getReportConfiguration());
    final ConfigStorage storage = ConfigFactory.getInstance().getUserStorage();
    try
    {
      setDialogContents(storage.loadProperties
          (ConfigFactory.encodePath(report.getName() + "_plaintextexport"),
              new Properties()));
    }
    catch (Exception cse)
    {
      Log.debug ("Unable to load the defaults in PlainText export dialog: " + cse.getLocalizedMessage());
    }

    setModal(true);
    setVisible(true);
    if (isConfirmed() == false)
    {
      Log.debug ("Is not confirmed...");
      return false;
    }
    storeToConfiguration(report.getReportConfiguration());
    try
    {
      Log.debug ("About to store:  "+ storage);
      storage.storeProperties
          (ConfigFactory.encodePath(report.getName() + "_plaintextexport"),
              getDialogContents());
    }
    catch (ConfigStoreException cse)
    {
      Log.debug ("Unable to store the defaults in PlainText export dialog.");
    }

    return true;
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
      resources = ResourceBundle.getBundle(PlainTextExportPlugin.BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidate()
  {
    final String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString(
              "plain-text-exportdialog.targetIsEmpty"),
          getResources().getString("plain-text-exportdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString("plain-text-exportdialog.targetIsNoFile"),
            getResources().getString("plain-text-exportdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString("plain-text-exportdialog.targetIsNotWritable"),
            getResources().getString("plain-text-exportdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      final String key1 = "plain-text-exportdialog.targetOverwriteConfirmation";
      final String key2 = "plain-text-exportdialog.targetOverwriteTitle";
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

  public String getSelectedPrinterModel ()
  {
    final String model = (String) cbEpsonPrinterType.getSelectedItem();
    if (model == null)
    {
      return AbstractEpsonPrinterDriver.getPrinterSpecificationManager().getGenericPrinter().getName();
    }
    return model;
  }

  public void setSelectedPrinterModel (final String selectedPrinterModel)
  {
    final PrinterSpecificationManager mgr = AbstractEpsonPrinterDriver.getPrinterSpecificationManager();
    final PrinterSpecification spec = mgr.getPrinter(selectedPrinterModel);
    if (spec != null)
    {
      epsonPrinters.setSelectedKey(spec);
    }
    else
    {
      epsonPrinters.setSelectedKey(mgr.getGenericPrinter());
    }
  }
}
