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
 * --------------------------
 * PlainTextExportDialog.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextExportDialog.java,v 1.1 2003/07/07 22:44:06 taqua Exp $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.plaintext;

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
import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
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
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionRadioButton;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.gui.plaintext.resources.PlainTextExportResources;
import org.jfree.report.modules.output.pageable.plaintext.EpsonPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.IBMPrinterCommandSet;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterCommandSet;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.util.NullOutputStream;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.ExtensionFileFilter;

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
  private class ActionSelectFile extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectFile()
    {
      putValue(NAME, getResources().getString("plain-text-exportdialog.selectFile"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performSelectFile();
    }
  }

  /**
   * An action to select a plain printer.
   */
  private class ActionSelectPlainPrinter extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectPlainPrinter()
    {
      putValue(NAME, getResources().getString("plain-text-exportdialog.printer.plain"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSelectedPrinter(TYPE_PLAIN_OUTPUT);
    }
  }

  /**
   * An action to select an Epson printer.
   */
  private class ActionSelectEpsonPrinter extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectEpsonPrinter()
    {
      putValue(NAME, getResources().getString("plain-text-exportdialog.printer.epson"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSelectedPrinter(TYPE_EPSON_OUTPUT);
    }
  }

  /**
   * An action to select an IBM printer.
   */
  private class ActionSelectIBMPrinter extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ActionSelectIBMPrinter()
    {
      putValue(NAME, getResources().getString("plain-text-exportdialog.printer.ibm"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  an action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSelectedPrinter(TYPE_IBM_OUTPUT);
    }
  }

  /** Plain text output. */
  public static final int TYPE_PLAIN_OUTPUT = 0;

  /** Epson printer output. */
  public static final int TYPE_EPSON_OUTPUT = 1;

  /** IBM printer output. */
  public static final int TYPE_IBM_OUTPUT = 2;

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

  /** The plain text encodings. */
  private EncodingComboBoxModel plainTextEncodingModel;

  /** The IBM printer encodings. */
  private EncodingComboBoxModel ibmPrinterEncodingModel;

  /** The Epson printer encodings. */
  private EncodingComboBoxModel epsonPrinterEncodingModel;

  /** The selected encoding model. */
  private EncodingComboBoxModel selectedEncodingModel;

  /** A combo-box for selecting the encoding. */
  private JComboBox cbEncoding;

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

  /** A file chooser. */
  private JFileChooser fileChooser;

  /** The printer command set for generic text printers. */
  private PrinterCommandSet plainTextCommandSet;
  /** The printer command set for ibm compatible text printers. */
  private PrinterCommandSet ibmPrinterCommandSet;
  /** The printer command set for epson compatible text printers. */
  private PrinterCommandSet epsonPrinterCommandSet;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      PlainTextExportResources.class.getName();

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

    plainTextCommandSet = new PrinterCommandSet(new NullOutputStream(),
        new PageFormat(), 10, 6);
    plainTextEncodingModel = createEncodingModel(plainTextCommandSet);

    epsonPrinterCommandSet = new EpsonPrinterCommandSet(new NullOutputStream(),
        new PageFormat(), 10, 6);
    epsonPrinterEncodingModel = createEncodingModel(epsonPrinterCommandSet);

    ibmPrinterCommandSet = new IBMPrinterCommandSet(new NullOutputStream(),
        new PageFormat(), 10, 6);
    ibmPrinterEncodingModel = createEncodingModel(ibmPrinterCommandSet);

    selectedEncodingModel = plainTextEncodingModel;
    cbEncoding = new JComboBox(selectedEncodingModel);

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

    rbPlainPrinterCommandSet = new ActionRadioButton(new ActionSelectPlainPrinter());
    rbEpsonPrinterCommandSet = new ActionRadioButton(new ActionSelectEpsonPrinter());
    rbIBMPrinterCommandSet = new ActionRadioButton(new ActionSelectIBMPrinter());

    txFilename = new JTextField();

    final ButtonGroup bg = new ButtonGroup();
    bg.add(rbPlainPrinterCommandSet);
    bg.add(rbEpsonPrinterCommandSet);
    bg.add(rbIBMPrinterCommandSet);

    final JComponent contentPane = createContentPane();
    final GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    gbc.gridy = 8;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(createButtonPanel(), gbc);
    setContentPane(contentPane);

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
    contentPane.add(rbPlainPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbEpsonPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbIBMPrinterCommandSet, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 0;
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbEncoding, gbc);

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
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.gridheight = 2;
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

    return buttonPanel;
  }

  /**
   * Sets the selected printer.
   *
   * @param type  the type.
   */
  public void setSelectedPrinter(final int type)
  {
    if (type == TYPE_EPSON_OUTPUT)
    {
      rbEpsonPrinterCommandSet.setSelected(true);
      selectedEncodingModel = epsonPrinterEncodingModel;
      cbEncoding.setModel(selectedEncodingModel);
      // transfer the selected encoding ...
      setEncoding(getEncoding());
    }
    else if (type == TYPE_IBM_OUTPUT)
    {
      rbIBMPrinterCommandSet.setSelected(true);
      selectedEncodingModel = ibmPrinterEncodingModel;
      cbEncoding.setModel(selectedEncodingModel);
      // transfer the selected encoding ...
      setEncoding(getEncoding());
    }
    else if (type == TYPE_PLAIN_OUTPUT)
    {
      rbPlainPrinterCommandSet.setSelected(true);
      selectedEncodingModel = plainTextEncodingModel;
      cbEncoding.setModel(selectedEncodingModel);
      // transfer the selected encoding ...
      setEncoding(getEncoding());
    }
    else
    {
      throw new IllegalArgumentException();
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
    rbPlainPrinterCommandSet.setSelected(true);
    selectedEncodingModel = plainTextEncodingModel;
    int idx = selectedEncodingModel.indexOf(System.getProperty("file.encoding", "Cp1251"));
    if (idx == -1 && selectedEncodingModel.getSize() > 0)
    {
      idx = 0;
    }
    cbEncoding.setSelectedIndex(idx);

    cbCharsPerInch.setSelectedItem(CPI_10);
    cbLinesPerInch.setSelectedItem(LPI_6);
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
   * @param i  the lines per inch.
   */
  public void setLinesPerInch(final int i)
  {
    if (i == LPI_10.intValue() || i == LPI_6.intValue())
    {
      cbLinesPerInch.setSelectedItem(new Integer(i));
    }
    else
    {
      throw new IllegalArgumentException();
    }
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
   * @param i  the characters per inch.
   */
  public void setCharsPerInch(final int i)
  {
    if (i == CPI_10.intValue() || i == CPI_12.intValue()
        || i == CPI_15.intValue()
        || i == CPI_17.intValue()
        || i == CPI_20.intValue())
    {
      cbCharsPerInch.setSelectedItem(new Integer(i));
    }
    else
    {
      throw new IllegalArgumentException();
    }
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
      if (selectedEncodingModel.getSize() > 0)
      {
        // return the first selected encoding, unless specified otherwise.
        return selectedEncodingModel.getEncoding(0);
      }
      return PlainTextOutputTarget.getTextTargetEncoding(null);
    }
    else
    {
      return selectedEncodingModel.getEncoding(cbEncoding.getSelectedIndex());
    }
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

    if (cbEncoding.getModel() != selectedEncodingModel)
    {
      throw new IllegalStateException("Model for the combobox is not set up correctly.");
    }

    ensureEncodingAvailable(plainTextCommandSet, plainTextEncodingModel, encoding);
    ensureEncodingAvailable(ibmPrinterCommandSet, ibmPrinterEncodingModel, encoding);
    ensureEncodingAvailable(epsonPrinterCommandSet, epsonPrinterEncodingModel, encoding);

    if (selectedEncodingModel.indexOf(encoding) == -1)
    {
      throw new IllegalStateException("This encoding is not known.");
    }

    final int ibmIndex = ibmPrinterEncodingModel.indexOf(encoding);
    final int epsonIndex = epsonPrinterEncodingModel.indexOf(encoding);
    final int plainIndex = plainTextEncodingModel.indexOf(encoding);

    if (ibmIndex >= 0)
    {
      ibmPrinterEncodingModel.setSelectedIndex(ibmIndex);
      if (ibmPrinterEncodingModel.getSelectedIndex() == -1)
      {
        throw new IllegalStateException("Unable to select encoding for IBM model.");
      }
    }
    if (plainIndex >= 0)
    {
      plainTextEncodingModel.setSelectedIndex(plainIndex);
      if (plainTextEncodingModel.getSelectedIndex() == -1)
      {
        throw new IllegalStateException("Unable to select encoding for plain text model.");
      }
    }
    if (epsonIndex >= 0)
    {
      epsonPrinterEncodingModel.setSelectedIndex(epsonIndex);
      if (epsonPrinterEncodingModel.getSelectedIndex() == -1)
      {
        throw new IllegalStateException("Unable to select encoding for epson model.");
      }
    }
    final int selectedIndex = selectedEncodingModel.indexOf(encoding);
    final Object o = cbEncoding.getModel().getElementAt(selectedIndex);
    cbEncoding.setSelectedItem(o);

  }

  /**
   * Initialises the CSV export dialog from the settings in the report configuration.
   *
   * @param config  the report configuration.
   */
  public void initFromConfiguration(final ReportConfiguration config)
  {
    setEncoding(PlainTextOutputTarget.getTextTargetEncoding(config));
  }

  /**
   * Exports a report to a text file.
   *
   * @param report  the report.
   *
   * @return A boolean.
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
    return writeReport(report);
  }

  /**
   * Returns the printer command set.
   *
   * @param out  the output stream.
   * @param report  the report.
   *
   * @return The printer command set.
   */
  private PrinterCommandSet getPrinterCommandSet(final OutputStream out, final JFreeReport report)
  {
    switch (getSelectedPrinter())
    {
      case TYPE_PLAIN_OUTPUT:
        {
          return new PrinterCommandSet(out, report.getDefaultPageFormat(), getCharsPerInch(),
              getLinesPerInch());
        }
      case TYPE_IBM_OUTPUT:
        {
          return new IBMPrinterCommandSet(out, report.getDefaultPageFormat(), getCharsPerInch(),
              getLinesPerInch());
        }
      case TYPE_EPSON_OUTPUT:
        {
          return new EpsonPrinterCommandSet(out, report.getDefaultPageFormat(), getCharsPerInch(),
              getLinesPerInch());
        }
    }
    throw new IllegalArgumentException();
  }

  /**
   * Writes a report.
   *
   * @param report  the report.
   *
   * @return true, if the report was successfully written, false otherwise.
   */
  public boolean writeReport(final JFreeReport report)
  {
    OutputStream out = null;
    try
    {

      out = new BufferedOutputStream(
          new FileOutputStream(
              new File(getFilename())));
      final PrinterCommandSet pc = getPrinterCommandSet(out, report);
      final PlainTextOutputTarget target = new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
      target.configure(report.getReportConfiguration());
      target.setProperty(PlainTextOutputTarget.ENCODING_PROPERTY, getEncoding());

      final PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setHandleInterruptedState(false);
      proc.setOutputTarget(target);

      target.open();
      proc.processReport();
      target.close();
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
   * Returns the display name for the action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.export-to-plaintext.name");
  }

  /**
   * Returns the short description for the action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.export-to-plaintext.description");
  }

  /**
   * Returns the small icon for the action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject("action.export-to-plaintext.small-icon");
  }

  /**
   * Returns the large icon for an action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject("action.export-to-plaintext.icon");
  }

  /**
   * Returns the accelerator key.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject("action.export-to-plaintext.accelerator");
  }

  /**
   * Returns the mnemonic key.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject("action.export-to-plaintext.mnemonic");
  }

  /**
   * Returns <code>false</code>.
   *
   * @return A boolean.
   */
  public boolean isSeparated()
  {
    return false;
  }

  /**
   * Returns <code>false</code>.
   *
   * @return A boolean.
   */
  public boolean isAddToToolbar()
  {
    return false;
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
   * Checks, whether the printer supports the given encoding and adds it if possible.
   *
   * @param cmd the printer command set that should used for printing.
   * @param model the encoding combobox model that should contain the encoding.
   * @param encoding the specified encoding.
   */
  private void ensureEncodingAvailable
      (final PrinterCommandSet cmd, final EncodingComboBoxModel model, final String encoding)
  {
    if (cmd.isEncodingSupported(encoding))
    {
      model.ensureEncodingAvailable(encoding);
    }
  }

  /**
   * Creates an encoding model.
   *
   * @param cmd  the printer command set.
   *
   * @return The encoding model.
   */
  private EncodingComboBoxModel createEncodingModel(final PrinterCommandSet cmd)
  {
    final EncodingComboBoxModel defaultEncodingModel = EncodingComboBoxModel.createDefaultModel();

    final EncodingComboBoxModel retval = new EncodingComboBoxModel();
    for (int i = 0; i < defaultEncodingModel.getSize(); i++)
    {
      final String encoding = defaultEncodingModel.getEncoding(i);
      if (cmd.isEncodingSupported(encoding))
      {
        final String description = defaultEncodingModel.getDescription(i);
        retval.addEncoding(encoding, description);
      }
    }
    retval.sort();
    return retval;
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      fileChooser.addChoosableFileFilter(new ExtensionFileFilter("Plain text files", ".txt"));
      fileChooser.setMultiSelectionEnabled(false);
    }

    fileChooser.setSelectedFile(new File(getFilename()));
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtil.endsWithIgnoreCase(selFileName, ".txt") == false)
      {
        selFileName = selFileName + ".txt";
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
}
