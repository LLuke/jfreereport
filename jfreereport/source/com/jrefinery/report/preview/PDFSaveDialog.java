/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * PDFSaveDialog.java
 * ------------------
 *
 * $Id: PDFSaveDialog.java,v 1.13 2002/11/27 12:20:33 taqua Exp $
 *
 * Changes
 * --------
 * 26-Aug-2002 : Initial version
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 31-Aug-2002 : Documentation
 * 01-Sep-2002 : Printing security was inaccurate; bug (exception in JOptionPane) when file exists
 *               More documentation
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.output.PDFOutputTarget;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.ReportConfiguration;
import com.jrefinery.ui.ExtensionFileFilter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * The PDFSaveDialog is used to perform the printing of a report into a PDF file. It is primarily
 * used to edit the properties of the PDFOutputTarget before the target is used to print the
 * report.
 * <p>
 * The main method to call the dialog is PDFSaveDialog.savePDF(). Given a report and a pageformat,
 * the dialog is shown and if the user approved the dialog, the pdf is saved using the settings
 * made in the dialog.
 *
 * @author TM
 */
public class PDFSaveDialog extends JDialog
{
  /** Useful constant. */
  private static final int CBMODEL_NOPRINTING = 0;

  /** Useful constant. */
  private static final int CBMODEL_DEGRADED = 1;

  /** Useful constant. */
  private static final int CBMODEL_FULL = 2;

  /**
   * Internal action class to enable/disable the Security-Settings panel. Without encryption a
   * pdf file cannot have any security settings enabled.
   */
  private class ActionSecuritySelection extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public ActionSecuritySelection()
    {
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      boolean b = (rbSecurityNone.isSelected() == false);
      txUserPassword.setEnabled(b);
      txOwnerPassword.setEnabled(b);
      txConfOwnerPassword.setEnabled(b);
      txConfUserPassword.setEnabled(b);
      cxAllowAssembly.setEnabled(b);
      cxAllowCopy.setEnabled(b);
      cbAllowPrinting.setEnabled(b);
      cxAllowFillIn.setEnabled(b);
      cxAllowModifyAnnotations.setEnabled(b);
      cxAllowModifyContents.setEnabled(b);
      cxAllowScreenReaders.setEnabled(b);
    }
  }

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
      putValue(Action.NAME, getResources().getString("pdfsavedialog.confirm"));
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
      putValue(Action.NAME, getResources().getString("pdfsavedialog.cancel"));
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
      putValue(Action.NAME, getResources().getString("pdfsavedialog.selectFile"));
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

  /** Security selection action. */
  private Action actionSecuritySelection;

  /** Select file action. */
  private Action actionSelectFile;

  /** Filename text field. */
  private JTextField txFilename;

  /** Author text field. */
  private JTextField txAuthor;

  /** Title text field. */
  private JTextField txTitle;

  /** Security (none) radio button. */
  private JRadioButton rbSecurityNone;

  /** Security (40 bit) radio button. */
  private JRadioButton rbSecurity40Bit;

  /** Security (128 bit) radio button. */
  private JRadioButton rbSecurity128Bit;

  /** User password text field. */
  private JTextField txUserPassword;

  /** Owner password text field. */
  private JTextField txOwnerPassword;

  /** Confirm user password text field. */
  private JTextField txConfUserPassword;

  /** Confirm ownder password text field. */
  private JTextField txConfOwnerPassword;

  /** Allow copy check box. */
  private JCheckBox cxAllowCopy;

  /** Allow screen readers check box. */
  private JCheckBox cxAllowScreenReaders;

  /** Allow printing check box. */
  private JComboBox cbAllowPrinting;

  /** Allow assembly check box. */
  private JCheckBox cxAllowAssembly;

  /** Allow modify contents check box. */
  private JCheckBox cxAllowModifyContents;

  /** Allow modify annotations check box. */
  private JCheckBox cxAllowModifyAnnotations;

  /** Allow fill in check box. */
  private JCheckBox cxAllowFillIn;

  /** Combo box for selecting the printing model. */
  private DefaultComboBoxModel printingModel;

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
   * Creates a new PDF save dialog.
   *
   * @param owner  the dialog owner.
   */
  public PDFSaveDialog(Frame owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new PDF save dialog.
   *
   * @param owner  the dialog owner.
   */
  public PDFSaveDialog(Dialog owner)
  {
    super(owner);
    initConstructor();
  }

  /**
   * Creates a new PDF save dialog.  The created dialog is modal.
   */
  public PDFSaveDialog()
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
    setTitle(getResources().getString("pdfsavedialog.dialogtitle"));
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
   * @return the combobox model containing the different values for the allowPrinting option.
   */
  private DefaultComboBoxModel getPrintingComboBoxModel()
  {
    if (printingModel == null)
    {
      Object[] data = {
        getResources().getString("pdfsavedialog.option.noprinting"),
        getResources().getString("pdfsavedialog.option.degradedprinting"),
        getResources().getString("pdfsavedialog.option.fullprinting")
      };
      printingModel = new DefaultComboBoxModel(data);
    }
    return printingModel;
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
   * Returns a single instance of the security selection action.
   *
   * @return the action.
   */
  private Action getActionSecuritySelection()
  {
    if (actionSecuritySelection == null)
    {
      actionSecuritySelection = new ActionSecuritySelection();
    }
    return actionSecuritySelection;
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

    JLabel lblFileName = new JLabel(getResources().getString("pdfsavedialog.filename"));
    JLabel lblAuthor = new JLabel(getResources().getString("pdfsavedialog.author"));
    JLabel lblTitel = new JLabel(getResources().getString("pdfsavedialog.title"));
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

    JPanel securityPanel = new JPanel();
    securityPanel.setLayout(new GridBagLayout());
    securityPanel.setBorder(
        BorderFactory.createTitledBorder(getResources().getString("pdfsavedialog.security")));

    rbSecurityNone = new JRadioButton(getResources().getString("pdfsavedialog.securityNone"));
    rbSecurity40Bit = new JRadioButton(getResources().getString("pdfsavedialog.security40bit"));
    rbSecurity128Bit = new JRadioButton(getResources().getString("pdfsavedialog.security128bit"));

    rbSecurityNone.addActionListener(getActionSecuritySelection());
    rbSecurity40Bit.addActionListener(getActionSecuritySelection());
    rbSecurity128Bit.addActionListener(getActionSecuritySelection());

    txUserPassword = new JPasswordField();
    txConfUserPassword = new JPasswordField();
    txOwnerPassword = new JPasswordField();
    txConfOwnerPassword = new JPasswordField();

    JLabel lblUserPass = new JLabel(getResources().getString("pdfsavedialog.userpassword"));
    JLabel lblUserPassConfirm =
        new JLabel(getResources().getString("pdfsavedialog.userpasswordconfirm"));
    JLabel lblOwnerPass =
        new JLabel(getResources().getString("pdfsavedialog.ownerpassword"));
    JLabel lblOwnerPassConfirm =
        new JLabel(getResources().getString("pdfsavedialog.ownerpasswordconfirm"));
    JLabel lbAllowPrinting =
        new JLabel(getResources().getString("pdfsavedialog.allowPrinting"));

    cxAllowCopy = new JCheckBox(getResources().getString("pdfsavedialog.allowCopy"));
    cbAllowPrinting = new JComboBox(getPrintingComboBoxModel());
    cxAllowScreenReaders =
        new JCheckBox(getResources().getString("pdfsavedialog.allowScreenreader"));

    cxAllowAssembly = new JCheckBox(getResources().getString("pdfsavedialog.allowAssembly"));
    cxAllowModifyContents =
        new JCheckBox(getResources().getString("pdfsavedialog.allowModifyContents"));
    cxAllowModifyAnnotations =
        new JCheckBox(getResources().getString("pdfsavedialog.allowModifyAnnotations"));
    cxAllowFillIn = new JCheckBox(getResources().getString("pdfsavedialog.allowFillIn"));

    JPanel pnlSecurityConfig = new JPanel();
    pnlSecurityConfig.setLayout(new GridLayout());
    pnlSecurityConfig.add(rbSecurityNone);
    pnlSecurityConfig.add(rbSecurity40Bit);
    pnlSecurityConfig.add(rbSecurity128Bit);

    ButtonGroup btGrpSecurity = new ButtonGroup();
    btGrpSecurity.add(rbSecurity128Bit);
    btGrpSecurity.add(rbSecurity40Bit);
    btGrpSecurity.add(rbSecurityNone);

    rbSecurity128Bit.setSelected(true);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(pnlSecurityConfig, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(lblUserPass, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.ipadx = 120;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(txUserPassword, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(lblOwnerPass, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.ipadx = 120;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(txOwnerPassword, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(lblUserPassConfirm, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.ipadx = 120;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(txConfUserPassword, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(lblOwnerPassConfirm, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.ipadx = 120;
    gbc.insets = new Insets(5, 5, 5, 5);
    securityPanel.add(txConfOwnerPassword, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowCopy, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowScreenReaders, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowFillIn, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowAssembly, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowModifyContents, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowModifyAnnotations, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 1;
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(lbAllowPrinting, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cbAllowPrinting, gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 3;
    gbc.gridy = 4;
    gbc.insets = new Insets(10, 0, 0, 0);
    gbc.anchor = GridBagConstraints.NORTH;
    contentPane.add(securityPanel, gbc);

    btnCancel = new ActionButton(getActionCancel());
    btnConfirm = new ActionButton(getActionConfirm());
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.add(btnConfirm);
    buttonPanel.add(btnCancel);
    btnConfirm.setDefaultCapable(true);
    buttonPanel.registerKeyboardAction(getActionConfirm(), KeyStroke.getKeyStroke('\n'), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    securityPanel.registerKeyboardAction(getActionConfirm(), KeyStroke.getKeyStroke('\n'), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
   * returns the defined user password for the pdf file. The user password limits read-only access
   * to the pdf in the PDF-Viewer. The reader/user has to enter the password when opening the file.
   *
   * @return the defined password. The password can be null.
   */
  public String getUserPassword()
  {
    String txt = txUserPassword.getText();
    if (txt.equals(""))
    {
      return null;
    }
    return txt;
  }

  /**
   * Defines the user password for the pdf file. The user password limits read-only access
   * to the pdf in the PDF-Viewer. The reader/user has to enter the password when opening the file.
   *
   * @param userPassword the defined password. The password can be null.
   */
  public void setUserPassword(String userPassword)
  {
    txUserPassword.setText(userPassword);
    txConfUserPassword.setText(userPassword);
  }

  /**
   * Returns the owner password for the pdf file. The owner password limits writing access
   * to the pdf in the PDF-Editor. The user has to enter the password when opening the file
   * to enable editing functionality or to modify the file.
   *
   * @return the defined password. The password can be null.
   */
  public String getOwnerPassword()
  {
    String txt = txOwnerPassword.getText();
    if (txt.equals(""))
    {
      return null;
    }
    return txt;
  }

  /**
   * Defines the owner password for the pdf file. The owner password limits writing access
   * to the pdf in the PDF-Editor. The user has to enter the password when opening the file
   * to enable editing functionality or to modify the file.
   *
   * @param ownerPassword the defined password. The password can be null.
   */
  public void setOwnerPassword(String ownerPassword)
  {
    txOwnerPassword.setText(ownerPassword);
    txConfOwnerPassword.setText(ownerPassword);
  }

  /**
   * @return true if a low quality printing is allowed, false otherwise.
   */
  public boolean isAllowDegradedPrinting()
  {
    return cbAllowPrinting.getSelectedIndex() == CBMODEL_DEGRADED;
  }

  /**
   * Defines whether low quality printing is allowed for the pdf file. Be aware that
   * the full-quality printing ability does also imply that low quality printing is allowed.
   * <p>
   * @param allowDegradedPrinting the boolean that defines whether to allow lowquality printing.
   */
  public void setAllowDegradedPrinting(boolean allowDegradedPrinting)
  {
    this.cbAllowPrinting.setSelectedIndex(CBMODEL_DEGRADED);
  }

  /**
   * @return true, if the generated pdf may be reassembled using an pdf editor.
   */
  public boolean isAllowAssembly()
  {
    return cxAllowAssembly.isSelected();
  }

  /**
   * Defines whether the generated pdf may be reassembled.
   *
   * @param allowAssembly the flag.
   */
  public void setAllowAssembly(boolean allowAssembly)
  {
    this.cxAllowAssembly.setSelected(allowAssembly);
  }

  /**
   * Defines whether the generated pdf may accessed using screenreaders. Screenreaders are
   * used to make pdf files accessible for disabled people.
   *
   * @return true, if screenreaders are allowed for accessing this file
   */
  public boolean isAllowScreenreaders()
  {
    return cxAllowScreenReaders.isSelected();
  }

  /**
   * Defines whether the generated pdf may accessed using screenreaders. Screenreaders are
   * used to make pdf files accessible for disabled people.
   *
   * @param allowScreenreaders a flag containing true, if screenreaders are allowed to access the
   * content of this file, false otherwise
   */
  public void setAllowScreenreaders(boolean allowScreenreaders)
  {
    this.cxAllowScreenReaders.setSelected(allowScreenreaders);
  }

  /**
   * Defines whether the contents of form fields may be changed by the user.
   *
   * @return true, if the user may change the contents of formulars.
   */
  public boolean isAllowFillIn()
  {
    return cxAllowFillIn.isSelected();
  }

  /**
   * Defines whether the contents of form fields may be changed by the user.
   *
   * @param allowFillIn set to true to allow the change/filling of form fields
   */
  public void setAllowFillIn(boolean allowFillIn)
  {
    this.cxAllowFillIn.setSelected(allowFillIn);
  }

  /**
   * Defines whether the contents of the file are allowed to be copied.
   *
   * @return true, if the user is allowed to copy the contents of the file.
   */
  public boolean isAllowCopy()
  {
    return cxAllowCopy.isSelected();
  }

  /**
   * Defines whether the contents of the file are allowed to be copied.
   *
   * @param allowCopy set to true, if the user is allowed to copy the contents of the file.
   */
  public void setAllowCopy(boolean allowCopy)
  {
    this.cxAllowCopy.setSelected(allowCopy);
  }

  /**
   * Defines whether the user is allowed to add or modify annotations in this file.
   *
   * @return true, if this files annotations can be modified, false otherwise
   */
  public boolean isAllowModifyAnnotations()
  {
    return cxAllowModifyAnnotations.isSelected();
  }

  /**
   * Defines whether the user is allowed to add or modify annotations in this file.
   *
   * @param allowModifyAnnotations the flag.
   */
  public void setAllowModifyAnnotations(boolean allowModifyAnnotations)
  {
    this.cxAllowModifyAnnotations.setSelected(allowModifyAnnotations);
  }

  /**
   * Defines whether the user is allowed to modify the contents of this file.
   *
   * @return true, if this files content can be modified, false otherwise
   */
  public boolean isAllowModifyContents()
  {
    return cxAllowModifyContents.isSelected();
  }

  /**
   * Defines whether the user is allowed to modify the contents of this file.
   *
   * @param allowModifyContents set to true, if this files content can be modified, false otherwise
   */
  public void setAllowModifyContents(boolean allowModifyContents)
  {
    this.cxAllowModifyContents.setSelected(allowModifyContents);
  }

  /**
   * Defines whether the user is allowed to print the file. If this right is granted, the
   * user is also able to print a degraded version of the file, regardless of the
   * allowDegradedPrinting property
   *
   * @return true, if this file can be printed, false otherwise
   */
  public boolean isAllowPrinting()
  {
    return cbAllowPrinting.getSelectedIndex() == CBMODEL_FULL;
  }

  /**
   * Defines whether the user is allowed to print the file. If this right is granted, the
   * user is also able to print a degraded version of the file, regardless of the
   * allowDegradedPrinting property
   *
   * @param allowPrinting set to true, if this files can be printed, false otherwise
   */
  public void setAllowPrinting(boolean allowPrinting)
  {
    this.cbAllowPrinting.setSelectedIndex(CBMODEL_FULL);
  }

  /**
   * Defines the filename of the pdf file.
   *
   * @return the name of the file where to save the pdf file.
   */
  public String getFilename()
  {
    return txFilename.getText();
  }

  /**
   * Defines the filename of the pdf file.
   *
   * @param filename the filename of the pdf file
   */
  public void setFilename(String filename)
  {
    this.txFilename.setText(filename);
  }

  /**
   * Defines the title of the pdf file.
   *
   * @return the title
   */
  public String getPDFTitle()
  {
    return txTitle.getText();
  }

  /**
   * Defines the title of the pdf file.
   *
   * @param title the title
   */
  public void setPDFTitle(String title)
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
   * Queries the currently selected encryption. If an encryption is selected this method returns
   * either Boolean.TRUE or Boolean.FALSE, when no encryption is set, <code>null</code> is
   * returned. If no encryption is set, the security properties have no defined state.
   *
   * @return the selection state for the encryption. If no encryption is set, this method returns
   * null, if 40-bit encryption is set, the method returns Boolean.FALSE and on 128-Bit-encryption,
   * Boolean.TRUE is returned.
   */
  public Boolean getEncryptionValue()
  {
    if (rbSecurity40Bit.isSelected())
    {
      return PDFOutputTarget.SECURITY_ENCRYPTION_40BIT;
    }
    if (rbSecurity128Bit.isSelected())
    {
      return PDFOutputTarget.SECURITY_ENCRYPTION_128BIT;
    }
    return null;
  }

  /**
   * Defines the currently selected encryption.
   *
   * @param b the new encryption state, one of null, Boolean.TRUE or Boolean.FALSE
   */
  public void setEncryptionValue(Boolean b)
  {
    if (b == null)
    {
      rbSecurityNone.setSelected(true);
    }
    if (b.booleanValue())
    {
      rbSecurity128Bit.setSelected(true);
    }
    else
    {
      rbSecurity40Bit.setSelected(false);
    }
  }

  /**
   * @return true, if the dialog has been confirmed and the pdf should be saved, false otherwise.
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
    txConfOwnerPassword.setText("");
    txConfUserPassword.setText("");
    txFilename.setText("");
    txOwnerPassword.setText("");
    txTitle.setText("");
    txUserPassword.setText("");

    cxAllowAssembly.setSelected(false);
    cxAllowCopy.setSelected(false);
    cbAllowPrinting.setSelectedIndex(CBMODEL_NOPRINTING);
    cxAllowFillIn.setSelected(false);
    cxAllowModifyAnnotations.setSelected(false);
    cxAllowModifyContents.setSelected(false);
    cxAllowScreenReaders.setSelected(false);

    rbSecurityNone.setSelected(true);
    getActionSecuritySelection().actionPerformed(null);
  }

  /**
   * selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    JFileChooser fileChooser = new JFileChooser();
    ExtensionFileFilter filter = new ExtensionFileFilter("PDF Documents", ".pdf");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setSelectedFile(new File(getFilename()));
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends of pdf
      if (selFileName.toUpperCase().endsWith(".PDF") == false)
      {
        selFileName = selFileName + ".pdf";
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
    if (getEncryptionValue() != null)
    {
      if (txUserPassword.getText().equals(txConfUserPassword.getText()) == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("pdfsavedialog.userpasswordNoMatch"));
        return false;
      }
      if (txOwnerPassword.getText().equals(txConfOwnerPassword.getText()) == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("pdfsavedialog.ownerpasswordNoMatch"));
        return false;
      }
    }

    String filename = getFilename();
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
                                    getResources().getString("pdfsavedialog.targetIsEmpty"),
                                    getResources().getString("pdfsavedialog.errorTitle"), JOptionPane.ERROR_MESSAGE);
      return false;
    }
    File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("pdfsavedialog.targetIsNoFile"),
                                      getResources().getString("pdfsavedialog.errorTitle"), JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
                                      getResources().getString("pdfsavedialog.targetIsNotWritable"),
                                      getResources().getString("pdfsavedialog.errorTitle"), JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (JOptionPane.showConfirmDialog(this,
                                        MessageFormat.format(
                                            getResources().getString("pdfsavedialog.targetOverwriteConfirmation"),
                                            new Object[]{getFilename()}
                                        ),
                                        getResources().getString("pdfsavedialog.targetOverwriteTitle"),
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
        return false;
      }
    }
    if (getEncryptionValue() != null)
    {
      if (txOwnerPassword.getText().trim().length() == 0)
      {
        if (JOptionPane.showConfirmDialog(this,
                                          getResources().getString("pdfsavedialog.ownerpasswordEmpty"),
                                          getResources().getString("pdfsavedialog.warningTitle"),
                                          JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
        {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into a PDF-File.
   *
   * @param report  the report being processed.
   * @param pf  the pageformat used to write the report
   */
  public boolean savePDF(JFreeReport report, PageFormat pf)
  {
    initFromConfiguration(report.getReportConfiguration());
    setVisible(true);
    if (isConfirmed() == false)
    {
      return false;
    }
    return writePDF(report, pf);
  }

  public boolean writePDF(JFreeReport report, PageFormat pf)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(new File(getFilename())));
      PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
      target.configure(report.getReportConfiguration());
      target.setProperty(PDFOutputTarget.AUTHOR, getAuthor());
      target.setProperty(PDFOutputTarget.TITLE, getPDFTitle());
      target.setProperty(PDFOutputTarget.SECURITY_ENCRYPTION, getEncryptionValue());
      target.setProperty(PDFOutputTarget.SECURITY_OWNERPASSWORD, getOwnerPassword());
      target.setProperty(PDFOutputTarget.SECURITY_USERPASSWORD, getUserPassword());
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_ASSEMBLY, new Boolean(isAllowAssembly()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_COPY, new Boolean(isAllowCopy()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_DEGRADED_PRINTING,
                         new Boolean(isAllowDegradedPrinting()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_FILLIN, new Boolean(isAllowFillIn()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_MODIFY_ANNOTATIONS,
                         new Boolean(isAllowModifyAnnotations()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_MODIFY_CONTENTS,
                         new Boolean(isAllowModifyContents()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_PRINTING,
                         new Boolean(isAllowPrinting()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_SCREENREADERS,
                         new Boolean(isAllowScreenreaders()));

      target.open();

      PageableReportProcessor proc = new PageableReportProcessor(report);
      proc.setOutputTarget(target);
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

  public void initFromConfiguration(ReportConfiguration config)
  {
    setAuthor(config.getConfigProperty(PDFOutputTarget.AUTHOR, getAuthor()));
    setAllowAssembly(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_ASSEMBLY, config, isAllowAssembly()));
    setAllowCopy(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_COPY, config, isAllowCopy()));
    setAllowDegradedPrinting(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_DEGRADED_PRINTING, config, isAllowDegradedPrinting()));
    setAllowFillIn(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_FILLIN, config, isAllowFillIn()));
    setAllowModifyAnnotations(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_MODIFY_ANNOTATIONS, config, isAllowModifyAnnotations()));
    setAllowModifyContents(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_MODIFY_CONTENTS, config, isAllowModifyContents()));
    setAllowPrinting(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_PRINTING, config, isAllowPrinting()));
    setAllowScreenreaders(parseBoolean(PDFOutputTarget.SECURITY_ALLOW_SCREENREADERS, config, isAllowScreenreaders()));
    setUserPassword(config.getConfigProperty(PDFOutputTarget.SECURITY_USERPASSWORD, getUserPassword()));
    setOwnerPassword(config.getConfigProperty(PDFOutputTarget.SECURITY_OWNERPASSWORD, getOwnerPassword()));
  }

  private boolean parseBoolean(String key, ReportConfiguration config, boolean orgVal)
  {
    String val = config.getConfigProperty(PDFOutputTarget.CONFIGURATION_PREFIX + key, String.valueOf(orgVal));
    return (val.equalsIgnoreCase("true"));
  }
}
