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
 * ---------------------
 * HTMLExportDialog.java
 * ---------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG) based on PDFSaveDialog;
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlExportDialog.java,v 1.15 2005/03/03 21:50:42 taqua Exp $
 *
 * Changes
 * -------
 * 02-Jan-2003 : Initial version
 * 25-Feb-2003 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.html;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.jfree.io.IOUtils;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.AbstractExportDialog;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.html.HtmlProcessor;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;
import org.jfree.ui.FilesystemFilter;
import org.jfree.ui.action.ActionButton;

/**
 * A dialog that is used to perform the printing of a report into an HTML file.
 *
 * @author Heiko Evermann
 */
public class HtmlExportDialog extends AbstractExportDialog
{
  /**
   * The 'HTML encoding' property key.
   */
  public static final String HTML_OUTPUT_ENCODING
          = "org.jfree.report.modules.output.table.html.Encoding";
  /**
   * A default value of the 'HTML encoding' property key.
   */
  public static final String HTML_OUTPUT_ENCODING_DEFAULT = "UTF-16";

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ConfirmAction extends AbstractConfirmAction
  {
    /**
     * Default constructor.
     */
    public ConfirmAction (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("htmlexportdialog.confirm"));
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
    public CancelAction (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("htmlexportdialog.cancel"));
    }
  }

  /**
   * An action to select the export target file.
   */
  private class ActionSelectDirFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectDirFile (final ResourceBundle resources)
    {
      putValue(Action.NAME, resources.getString("htmlexportdialog.selectDirFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (getSelectedExportMethod() == EXPORT_ZIP)
      {
        performSelectFileZip();
      }
      else
      {
        performSelectFile();
      }
    }
  }

  private class ExportTypeSelectionHandler implements ActionListener
  {
    public ExportTypeSelectionHandler ()
    {
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      performUpdateExportTypeSelection();
    }
  }

  /**
   * Filename text field.
   */
  private JTextField txFilename;

  /**
   * Data file name field.
   */
  private JTextField txDataFilename;

  /**
   * Title text field.
   */
  private JTextField txTitle;

  /**
   * Author text field.
   */
  private JTextField txAuthor;

  /**
   * A combo-box for selecting the encoding.
   */
  private JComboBox cbEncoding;

  /**
   * The encoding data model.
   */
  private EncodingComboBoxModel encodingModel;

  /**
   * A check-box for selecting 'strict layout'.
   */
  private JCheckBox cbxStrictLayout;

  /**
   * A radio button for selecting XHTML.
   */
  private JRadioButton rbGenerateXHTML;

  /**
   * A radio button for selecting HTML4.
   */
  private JRadioButton rbGenerateHTML4;

  /**
   * A check-box for...
   */
  private JCheckBox cbxCopyExternalReferences;

  /**
   * A file chooser for a ZIP file.
   */
  private JFileChooser fileChooserZip;

  /**
   * A file chooser for directory and stream export.
   */
  private JFileChooser fileChooserHtml;

  /**
   * Tabs for the export selection.
   */
  private JRadioButton rbExportToStream;
  private JRadioButton rbExportToDirectory;
  private JRadioButton rbExportToZIPFile;

  public static final int EXPORT_STREAM = 0;
  public static final int EXPORT_DIR = 1;
  public static final int EXPORT_ZIP = 2;

  /**
   * Creates a new HTML save dialog.
   *
   * @param owner the dialog owner.
   */
  public HtmlExportDialog (final Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new HTML export dialog.
   *
   * @param owner the dialog owner.
   */
  public HtmlExportDialog (final Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new HTML save dialog.  The created dialog is modal.
   */
  public HtmlExportDialog ()
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

    setTitle(getResources().getString("htmlexportdialog.dialogtitle"));
    initialize();
    clear();
  }

  /**
   * Initializes the Swing components of this dialog.
   */
  private void initialize ()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    final JLabel lblAuthor = new JLabel(getResources().getString("htmlexportdialog.author"));
    final JLabel lblTitel = new JLabel(getResources().getString("htmlexportdialog.title"));
    final JLabel lblEncoding = new JLabel(getResources().getString("htmlexportdialog.encoding"));
    final JLabel lblDirFileName = new JLabel(getResources().getString("htmlexportdialog.filename"));
    final JLabel lblDirDataFileName = new JLabel(getResources().getString("htmlexportdialog.datafilename"));
    final JLabel lblExportMethod = new JLabel(getResources().getString("htmlexportdialog.exportMethod"));
    final JLabel lblExportFormat = new JLabel(getResources().getString("htmlexportdialog.exportFormat"));

    txAuthor = new JTextField();
    txTitle = new JTextField();
    txFilename = new JTextField();
    txDataFilename = new JTextField();

    encodingModel = EncodingComboBoxModel.createDefaultModel();
    encodingModel.sort();

    cbEncoding = new JComboBox(encodingModel);
    cbxStrictLayout = new JCheckBox(getResources().getString("htmlexportdialog.strict-layout"));
    cbxCopyExternalReferences = new JCheckBox(getResources().getString("htmlexportdialog.copy-external-references"));
    rbGenerateHTML4 = new JRadioButton(getResources().getString("htmlexportdialog.generate-html4"));
    rbGenerateXHTML = new JRadioButton(getResources().getString("htmlexportdialog.generate-xhtml"));

    rbExportToDirectory = new JRadioButton(getResources().getString
            ("htmlexportdialog.directory-export"));
    rbExportToStream = new JRadioButton(getResources().getString
            ("htmlexportdialog.stream-export"));
    rbExportToZIPFile = new JRadioButton(getResources().getString
            ("htmlexportdialog.zip-export"));


    final ExportTypeSelectionHandler exportTypeHandler = new ExportTypeSelectionHandler();
    rbExportToDirectory.addActionListener(exportTypeHandler);
    rbExportToZIPFile.addActionListener(exportTypeHandler);
    rbExportToStream.addActionListener(exportTypeHandler);
    final ButtonGroup bgExport = new ButtonGroup();
    bgExport.add(rbExportToDirectory);
    bgExport.add(rbExportToZIPFile);
    bgExport.add(rbExportToStream);

    final ButtonGroup bg = new ButtonGroup();
    bg.add(rbGenerateHTML4);
    bg.add(rbGenerateXHTML);


    final JPanel exportTypeSelectionPanel = new JPanel();
    exportTypeSelectionPanel.setLayout(new GridLayout(1, 3, 0, 0));
    exportTypeSelectionPanel.add(rbExportToDirectory);
    exportTypeSelectionPanel.add(rbExportToZIPFile);
    exportTypeSelectionPanel.add(rbExportToStream);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(1, 1, 1, 5);
    contentPane.add(lblExportMethod, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(exportTypeSelectionPanel, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 5);
    contentPane.add(lblDirFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.weightx = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 2;
    gbc.gridy = 1;
    contentPane.add(new ActionButton(new ActionSelectDirFile(getResources())), gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 5);
    contentPane.add(lblDirDataFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.weightx = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(txDataFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.weightx = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbxCopyExternalReferences, gbc);


    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(8, 1, 8, 1);
    gbc.weightx = 1;
    contentPane.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);


    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbxStrictLayout, gbc);


    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblExportFormat, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 7;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 0, 1);
    contentPane.add(rbGenerateHTML4, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 1, 1, 1);
    contentPane.add(rbGenerateXHTML, gbc);


    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(8, 1, 8, 1);
    gbc.weightx = 1;
    contentPane.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);


    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(lblTitel, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 11;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblAuthor, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 10;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txTitle, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 11;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(txAuthor, gbc);


    final JButton btnCancel = new ActionButton(getCancelAction());
    final JButton btnConfirm = new ActionButton(getConfirmAction());

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(getConfirmAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 15;
    gbc.insets = new Insets(10, 0, 10, 0);
    contentPane.add(buttonPanel, gbc);


    final JPanel contentWithStatus = new JPanel();
    contentWithStatus.setLayout(new BorderLayout());
    contentWithStatus.add(contentPane, BorderLayout.CENTER);
    contentWithStatus.add(getStatusBar(), BorderLayout.SOUTH);

    setContentPane(contentWithStatus);

    getFormValidator().registerButton(cbxStrictLayout);
    getFormValidator().registerTextField(txFilename);
    getFormValidator().registerTextField(txDataFilename);
    getFormValidator().registerComboBox(cbEncoding);
    getFormValidator().registerButton(rbExportToDirectory);
    getFormValidator().registerButton(rbExportToStream);
    getFormValidator().registerButton(rbExportToZIPFile);

  }

  /**
   * Returns the title of the HTML file.
   *
   * @return the title
   */
  public String getHTMLTitle ()
  {
    return txTitle.getText();
  }

  /**
   * Defines the title of the HTML file.
   *
   * @param title the title
   */
  public void setHTMLTitle (final String title)
  {
    this.txTitle.setText(title);
  }

  /**
   * Gets the author of the dialog. This is not yet implemented in the HTML-Target.
   *
   * @return the name of the author of this report.
   */
  public String getAuthor ()
  {
    return txAuthor.getText();
  }

  /**
   * Defines the Author of the report. Any freeform text is valid. This defaults to the
   * value of the systemProperty "user.name".
   *
   * @param author the name of the author.
   */
  public void setAuthor (final String author)
  {
    this.txAuthor.setText(author);
  }

  /**
   * Clears all selections and input fields.
   */
  public void clear ()
  {
    txAuthor.setText(ReportConfiguration.getGlobalConfig().getConfigProperty("user.name", ""));
    txFilename.setText("");
    txDataFilename.setText("");
    txTitle.setText("");
    cbEncoding.setSelectedIndex(encodingModel.indexOf
            (ReportConfiguration.getPlatformDefaultEncoding()));
    cbxCopyExternalReferences.setSelected(false);
    cbxStrictLayout.setSelected(false);
    rbGenerateHTML4.setSelected(true);
    rbExportToDirectory.setSelected(true);
  }

  /**
   * Returns the user input of this dialog as properties collection.
   *
   * @return the user input.
   */
  public Properties getDialogContents ()
  {
    final Properties p = new Properties();
    p.setProperty("author", getAuthor());
    p.setProperty("data-file", getDataFilename());
    p.setProperty("filename", getFilename());
    p.setProperty("encoding", getEncoding());
    p.setProperty("html.title", getHTMLTitle());

    p.setProperty("selected-exportmethod", String.valueOf(getSelectedExportMethod()));
    p.setProperty("strict-layout", String.valueOf(isStrictLayout()));
    p.setProperty("generate-xhtml", String.valueOf(isGenerateXHTML()));
    return p;
  }

  /**
   * Restores the user input from a properties collection.
   *
   * @param p the user input.
   */
  public void setDialogContents (final Properties p)
  {
    setAuthor(p.getProperty("author", getAuthor()));
    setHTMLTitle(p.getProperty("html.title", getHTMLTitle()));

    setDataFilename(p.getProperty("data-file", getDataFilename()));
    setFilename(p.getProperty("filename", getFilename()));
    setEncoding(p.getProperty("encoding", getEncoding()));

    setSelectedExportMethod(StringUtil.parseInt(p.getProperty("selected-exportmethod"), getSelectedExportMethod()));
    setStrictLayout(StringUtil.parseBoolean(p.getProperty("strict-layout"), isStrictLayout()));
    setGenerateXHTML(StringUtil.parseBoolean(p.getProperty("generate-xhtml"), isGenerateXHTML()));
  }

  /**
   * Returns the directory data file name.
   *
   * @return The file name.
   */
  public String getDataFilename ()
  {
    return txDataFilename.getText();
  }

  /**
   * Sets the directory data file name.
   *
   * @param dirFilename the file name.
   */
  public void setDataFilename (final String dirFilename)
  {
    this.txDataFilename.setText(dirFilename);
  }

  /**
   * Returns the directory file name.
   *
   * @return The directory file name.
   */
  public String getFilename ()
  {
    return txFilename.getText();
  }

  /**
   * Sets the directory file name.
   *
   * @param dirFilename the file name.
   */
  public void setFilename (final String dirFilename)
  {
    this.txFilename.setText(dirFilename);
  }

  /**
   * Sets the radio buttons for XHTML or HTML4 generation.
   *
   * @param generateXHTML boolean.
   */
  public void setGenerateXHTML (final boolean generateXHTML)
  {
    if (generateXHTML)
    {
      this.rbGenerateXHTML.setSelected(true);
    }
    else
    {
      this.rbGenerateHTML4.setSelected(true);
    }
  }

  /**
   * Returns true if XHTML is selected, false if HTML4.
   *
   * @return A boolean.
   */
  public boolean isGenerateXHTML ()
  {
    return rbGenerateXHTML.isSelected();
  }

  /**
   * Returns the setting of the 'strict layout' check-box.
   *
   * @return A boolean.
   */
  public boolean isStrictLayout ()
  {
    return cbxStrictLayout.isSelected();
  }

  /**
   * Sets the 'strict layout' check-box.
   *
   * @param s boolean.
   */
  public void setStrictLayout (final boolean s)
  {
    cbxStrictLayout.setSelected(s);
  }

  /**
   * Returns the selected encoding.
   *
   * @return The encoding name.
   */
  public String getEncoding ()
  {
    if (cbEncoding.getSelectedIndex() == -1)
    {
      return ReportConfiguration.getPlatformDefaultEncoding();
    }
    else
    {
      return encodingModel.getEncoding(cbEncoding.getSelectedIndex());
    }
  }

  /**
   * Sets the encoding.
   *
   * @param encoding the encoding name.
   */
  public void setEncoding (final String encoding)
  {
    cbEncoding.setSelectedIndex(encodingModel.indexOf(encoding));
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFile ()
  {
    final File file = new File(getFilename());

    if (fileChooserHtml == null)
    {
      fileChooserHtml = new JFileChooser();
      fileChooserHtml.addChoosableFileFilter
              (new FilesystemFilter(new String[]{".html", ".htm"},
                      getResources().getString("htmlexportdialog.html-documents"), true));
      fileChooserHtml.setMultiSelectionEnabled(false);
    }

    fileChooserHtml.setCurrentDirectory(file);
    fileChooserHtml.setSelectedFile(file);
    final int option = fileChooserHtml.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooserHtml.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on html
      if ((StringUtil.endsWithIgnoreCase(selFileName, ".html") == false)
              && (StringUtil.endsWithIgnoreCase(selFileName, ".htm") == false))
      {
        selFileName = selFileName + ".html";
      }
      setFilename(selFileName);
    }
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFileZip ()
  {
    final File file = new File(getFilename());

    if (fileChooserZip == null)
    {
      fileChooserZip = new JFileChooser();
      fileChooserZip.addChoosableFileFilter
              (new FilesystemFilter(new String[]{".zip", ".jar"},
                      getResources().getString("htmlexportdialog.zip-archives"), true));
      fileChooserZip.setMultiSelectionEnabled(false);
    }

    fileChooserZip.setCurrentDirectory(file);
    fileChooserZip.setSelectedFile(file);
    final int option = fileChooserZip.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooserZip.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtil.endsWithIgnoreCase(selFileName, ".zip") == false)
      {
        selFileName = selFileName + ".zip";
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
              getResources().getString("htmlexportdialog.targetIsEmpty"));
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("htmlexportdialog.targetIsNoFile"));
        return false;
      }
      if (f.canWrite() == false)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                getResources().getString("htmlexportdialog.targetIsNotWritable"));
        return false;
      }

      final String message = MessageFormat.format(getResources().getString
              ("htmlexportdialog.targetExistsWarning"),
              new Object[]{filename});
      getStatusBar().setStatus(JStatusBar.TYPE_WARNING, message);
    }

    if (rbExportToZIPFile.isSelected())
    {
      try
      {
        final File dataDir = new File(getDataFilename());
        final File baseDir = new File("");

        if (IOUtils.getInstance().isSubDirectory(baseDir, dataDir) == false)
        {
          getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                  getResources().getString("htmlexportdialog.targetPathIsAbsolute"));
          return false;
        }
      }
      catch (Exception e)
      {
        getStatusBar().setStatus(JStatusBar.TYPE_ERROR, "error.validationfailed");
        return false;
      }
    }
    else if (rbExportToDirectory.isSelected())
    {

      File dataDir = new File(getDataFilename());
      if (dataDir.isAbsolute() == false)
      {
        dataDir = new File(f.getParentFile(), dataDir.getPath());
      }

      if (dataDir.exists())
      {
        // dataDirectory does exist ... if no directory : fail
        if (dataDir.isDirectory() == false)
        {
          getStatusBar().setStatus(JStatusBar.TYPE_ERROR,
                  getResources().getString("htmlexportdialog.targetDataDirIsNoDirectory"));
          return false;
        }
      }
    }

    return true;
  }

  protected boolean performConfirm ()
  {
    final String filename = getFilename();
    final File f = new File(filename);
    if (f.exists())
    {
      final String key1 = "htmlexportdialog.targetOverwriteConfirmation";
      final String key2 = "htmlexportdialog.targetOverwriteTitle";
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

    if (rbExportToDirectory.isSelected())
    {
      final File dataDir = new File(getDataFilename());
      if (dataDir.exists() == false)
      {
        final String dataDirKey1 = "htmlexportdialog.targetCreateDataDirConfirmation";
        final String dataDirKey2 = "htmlexportdialog.targetCreateDataDirTitle";
        if (JOptionPane.showConfirmDialog(this,
                MessageFormat.format(getResources().getString(dataDirKey1),
                        new Object[]{getDataFilename()}),
                getResources().getString(dataDirKey2),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.NO_OPTION)
        {
          return false;
        }
      }
    }
    return true;
  }

  protected String getConfigurationSuffix ()
  {
    return "_htmlexport";
  }

  /**
   * Returns the selected export method.
   *
   * @return the selected Export method, one of EXPORT_STREAM, EXPORT_ZIP or EXPORT_DIR.
   */
  public int getSelectedExportMethod ()
  {
    if (rbExportToStream.isSelected())
    {
      return EXPORT_STREAM;
    }
    else if (rbExportToZIPFile.isSelected())
    {
      return EXPORT_ZIP;
    }
    return EXPORT_DIR;
  }

  /**
   * Sets the export method.
   *
   * @param index the method index.
   */
  public void setSelectedExportMethod (final int index)
  {
    if (index == EXPORT_DIR)
    {
      rbExportToDirectory.setSelected(true);
    }
    else if (index == EXPORT_STREAM)
    {
      rbExportToStream.setSelected(true);
    }
    else
    {
      rbExportToZIPFile.setSelected(true);
    }
  }

  /**
   * Initialises the Html export dialog from the settings in the report configuration.
   *
   * @param config the report configuration.
   */
  public void initFromConfiguration (final ReportConfiguration config)
  {
    final String strict = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX +
            TableProcessor.STRICT_LAYOUT,
                    config.getConfigProperty(TableProcessor.STRICT_LAYOUT,
                            TableProcessor.STRICT_LAYOUT_DEFAULT));
    setStrictLayout(strict.equals("true"));

    setAuthor
            (config.getConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
                TableProcessor.AUTHOR, getAuthor()));

    setHTMLTitle
            (config.getConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
                TableProcessor.TITLE, getHTMLTitle()));

    final String encoding = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX +
            HtmlProcessor.ENCODING, HtmlProcessor.ENCODING_DEFAULT);
    encodingModel.ensureEncodingAvailable(encoding);
    setEncoding(encoding);

    final String isXHTML = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + HtmlProcessor.GENERATE_XHTML);
    setGenerateXHTML("true".equals(isXHTML));
  }

  /**
   * Stores the input from the dialog into the report configuration of the report.
   *
   * @param config the report configuration that should receive the new settings.
   */
  public void storeToConfiguration (final ReportConfiguration config)
  {
    config.setConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
            HtmlProcessor.ENCODING, getEncoding());
    config.setConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
        TableProcessor.AUTHOR, getAuthor());
    config.setConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
        TableProcessor.TITLE, getHTMLTitle());
    config.setConfigProperty(HtmlProcessor.CONFIGURATION_PREFIX +
        TableProcessor.STRICT_LAYOUT, String.valueOf(isStrictLayout()));
  }

  protected String getResourceBaseName ()
  {
    return HtmlExportPlugin.BASE_RESOURCE_CLASS;
  }

  /**
   * 
   */
  protected void performUpdateExportTypeSelection()
  {
    if (rbExportToDirectory.isSelected())
    {
      txDataFilename.setEnabled(true);
      cbxCopyExternalReferences.setEnabled(true);
    }
    else if (rbExportToStream.isSelected())
    {
      txDataFilename.setEnabled(false);
      cbxCopyExternalReferences.setEnabled(false);
    }
    else if (rbExportToZIPFile.isSelected())
    {
      txDataFilename.setEnabled(true);
      cbxCopyExternalReferences.setEnabled(true);
    }
  }

  public static void main (final String[] args)
  {
    final HtmlExportDialog dialog = new HtmlExportDialog();
    dialog.setModal(true);
    dialog.pack();
    dialog.performQueryForExport(new JFreeReport());
    System.exit(0);
  }
}
