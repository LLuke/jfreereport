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
 * ---------------------
 * HTMLExportDialog.java
 * ---------------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG) based on PDFSaveDialog;
 * Contributor(s):   Thomas Morgner;
 *                   David Gilbert (for Simba Management Limited); 
 *
 * $Id: HtmlExportDialog.java,v 1.12 2003/02/25 20:15:36 taqua Exp $
 *
 * Changes
 * -------
 * 02-Jan-2003 : Initial version
 * 25-Feb-2003 : Updated Javadocs (DG);
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
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.table.html.DirectoryHtmlFilesystem;
import com.jrefinery.report.targets.table.html.HtmlProcessor;
import com.jrefinery.report.targets.table.html.StreamHtmlFilesystem;
import com.jrefinery.report.targets.table.html.ZIPHtmlFilesystem;
import com.jrefinery.report.targets.table.html.HtmlProducer;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FilesystemFilter;
import com.jrefinery.report.util.IOUtils;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.report.util.StringUtil;

/**
 * A dialog that is used to perform the printing of a report into an HTML file.
 *
 * @author Heiko Evermann
 */
public class HtmlExportDialog extends JDialog implements ExportPlugin
{
  /** Export to a single stream or file. */
  public static final int EXPORT_STREAM = 0;
  
  /** Export to a directory. */
  public static final int EXPORT_DIR = 1;
  
  /** Export to a ZIP file. */
  public static final int EXPORT_ZIP = 2;

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionConfirmZip extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionConfirmZip()
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
      if (performValidateZip())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionConfirmDir extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionConfirmDir()
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
      if (performValidateDir())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  /**
   * Internal action class to confirm the dialog and to validate the input.
   */
  private class ActionConfirmStream extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionConfirmStream()
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
      if (performValidateStream())
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
  private class ActionSelectZipFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectZipFile()
    {
      putValue(Action.NAME, getResources().getString("htmlexportdialog.selectZipFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      performSelectFileZip();
    }
  }

  /**
   * An action to select a directory.
   */
  private class ActionSelectDirFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectDirFile()
    {
      putValue(Action.NAME, getResources().getString("htmlexportdialog.selectDirFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      performSelectFileDir();
    }
  }

  /**
   * An action to select a file for the single-stream report.
   */
  private class ActionSelectStreamFile extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSelectStreamFile()
    {
      putValue(Action.NAME, getResources().getString("htmlexportdialog.selectStreamFile"));
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      performSelectFileStream();
    }
  }

  /** Cancel action. */
  private Action actionCancel;

  /** Filename text field. */
  private JTextField txStreamFilename;

  /** ZIP filename text field. */
  private JTextField txZipFilename;
  
  /** Directory filename text field. */
  private JTextField txDirFilename;
  
  /** ZIP data file name. */
  private JTextField txZipDataFilename;

  /** Directory data file name text field. */
  private JTextField txDirDataFilename;

  /** Title text field. */
  private JTextField txTitle;

  /** Author text field. */
  private JTextField txAuthor;

  /** A combo-box for selecting the encoding. */
  private JComboBox cbEncoding;
  
  /** The encoding data model. */
  private EncodingComboBoxModel encodingModel;

  /** A check-box for selecting 'strict layout'. */
  private JCheckBox cbxStrictLayout;
  
  /** A radio button for selecting XHTML. */
  private JRadioButton rbGenerateXHTML;
  
  /** A radio button for selecting HTML4. */
  private JRadioButton rbGenerateHTML4;
  
  /** A check-box for... */
  private JCheckBox cbxCopyExternalReferencesZip;

  /** A check-boc for... */
  private JCheckBox cbxCopyExternalReferencesDir;

  /** Confirmed flag. */
  private boolean confirmed;

  /** A file chooser for a ZIP file. */
  private JFileChooser fileChooserZip;
  
  /** A file chooser for a directory. */
  private JFileChooser fileChooserDir;
  
  /** A file chooser for a stream. */
  private JFileChooser fileChooserStream;

  /** Tabs for the export selection. */
  private JTabbedPane exportSelection;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  /**
   * Creates a new HTML save dialog.
   *
   * @param owner  the dialog owner.
   */
  public HtmlExportDialog(Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new HTML export dialog.
   *
   * @param owner  the dialog owner.
   */
  public HtmlExportDialog(Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new HTML save dialog.  The created dialog is modal.
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
    setTitle(getResources().getString("htmlexportdialog.dialogtitle"));
    initialize();
    clear();

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        actionCancel.actionPerformed(null);
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
    actionCancel = new ActionCancel();

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

    JLabel lblAuthor = new JLabel(getResources().getString("htmlexportdialog.author"));
    JLabel lblTitel = new JLabel(getResources().getString("htmlexportdialog.title"));
    JLabel lblEncoding = new JLabel(getResources().getString("htmlexportdialog.encoding"));

    txAuthor = new JTextField();
    txTitle = new JTextField();

    encodingModel = EncodingComboBoxModel.createDefaultModel();
    encodingModel.sort();
    cbEncoding = new JComboBox(encodingModel);
    cbxStrictLayout = new JCheckBox(getResources().getString("htmlexportdialog.strict-layout"));
    rbGenerateHTML4 = new JRadioButton(getResources().getString("htmlexportdialog.generate-html4"));
    rbGenerateXHTML = new JRadioButton(getResources().getString("htmlexportdialog.generate-xhtml"));
    ButtonGroup bg = new ButtonGroup();
    bg.add(rbGenerateHTML4);
    bg.add(rbGenerateXHTML);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(lblTitel, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblAuthor, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(lblEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(txTitle, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(txAuthor, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbEncoding, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(cbxStrictLayout, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbGenerateHTML4, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.ipadx = 120;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(rbGenerateXHTML, gbc);

    exportSelection = new JTabbedPane();
    exportSelection.add("Stream export", createStreamExportPanel());
    exportSelection.add("Directory export", createDirExportPanel());
    exportSelection.add("Zip export", createZipExportPanel());

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    contentPane.add(exportSelection, gbc);

    setContentPane(contentPane);

  }

  /**
   * Creates a panel for the directory export.
   * 
   * @return The panel.
   */
  private JPanel createDirExportPanel ()
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());

    JLabel lblDirFileName = new JLabel(getResources().getString("htmlexportdialog.filename"));
    JLabel lblDirDataFileName = new JLabel(
        getResources().getString("htmlexportdialog.datafilename"));
    cbxCopyExternalReferencesDir = new JCheckBox(
        getResources().getString("htmlexportdialog.copy-external-references"));

    txDirDataFilename = new JTextField();
    txDirFilename = new JTextField();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 0;
    contentPane.add(lblDirFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 1;
    contentPane.add(lblDirDataFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    contentPane.add(txDirFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    contentPane.add(txDirDataFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    contentPane.add(cbxCopyExternalReferencesDir, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 2;
    gbc.gridy = 0;
    contentPane.add(new ActionButton(new ActionSelectDirFile()), gbc);

    Action actionConfirm = new ActionConfirmDir();
    JButton btnCancel = new ActionButton(actionCancel);
    JButton btnConfirm = new ActionButton(actionConfirm);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(actionConfirm, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                                       JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 3;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(buttonPanel, gbc);

    fileChooserDir = new JFileChooser();
    fileChooserDir.addChoosableFileFilter(new FilesystemFilter(new String[]{".html", ".htm"}, 
        "Html Documents", true));
    fileChooserDir.setMultiSelectionEnabled(false);

    return contentPane;
  }

  /**
   * Creates a panel for the ZIP file export.
   * 
   * @return The panel.
   */
  private JPanel createZipExportPanel ()
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());

    JLabel lblZipFileName = new JLabel(getResources().getString("htmlexportdialog.filename"));
    JLabel lblZipDataFileName = new JLabel(
        getResources().getString("htmlexportdialog.datafilename"));
    cbxCopyExternalReferencesZip =  new JCheckBox(
        getResources().getString("htmlexportdialog.copy-external-references"));

    txZipDataFilename = new JTextField();
    txZipFilename = new JTextField();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 0;
    contentPane.add(lblZipFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 1;
    contentPane.add(lblZipDataFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    contentPane.add(txZipFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    contentPane.add(txZipDataFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    contentPane.add(cbxCopyExternalReferencesZip, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 2;
    gbc.gridy = 0;
    contentPane.add(new ActionButton(new ActionSelectZipFile()), gbc);

    Action actionConfirm = new ActionConfirmZip();
    JButton btnCancel = new ActionButton(actionCancel);
    JButton btnConfirm = new ActionButton(actionConfirm);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(actionConfirm, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                                       JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(buttonPanel, gbc);

    fileChooserZip = new JFileChooser();
    fileChooserZip.addChoosableFileFilter(new FilesystemFilter(new String[]{".zip", ".jar"}, 
        "Zip Archives", true));
    fileChooserZip.setMultiSelectionEnabled(false);
    return contentPane;
  }

  /**
   * Creates a panel for the stream export.
   * 
   * @return The panel.
   */
  private JPanel createStreamExportPanel ()
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());

    JLabel lblStreamFileName = new JLabel(getResources().getString("htmlexportdialog.filename"));
    txStreamFilename = new JTextField();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 0;
    contentPane.add(lblStreamFileName, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    contentPane.add(txStreamFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 2;
    gbc.gridy = 0;
    contentPane.add(new ActionButton(new ActionSelectStreamFile()), gbc);

    Action actionConfirm = new ActionConfirmStream();
    JButton btnCancel = new ActionButton(actionCancel);
    JButton btnConfirm = new ActionButton(actionConfirm);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(actionConfirm, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                                       JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    gbc.insets = new Insets(10, 0, 0, 0);
    contentPane.add(buttonPanel, gbc);

    fileChooserStream = new JFileChooser();
    fileChooserStream.addChoosableFileFilter(new FilesystemFilter(new String[]{".html", ".htm"}, 
        "Html Documents", true));
    fileChooserStream.setMultiSelectionEnabled(false);

    return contentPane;
  }

  /**
   * Returns the title of the HTML file.
   *
   * @return the title
   */
  public String getHTMLTitle()
  {
    return txTitle.getText();
  }

  /**
   * Defines the title of the HTML file.
   *
   * @param title the title
   */
  public void setHTMLTitle(String title)
  {
    this.txTitle.setText(title);
  }

  /**
   * Gets the author of the dialog. This is not yet implemented in the HTML-Target.
   *
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
   * Gets the confirmation state of the dialog. A confirmed dialog has no invalid
   * settings and the user confirmed any resource conflicts.
   *
   * @return true, if the dialog has been confirmed and the HTML file should be saved,
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
   * Clears all selections and input fields.
   */
  public void clear()
  {
    txAuthor.setText(System.getProperty("user.name"));
    txDirFilename.setText("");
    txDirDataFilename.setText("");
    txZipFilename.setText("");
    txZipDataFilename.setText("");
    txStreamFilename.setText("");
    txTitle.setText("");
    cbEncoding.setSelectedIndex(
        encodingModel.indexOf(System.getProperty ("file.encoding", "Cp1251")));
    cbxCopyExternalReferencesDir.setSelected(false);
    cbxCopyExternalReferencesZip.setSelected(false);
    cbxStrictLayout.setSelected(false);
    rbGenerateHTML4.setSelected(true);
  }

  /**
   * Returns the directory data file name.
   * 
   * @return The file name.
   */
  public String getDirDataFilename()
  {
    return txDirDataFilename.getText();
  }

  /**
   * Sets the directory data file name.
   * 
   * @param dirFilename  the file name.
   */
  public void setDirDataFilename(String dirFilename)
  {
    this.txDirDataFilename.setText (dirFilename);
  }

  /**
   * Returns the directory file name. 
   * 
   * @return The directory file name.
   */
  public String getDirFilename()
  {
    return txDirFilename.getText();
  }

  /**
   * Sets the directory file name.
   * 
   * @param dirFilename  the file name.
   */
  public void setDirFilename(String dirFilename)
  {
    this.txDirFilename.setText (dirFilename);
  }

  /**
   * Returns the zip file name.
   * 
   * @return The file name.
   */
  public String getZipFilename()
  {
    return txZipFilename.getText();
  }

  /**
   * Sets the zip file name.
   * 
   * @param zipFilename  the zip file name.
   */
  public void setZipFilename(String zipFilename)
  {
    this.txZipFilename.setText(zipFilename);
  }

  /**
   * Returns the zip data file name.
   * 
   * @return The zip data file name.
   */
  public String getZipDataFilename()
  {
    return txZipDataFilename.getText();
  }

  /**
   * Sets the zip data file name.
   * 
   * @param zipFilename  the file name.
   */
  public void setZipDataFilename(String zipFilename)
  {
    this.txZipDataFilename.setText(zipFilename);
  }

  /**
   * Returns the stream file name. 
   * 
   * @return The file name.
   */
  public String getStreamFilename()
  {
    return txStreamFilename.getText();
  }

  /**
   * Sets the stream file name.
   * 
   * @param streamFilename  the file name.
   */
  public void setStreamFilename(String streamFilename)
  {
    this.txStreamFilename.setText(streamFilename);
  }

  /**
   * Sets the radio buttons for XHTML or HTML4 generation.
   * 
   * @param generateXHTML  boolean.
   */
  public void setGenerateXHTML (boolean generateXHTML)
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
   * @param s  boolean.
   */
  public void setStrictLayout (boolean s)
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
   * @param encoding  the encoding name.
   */
  public void setEncoding (String encoding)
  {
    cbEncoding.setSelectedIndex(encodingModel.indexOf(encoding));
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFileStream()
  {
    File file = new File(getStreamFilename());
    fileChooserStream.setCurrentDirectory(file);
    fileChooserStream.setSelectedFile(file);
    int option = fileChooserStream.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooserStream.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on html
      if ((StringUtil.endsWithIgnoreCase(selFileName, ".html") == false) 
          && (StringUtil.endsWithIgnoreCase(selFileName, ".htm") == false))
      {
        selFileName = selFileName + ".html";
      }
      setStreamFilename(selFileName);
    }
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFileZip()
  {
    File file = new File(getZipFilename());
    fileChooserZip.setCurrentDirectory(file);
    fileChooserZip.setSelectedFile(file);
    int option = fileChooserZip.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooserZip.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtil.endsWithIgnoreCase(selFileName, ".zip") == false)
      {
        selFileName = selFileName + ".zip";
      }
      setZipFilename(selFileName);
    }
  }

  /**
   * Selects a file to use as target for the report processing.
   */
  protected void performSelectFileDir()
  {
    File file = new File(getDirFilename());
    fileChooserDir.setCurrentDirectory(file);
    fileChooserDir.setSelectedFile(file);
    int option = fileChooserDir.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooserDir.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();
      // Test if ends on html
      if ((StringUtil.endsWithIgnoreCase(selFileName, ".html") == false) 
          && (StringUtil.endsWithIgnoreCase(selFileName, ".htm") == false))
      {
        selFileName = selFileName + ".html";
      }
      setDirFilename(selFileName);
    }
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidateStream()
  {
    String filename = getStreamFilename();
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
                                      getResources().getString(
                                          "htmlexportdialog.targetIsNotWritable"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "htmlexportdialog.targetOverwriteConfirmation";
      String key2 = "htmlexportdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{getStreamFilename()}
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
   * Validates the contents of the dialog's input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidateZip()
  {
    String filename = getZipFilename();
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
                                      getResources().getString(
                                          "htmlexportdialog.targetIsNotWritable"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "htmlexportdialog.targetOverwriteConfirmation";
      String key2 = "htmlexportdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{getZipFilename()}
                                        ),
                                        getResources().getString(key2),
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                                        == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }

    try
    {
      File dataDir = new File (getZipDataFilename());
      File baseDir = new File ("");

      if (IOUtils.getInstance().isSubDirectory(baseDir, dataDir) == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString(
                                          "htmlexportdialog.targetPathIsAbsolute"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    catch (Exception e)
    {
      showExceptionDialog("error.validationfailed", e);
      return false;
    }
    return true;
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * HTML file.
   *
   * @param report  the report being processed.
   * 
   * @return A boolean.
   */
  public boolean performExport(JFreeReport report)
  {
    setModal(true);
    initFromConfiguration(report.getReportConfiguration());
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }
    if (getSelectedExportMethod() == EXPORT_STREAM)
    {
      return writeHtmlStream(report);
    }
    else if (getSelectedExportMethod() == EXPORT_DIR)
    {
      return writeHtmlDir(report);
    }
    else if (getSelectedExportMethod() == EXPORT_ZIP)
    {
      return writeHtmlZip(report);
    }
    return false;
  }

  /**
   * Returns the selected export method.
   * 
   * @return the selected Export method, one of EXPORT_STREAM, EXPORT_ZIP or EXPORT_DIR.
   */
  public int getSelectedExportMethod ()
  {
    return exportSelection.getSelectedIndex();
  }

  /**
   * Sets the export method.
   * 
   * @param index  the method index.
   */
  public void setSelectedExportMethod (int index)
  {
    exportSelection.setSelectedIndex(index);
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performValidateDir()
  {
    String filename = getDirFilename();
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
                                      getResources().getString(
                                          "htmlexportdialog.targetIsNotWritable"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      String key1 = "htmlexportdialog.targetOverwriteConfirmation";
      String key2 = "htmlexportdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{getDirFilename()}
                                        ),
                                        getResources().getString(key2),
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                                        == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }

    File dataDir = new File(getDirDataFilename());
    if (dataDir.isAbsolute() == false)
    {
      dataDir = new File (f.getParentFile(), dataDir.getPath());
    }

    if (dataDir.exists())
    {
      // dataDirectory does exist ... if no directory : fail
      if (dataDir.isDirectory() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString(
                                          "htmlexportdialog.targetDataDirIsNoDirectory"),
                                      getResources().getString("htmlexportdialog.errorTitle"),
                                      JOptionPane.ERROR_MESSAGE);
        return false;

      }
    }
    else
    {
      String key1 = "htmlexportdialog.targetCreateDataDirConfirmation";
      String key2 = "htmlexportdialog.targetCreateDataDirTitle";
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(getResources().getString(key1),
                                            new Object[]{getDirFilename()}
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
   * Writes an HTML stream.
   * 
   * @param report  the report.
   * 
   * @return A boolean.
   */
  public boolean writeHtmlStream(JFreeReport report)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(getStreamFilename())));
      HtmlProcessor target = new HtmlProcessor(report);
      target.setFilesystem(new StreamHtmlFilesystem (out));
      target.setStrictLayout(isStrictLayout());
      target.setProperty(HtmlProducer.ENCODING, getEncoding());
      target.setProperty(HtmlProducer.AUTHOR, getAuthor());
      target.setProperty(HtmlProducer.TITLE, getHTMLTitle());
      target.setGenerateXHTML(isGenerateXHTML());
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
   * Writers the report to HTML inside a ZIP file.
   * 
   * @param report  the report.
   * 
   * @return A boolean.
   */
  public boolean writeHtmlZip(JFreeReport report)
  {
    OutputStream out = null;
    try
    {

      out = new BufferedOutputStream(new FileOutputStream(new File(getZipFilename())));
      HtmlProcessor target = new HtmlProcessor(report);
      target.setFilesystem(new ZIPHtmlFilesystem (out, getZipDataFilename()));
      target.setStrictLayout(isStrictLayout());
      target.setProperty(HtmlProducer.ENCODING, getEncoding());
      target.setProperty(HtmlProducer.AUTHOR, getAuthor());
      target.setProperty(HtmlProducer.TITLE, getHTMLTitle());
      target.setGenerateXHTML(isGenerateXHTML());
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
   * Writes the report to HTML in a directory.
   * 
   * @param report  the report.
   * 
   * @return A boolean.
   */
  public boolean writeHtmlDir(JFreeReport report)
  {
    try
    {
      File targetFile = new File(getDirFilename());
      File targetDataFile = new File(getDirDataFilename());
      if (targetDataFile.isAbsolute() == false)
      {
        targetDataFile = new File (targetFile.getParentFile(), targetDataFile.getPath());
      }
      if (targetDataFile.mkdirs() == false)
      {
        if ((targetDataFile.exists() && targetDataFile.isDirectory()) == false)
        {
          throw new IOException ("Unable to create the mssing directories.");
        }
      }
      DirectoryHtmlFilesystem fs = new DirectoryHtmlFilesystem(targetFile, targetDataFile);
      HtmlProcessor target = new HtmlProcessor(report);
      target.setProperty(HtmlProducer.ENCODING, getEncoding());
      target.setProperty(HtmlProducer.AUTHOR, getAuthor());
      target.setProperty(HtmlProducer.TITLE, getHTMLTitle());
      target.setStrictLayout(isStrictLayout());
      target.setGenerateXHTML(isGenerateXHTML());
      target.setFilesystem(fs);
      target.processReport();
      return true;
    }
    catch (Exception re)
    {
      showExceptionDialog("error.processingfailed", re);
      return false;
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
   * Returns the action display name.
   * 
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString ("action.export-to-html.name");
  }

  /**
   * Returns the short description for the action.
   * 
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString ("action.export-to-html.description");
  }

  /**
   * Returns the small icon for the action.
   * 
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    return (Icon) resources.getObject ("action.export-to-html.small-icon");
  }

  /**
   * Returns the large icon for the action.
   * 
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    return (Icon) resources.getObject ("action.export-to-html.icon");
  }

  /**
   * Returns the accelerator key for the action.
   * 
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return (KeyStroke) resources.getObject ("action.export-to-html.accelerator");
  }

  /**
   * Returns the mnemonic key code for the action.
   * 
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return (Integer) resources.getObject ("action.export-to-html.mnemonic");
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
   * For debugging.
   * 
   * @param args  ignored.
   */
  public static void main (String [] args)
  {
    HtmlExportDialog dialog = new HtmlExportDialog();
    dialog.addWindowListener(new WindowAdapter(){
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
    dialog.pack();
    dialog.setVisible(true);
  }
}
