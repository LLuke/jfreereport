/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * --------------------
 * HugeJFreeReportDemo.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 31-May-2002 : Derived from JFreeReportDemo
 * 04-Jun-2002 : Some documentation
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.targets.PDFOutputTarget;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FloatingButtonEnabler;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.SystemOutLogTarget;
import com.jrefinery.ui.JRefineryUtilities;
import com.jrefinery.ui.L1R2ButtonPanel;
import com.jrefinery.ui.about.AboutFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * The main frame in the report demonstration application. This demo has huge reports
 * added, so calling report 5 and 6 can require some patience.
 * <p>
 * If the system property "com.jrefinery.report.demo.DEBUG" is set to "true", debugging
 * messages to System.out stream are enabled.
 */
public class HugeJFreeReportDemo extends JFrame
{
  private class CloseHandler extends WindowAdapter
  {
    public void windowClosing (WindowEvent event)
    {
      attemptExit ();
    }
  }

  private class DemoAboutAction extends AboutAction
  {
    public DemoAboutAction ()
    {
      super (getResources ());
    }

    public void actionPerformed (ActionEvent event)
    {
      displayAbout ();
    }
  }

  private class DemoPreviewAction extends PreviewAction
  {
    public DemoPreviewAction ()
    {
      super (getResources ());
    }

    public void actionPerformed (ActionEvent event)
    {
      attemptPreview ();
    }
  }

  private class DemoCloseAction extends CloseAction
  {
    public DemoCloseAction ()
    {
      super (getResources ());
    }

    public void actionPerformed (ActionEvent event)
    {
      attemptExit ();
    }
  }

  protected PreviewAction previewAction;
  protected AboutAction aboutAction;
  protected CloseAction closeAction;

  /** A frame for displaying information about the demo application. */
  private AboutFrame aboutFrame;

  /** A frame for displaying information about the system. */
  private JFrame infoFrame;

  /** A tabbed pane for displaying the sample data sets. */
  private JTabbedPane tabbedPane;

  private previewConf[] previewData =
    {
      new previewConf(
        "/com/jrefinery/report/demo/report1.xml",
        new SampleData1()),
      new previewConf(
        "/com/jrefinery/report/demo/report2.xml",
        new SampleData2()),
      new previewConf(
        "/com/jrefinery/report/demo/report3.xml",
        new SampleData3()),
      new previewConf(
        "/com/jrefinery/report/demo/report4.xml",
        new SampleData4()),
      new previewConf(
        "/com/jrefinery/report/demo/report2.xml",
        new SampleData5()),
      new previewConf(
        "/com/jrefinery/report/demo/report2.xml",
        new SampleData6()),
      };

  private ResourceBundle m_resources;

  private ResourceBundle getResources ()
  {
    return m_resources;
  }

  /**
   * Constructs a frame containing sample reports created using the JFreeReport Class Library.
   */
  public HugeJFreeReportDemo (ResourceBundle resources)
  {
    setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
    addWindowListener (new CloseHandler ());
    m_resources = resources;
    Object[] arguments = new Object[]{JFreeReport.INFO.getVersion ()};
    String pattern = resources.getString ("main-frame.title.pattern");
    setTitle (MessageFormat.format (pattern, arguments));


    createActions ();

    // set up the menu
    setJMenuBar (createMenuBar ());

    JPanel content = (JPanel) getContentPane ();
    JToolBar toolbar = createToolBar (resources);
    content.add (toolbar, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    tabbedPane.addTab(
      "Example 1",
      JRefineryUtilities.createTablePanel(previewData[0].m_data));
    tabbedPane.addTab(
      "Example 2",
      JRefineryUtilities.createTablePanel(previewData[1].m_data));
    tabbedPane.addTab(
      "Example 3",
      JRefineryUtilities.createTablePanel(previewData[2].m_data));
    tabbedPane.addTab(
      "Example 4",
      JRefineryUtilities.createTablePanel(previewData[3].m_data));
    tabbedPane.addTab(
      "Example 5 (HUGE)",
      JRefineryUtilities.createTablePanel(previewData[4].m_data));
    tabbedPane.addTab(
      "Example 6 (HUGE)",
      JRefineryUtilities.createTablePanel(previewData[5].m_data));

    content.add (tabbedPane);

    L1R2ButtonPanel buttons = new L1R2ButtonPanel ("Help", "Preview", "Close");

    JButton helpButton = buttons.getLeftButton ();
    helpButton.setAction (aboutAction);
    FloatingButtonEnabler.getInstance().addButton (helpButton);

    JButton previewButton = buttons.getRightButton1 ();
    previewButton.setAction (previewAction);
    FloatingButtonEnabler.getInstance().addButton (previewButton);

    JButton closeButton = buttons.getRightButton2 ();
    closeButton.setAction (closeAction);
    FloatingButtonEnabler.getInstance().addButton (closeButton);

    buttons.setBorder (BorderFactory.createEmptyBorder (0, 4, 4, 4));
    content.add (buttons, BorderLayout.SOUTH);

    setContentPane (content);

  }

  /**
   * Handles a request to preview a report.  First determines which data set is visible, then
   * calls the appropriate preview method.
   */
  public void attemptPreview ()
  {
    int index = tabbedPane.getSelectedIndex ();

    preview(previewData[index].m_reportFile, previewData[index].m_data);
  }

  /**
   * Displays a preview frame for report defined in the file specified by <code>urlname</code>.
   * The contents of the url are parsed and the report is fed into a new PreviewPane.
   * The given TableModel is assigned to the report as report data source.
   * <p>
   * If the report contains external references in specified in relative urls, the urls
   * are loaded using the reports parent directory as content base.
   *
   * @param urlname the filename from where to load the report
   * @param data the datamodel for the report
   */
  public void preview (String urlname, TableModel data)
  {
    URL in = getClass ().getResource (urlname);
    if (in == null)
    {
      JOptionPane.showMessageDialog (this, "ReportDefinition " + urlname + " not found");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance ();

    JFreeReport report1 = null;
    try
    {
      report1 = gen.parseReport (in, in);
    }
    catch (Exception ioe)
    {
      ExceptionDialog.showExceptionDialog (urlname, "Failed to load ReportDefinition", ioe);
      return;
    }

    if (report1 == null)
      throw new NullPointerException ("Damn, is null");

    report1.setData (data);

    PreviewFrame frame1 = new PreviewFrame (report1);
    frame1.pack ();
    JRefineryUtilities.positionFrameRandomly (frame1);
    frame1.setVisible (true);
    frame1.requestFocus ();
  }

  /**
   * Returns the preferred size of the frame.
   */
  public Dimension getPreferredSize ()
  {
    return new Dimension (440, 300);
  }

  /**
   * Exits the application, but only if the user agrees.
   */
  public boolean attemptExit ()
  {
    boolean close =
            JOptionPane.showConfirmDialog (
                    this,
                    "Are you sure you want to exit?",
                    "Confirmation...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
            == JOptionPane.YES_OPTION;
    if (close)
    {
      dispose ();
      System.exit (0);
    }

    return close;
  }

  /**
   * Displays information about the application.
   */
  public void displayAbout ()
  {
    if (aboutFrame == null)
    {
      aboutFrame = new AboutFrame ("About...", JFreeReport.INFO);

      aboutFrame.pack ();
      JRefineryUtilities.centerFrameOnScreen (aboutFrame);
    }
    aboutFrame.setVisible (true);
    aboutFrame.requestFocus ();
  }

  /**
   * Create the actions used in the demo
   */
  private void createActions ()
  {
    previewAction = new DemoPreviewAction ();
    aboutAction = new DemoAboutAction ();
    closeAction = new DemoCloseAction ();
  }

  /**
   * Creates and returns a menu-bar for the frame.
   */
  private JMenuBar createMenuBar ()
  {
    // create the menus
    JMenuBar menuBar = new JMenuBar ();
    menuBar.setBorder (null);
    // first the file menu
    JMenu fileMenu = createJMenuItem ("menu.file");

    JMenuItem printItem = new JMenuItem (previewAction);
    KeyStroke accelerator = (KeyStroke) previewAction.getValue (Action.ACCELERATOR_KEY);
    if (accelerator != null)
      printItem.setAccelerator (accelerator);
    fileMenu.add (printItem);

    fileMenu.add (new JSeparator ());

    JMenuItem exitItem = new JMenuItem (closeAction);
    fileMenu.add (exitItem);

    // then the help menu
    JMenu helpMenu = createJMenuItem ("menu.help");

    JMenuItem aboutItem = new JMenuItem (aboutAction);
    helpMenu.add (aboutItem);

    // finally, glue together the menu and return it
    menuBar.add (fileMenu);
    menuBar.add (helpMenu);

    return menuBar;
  }

  /**
   * Creates a JMenu which gets initialized from the current resource bundle.
   */
  private JMenu createJMenuItem (String base)
  {
    String label = m_resources.getString (base + ".name");
    Character mnemonic = (Character) m_resources.getObject (base + ".mnemonic");

    JMenu menu = new JMenu (label);
    if (mnemonic != null)
    {
      menu.setMnemonic (mnemonic.charValue ());
    }
    return menu;
  }


   /**
    * Creates a new button based on the action. The button will be floating enabled,
    * so that the buttons borders are only visible when the mouse has entered the button area.
    */
  protected JButton createButton (Action action)
  {
    JButton button = new JButton (action);
    button.setMargin (new Insets (0, 0, 0, 0));
    button.setText (null);
    FloatingButtonEnabler.getInstance().addButton (button);
    return button;
  }

  /**
   * Creates the demos toolbar
   */
  private JToolBar createToolBar (ResourceBundle resources)
  {
    JToolBar toolbar = new JToolBar ();
    toolbar.setBorder (BorderFactory.createEmptyBorder (4, 0, 4, 0));

    toolbar.add (createButton (previewAction));
    toolbar.addSeparator ();
    toolbar.add (createButton (aboutAction));

    return toolbar;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * The starting point for the demonstration application.
   */
  public static void main (String[] args)
  {
    try
    {
      UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
    }
    catch (Exception e)
    {
    }
    if (System.getProperty ("com.jrefinery.report.demo.DEBUG", "false").equals ("true"))
    {
      Log.addTarget (new SystemOutLogTarget ());
    }

    PDFOutputTarget.getFontFactory ().registerDefaultFontPath ();

    String baseName = "com.jrefinery.report.demo.resources.DemoResources";
    ResourceBundle resources = ResourceBundle.getBundle (baseName);

    HugeJFreeReportDemo frame = new HugeJFreeReportDemo (resources);
    frame.pack ();
    JRefineryUtilities.centerFrameOnScreen (frame);
    frame.setVisible (true);
  }

  private class previewConf
  {
    String m_reportFile;
    AbstractTableModel m_data;

    public previewConf(String reportFile, AbstractTableModel data)
    {
      m_reportFile = reportFile;
      m_data = data;
    }
  }
}
