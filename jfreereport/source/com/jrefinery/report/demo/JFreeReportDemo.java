/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * --------------------
 * JFreeReportDemo.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: JFreeReportDemo.java,v 1.55 2003/02/26 16:42:05 mungady Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 07-May-2002 : Demo now uses resource bundles for localisation...just need some translations
 *               now (DG);
 * 16-May-2002 : Line delimiters adjusted
 *               get report definition from jar (getResource())
 *               close behaviour unified
 * 05-Jun-2002 : Documentation
 * 10-Jun-2002 : Updated code to work with latest version of the JCommon class library;
 * 27-Aug-2002 : Added more demos
 * 29-Aug-2002 : Downport to JDK 1.2.2
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ActionDowngrade;
import com.jrefinery.report.util.ActionMenuItem;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FloatingButtonEnabler;
import com.jrefinery.report.util.Log;
import com.jrefinery.ui.RefineryUtilities;
import com.jrefinery.ui.about.AboutFrame;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * The main frame in the report demonstration application. This demo has huge reports
 * added, so calling report 5 and 6 can require some patience.
 * <p>
 * If the system property "com.jrefinery.report.demo.DEBUG" is set to "true", debugging
 * messages to System.out stream are enabled.
 *
 * @author David Gilbert
 */
public class JFreeReportDemo extends JFrame
{
  /**
   * About action.
   */
  private class DemoAboutAction extends AboutAction
  {
    /**
     * Default constructor.
     */
    public DemoAboutAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(ActionEvent event)
    {
      System.gc();
      displayAbout();
    }
  }

  /**
   * Preview action.
   */
  private class DemoPreviewAction extends PreviewAction
  {
    /**
     * Default constructor.
     */
    public DemoPreviewAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(ActionEvent event)
    {
      attemptPreview();
    }
  }

  /**
   * Close action.
   */
  private class DemoCloseAction extends CloseAction
  {
    /**
     * Default constructor.
     */
    public DemoCloseAction()
    {
      super(getResources());
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(ActionEvent event)
    {
      attemptExit();
    }
  }

  /**
   * Window close handler.
   */
  private class CloseHandler extends WindowAdapter
  {
    /**
     * Handles the window closing event.
     *
     * @param event  the window event.
     */
    public void windowClosing(WindowEvent event)
    {
      attemptExit();
    }
  }

  /** Preview action. */
  private PreviewAction previewAction;

  /** About action. */
  private AboutAction aboutAction;

  /** Close action. */
  private CloseAction closeAction;

  /** A frame for displaying information about the demo application. */
  private AboutFrame aboutFrame;

  /** A tabbed pane for displaying the sample data sets. */
  private JTabbedPane tabbedPane;

  /** A table model containing sample data. */
  private SampleData1 data1;

  /** A table model containing sample data. */
  private SampleData2 data2;

  /** A table model containing sample data. */
  private SampleData3 data3;

  /** A table model containing sample data. */
  private SampleData4 data4;

  /** Localised resources. */
  private ResourceBundle resources;

  /**
   * Returns the localised resources.
   *
   * @return localised resources.
   */
  private ResourceBundle getResources()
  {
    return this.resources;
  }

  /**
   * Constructs a frame containing sample reports created using the JFreeReport Class Library.
   *
   * @param resources  localised resources.
   */
  public JFreeReportDemo(ResourceBundle resources)
  {
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new CloseHandler());
    this.resources = resources;
    Object[] arguments = new Object[]{JFreeReport.getInfo().getVersion()};
    String pattern = resources.getString("main-frame.title.pattern");
    setTitle(MessageFormat.format(pattern, arguments));

    // create a couple of sample data sets
    data1 = new SampleData1();
    data2 = new SampleData2();
    data3 = new SampleData3();
    data4 = new SampleData4();

    createActions();

    // set up the menu
    setJMenuBar(createMenuBar());

    JPanel content = (JPanel) getContentPane();
    JToolBar toolbar = createToolBar();
    content.add(toolbar, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    tabbedPane.addTab(formExample(1), RefineryUtilities.createTablePanel(data1));
    tabbedPane.addTab(formExample(2), RefineryUtilities.createTablePanel(data2));
    tabbedPane.addTab(formExample(3), RefineryUtilities.createTablePanel(data3));
    tabbedPane.addTab(formExample(4), RefineryUtilities.createTablePanel(data4));
    tabbedPane.addTab("Manual Created Report (see Source)",
                      RefineryUtilities.createTablePanel(data1));
    tabbedPane.addTab("ExtReport definition format (report1a.xml)",
                      RefineryUtilities.createTablePanel(data1));
    tabbedPane.addTab("Example 2 - with Image-Function",
                      RefineryUtilities.createTablePanel(data2));
    tabbedPane.addTab("ItemHideFunction-Demo", RefineryUtilities.createTablePanel(data2));
    tabbedPane.addTab("Dynamic-Demo", RefineryUtilities.createTablePanel(data2));
    tabbedPane.addTab("Band in Band Stacking", 
                      RefineryUtilities.createTablePanel(new DefaultTableModel()));
    tabbedPane.addTab("Advanced report demo",
                      RefineryUtilities.createTablePanel(new DefaultTableModel()));

    content.add(tabbedPane);

    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout());

    ActionButton helpButton = new ActionButton();
    helpButton.setAction (aboutAction);
    FloatingButtonEnabler.getInstance ().addButton (helpButton);
    buttons.add(helpButton);

    ActionButton previewButton = new ActionButton ();
    previewButton.setAction (previewAction);
    FloatingButtonEnabler.getInstance ().addButton (previewButton);
    buttons.add(previewButton);

    ActionButton closeButton = new ActionButton();
    closeButton.setAction (closeAction);
    FloatingButtonEnabler.getInstance ().addButton (closeButton);
    buttons.add(closeButton);

    buttons.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
    content.add(buttons, BorderLayout.SOUTH);
    setContentPane(content);

  }

  /**
   * Forms the localized example string.
   *
   * @param ex  the example number.
   *
   * @return the string.
   */
  private String formExample(int ex)
  {
    return MessageFormat.format(getResources().getString("example"),
                                new Object[]{new Integer(ex)});
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
      preview("/com/jrefinery/report/demo/report1.xml", data1);
    }
    else if (index == 1)
    {
      preview("/com/jrefinery/report/demo/report2.xml", data2);
    }
    else if (index == 2)
    {
      preview("/com/jrefinery/report/demo/report3.xml", data3);
    }
    else if (index == 3)
    {
      preview("/com/jrefinery/report/demo/report4.xml", data4);
    }
    else if (index == 4)
    {
      previewManual();
    }
    else if (index == 5)
    {
      preview("/com/jrefinery/report/demo/report1a.xml", data1);
    }
    else if (index == 6)
    {
      preview("/com/jrefinery/report/demo/report2a.xml", data2);
    }
    else if (index == 7)
    {
      preview("/com/jrefinery/report/demo/report2b.xml", data2);
    }
    else if (index == 8)
    {
      preview("/com/jrefinery/report/demo/report2c.xml", data2);
    }
    else if (index == 9)
    {
      previewManual2();
    }
    else if (index == 10)
    {
      preview("/com/jrefinery/report/demo/advanced-report.xml", new DefaultTableModel());
    }
  }

  /**
   * Preview.
   */
  private void previewManual()
  {
    try
    {
      JFreeReport report1 = new SampleReport1().createReport();
      report1.setData(data1);

      PreviewFrame frame1 = new PreviewFrame(report1);
      frame1.pack();
      RefineryUtilities.positionFrameRandomly(frame1);
      frame1.setVisible(true);
      frame1.requestFocus();
    }
    catch (Exception e)
    {
      showExceptionDialog("report.definitionfailure", e);
    }
  }

  /**
   * Preview.
   */
  private void previewManual2()
  {
    try
    {
      JFreeReport report1 = new SampleReport2().createReport();
      report1.setData(new DefaultTableModel());

      PreviewFrame frame1 = new PreviewFrame(report1);
      frame1.pack();
      RefineryUtilities.positionFrameRandomly(frame1);
      frame1.setVisible(true);
      frame1.requestFocus();
    }
    catch (Exception e)
    {
      showExceptionDialog("report.definitionfailure", e);
    }
  }

  /**
   * Displays a preview frame for report defined in the file specified by <code>urlname</code>.
   * The contents of the url are parsed and the report is fed into a new PreviewPane.
   * The given TableModel is assigned to the report as report data source.
   * <p>
   * If the report contains external references in specified in relative urls, the urls
   * are loaded using the reports parent directory as content base.
   *
   * @param urlname  the filename from where to load the report
   * @param data  the datamodel for the report
   */
  public void preview(String urlname, TableModel data)
  {
    URL in = getClass().getResource(urlname);
    if (in == null)
    {

      JOptionPane.showMessageDialog(this,
          MessageFormat.format(getResources().getString("report.definitionnotfound"),
                               new Object[]{urlname}),
          getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
      return;
    }

    Log.debug ("Processing Report: " + in);
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      JFreeReport report1 = gen.parseReport(in, in);
      if (report1 == null)
      {
        JOptionPane.showMessageDialog(this,
            MessageFormat.format(getResources().getString("report.definitionnull"),
                                 new Object[]{urlname}),
            getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
        return;
      }

      report1.setData(data);
      PreviewFrame frame1 = new PreviewFrame(report1);
      frame1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      frame1.pack();
      RefineryUtilities.positionFrameRandomly(frame1);
      frame1.setVisible(true);
      frame1.requestFocus();
    }
    catch (Exception e)
    {
      showExceptionDialog("report.definitionfailure", e);
    }
  }

  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
   *
   * @param localisationBase  the resource prefix.
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
   * Returns the preferred size of the frame.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(440, 300);
  }

  /**
   * Exits the application, but only if the user agrees.
   *
   * @return false if the user decides not to exit the application.
   */
  public boolean attemptExit()
  {
    boolean close =
        JOptionPane.showConfirmDialog(
            this,
            getResources().getString("exitdialog.message"),
            getResources().getString("exitdialog.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE)
        == JOptionPane.YES_OPTION;
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
      aboutFrame = new AboutFrame(getResources().getString("action.about.name"),
                                  JFreeReport.getInfo());

      aboutFrame.pack();
      RefineryUtilities.centerFrameOnScreen(aboutFrame);
    }
    aboutFrame.setVisible(true);
    aboutFrame.requestFocus();
  }

  /**
   * Create the actions used in the demo.
   */
  private void createActions()
  {
    previewAction = new DemoPreviewAction();
    aboutAction = new DemoAboutAction();
    closeAction = new DemoCloseAction();
  }

  /**
   * Creates and returns a menu-bar for the frame.
   *
   * @return the menu bar.
   */
  private JMenuBar createMenuBar()
  {

    // create the menus
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBorder(null);
    // first the file menu
    JMenu fileMenu = createJMenuItem("menu.file");

    JMenuItem printItem = new ActionMenuItem(previewAction);
    KeyStroke accelerator = (KeyStroke) previewAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
    {
      printItem.setAccelerator(accelerator);
    }
    fileMenu.add(printItem);

    fileMenu.add(new JSeparator());

    JMenuItem exitItem = new ActionMenuItem(closeAction);
    fileMenu.add(exitItem);

    // then the help menu
    JMenu helpMenu = createJMenuItem("menu.help");

    JMenuItem aboutItem = new ActionMenuItem(aboutAction);
    helpMenu.add(aboutItem);

    // finally, glue together the menu and return it
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    return menuBar;
  }

  /**
   * Creates a JMenu which gets initialized from the current resource bundle.
   *
   * @param base the resource prefix.
   *
   * @return the menu.
   */
  private JMenu createJMenuItem(String base)
  {
    String label = this.resources.getString(base + ".name");
    Character mnemonic = (Character) this.resources.getObject(base + ".mnemonic");

    JMenu menu = new JMenu(label);
    if (mnemonic != null)
    {
      menu.setMnemonic(mnemonic.charValue());
    }
    return menu;
  }

  /**
   * Creates a new button based on the action. The button will be floating enabled,
   * so that the buttons borders are only visible when the mouse has entered the button area.
   *
   * @param action  the action.
   *
   * @return a button based on the action.
   */
  protected JButton createButton(Action action)
  {
    ActionButton button = new ActionButton (action);
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setText(null);
    FloatingButtonEnabler.getInstance().addButton(button);
    return button;
  }

  /**
   * Creates the demos toolbar.
   *
   * @return the toolbar.
   */
  private JToolBar createToolBar()
  {
    JToolBar toolbar = new JToolBar();
    toolbar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

    toolbar.add(createButton(previewAction));
    toolbar.addSeparator();
    toolbar.add(createButton(aboutAction));

    return toolbar;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * The starting point for the demonstration application.
   *
   * @param args ignored.
   */
  public static void main(String[] args)
  {

    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
        System.out.println("Look and feel problem.");
      }

      String baseName = "com.jrefinery.report.demo.resources.DemoResources";
      ResourceBundle resources = ResourceBundle.getBundle(baseName);

      JFreeReportDemo frame = new JFreeReportDemo(resources);
      frame.pack();
      frame.setBounds(100, 100, 700, 400);
      RefineryUtilities.centerFrameOnScreen(frame);
      frame.setVisible(true);
    }
    catch (Throwable th)
    {
      th.printStackTrace();
    }
  }
}
