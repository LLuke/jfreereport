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
 * JFreeReportDemo.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: JFreeReportDemo.java,v 1.4 2002/05/16 13:39:38 jaosch Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 07-May-2002 : Demo now uses resource bundles for localisation...just need some translations
 *               now (DG);
 * 16-May-2002 : Line delimiters adjusted
 *               get report definition from jar (getResource())
 *               close behaviour unified
 *
 */

package com.jrefinery.report.demo;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
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

import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.SystemOutLogTarget;
import com.jrefinery.ui.JRefineryUtilities;
import com.jrefinery.ui.L1R2ButtonPanel;
import com.jrefinery.ui.about.AboutFrame;

/**
 * The main frame in the report demonstration application.
 *
 * ToDo: Localisation ...
 */
public class JFreeReportDemo extends JFrame implements WindowListener
{

  /** Constant for the 'About' command. */
  public static final String ABOUT_COMMAND = "ABOUT";

  /** Constant for the 'Print Preview' command. */
  public static final String PRINT_PREVIEW_COMMAND = "PRINT_PREVIEW";

  /** Constant for the 'Exit' command. */
  public static final String EXIT_COMMAND = "EXIT";

  protected PreviewAction previewAction;
  protected AboutAction aboutAction;
  protected CloseAction closeAction;

  /** A frame for displaying information about the demo application. */
  protected AboutFrame aboutFrame;

  /** A frame for displaying information about the system. */
  protected JFrame infoFrame;

  /** A tabbed pane for displaying the sample data sets. */
  protected JTabbedPane tabbedPane;

  /** A table model containing sample data. */
  protected SampleData1 data1;

  /** The first sample report. */
  protected JFreeReport report1;

  /** The preview frame for sample report 1. */
  protected PreviewFrame frame1;

  /** A table model containing sample data. */
  protected SampleData2 data2;

  /** The second sample report. */
  protected JFreeReport report2;

  /** The preview frame for sample report 2. */
  protected PreviewFrame frame2;

  /** A table model containing sample data. */
  private SampleData3 data3;

  /** The second sample report. */
  protected JFreeReport report3;

  /** The preview frame for sample report 3. */
  protected PreviewFrame frame3;

  private ResourceBundle m_resources;

  /**
   * Constructs a frame containing sample reports created using the JFreeReport Class Library.
   */
  public JFreeReportDemo(ResourceBundle resources)
  {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    m_resources = resources;
    Object[] arguments = new Object[] { JFreeReport.INFO.getVersion()};
    String pattern = resources.getString("main-frame.title.pattern");
    setTitle(MessageFormat.format(pattern, arguments));

    // create a couple of sample data sets
    data1 = new SampleData1();
    data2 = new SampleData2();
    data3 = new SampleData3();

    createActions();

    // set up the menu
    setJMenuBar(createMenuBar());

    JPanel content = (JPanel) getContentPane();
    JToolBar toolbar = createToolBar(resources);
    content.add(toolbar, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    tabbedPane.addTab("Example 1", JRefineryUtilities.createTablePanel(data1));
    tabbedPane.addTab("Example 2", JRefineryUtilities.createTablePanel(data2));
    tabbedPane.addTab("Example 3", JRefineryUtilities.createTablePanel(data3));

    content.add(tabbedPane);

    L1R2ButtonPanel buttons = new L1R2ButtonPanel("Help", "Preview", "Close");

    JButton helpButton = buttons.getLeftButton();
    helpButton.setAction(aboutAction);

    JButton previewButton = buttons.getRightButton1();
    previewButton.setAction(previewAction);

    JButton closeButton = buttons.getRightButton2();
    closeButton.setAction(closeAction);

    buttons.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
    content.add(buttons, BorderLayout.SOUTH);

    setContentPane(content);

  }

  /**
   * Handles a request to preview a report.  First determines which data set is visible, then
   * calls the appropriate preview method.
   */
  public void attemptPreview()
  {
    int index = tabbedPane.getSelectedIndex();

    if (index == 0)
    {
      preview1();
    }
    else if (index == 1)
    {
      preview2();
    }
    else
    {
      preview3();
    }
  }

  /**
   * Displays a preview frame for report one.  If the preview frame already exists, it is brought
   * to the front.
   */
  public void preview1()
  {
    File in = new File(getClass().getResource("report1.xml").getFile());
    if (in == null)
    {
      JOptionPane.showMessageDialog(this, "ReportDefinition report1.xml not found");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      report1 = gen.parseReport(in);
    }
    catch (Exception ioe)
    {
      JOptionPane.showMessageDialog(
        this,
        ioe.getMessage(),
        "Error: " + ioe.getClass().getName(),
        JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (report1 == null)
      throw new NullPointerException("Damn, is null");
    ItemBand band = report1.getItemBand();
    report1.setData(data1);

    frame1 = new PreviewFrame(report1);
    frame1.addWindowListener(this);
    frame1.pack();
    JRefineryUtilities.positionFrameRandomly(frame1);
    frame1.show();
    frame1.requestFocus();
  }

  /**
   * Displays a preview frame for report two.  If the preview frame already exists, it is brought
   * to the front.
   */
  public void preview2()
  {
    File in = new File(getClass().getResource("report2.xml").getFile());
    if (in == null)
    {
      JOptionPane.showMessageDialog(
        this,
        "ReportDefinition report2.xml not found on classpath");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      report2 = gen.parseReport(in);
    }
    catch (Exception ioe)
    {
      JOptionPane.showMessageDialog(
        this,
        ioe.getMessage(),
        "Error: " + ioe.getClass().getName(),
        JOptionPane.ERROR_MESSAGE);
      return;
    }

    report2.setData(data2);
    frame2 = new PreviewFrame(report2);
    frame2.addWindowListener(this);
    frame2.pack();
    JRefineryUtilities.positionFrameRandomly(frame2);
    frame2.show();
    frame2.requestFocus();
  }

  /**
   * Displays a preview frame for report two.  If the preview frame already exists, it is brought
   * to the front.
   */
  public void preview3()
  {
    // it would be nice to load the report file from the arcive with 
    // getResourceAsStream() ! (JS)
    File in = new File(getClass().getResource("report3.xml").getFile());
    if (in == null)
    {
      JOptionPane.showMessageDialog(
        this,
        "ReportDefinition report3.xml not found on classpath");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      report3 = gen.parseReport(in);
    }
    catch (Exception ioe)
    {
      JOptionPane.showMessageDialog(
        this,
        ioe.getMessage(),
        "Error: " + ioe.getClass().getName(),
        JOptionPane.ERROR_MESSAGE);
      return;
    }
    report3.setData(data3);
    frame3 = new PreviewFrame(report3);
    frame3.addWindowListener(this);
    frame3.pack();
    JRefineryUtilities.positionFrameRandomly(frame3);
    frame3.show();
    frame3.requestFocus();
  }

  /**
   * Returns the preferred size of the frame.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(440, 300);
  }

  /**
   * Exits the application, but only if the user agrees.
   */
  public boolean attemptExit()
  {
    boolean close =
      JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to exit?",
        "Confirmation...",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    if (close)
    {
      dispose();
      System.exit(0);
    }
    
    return close;
  }

  /**
   * Displays information about the application.
   */
  public void displayAbout()
  {
    if (aboutFrame == null)
    {
      aboutFrame = new AboutFrame("About...", JFreeReport.INFO);

      aboutFrame.pack();
      JRefineryUtilities.centerFrameOnScreen(aboutFrame);
    }
    aboutFrame.setVisible(true);
    aboutFrame.requestFocus();
  }

  private void createActions()
  {
    previewAction = new PreviewAction(this, m_resources);
    aboutAction = new AboutAction(this, m_resources);
    closeAction = new CloseAction(this, m_resources);
  }

  /**
   * Creates and returns a menu-bar for the frame.
   *
   * @param m_resources Localised m_resources
   */
  private JMenuBar createMenuBar()
  {

    // create the menus
    JMenuBar menuBar = new JMenuBar();

    // first the file menu
    JMenu fileMenu = createJMenuItem("menu.file");

    JMenuItem printItem = new JMenuItem(previewAction);
    KeyStroke accelerator = (KeyStroke) previewAction.getValue(Action.ACCELERATOR_KEY);
    if (accelerator != null)
      printItem.setAccelerator(accelerator);
    fileMenu.add(printItem);

    fileMenu.add(new JSeparator());

    JMenuItem exitItem = new JMenuItem(closeAction);
    fileMenu.add(exitItem);

    // then the help menu
    JMenu helpMenu = createJMenuItem("menu.help");

    JMenuItem aboutItem = new JMenuItem(aboutAction);
    helpMenu.add(aboutItem);

    // finally, glue together the menu and return it
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    return menuBar;
  }

  private JMenu createJMenuItem(String base)
  {
    // first the file menu

    String label = m_resources.getString(base + ".name");
    Character mnemonic = (Character) m_resources.getObject(base + ".mnemonic");

    JMenu menu = new JMenu(label);
    if (mnemonic != null)
    {
      menu.setMnemonic(mnemonic.charValue());
    }
    return menu;
  }

  private JToolBar createToolBar(ResourceBundle resources)
  {
    JToolBar toolbar = new JToolBar();

    toolbar.add(previewAction);
    toolbar.addSeparator();
    toolbar.add(aboutAction);

    return toolbar;
  }

  /**
   * Clears the reference to the print preview frames when they are closed.
   */
  public void windowClosed(WindowEvent e)
  {
    if (e.getWindow() == this.frame1)
    {
      frame1 = null;
    }
    else if (e.getWindow() == this.frame2)
    {
      frame2 = null;
    }
    else if (e.getWindow() == this.infoFrame)
    {
      infoFrame = null;
    }
    else if (e.getWindow() == this.aboutFrame)
    {
      aboutFrame = null;
    }
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowActivated(WindowEvent e)
  {
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowClosing(WindowEvent e)
  {
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowDeactivated(WindowEvent e)
  {
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowDeiconified(WindowEvent e)
  {
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowIconified(WindowEvent e)
  {
  }

  /**
   * Required for WindowListener interface, but not used by this class.
   */
  public void windowOpened(WindowEvent e)
  {
  }

  /**
   * @see JFrame#processWindowEvent(WindowEvent)
   */
  protected void processWindowEvent(WindowEvent windowEvent)
  {
    if (windowEvent.getID() == WindowEvent.WINDOW_CLOSING) {
      if (attemptExit()) {
        super.processWindowEvent(windowEvent);
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * The starting point for the demonstration application.
   */
  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
    }
    Log.addTarget(new SystemOutLogTarget());
    String baseName = "com.jrefinery.report.demo.resources.DemoResources";
    ResourceBundle resources = ResourceBundle.getBundle(baseName);

    JFreeReportDemo frame = new JFreeReportDemo(resources);
    frame.pack();
    JRefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}