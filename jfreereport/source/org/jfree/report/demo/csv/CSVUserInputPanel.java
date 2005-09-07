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
 * ------------------------
 * CSVReaderDemo.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Mimil;
 *
 * $Id: CSVUserInputPanel.java,v 1.1 2005/08/29 17:34:18 taqua Exp $
 *
 * $Log: CSVUserInputPanel.java,v $
 * Revision 1.1  2005/08/29 17:34:18  taqua
 * Demo restructuring - work in progress ...
 *
 * Revision 1.6  2005/05/20 16:06:20  taqua
 * More on the classloader topic: Changed resource paths to global paths;
 * Fixed the i18n issues (parser, demo, resource bundle lookup)
 *
 * Revision 1.5  2005/05/18 18:38:26  taqua
 * Changed the classloader handling. Now the used classloader is configurable
 * using JCommon.
 *
 * Revision 1.4  2005/03/03 21:50:38  taqua
 * Resolved some warnings for Eclipse configured with paranoid compile settings.
 *
 * Revision 1.3  2005/02/23 21:04:37  taqua
 * More build process fixes - ready for JDK 1.2.2 now
 *
 * Revision 1.2  2005/02/23 19:31:42  taqua
 * First part of the ANT build update.
 *
 * Revision 1.1  2005/02/19 18:37:07  taqua
 * CSVTableModel classes moved into modules/misc/tablemodel
 *
 * Revision 1.3  2005/01/31 17:16:19  taqua
 * Module and JUnit updates for 0.8.5
 *
 * Revision 1.2  2004/08/07 17:45:47  mimil
 * Some JavaDocs
 *
 * Revision 1.1  2004/08/07 14:35:14  mimil
 * Initial version
 *
 */

package org.jfree.report.demo.csv;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.io.IOUtils;
import org.jfree.report.demo.helper.DemoControler;
import org.jfree.report.modules.gui.base.components.FormValidator;
import org.jfree.report.modules.gui.base.components.JStatusBar;
import org.jfree.report.modules.misc.tablemodel.CSVTableModelProducer;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.LengthLimitingDocument;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.ResourceBundleSupport;

/**
 * Demo that show how to use <code>CSVTableModelProducer</code> to generate
 * <code>TableModel</code> for JFreeReport input data.
 * <p>
 * This version allows the user to enter data directly.
 *
 * @see CSVTableModelProducer
 */
public class CSVUserInputPanel extends JPanel
{
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


  private class CSVDialogValidator extends FormValidator
  {
    public CSVDialogValidator ()
    {
      super();
    }

    public boolean performValidate ()
    {
      return CSVUserInputPanel.this.performValidate();
    }

    public Action getConfirmAction ()
    {
      return CSVUserInputPanel.this.getControler().getExportAction();
    }
  }

  private class LoadCSVDataAction extends AbstractActionDowngrade
  {
    public LoadCSVDataAction()
    {
      putValue(NAME, "Select");
    }

    public void actionPerformed(ActionEvent e)
    {
      performLoadFile();
    }
  }

  public static final String RESOURCE_BASE =
          "org.jfree.report.demo.resources.demo-resources";

  private static final String COMMA_SEPARATOR = ",";
  private static final String SEMICOLON_SEPARATOR = ";";
  private static final String TAB_SEPARATOR = "\t";
  private static final String CSV_FILE_EXTENSION = ".csv";

  private JTextArea txDataArea;
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

  private FormValidator formValidator;
  private DemoControler controler;
  private ResourceBundleSupport resources;
  private JFileChooser fileChooser;

  /**
   * Creates the demo workspace.
   */
  public CSVUserInputPanel (final DemoControler controler)
  {
    this.controler = controler;
    this.resources = new ResourceBundleSupport(RESOURCE_BASE);
    this.formValidator = new CSVDialogValidator();

    setLayout(new BorderLayout());
    add(createDataArea(), BorderLayout.CENTER);
    add(createSeparatorPanel(), BorderLayout.SOUTH);
  }

  private JComponent createDataArea ()
  {
    txDataArea = new JTextArea();
    cbxColumnNamesAsFirstRow = new JCheckBox("First Row contains Column-Names");

    final JLabel lbDataArea = new JLabel ("CSV-Data");
    final JLabel lbLoadData = new JLabel ("Load Data from File");
    final ActionButton btLoadData = new ActionButton(new LoadCSVDataAction());

    final JPanel dataPanel = new JPanel();
    dataPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    dataPanel.add(lbDataArea, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridwidth = 2;
    dataPanel.add(txDataArea, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    dataPanel.add(lbLoadData, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.EAST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(1, 1, 1, 1);
    dataPanel.add(btLoadData, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(1, 1, 1, 1);
    dataPanel.add(cbxColumnNamesAsFirstRow, gbc);

    formValidator.registerButton(cbxColumnNamesAsFirstRow);

    return dataPanel;
  }

  public DemoControler getControler()
  {
    return controler;
  }

  /**
   * Validates the contents of the dialog's input fields. If the selected file exists, it
   * is also checked for validity.
   *
   * @return <code>true</code> if the input is valid, <code>false</code> otherwise
   */
  protected boolean performValidate ()
  {
    final JStatusBar statusBar = controler.getStatusBar();
    statusBar.clear();

    return true;
  }

  protected FormValidator getFormValidator ()
  {
    return formValidator;
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
            new TitledBorder(resources.getString("csvdemodialog.separatorchar"));
    separatorPanel.setBorder(tb);

    rbSeparatorTab = new JRadioButton(resources.getString("csvdemodialog.separator.tab"));
    rbSeparatorColon = new JRadioButton(resources.getString("csvdemodialog.separator.colon"));
    rbSeparatorSemicolon = new JRadioButton(resources.getString("csvdemodialog.separator.semicolon"));
    rbSeparatorOther = new JRadioButton(resources.getString("csvdemodialog.separator.other"));

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
   * Selects a file to use as target for the report processing.
   */
  protected void performLoadFile ()
  {
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      fileChooser.addChoosableFileFilter(new ExtensionFileFilter
              (resources.getString("csvdemodialog.csv-file-description"), CSV_FILE_EXTENSION));
      fileChooser.setMultiSelectionEnabled(false);
    }

    final int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      // now load the file
      try
      {
        final BufferedReader in = new BufferedReader(new FileReader(selFile));
        final StringWriter out = new StringWriter((int) selFile.length());
        IOUtils.getInstance().copyWriter(in, out);
        in.close();
        txDataArea.setText(out.toString());
      }
      catch(IOException ioe)
      {
        ioe.printStackTrace();
      }
    }
  }

  public String getData ()
  {
    return txDataArea.getText();
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
}
