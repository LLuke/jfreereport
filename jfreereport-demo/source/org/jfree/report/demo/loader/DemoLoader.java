/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DemoLoader.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo.loader;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.report.JFreeReport;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.modules.gui.swing.preview.PreviewPane;
import org.jfree.report.modules.gui.swing.preview.ReportController;
import org.jfree.report.modules.gui.swing.common.JStatusBar;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * Creation-Date: 03.12.2006, 14:16:23
 *
 * @author Thomas Morgner
 */
public class DemoLoader implements ReportController
{
  /**
   * Internal action class to select a target file.
   */
  private class LoadReportAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public LoadReportAction()
    {
      putValue(Action.NAME, "Load");
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performLoad();
    }

  }

  /**
   * Internal action class to select a target file.
   */
  private class SelectFileAction extends AbstractAction
  {
    /**
     * Default constructor.
     */
    public SelectFileAction()
    {
      putValue(Action.NAME, "Select");
    }

    /**
     * Receives notification that the action has occurred.
     *
     * @param e the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      performSelectFile();
    }
  }

  private JPanel loaderPanel;
  private JTextField txFilename;
  private JFileChooser fileChooser;
  private PreviewPane pane;


  public DemoLoader()
  {
    loaderPanel = new JPanel();
    loaderPanel.setLayout(new GridBagLayout());

    txFilename = new JTextField();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    loaderPanel.add(new JLabel("Report-Definition"), gbc);

    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipadx = 120;
    gbc.insets = new Insets(3, 1, 1, 1);
    loaderPanel.add(txFilename, gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 2;
    gbc.gridy = 0;
    loaderPanel.add(new ActionButton(new SelectFileAction()), gbc);

    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.gridx = 3;
    gbc.gridy = 0;
    loaderPanel.add(new ActionButton(new LoadReportAction()), gbc);
  }


  /**
   * selects a file to use as target for the report processing.
   */
  protected void performSelectFile()
  {
    // lazy initialize ... the file chooser is one of the hot spots here ...
    if (fileChooser == null)
    {
      fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(false);
    }

    final File file = new File(txFilename.getText());
    fileChooser.setCurrentDirectory(file);
    fileChooser.setSelectedFile(file);
    final int option = fileChooser.showSaveDialog(loaderPanel);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      final File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();
      txFilename.setText(selFileName);
    }
  }

  /**
   * Returns the graphical representation of the controler. This component will
   * be added between the menu bar and the toolbar.
   * <p/>
   * Changes to this property are not detected automaticly, you have to call
   * "refreshControler" whenever you want to display a completly new control
   * panel.
   *
   * @return the controler component.
   */
  public JComponent getControlPanel()
  {
    return loaderPanel;
  }

  /**
   * Returns the menus that should be inserted into the menubar.
   * <p/>
   * Changes to this property are not detected automaticly, you have to call
   * "refreshControler" whenever the contents of the menu array changed.
   *
   * @return the menus as array, never null.
   */
  public JMenu[] getMenus()
  {
    return new JMenu[0];
  }

  /**
   * Defines, whether the controler component is placed between the preview pane
   * and the toolbar.
   *
   * @return true, if this is a inner component.
   */
  public boolean isInnerComponent()
  {
    return false;
  }

  /**
   * Returns the location for the report controler, one of BorderLayout.NORTH,
   * BorderLayout.SOUTH, BorderLayout.EAST or BorderLayout.WEST.
   *
   * @return the location;
   */
  public String getControllerLocation()
  {
    return BorderLayout.NORTH;
  }

  public void initialize(PreviewPane pane)
  {
    this.pane = pane;
  }

  protected void performLoad()
  {
    if (pane == null)
    {
      return;
    }

    try
    {
      ResourceManager manager = new ResourceManager();
      manager.registerDefaults();
      Resource res = manager.createDirectly(new File(txFilename.getText()), JFreeReport.class);
      final JFreeReport resource = (JFreeReport) res.getResource();
      pane.setReportJob(new DefaultReportJob (resource));
    }
    catch(Exception e)
    {
      Log.error ("Failed", e);
      pane.setStatusType(JStatusBar.TYPE_ERROR);
      pane.setStatusText("Failed to load: "  + e.getMessage());
      return;
    }

  }
}
