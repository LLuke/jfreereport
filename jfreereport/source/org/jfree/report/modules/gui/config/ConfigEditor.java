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
 * ------------------------------
 * ConfigEditor.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigEditor.java,v 1.5 2003/09/14 19:24:07 taqua Exp $
 *
 * Changes
 * -------------------------
 * 28-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.report.Boot;
import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.FilesystemFilter;
import org.jfree.report.modules.gui.config.editor.ConfigEditorPanel;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigTreeModel;
import org.jfree.report.modules.gui.config.model.ConfigTreeModelException;
import org.jfree.report.modules.gui.config.model.ConfigTreeModuleNode;
import org.jfree.report.modules.gui.config.resources.ConfigResources;
import org.jfree.report.util.LineBreakIterator;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.StringUtil;

/**
 * The ConfigEditor can be used to edit the global jfreereport.properties files.
 * These files provide global settings for all reports and contain the system
 * level configuration of JFreeReport.
 *
 * @author Thomas Morgner
 */
public class ConfigEditor extends JFrame
{
  /**
   * An Action to handle close requests.
   */
  private class CloseAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    public CloseAction()
    {
      putValue(NAME, resources.getString("action.exit.name"));
    }

    /**
     * Invoked when an action occurs. The action invokes System.exit(0).
     *
     * @param e the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      attempClose();
    }
  }

  /**
   * An action to handle save requests.
   */
  private class SaveAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    public SaveAction()
    {
      putValue(NAME, resources.getString("action.save.name"));
      putValue(SMALL_ICON, resources.getObject("action.save.small-icon"));
    }

    /**
     * Saves the configuration
     * @param e the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      save();
    }
  }

  /**
   * An action to handle load requests.
   */
  private class LoadAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    public LoadAction()
    {
      putValue(NAME, resources.getString("action.load.name"));
      putValue(SMALL_ICON, resources.getObject("action.load.small-icon"));
    }

    /**
     * Loads the configuration
     * @param e the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      load();
    }
  }

  /**
   * An action to handle new requests, which reset the report configuration.
   */
  private class NewAction extends AbstractActionDowngrade
  {
    /**
     * DefaultConstructor.
     */
    public NewAction()
    {
      putValue(NAME, resources.getString("action.new.name"));
      putValue(SMALL_ICON, resources.getObject("action.new.small-icon"));
    }

    /**
     * Reset the configuration
     * @param e the action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      reset();
    }
  }

  /**
   * This class handles the tree selection events and activates the
   * detail editors.
   */
  private class ModuleTreeSelectionHandler implements TreeSelectionListener
  {
    /**
     * DefaultConstructor.
     */
    public ModuleTreeSelectionHandler()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(TreeSelectionEvent e)
    {
      TreePath path = e.getPath();
      Object lastPathElement = path.getLastPathComponent();
      if (lastPathElement instanceof ConfigTreeModuleNode)
      {
        ConfigTreeModuleNode node = (ConfigTreeModuleNode) lastPathElement;
        detailEditorPane.store();
        detailEditorPane.editModule
            (node.getModule(), node.getConfiguration(), node.getAssignedKeys());
      }
    }
  }

  private static final int ESCAPE_KEY = 0;
  private static final int ESCAPE_VALUE = 1;
  private static final int ESCAPE_COMMENT = 2;

  /** The name of the resource bundle implementation used in this dialog. */
  private static final String RESOURCE_BUNDLE =
      ConfigResources.class.getName();
  /** A label that serves as status bar. */
  private JLabel statusHolder;
  /** The resource bundle instance of this dialog. */
  private ResourceBundle resources;

  /** The detail editor for the currently selected tree node. */
  private ConfigEditorPanel detailEditorPane;
  /** The tree model used to display the structure of the report configuration.*/
  private ConfigTreeModel treeModel;

  /** The currently used report configuration. */
  private ReportConfiguration currentReportConfiguration;
  /** The file chooser used to load and save the report configuration. */
  private JFileChooser fileChooser;


  /**
   * Constructs a new ConfigEditor.
   *
   * @throws ConfigTreeModelException if the tree model could not be built.
   */
  public ConfigEditor() throws ConfigTreeModelException
  {
    this.resources = ResourceBundle.getBundle(RESOURCE_BUNDLE);
    currentReportConfiguration = new ReportConfiguration
        (ReportConfiguration.getGlobalConfig());

    setTitle(resources.getString("config-editor.title"));

    detailEditorPane = new ConfigEditorPanel();

    JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, createEntryTree(),
            detailEditorPane);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);


    JPanel cPaneStatus = new JPanel();
    cPaneStatus.setLayout(new BorderLayout());
    cPaneStatus.add(contentPane, BorderLayout.CENTER);
    cPaneStatus.add(createStatusBar(), BorderLayout.SOUTH);

    setContentPane(cPaneStatus);

    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        attempClose();
      }
    });

  }

  /**
   * Creates the JTree for the report configuration.
   *
   * @return the tree component.
   * @throws ConfigTreeModelException if the model could not be built.
   */
  private JComponent createEntryTree() throws ConfigTreeModelException
  {
    InputStream in = getClass().getResourceAsStream("config-description.xml");
    if (in == null)
    {
      throw new IllegalStateException("Missing resource 'config-description.xml'");
    }
    treeModel = new ConfigTreeModel(in);
    treeModel.init(currentReportConfiguration);

    TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
    selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    JTree tree = new JTree(treeModel);
    tree.setSelectionModel(selectionModel);
    tree.setCellRenderer(new ConfigTreeRenderer());
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.addTreeSelectionListener(new ModuleTreeSelectionHandler());

    JScrollPane pane = new JScrollPane
        (tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    return pane;
  }

  /**
   * Creates the button pane to hold all control buttons.
   *
   * @return the created panel with all control buttons.
   */
  private JPanel createButtonPane()
  {
    Action closeAction = new CloseAction();
    Action saveAction = new SaveAction();
    Action loadAction = new LoadAction();
    Action newAction = new NewAction();

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1, 4));
    buttonHolder.add(new ActionButton(newAction));
    buttonHolder.add(new ActionButton(loadAction));
    buttonHolder.add(new ActionButton(saveAction));
    buttonHolder.add(new ActionButton(closeAction));

    panel.add(buttonHolder);
    return panel;
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
   * Defines the text to be displayed on the status bar. Setting text will
   * replace any other previously defined text.
   *
   * @param text the new statul bar text.
   */
  private void setStatusText(String text)
  {
    statusHolder.setText(text);
  }

//  private String getStatusText ()
//  {
//    return statusHolder.getText();
//  }

  /**
   * Loads the report configuration from a user selectable report properties file.
   */
  private void load()
  {
    setStatusText("Loading: Select file ...");
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter(".properties", "Properties files");
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on .properties
      if (StringUtil.endsWithIgnoreCase(selFileName, ".properties") == false)
      {
        selFileName = selFileName + ".properties";
      }
      Properties prop = new Properties();
      try
      {
        InputStream in = new BufferedInputStream(new FileInputStream(selFileName));
        prop.load(in);
        in.close();
      }
      catch (IOException ioe)
      {
        Log.debug("Failed to load the properties.", ioe);
        setStatusText("Failed to load the properties." + ioe.getMessage());
        return;
      }

      reset();

      Enumeration enum = prop.keys();
      while (enum.hasMoreElements())
      {
        String key = (String) enum.nextElement();
        String value = prop.getProperty(key);
        currentReportConfiguration.setConfigProperty(key, value);
      }
      try
      {
        treeModel.init(currentReportConfiguration);
        setStatusText("Loading the properties complete.");
      }
      catch (ConfigTreeModelException e)
      {
        Log.debug("Failed to update the model.", e);
        setStatusText("Failed to update the model.");
      }
    }
  }

  /**
   * Resets all values.
   */
  private void reset()
  {
    // clear all previously set configuration settings ...
    Enumeration defaults = currentReportConfiguration.getConfigProperties();
    while (defaults.hasMoreElements())
    {
      String key = (String) defaults.nextElement();
      currentReportConfiguration.setConfigProperty(key, null);
    }
  }

  /**
   * Saves the report configuration to a user selectable report properties file.
   */
  private void save()
  {
    setStatusText("Saving: Select file ...");
    detailEditorPane.store();

    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      final FilesystemFilter filter = new FilesystemFilter(".properties", "Properties files");
      fileChooser.addChoosableFileFilter(filter);
      fileChooser.setMultiSelectionEnabled(false);
    }

    final int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends on xls
      if (StringUtil.endsWithIgnoreCase(selFileName, ".properties") == false)
      {
        selFileName = selFileName + ".properties";
      }
      write(selFileName);
    }
  }

  private void write(String filename)
  {
    Properties prop = new Properties();
    ArrayList names = new ArrayList();
    // clear all previously set configuration settings ...
    Enumeration defaults = currentReportConfiguration.getConfigProperties();
    while (defaults.hasMoreElements())
    {
      String key = (String) defaults.nextElement();
      names.add(key);
      prop.setProperty(key, currentReportConfiguration.getConfigProperty(key));
    }

    Collections.sort(names);

    try
    {
      PrintWriter out =
          new PrintWriter(new OutputStreamWriter
              (new BufferedOutputStream(new FileOutputStream(filename))));

      for (int i = 0; i < names.size(); i++)
      {
        String key = (String) names.get(i);
        String value = prop.getProperty(key);

        ConfigDescriptionEntry entry = treeModel.getEntryForKey(key);
        if (entry != null)
        {
          String description = entry.getDescription();
          writeDescription(description, out);
        }
        saveConvert(key, ESCAPE_KEY, out);
        out.print("=");
        saveConvert(value, ESCAPE_VALUE, out);
        out.println();
      }
      out.close();
      setStatusText("Saving the properties complete.");
    }
    catch (IOException ioe)
    {
      Log.debug("Failed to save the properties.", ioe);
      setStatusText("Failed to savethe properties." + ioe.getMessage());
    }

  }

  private void writeDescription(String text, PrintWriter writer)
  {
    // check if empty content ... this case is easy ...
    if (text.length() == 0)
    {
      return;
    }

    writer.println("# ");
    final LineBreakIterator iterator = new LineBreakIterator(text);
    while (iterator.hasNext())
    {
      writer.print("# ");
      saveConvert((String) iterator.next(), ESCAPE_COMMENT, writer);
      writer.println();
    }
  }

  private void saveConvert(String text, int escapeMode, PrintWriter writer)
  {
    char[] string = text.toCharArray();
    char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7',
                        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    for (int x = 0; x < string.length; x++)
    {
      char aChar = string[x];
      switch (aChar)
      {
        case ' ':
          {
            if ((escapeMode != ESCAPE_COMMENT) &&
                (x == 0 || escapeMode == ESCAPE_KEY))
            {
              writer.print('\\');
            }
            writer.print(' ');
            break;
          }
        case '\\':
          {
            writer.print('\\');
            writer.print('\\');
            break;
          }
        case '\t':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print('t');
            }
            break;
          }
        case '\n':
          {
            writer.print('\\');
            writer.print('n');
            break;
          }
        case '\r':
          {
            writer.print('\\');
            writer.print('r');
            break;
          }
        case '\f':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print('f');
            }
            break;
          }
        case '#':
        case '"':
        case '!':
        case '=':
        case ':':
          {
            if (escapeMode == ESCAPE_COMMENT)
            {
              writer.print(aChar);
            }
            else
            {
              writer.print('\\');
              writer.print(aChar);
            }
            break;
          }
        default:
          if ((aChar < 0x0020) || (aChar > 0x007e))
          {
            writer.print('\\');
            writer.print('u');
            writer.print(hexChars[(aChar >> 12) & 0xF]);
            writer.print(hexChars[(aChar >> 8) & 0xF]);
            writer.print(hexChars[(aChar >> 4) & 0xF]);
            writer.print(hexChars[aChar & 0xF]);
          }
          else
          {
            writer.print(aChar);
          }
      }
    }
  }

  /**
   * Closes this frame and exits the JavaVM.
   *
   */
  private void attempClose()
  {
    System.exit(0);
  }

  /**
   * main Method to start the editor.
   * @param args not used.
   */
  public static void main(String[] args)
  {
    try
    {
      Boot.start();
      ConfigEditor ed = new ConfigEditor();
      ed.pack();
      ed.setVisible(true);
    }
    catch (Exception e)
    {
      Log.debug("Failed to init", e);
      JOptionPane.showMessageDialog(null, "Failed to initialize.");
    }
  }
}
