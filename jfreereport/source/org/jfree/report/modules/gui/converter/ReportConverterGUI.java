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
 * -----------------------
 * ReportConverterGUI.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConverterGUI.java,v 1.10 2003/09/15 18:26:50 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.gui.converter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.modules.gui.base.components.FilesystemFilter;
import org.jfree.report.modules.gui.converter.components.OperationResultTableModel;
import org.jfree.report.modules.gui.converter.parser.ConverterParserFrontend;
import org.jfree.report.modules.gui.converter.resources.ConverterResources;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.util.Log;
import org.jfree.report.util.StringUtil;
import org.jfree.util.DefaultConfiguration;
import org.jfree.xml.Parser;
import org.jfree.xml.factory.objects.ArrayClassFactory;
import org.jfree.xml.factory.objects.URLClassFactory;

/**
 * A utility application for converting XML report files from the old format to the
 * new format.
 *
 * @author Thomas Morgner.
 */
public class ReportConverterGUI extends JFrame
{
  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      ConverterResources.class.getName();

  /**
   * An action for selecting the target.
   */
  private class SelectTargetAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectTargetAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectTarget.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.selectTarget.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setTargetFile(performSelectFile(getTargetFile(), true));
    }
  }

  /**
   * An action for selecting the source file.
   */
  private class SelectSourceAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SelectSourceAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.selectSource.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.selectSource.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setSourceFile(performSelectFile(getSourceFile(), false));
    }
  }

  /**
   * An action for converting an XML report definition from the old format to the new.
   */
  private class ConvertAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public ConvertAction()
    {
      putValue(Action.NAME, getResources().getString("convertdialog.action.convert.name"));
      putValue(Action.SHORT_DESCRIPTION,
          getResources().getString("convertdialog.action.convert.description"));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      convert();
    }
  }

  /** The source field. */
  private final JTextField sourceField;

  /** The target field. */
  private final JTextField targetField;

  /** A file chooser. */
  private final JFileChooser fileChooser;

  /** Localised resources. */
  private ResourceBundle resources;

  /** The encoding combo box model used to select the target file encoding. */
  private final EncodingComboBoxModel encodingModel;

  /** The table model that displays all messages from the conversion. */
  private final OperationResultTableModel resultTableModel;

  /** A primitive status bar. */
  private JLabel statusHolder;

  /**
   * Default constructor.
   */
  public ReportConverterGUI()
  {
    encodingModel = EncodingComboBoxModel.createDefaultModel();
    encodingModel.ensureEncodingAvailable("UTF-16");
    encodingModel.setSelectedIndex(encodingModel.indexOf("UTF-16"));
    encodingModel.sort();

    resultTableModel = new OperationResultTableModel();

    sourceField = new JTextField();
    targetField = new JTextField();

    final JTable table = new JTable(resultTableModel);
    table.setMinimumSize(new Dimension (100, 100));
    final JSplitPane componentPane = new JSplitPane
        (JSplitPane.VERTICAL_SPLIT, createMainPane(), new JScrollPane(table));
    componentPane.setOneTouchExpandable(true);
    componentPane.resetToPreferredSizes();

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(componentPane, BorderLayout.CENTER);
    contentPane.add(createStatusBar(), BorderLayout.SOUTH);
    setContentPane(contentPane);

    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FilesystemFilter(new String[]{".xml"},
        "XML-Report definitions", true));
    fileChooser.setMultiSelectionEnabled(false);

    setTitle(getResources().getString("convertdialog.title"));
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar()
  {
    final JPanel statusPane = new JPanel();
    statusPane.setLayout(new BorderLayout());
    statusPane.setBorder(
        BorderFactory.createLineBorder(UIManager.getDefaults().getColor("controlShadow")));
    statusHolder = new JLabel(" ");
    statusPane.setMinimumSize(statusHolder.getPreferredSize());
    statusPane.add(statusHolder, BorderLayout.WEST);

    return statusPane;
  }

  /**
   * Sets the text of the status line.
   * 
   * @param text the new text that should be displayed in the status bar.
   */
  public void setStatusText (final String text)
  {
    statusHolder.setText(text);
  }

  /**
   * Returns the text from the status bar.
   * 
   * @return the status bar text.
   */
  public String getStatusText ()
  {
    return statusHolder.getText();
  }

  /**
   * Creates the main panel and all child components.
   *  
   * @return the created panel.
   */
  private JPanel createMainPane ()
  {
    final JButton selectSourceButton = new ActionButton(new SelectSourceAction());
    final JButton selectTargetButton = new ActionButton(new SelectTargetAction());
    final JButton convertFilesButton = new ActionButton(new ConvertAction());

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.sourceFile")), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(sourceField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(selectSourceButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.targetFile")), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(targetField, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(selectTargetButton, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(new JLabel(getResources().getString("convertdialog.encoding")), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.ipadx = 120;
    contentPane.add(new JComboBox(encodingModel), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(convertFilesButton, gbc);

    return contentPane;
  }
  /**
   * Starting point for the utility application.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    final ReportConverterGUI gui = new ReportConverterGUI();
    gui.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(final WindowEvent e)
      {
        System.exit(0);
      }
    });
    gui.pack();
    gui.setVisible(true);
  }

  /**
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @param filename  the file name.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performTargetValidate(final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.targetIsEmpty"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists())
    {
      if (f.isFile() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString("convertdialog.targetIsNoFile"),
            getResources().getString("convertdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (f.canWrite() == false)
      {
        JOptionPane.showMessageDialog(this,
            getResources().getString("convertdialog.targetIsNotWritable"),
            getResources().getString("convertdialog.errorTitle"),
            JOptionPane.ERROR_MESSAGE);
        return false;
      }
      final String key1 = "convertdialog.targetOverwriteConfirmation";
      final String key2 = "convertdialog.targetOverwriteTitle";
      if (JOptionPane.showConfirmDialog(this,
          MessageFormat.format(getResources().getString(key1),
              new Object[]{filename}
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
   * Validates the contents of the dialogs input fields. If the selected file exists, it is also
   * checked for validity.
   *
   * @param filename  the file name.
   *
   * @return true, if the input is valid, false otherwise
   */
  public boolean performSourceValidate(final String filename)
  {
    if (filename.trim().length() == 0)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.sourceIsEmpty"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    final File f = new File(filename);
    if (f.exists() == false)
    {
      return false;
    }

    if (f.isFile() == false)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.sourceIsNoFile"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (f.canRead() == false)
    {
      JOptionPane.showMessageDialog(this,
          getResources().getString("convertdialog.sourceIsNotReadable"),
          getResources().getString("convertdialog.errorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Retrieves the resources for the frame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return The resources.
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
   * Sets the source file.
   *
   * @param file  the file name.
   */
  protected void setSourceFile(final String file)
  {
    sourceField.setText(file);
  }

  /**
   * Sets the target file.
   *
   * @param file  the file name.
   */
  protected void setTargetFile(final String file)
  {
    targetField.setText(file);
  }

  /**
   * Returns the source file name.
   *
   * @return The name.
   */
  protected String getSourceFile()
  {
    return sourceField.getText();
  }

  /**
   * Returns the name of the target file.
   *
   * @return The name.
   */
  protected String getTargetFile()
  {
    return targetField.getText();
  }

  /**
   * Performs the conversion, returning <code>true</code> if the conversion is successful, and
   * <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public boolean convert()
  {

    if (performSourceValidate(getSourceFile()) == false)
    {
      setStatusText("Validating the source file failed. Please check your inputs.");
      return false;
    }
    if (performTargetValidate(getTargetFile()) == false)
    {
      setStatusText("Validating the target file failed. Please check your inputs.");
      return false;
    }

    try
    {
      final ConverterParserFrontend frontend = new ConverterParserFrontend();
      final File sourceFile = new File (getSourceFile());
      final File targetFile = new File (getTargetFile());
      final String encoding = encodingModel.getSelectedEncoding();
      final JFreeReport report = (JFreeReport)
          frontend.parse(sourceFile.toURL(), sourceFile.toURL());

      final DefaultConfiguration config = new DefaultConfiguration();
      config.setProperty(Parser.CONTENTBASE_KEY, targetFile.toURL().toExternalForm());

      // adding all factories will make sure that all stylekeys are found,
      // even if the report was parsed from a simple report definition  
      final ReportWriter writer = new ReportWriter(report, encoding, config);
      writer.addClassFactoryFactory(new URLClassFactory());
      writer.addClassFactoryFactory(new DefaultClassFactory());
      writer.addClassFactoryFactory(new BandLayoutClassFactory());
      writer.addClassFactoryFactory(new ArrayClassFactory());

      writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
      writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
      writer.addTemplateCollection(new DefaultTemplateCollection());
      writer.addElementFactory(new DefaultElementFactory());
      writer.addDataSourceFactory(new DefaultDataSourceFactory());

      final OutputStream base = new FileOutputStream(targetFile);
      final Writer w = new BufferedWriter(new OutputStreamWriter(base, encoding));
      writer.write(w);
      w.close();
      setStatusText("Conversion done.");
      return true;
    }
    catch (Exception e)
    {
      Log.warn ("Failed to convert the report. ", e);
      setStatusText("Failed to convert the report:" + e.getMessage());
      return false;
    }
  }
  /**
   * Selects a file to use.
   *
   * @param filename  the current selection.
   * @param appendExt  append an extension?
   *
   * @return The file name.
   */
  protected String performSelectFile(final String filename, final boolean appendExt)
  {
    final File file = new File(filename);
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      if (appendExt)
      {
        if ((StringUtil.endsWithIgnoreCase(selFileName, ".xml") == false))
        {
          selFileName = selFileName + ".xml";
        }
      }
      return selFileName;
    }
    return filename;
  }

}
