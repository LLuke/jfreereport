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
 * $Id: ConfigEditor.java,v 1.1 2003/08/30 15:05:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.border.EmptyBorder;

import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.config.resources.ConfigResources;
import org.jfree.report.modules.gui.config.model.ConfigTreeModel;
import org.jfree.report.modules.gui.config.model.ConfigTreeModelException;
import org.jfree.report.modules.gui.config.model.ConfigTreeModuleNode;
import org.jfree.report.modules.gui.config.editor.ConfigEditorPanel;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

public class ConfigEditor extends JFrame
{
  private class CloseAction extends AbstractActionDowngrade
  {
    public CloseAction()
    {
      putValue(NAME, "Exit");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      System.exit(0);
    }
  }

  private class SaveAction extends AbstractActionDowngrade
  {
    public SaveAction()
    {
      putValue(NAME, resources.getString("action.save.name"));
      putValue(SMALL_ICON, resources.getObject("action.save.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      save();
    }
  }

  private class LoadAction extends AbstractActionDowngrade
  {
    public LoadAction()
    {
      putValue(NAME, resources.getString("action.load.name"));
      putValue(SMALL_ICON, resources.getObject("action.load.small-icon"));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      //load();
      Log.debug (detailEditorPane.getMinimumSize());
      Log.debug (detailEditorPane.getPreferredSize());
      Log.debug ("---");
      detailEditorPane.debug();
      Log.debug ("---");
    }
  }

  private class ModuleTreeSelectionHandler implements TreeSelectionListener
  {
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
        detailEditorPane.setModule
            (node.getModule(), node.getConfiguration(), node.getAssignedKeys());
      }
    }
  }
  private static final String RESOURCE_BUNDLE =
      ConfigResources.class.getName();

  private JLabel statusHolder;
  private ResourceBundle resources;

  private LoadAction loadAction;
  private CloseAction closeAction;
  private SaveAction saveAction;

  private ConfigEditorPanel detailEditorPane;
  private ConfigTreeModel treeModel;

  /**
   * Constructs a new frame that is initially invisible.
   * <p>
   * This constructor sets the component's locale property to the value
   * returned by <code>JComponent.getDefaultLocale</code>.
   */
  public ConfigEditor() throws ConfigTreeModelException
  {
    this.resources = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    detailEditorPane = new ConfigEditorPanel();

    JSplitPane splitPane = new JSplitPane
        (JSplitPane.HORIZONTAL_SPLIT, createEntryTree(),
            detailEditorPane);
//            new JScrollPane(detailEditorPane,
//                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
//                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(splitPane, BorderLayout.CENTER);
    contentPane.add(createButtonPane(), BorderLayout.SOUTH);


    JPanel cPaneStatus = new JPanel();
    cPaneStatus.setLayout(new BorderLayout());
    cPaneStatus.add (contentPane, BorderLayout.CENTER);
    cPaneStatus.add (createStatusBar(), BorderLayout.SOUTH);

    setContentPane(cPaneStatus);


    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        closeAction.actionPerformed(new ActionEvent (e.getSource(), 0, "close"));
      }
    });

  }

  private JComponent createEntryTree () throws ConfigTreeModelException
  {
    InputStream in = getClass().getResourceAsStream("config-description.xml");
    if (in == null)
    {
      throw new IllegalStateException("Missing resource 'config-description.xml'");
    }
    treeModel = new ConfigTreeModel(in);
    treeModel.init(ReportConfiguration.getGlobalConfig());

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

  private JPanel createButtonPane ()
  {
    closeAction = new CloseAction();
    saveAction = new SaveAction();
    loadAction = new LoadAction();

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(new EmptyBorder (5,5,5,5));

    JPanel buttonHolder = new JPanel();
    buttonHolder.setLayout(new GridLayout(1,3));
    buttonHolder.add (new ActionButton(loadAction));
    buttonHolder.add (new ActionButton(saveAction));
    buttonHolder.add (new ActionButton (closeAction));

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

  private void setStatusText (String text)
  {
    statusHolder.setText(text);
  }

  private String getStatusText ()
  {
    return statusHolder.getText();
  }

  private void load()
  {
    setStatusText("Loading: Select file ...");
  }

  private void save()
  {
    setStatusText("Saving: Select file ...");
  }

  public static void main(String[] args)
  {
    try
    {
      ConfigEditor ed = new ConfigEditor();
      ed.pack();
      ed.setVisible(true);
    }
    catch (Exception e)
    {
      Log.debug ("Failed to init", e);
      JOptionPane.showMessageDialog(null, "Failed to initialize.");
    }
  }
}
