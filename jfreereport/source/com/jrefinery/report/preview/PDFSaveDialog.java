/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 26.08.2002
 * Time: 18:58:13
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.preview;

import com.jrefinery.ui.ExtensionFileFilter;
import com.jrefinery.report.targets.PDFOutputTarget;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ActionButton;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.text.MessageFormat;

public class PDFSaveDialog extends JDialog
{
  private class ActionSecuritySelection extends AbstractAction
  {
    public ActionSecuritySelection()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      boolean b = (rbSecurityNone.isSelected() == false);
      txUserPassword.setEnabled(b);
      txOwnerPassword.setEnabled(b);
      txConfOwnerPassword.setEnabled(b);
      txConfUserPassword.setEnabled(b);
      cxAllowAssembly.setEnabled(b);
      cxAllowCopy.setEnabled(b);
      cxAllowDegradedPrinting.setEnabled(b);
      cxAllowFillIn.setEnabled(b);
      cxAllowModifyAnnotations.setEnabled(b);
      cxAllowModifyContents.setEnabled(b);
      cxAllowPrinting.setEnabled(b);
      cxAllowScreenReaders.setEnabled(b);
    }
  }

  private class ActionConfirm extends AbstractAction
  {
    public ActionConfirm()
    {
      putValue(Action.NAME, getResources().getString("pdfsavedialog.confirm"));
    }

    public void actionPerformed(ActionEvent e)
    {
      if (performValidate())
      {
        setConfirmed(true);
        setVisible(false);
      }
    }
  }

  private class ActionCancel extends AbstractAction
  {
    public ActionCancel()
    {
      putValue(Action.NAME, getResources().getString("pdfsavedialog.cancel"));
    }

    public void actionPerformed(ActionEvent e)
    {
      setConfirmed(false);
      setVisible(false);
    }
  }

  private class ActionSelectFile extends AbstractAction
  {
    public ActionSelectFile()
    {
      putValue(Action.NAME, getResources().getString("pdfsavedialog.selectFile"));
    }

    public void actionPerformed(ActionEvent e)
    {
      performSelectFile();
    }
  }

  private Action actionConfirm;
  private Action actionCancel;
  private Action actionSecuritySelection;
  private Action actionSelectFile;

  private JTextField txFilename;
  private JTextField txAuthor;
  private JTextField txTitle;

  private JRadioButton rbSecurityNone;
  private JRadioButton rbSecurity40Bit;
  private JRadioButton rbSecurity128Bit;

  private JTextField txUserPassword;
  private JTextField txOwnerPassword;
  private JTextField txConfUserPassword;
  private JTextField txConfOwnerPassword;

  private JCheckBox cxAllowCopy;
  private JCheckBox cxAllowPrinting;
  private JCheckBox cxAllowDegradedPrinting;
  private JCheckBox cxAllowScreenReaders;

  private JCheckBox cxAllowAssembly;
  private JCheckBox cxAllowModifyContents;
  private JCheckBox cxAllowModifyAnnotations;
  private JCheckBox cxAllowFillIn;

  private boolean confirmed;

  private JButton btnConfirm;
  private JButton btnCancel;

  private ResourceBundle resources;
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  public PDFSaveDialog()
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
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @returns this frames ResourceBundle.
   */
  public ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  public Action getActionSecuritySelection()
  {
    if (actionSecuritySelection == null)
    {
      actionSecuritySelection = new ActionSecuritySelection();
    }
    return actionSecuritySelection;
  }

  public Action getActionSelectFile()
  {
    if (actionSelectFile == null)
    {
      actionSelectFile = new ActionSelectFile();
    }
    return actionSelectFile;
  }

  public Action getActionConfirm()
  {
    if (actionConfirm == null)
    {
      actionConfirm = new ActionConfirm();
    }
    return actionConfirm;
  }

  public Action getActionCancel()
  {
    if (actionCancel == null)
    {
      actionCancel = new ActionCancel();
    }
    return actionCancel;
  }

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
    securityPanel.setBorder(BorderFactory.createTitledBorder(getResources().getString("pdfsavedialog.security")));

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
    JLabel lblUserPassConfirm = new JLabel(getResources().getString("pdfsavedialog.userpasswordconfirm"));
    JLabel lblOwnerPass = new JLabel(getResources().getString("pdfsavedialog.ownerpassword"));
    JLabel lblOwnerPassConfirm = new JLabel(getResources().getString("pdfsavedialog.ownerpasswordconfirm"));

    cxAllowCopy = new JCheckBox(getResources().getString("pdfsavedialog.allowCopy"));
    cxAllowPrinting = new JCheckBox(getResources().getString("pdfsavedialog.allowPrinting"));
    cxAllowDegradedPrinting = new JCheckBox(getResources().getString("pdfsavedialog.allowDegradedPrinting"));
    cxAllowScreenReaders = new JCheckBox(getResources().getString("pdfsavedialog.allowScreenreader"));

    cxAllowAssembly = new JCheckBox(getResources().getString("pdfsavedialog.allowAssembly"));
    cxAllowModifyContents = new JCheckBox(getResources().getString("pdfsavedialog.allowModifyContents"));
    cxAllowModifyAnnotations = new JCheckBox(getResources().getString("pdfsavedialog.allowModifyAnnotations"));
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
    securityPanel.add(cxAllowPrinting, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowDegradedPrinting, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowScreenReaders, gbc);

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
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;
    securityPanel.add(cxAllowFillIn, gbc);

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

  public String getUserPassword()
  {
    String txt = txUserPassword.getText();
    if (txt.equals("")) return null;
    return txt;
  }

  public void setUserPassword(String userPassword)
  {
    txUserPassword.setText(userPassword);
    txConfUserPassword.setText(userPassword);
  }

  public String getOwnerPassword()
  {
    String txt = txOwnerPassword.getText();
    if (txt.equals("")) return null;
    return txt;
  }

  public void setOwnerPassword(String ownerPassword)
  {
    txOwnerPassword.setText(ownerPassword);
    txConfOwnerPassword.setText(ownerPassword);
  }

  public boolean isAllowDegradedPrinting()
  {
    return cxAllowDegradedPrinting.isSelected();
  }

  public void setAllowDegradedPrinting(boolean allowDegradedPrinting)
  {
    this.cxAllowDegradedPrinting.setSelected(allowDegradedPrinting);
  }

  public boolean isAllowAssembly()
  {
    return cxAllowAssembly.isSelected();
  }

  public void setAllowAssembly(boolean allowAssembly)
  {
    this.cxAllowAssembly.setSelected(allowAssembly);
  }

  public boolean isAllowScreenreaders()
  {
    return cxAllowScreenReaders.isSelected();
  }

  public void setAllowScreenreaders(boolean allowScreenreaders)
  {
    this.cxAllowScreenReaders.setSelected(allowScreenreaders);
  }

  public boolean isAllowFillIn()
  {
    return cxAllowFillIn.isSelected();
  }

  public void setAllowFillIn(boolean allowFillIn)
  {
    this.cxAllowFillIn.setSelected(allowFillIn);
  }

  public boolean isAllowCopy()
  {
    return cxAllowCopy.isSelected();
  }

  public void setAllowCopy(boolean allowCopy)
  {
    this.cxAllowCopy.setSelected(allowCopy);
  }

  public boolean isAllowModifyAnnotations()
  {
    return cxAllowModifyAnnotations.isSelected();
  }

  public void setAllowModifyAnnotations(boolean allowModifyAnnotations)
  {
    this.cxAllowModifyAnnotations.setSelected(allowModifyAnnotations);
  }

  public boolean isAllowModifyContents()
  {
    return cxAllowModifyContents.isSelected();
  }

  public void setAllowModifyContents(boolean allowModifyContents)
  {
    this.cxAllowModifyContents.setSelected(allowModifyContents);
  }

  public boolean isAllowPrinting()
  {
    return cxAllowPrinting.isSelected();
  }

  public void setAllowPrinting(boolean allowPrinting)
  {
    this.cxAllowPrinting.setSelected(allowPrinting);
  }

  public String getFilename()
  {
    return txFilename.getText();
  }

  public void setFilename(String filename)
  {
    this.txFilename.setText(filename);
  }

  public String getPDFTitle()
  {
    return txTitle.getText();
  }

  public void setPDFTitle(String title)
  {
    this.txTitle.setText(title);
  }

  public String getAuthor()
  {
    return txAuthor.getText();
  }

  public void setAuthor(String author)
  {
    this.txAuthor.setText(author);
  }

  public Boolean getEncryptionValue ()
  {
    if (rbSecurity40Bit.isSelected()) return PDFOutputTarget.SECURITY_ENCRYPTION_40BIT;
    if (rbSecurity128Bit.isSelected()) return PDFOutputTarget.SECURITY_ENCRYPTION_128BIT;
    return null;
  }

  public void setEncryptionValue (Boolean b)
  {
    if (b == null) rbSecurityNone.setSelected(true);
    if (b.booleanValue())
      rbSecurity128Bit.setSelected(true);
    else
      rbSecurity40Bit.setSelected(false);
  }

  public boolean isConfirmed()
  {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed)
  {
    this.confirmed = confirmed;
  }

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
    cxAllowDegradedPrinting.setSelected(false);
    cxAllowFillIn.setSelected(false);
    cxAllowModifyAnnotations.setSelected(false);
    cxAllowModifyContents.setSelected(false);
    cxAllowPrinting.setSelected(false);
    cxAllowScreenReaders.setSelected(false);

    rbSecurity128Bit.setSelected(true);
  }

  private void performSelectFile ()
  {
    JFileChooser fileChooser = new JFileChooser ();
    ExtensionFileFilter filter = new ExtensionFileFilter ("PDF Documents", ".pdf");
    fileChooser.addChoosableFileFilter (filter);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setSelectedFile(new File (getFilename()));
    int option = fileChooser.showSaveDialog (this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      File selFile = fileChooser.getSelectedFile ();
      String selFileName = selFile.getAbsolutePath ();

      // Test if ends of pdf

      if (selFileName.toUpperCase ().endsWith (".PDF") == false)
      {
        selFileName = selFileName + ".pdf";
      }
      setFilename(selFileName);
    }
  }

  public boolean performValidate ()
  {
    if (getEncryptionValue() != null)
    {
      if (txUserPassword.getText().equals (txConfUserPassword.getText()) == false)
      {
        JOptionPane.showMessageDialog(this, "UserPassword does not match");
        return false;
      }
      if (txOwnerPassword.getText().equals (txConfOwnerPassword.getText()) == false)
      {
        JOptionPane.showMessageDialog(this, "ownerPassword does not match");
        return false;
      }
    }
    return true;
  }

  public void savePDF (JFreeReport report, PageFormat pf)
  {
    setVisible(true);
    if (isConfirmed() == false)
    {
      Log.debug ("is not confirmed");
      return;
    }
    try
    {
      OutputStream out = new BufferedOutputStream (new FileOutputStream (new File (getFilename())));
      PDFOutputTarget target = new PDFOutputTarget (out, pf, true);
      target.setProperty(PDFOutputTarget.AUTHOR, getAuthor());
      target.setProperty(PDFOutputTarget.TITLE, getPDFTitle());
      target.setProperty(PDFOutputTarget.SECURITY_ENCRYPTION, getEncryptionValue());
      target.setProperty(PDFOutputTarget.SECURITY_OWNERPASSWORD, getOwnerPassword());
      target.setProperty(PDFOutputTarget.SECURITY_USERPASSWORD, getUserPassword());
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_ASSEMBLY, new Boolean (isAllowAssembly()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_COPY, new Boolean(isAllowCopy()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_DEGRADED_PRINTING, new Boolean (isAllowDegradedPrinting()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_FILLIN, new Boolean(isAllowFillIn()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_MODIFY_ANNOTATIONS, new Boolean (isAllowModifyAnnotations()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_MODIFY_CONTENTS, new Boolean(isAllowModifyContents()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_PRINTING, new Boolean (isAllowPrinting()));
      target.setProperty(PDFOutputTarget.SECURITY_ALLOW_SCREENREADERS, new Boolean(isAllowScreenreaders()));

      target.setFontEncoding("Identity-H");
      target.open ();
      report.processReport (target);
      target.close ();
    }
    catch (IOException ioe)
    {
      showExceptionDialog ("error.savefailed", ioe);
    }
    catch (Exception re)
    {
      showExceptionDialog ("error.processingfailed", re);
    }
  }

  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
   */
  private void showExceptionDialog (String localisationBase, Exception e)
  {
    ExceptionDialog.showExceptionDialog (
            getResources ().getString (localisationBase + ".title"),
            MessageFormat.format (
                    getResources ().getString (localisationBase + ".message"),
                    new Object[]{e.getLocalizedMessage ()}
            ),
            e);
  }

  public static void main(String[] args)
  {
    PDFSaveDialog dialog = new PDFSaveDialog();
    dialog.pack();
    dialog.setVisible(true);
  }
}
