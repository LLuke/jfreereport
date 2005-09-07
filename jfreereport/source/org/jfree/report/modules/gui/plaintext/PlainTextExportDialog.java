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
 * $Id: PlainTextExportDialog.java,v 1.20 2005/03/18 13:49:38 taqua Exp $
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.AbstractExportDialog;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.modules.output.pageable.plaintext.Epson24PinPrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.Epson9PinPrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.IBMCompatiblePrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecification;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecificationManager;
import org.jfree.report.util.EncodingSupport;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionRadioButton;
import org.jfree.util.Configuration;

/**
 * A dialog that is used to export reports to plain text.
 *
 * @author Thomas Morgner.
 */
public class PlainTextExportDialog extends AbstractExportDialog
{
  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionConfirm extends AbstractConfirmAction
  {
    /**
     * Default constructor.
     */
    public ActionConfirm (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("plain-text-exportdialog.confirm"));
    }
  }

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionCancel extends AbstractCancelAction
  {
    /**
     * Default constructor.
     */
    public ActionCancel (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("plain-text-exportdialog.cancel"));
    }
  }

  /**
   * An action to select a file.
   */
  private class ActionSelectFile extends AbstractFileSelectionAction
  {
    private final ResourceBundle resources;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public ActionSelectFile (final ResourceBundle resources)
    {
      super(PlainTextExportDialog.this);
      this.resources = resources;
      putValue(Action.NAME, resources.getString("plain-text-exportdialog.selectFile"));
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return resources.getString("plain-text-exportdialog.fileDescription");
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
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File selectedFile = performSelectFile
              (new File(getFilename()), JFileChooser.SAVE_DIALOG, true);
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
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public ActionSelectPrinter (final String printerName, final int printer)
    {
      putValue(NAME, printerName);
      this.printer = printer;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
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
      if (getSelectedPrinter() == TYPE_EPSON9_OUTPUT)
      {
        updateEpson9Encoding();
      }
      else if (getSelectedPrinter() == TYPE_EPSON24_OUTPUT)
      {
        updateEpson24Encoding();
      }
    }
  }

  /**
   * Plain text output.
   */
  public static final int TYPE_PLAIN_OUTPUT = 0;

  /**
   * Epson printer output.
   */
  public static final int TYPE_EPSON9_OUTPUT = 1;

  /**
   * IBM printer output.
   */
  public static final int TYPE_IBM_OUTPUT = 2;

  /**
   * Epson printer output.
   */
  public static final int TYPE_EPSON24_OUTPUT = 3;

  private static final String[] PRINTER_NAMES = new String[]{
    "plain-text-exportdialog.printer.plain",
    "plain-text-exportdialog.printer.epson9",
    "plain-text-exportdialog.printer.ibm",
    "plain-text-exportdialog.printer.epson24",
  };

  /**
   * 6 lines per inch.
   */
  public static final Float LPI_6 = new Float(6);

  /**
   * 10 lines per inch.
   */
  public static final Float LPI_10 = new Float(10);

  /**
   * 10 characters per inch.
   */
  public static final Float CPI_10 = new Float(10);

  /**
   * 12 characters per inch.
   */
  public static final Float CPI_12 = new Float(12);

  /**
   * 15 characters per inch.
   */
  public static final Float CPI_15 = new Float(15);

  /**
   * 17 characters per inch.
   */
  public static final Float CPI_17 = new Float(17.14f);

  /**
   * 20 characters per inch.
   */
  public static final Float CPI_20 = new Float(20);

  /**
   * A combo-box for selecting the encoding.
   */
  private EncodingSelector encodingSelector;

  /**
   * A radio button for selecting plain printer commands.
   */
  private JRadioButton rbPlainPrinterCommandSet;

  /**
   * A radio button for selecting Epson 9-pin printer commands.
   */
  private JRadioButton rbEpson9PrinterCommandSet;

  /**
   * A radio button for selecting Epson 24-pin printer commands.
   */
  private JRadioButton rbEpson24PrinterCommandSet;

  /**
   * A radio button for selecting IBM printer commands.
   */
  private JRadioButton rbIBMPrinterCommandSet;

  /**
   * The filename text field.
   */
  private JTextField txFilename;

  /**
   * A combo-box for selecting lines per inch.
   */
  private JComboBox cbLinesPerInch;

  /**
   * A combo-box for selecting characters per inch.
   */
  private JComboBox cbCharsPerInch;

  private JComboBox cbEpson9PrinterType;
  private JComboBox cbEpson24PrinterType;

  private KeyedComboBoxModel epson9Printers;
  private KeyedComboBoxModel epson24Printers;

  /**
   * Creates a non-modal dialog without a title and without a specified Frame owner.  A
   * shared, hidden frame will be set as the owner of the Dialog.
   */
  public PlainTextExportDialog ()
  {
    init();
  }

  /**
   * Creates a non-modal dialog without a title with the specifed Frame as its owner.
   *
   * @param owner the Frame from which the dialog is displayed
   */
  public PlainTextExportDialog (final Frame owner)
  {
    super(owner);
    init();
  }

  /**
   * Creates a non-modal dialog without a title with the specifed Dialog as its owner.
   *
   * @param owner the Dialog from which the dialog is displayed
   */
  public PlainTextExportDialog (final Dialog owner)
  {
    super(owner);
    init();
  }

  /**
   * Initialise the dialog.
   */
  private void init ()
  {
    setCancelAction(new ActionCancel(getResources()));
    setConfirmAction(new ActionConfirm(getResources()));
    setTitle(getResources().getString("plain-text-exportdialog.dialogtitle"));

    epson9Printers = loadEpson9Printers();
    epson24Printers = loadEpson24Printers();

    cbEpson9PrinterType = new JComboBox(epson9Printers);
    cbEpson9PrinterType.addActionListener(new SelectEpsonModelAction());

    cbEpson24PrinterType = new JComboBox(epson24Printers);
    cbEpson24PrinterType.addActionListener(new SelectEpsonModelAction());

    final Float[] lpiModel = {
      LPI_6,
      LPI_10
    };

    final Float[] cpiModel = {
      CPI_10,
      CPI_12,
      CPI_15,
      CPI_17,
      CPI_20
    };

    cbLinesPerInch = new JComboBox(new DefaultComboBoxModel(lpiModel));
    cbCharsPerInch = new JComboBox(new DefaultComboBoxModel(cpiModel));

    final String plainPrinterName = getResources().getString(PRINTER_NAMES[TYPE_PLAIN_OUTPUT]);
    final String epson9PrinterName = getResources().getString(PRINTER_NAMES[TYPE_EPSON9_OUTPUT]);
    final String epson24PrinterName = getResources().getString(PRINTER_NAMES[TYPE_EPSON24_OUTPUT]);
    final String ibmPrinterName = getResources().getString(PRINTER_NAMES[TYPE_IBM_OUTPUT]);

    rbPlainPrinterCommandSet =
      new ActionRadioButton(new ActionSelectPrinter(plainPrinterName, TYPE_PLAIN_OUTPUT));
    rbEpson9PrinterCommandSet =
      new ActionRadioButton(new ActionSelectPrinter(epson9PrinterName, TYPE_EPSON9_OUTPUT));
    rbEpson24PrinterCommandSet =
      new ActionRadioButton(new ActionSelectPrinter(epson24PrinterName, TYPE_EPSON24_OUTPUT));
    rbIBMPrinterCommandSet =
      new ActionRadioButton(new ActionSelectPrinter(ibmPrinterName, TYPE_IBM_OUTPUT));

    txFilename = new JTextField();
    encodingSelector = new EncodingSelector();

    final ButtonGroup bg = new ButtonGroup();
    bg.add(rbPlainPrinterCommandSet);
    bg.add(rbIBMPrinterCommandSet);
    bg.add(rbEpson9PrinterCommandSet);
    bg.add(rbEpson24PrinterCommandSet);

    final JPanel rootPane = new JPanel();
    rootPane.setLayout(new BorderLayout());
    rootPane.add(createButtonPanel(), BorderLayout.SOUTH);
    rootPane.add(createContentPane(), BorderLayout.CENTER);


    final JPanel contentWithStatus = new JPanel();
    contentWithStatus.setLayout(new BorderLayout());
    contentWithStatus.add(rootPane, BorderLayout.CENTER);
    contentWithStatus.add(getStatusBar(), BorderLayout.SOUTH);

    setContentPane(contentWithStatus);

    getFormValidator().registerTextField(txFilename);
    getFormValidator().registerButton(rbEpson24PrinterCommandSet);
    getFormValidator().registerButton(rbEpson9PrinterCommandSet);
    getFormValidator().registerButton(rbIBMPrinterCommandSet);
    getFormValidator().registerButton(rbPlainPrinterCommandSet);

    clear();
  }

  private KeyedComboBoxModel loadEpson24Printers ()
  {
    final KeyedComboBoxModel epsonPrinters = new KeyedComboBoxModel();
    final PrinterSpecificationManager spec24Manager =
            Epson24PinPrinterDriver.loadSpecificationManager();
    final String[] printer24Names =
            spec24Manager.getPrinterNames();
    Arrays.sort(printer24Names);
    for (int i = 0; i < printer24Names.length; i++)
    {
      final PrinterSpecification pspec = spec24Manager.getPrinter(printer24Names[i]);
      epsonPrinters.add(pspec, pspec.getDisplayName());
    }
    return epsonPrinters;
  }

  private KeyedComboBoxModel loadEpson9Printers ()
  {
    final KeyedComboBoxModel epsonPrinters = new KeyedComboBoxModel();
    final PrinterSpecificationManager spec9Manager =
            Epson9PinPrinterDriver.loadSpecificationManager();
    final String[] printer9Names =
            spec9Manager.getPrinterNames();
    Arrays.sort(printer9Names);
    for (int i = 0; i < printer9Names.length; i++)
    {
      final PrinterSpecification pspec = spec9Manager.getPrinter(printer9Names[i]);
      epsonPrinters.add(pspec, pspec.getDisplayName());
    }
    return epsonPrinters;
  }

  /**
   * Creates the content pane for the export dialog.
   *
   * @return the created content pane.
   */
  private JComponent createContentPane ()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblPrinterSelect = new JLabel(getResources().getString("plain-text-exportdialog.printer"));
    final JLabel lblFileName
            = new JLabel(getResources().getString("plain-text-exportdialog.filename"));
    final JLabel lblEncoding
            = new JLabel(getResources().getString("plain-text-exportdialog.encoding"));
    final JButton btnSelect = new ActionButton(new ActionSelectFile(getResources()));

    final JLabel lblCharsPerInch = new JLabel(getResources().getString("plain-text-exportdialog.chars-per-inch"));
    final JLabel lblLinesPerInch = new JLabel(getResources().getString("plain-text-exportdialog.lines-per-inch"));
    final JLabel lblFontSettings = new JLabel(getResources().getString("plain-text-exportdialog.font-settings"));

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
    gbc.gridwidth = 2;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 3;
    gbc.gridy = 0;
    contentPane.add(btnSelect, gbc);

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
    contentPane.add(rbEpson9PrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(cbEpson9PrinterType, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbEpson24PrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(cbEpson24PrinterType, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(encodingSelector, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblFontSettings, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 2;
    gbc.gridy = 6;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblCharsPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbCharsPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 2;
    gbc.gridy = 7;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblLinesPerInch, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 7;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbLinesPerInch, gbc);

    return contentPane;
  }

  /**
   * Creates the button panel for the export dialog.
   *
   * @return the created button panel
   */
  private JPanel createButtonPanel ()
  {
    // button panel
    final JButton btnCancel = new ActionButton(getCancelAction());
    final JButton btnConfirm = new ActionButton(getConfirmAction());
    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1,2,5,5));
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(getConfirmAction(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    final JPanel buttonCarrier = new JPanel();
    buttonCarrier.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonCarrier.add(buttonPanel);
    return buttonCarrier;
  }

  protected void updateEpson9Encoding ()
  {
    final PrinterSpecification spec = (PrinterSpecification)
            epson9Printers.getSelectedKey();
    if (spec == null)
    {
      encodingSelector.setEncodings
              (PrinterSpecificationManager.getGenericPrinter());
    }
    else
    {
      encodingSelector.setEncodings(spec);
    }
  }

  protected void updateEpson24Encoding ()
  {
    final PrinterSpecification spec = (PrinterSpecification)
            epson9Printers.getSelectedKey();
    if (spec == null)
    {
      encodingSelector.setEncodings
              (PrinterSpecificationManager.getGenericPrinter());
    }
    else
    {
      encodingSelector.setEncodings(spec);
    }
  }

  /**
   * Sets the selected printer.
   *
   * @param type the type.
   */
  public void setSelectedPrinter (final int type)
  {
    final String oldEncoding = getEncoding();
    if (type == TYPE_EPSON9_OUTPUT)
    {
      rbEpson9PrinterCommandSet.setSelected(true);
      cbEpson9PrinterType.setEnabled(true);
      cbEpson24PrinterType.setEnabled(false);
      updateEpson9Encoding();
    }
    else if (type == TYPE_EPSON24_OUTPUT)
    {
      rbEpson24PrinterCommandSet.setSelected(true);
      cbEpson24PrinterType.setEnabled(true);
      cbEpson9PrinterType.setEnabled(false);
      updateEpson24Encoding();
    }
    else if (type == TYPE_IBM_OUTPUT)
    {
      rbIBMPrinterCommandSet.setSelected(true);
      cbEpson9PrinterType.setEnabled(false);
      cbEpson24PrinterType.setEnabled(false);
      encodingSelector.setEncodings(new IBMCompatiblePrinterDriver.GenericIBMPrinterSpecification());
    }
    else if (type == TYPE_PLAIN_OUTPUT)
    {
      rbPlainPrinterCommandSet.setSelected(true);
      cbEpson9PrinterType.setEnabled(false);
      cbEpson24PrinterType.setEnabled(false);
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
  public int getSelectedPrinter ()
  {
    if (rbPlainPrinterCommandSet.isSelected())
    {
      return TYPE_PLAIN_OUTPUT;
    }
    if (rbEpson9PrinterCommandSet.isSelected())
    {
      return TYPE_EPSON9_OUTPUT;
    }
    if (rbEpson24PrinterCommandSet.isSelected())
    {
      return TYPE_EPSON24_OUTPUT;
    }
    return TYPE_IBM_OUTPUT;
  }

  /**
   * Returns the filename.
   *
   * @return the name of the file where to save the file.
   */
  public String getFilename ()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the file.
   *
   * @param filename the filename of the file
   */
  public void setFilename (final String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * clears all selections, input fields and set the selected encryption level to none.
   */
  public void clear ()
  {
    txFilename.setText("");
    setSelectedPrinter(TYPE_PLAIN_OUTPUT);
    cbEpson9PrinterType.setEnabled(false);
    cbEpson9PrinterType.setSelectedItem(Epson9PinPrinterDriver.getDefaultPrinter());
    cbEpson24PrinterType.setEnabled(false);
    cbEpson24PrinterType.setSelectedItem(Epson24PinPrinterDriver.getDefaultPrinter());
    cbCharsPerInch.setSelectedItem(CPI_10);
    cbLinesPerInch.setSelectedItem(LPI_6);
    setEncoding(EncodingSupport.getPlatformDefaultEncoding());
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
    if (getSelected9PinPrinterModel() != null)
    {
      p.setProperty("epson-9pin-printer-model", getSelected9PinPrinterModel());
    }
    if (getSelected24PinPrinterModel() != null)
    {
      p.setProperty("epson-24pin-printer-model", getSelected24PinPrinterModel());
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

    setCharsPerInch(StringUtil.parseFloat(p.getProperty("chars-per-inch"), getCharsPerInch()));
    setLinesPerInch(StringUtil.parseFloat(p.getProperty("lines-per-inch"), getLinesPerInch()));

    setEncoding(p.getProperty("encoding", getEncoding()));
    setFilename(p.getProperty("filename", getFilename()));
    setSelected9PinPrinterModel
            (p.getProperty("epson-9pin-printer-model", getSelected9PinPrinterModel()));
    setSelected24PinPrinterModel
            (p.getProperty("epson-24pin-printer-model", getSelected24PinPrinterModel()));
  }

  /**
   * Returns the lines-per-inch setting.
   *
   * @return The lines-per-inch setting.
   */
  public float getLinesPerInch ()
  {
    final Float i = (Float) cbLinesPerInch.getSelectedItem();
    if (i == null)
    {
      return LPI_6.floatValue();
    }
    return i.floatValue();
  }

  /**
   * Sets the lines per inch.
   *
   * @param lpi the lines per inch.
   */
  public void setLinesPerInch (final float lpi)
  {
    final Float lpiObj = new Float(lpi);
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
  public float getCharsPerInch ()
  {
    final Float i = (Float) cbCharsPerInch.getSelectedItem();
    if (i == null)
    {
      return CPI_10.floatValue();
    }
    return i.floatValue();
  }

  /**
   * Sets the characters per inch.
   *
   * @param cpi the characters per inch.
   */
  public void setCharsPerInch (final float cpi)
  {
    final Float cpiObj = new Float(cpi);
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
  public String getEncoding ()
  {
    return encodingSelector.getSelectedEncoding();
  }

  /**
   * Sets the encoding.
   *
   * @param encoding the encoding.
   */
  public void setEncoding (final String encoding)
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
   * @param config the report configuration.
   */
  public void initFromConfiguration (final Configuration config)
  {
    setEncoding(config.getConfigProperty
            (PlainTextOutputTarget.TEXT_OUTPUT_ENCODING,
                    PlainTextOutputTarget.TEXT_OUTPUT_ENCODING_DEFAULT));
    setSelected9PinPrinterModel(config.getConfigProperty
            (Epson9PinPrinterDriver.EPSON_9PIN_PRINTER_TYPE, getSelected9PinPrinterModel()));
    setSelected24PinPrinterModel(config.getConfigProperty
            (Epson24PinPrinterDriver.EPSON_24PIN_PRINTER_TYPE, getSelected24PinPrinterModel()));

    try
    {
      setLinesPerInch(StringUtil.parseFloat(config.getConfigProperty
              (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.LINES_PER_INCH, "6"),
              getLinesPerInch()));
    }
    catch (IllegalArgumentException e)
    {
      // ignore
    }
    try
    {
      setCharsPerInch(StringUtil.parseFloat(config.getConfigProperty
              (PlainTextOutputTarget.CONFIGURATION_PREFIX + PlainTextOutputTarget.CHARS_PER_INCH, "10"),
              getCharsPerInch()));
    }
    catch (IllegalArgumentException e)
    {
      // ignore
    }
  }

  /**
   * Stores the input from the dialog into the report configuration of the report.
   *
   * @param config the report configuration that should receive the new settings.
   */
  public void storeToConfiguration (final ModifiableConfiguration config)
  {
    config.setConfigProperty(PlainTextOutputTarget.TEXT_OUTPUT_ENCODING,
            getEncoding());
    config.setConfigProperty(PlainTextOutputTarget.CONFIGURATION_PREFIX +
            PlainTextOutputTarget.CHARS_PER_INCH, String.valueOf(getCharsPerInch()));
    config.setConfigProperty(PlainTextOutputTarget.CONFIGURATION_PREFIX +
            PlainTextOutputTarget.LINES_PER_INCH, String.valueOf(getLinesPerInch()));
    config.setConfigProperty
          (Epson9PinPrinterDriver.EPSON_9PIN_PRINTER_TYPE, getSelected9PinPrinterModel());
    config.setConfigProperty
          (Epson24PinPrinterDriver.EPSON_24PIN_PRINTER_TYPE, getSelected24PinPrinterModel());
  }

  protected String getConfigurationSuffix ()
  {
    return "_plaintextexport";
  }

  protected String getResourceBaseName ()
  {
    return PlainTextExportPlugin.BASE_RESOURCE_CLASS;
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
              getResources().getString("plain-text-exportdialog.targetIsEmpty"));
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("plain-text-exportdialog.targetIsNoFile"));
        return false;
      }
      if (f.canWrite() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("plain-text-exportdialog.targetIsNotWritable"));
        return false;
      }

      final String message = MessageFormat.format(getResources().getString
              ("plain-text-exportdialog.targetOverwriteWarning"),
              new Object[]{filename});
      getStatusBar().setStatus(JStatusBar.TYPE_WARNING, message);

    }

    return true;
  }

  protected boolean performConfirm ()
  {
    final String filename = getFilename();
    final File f = new File(filename);
    if (f.exists())
    {
      final String key1 = "plain-text-exportdialog.targetOverwriteConfirmation";
      final String key2 = "plain-text-exportdialog.targetOverwriteTitle";
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
   * Warning: Might return null!
   *
   * @return
   */
  public String getSelected9PinPrinterModel ()
  {
    return (String) cbEpson9PrinterType.getSelectedItem();
  }

  public String getSelected24PinPrinterModel ()
  {
    return (String) cbEpson24PrinterType.getSelectedItem();
  }

  public void setSelected9PinPrinterModel (final String selectedPrinterModel)
  {
    final int size = epson9Printers.getSize();
    for (int i = 0; i < size; i++)
    {
      final PrinterSpecification spec =
              (PrinterSpecification) epson9Printers.getKeyAt(i);
      if (spec.getDisplayName().equals(selectedPrinterModel))
      {
        epson9Printers.setSelectedKey(spec);
        return;
      }
    }
    epson9Printers.setSelectedKey(null);
  }

  public void setSelected24PinPrinterModel (final String selectedPrinterModel)
  {
    final int size = epson24Printers.getSize();
    for (int i = 0; i < size; i++)
    {
      final PrinterSpecification spec =
              (PrinterSpecification) epson24Printers.getKeyAt(i);
      if (spec.getDisplayName().equals(selectedPrinterModel))
      {
        epson24Printers.setSelectedKey(spec);
        return;
      }
    }
    epson24Printers.setSelectedKey(null);
  }

  public static void main (final String[] args)
  {
    final PlainTextExportDialog dialog = new PlainTextExportDialog();
    dialog.setModal(true);
    dialog.pack();
    dialog.performQueryForExport(new JFreeReport());
    System.exit(0);
  }
}
