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
 * --------------------
 * JFreeReportDemo.java
 * --------------------
 * (C)opyright 2000-2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: JFreeReportDemo.java,v 1.5 2003/09/15 15:31:58 taqua Exp $
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

package org.jfree.report.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import org.jfree.report.Boot;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AboutAction;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionMenuItem;
import org.jfree.report.modules.gui.base.components.FloatingButtonEnabler;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.about.AboutFrame;

/**
 * The main frame in the report demonstration application. This demo has huge reports
 * added, so calling report 5 and 6 can require some patience.
 * <p>
 * If the system property "org.jfree.report.demo.DEBUG" is set to "true", debugging
 * messages to System.out stream are enabled.
 *
 * @author David Gilbert
 */
public class JFreeReportDemo extends AbstractDemoFrame
{
  /**
   * A demo handler class.
   */
  protected abstract static class DemoHandler
  {
    /**
     * Launches a preview of the report demo.
     *
     * @param def  the demo definition.
     */
    public abstract void performPreview(DemoDefinition def);
  }

  /**
   * A URL demo handler.
   */
  protected class URLDemoHandler extends DemoHandler
  {
    /** The report definition URL. */
    private final String definitionURL;

    /**
     * Creates a new handler.
     *
     * @param definitionURL  the URL for the report definition.
     */
    public URLDemoHandler(final String definitionURL)
    {
      this.definitionURL = definitionURL;
    }

    /**
     * Performs the preview.
     *
     * @param def  the demo definition.
     */
    public void performPreview(final DemoDefinition def)
    {
      preview(definitionURL, def.getData());
    }
  }

  /**
   * A demo definition.
   */
  protected static class DemoDefinition
  {
    /** The demo name. */
    private String name;

    /** The data for the demo. */
    private TableModel data;

    /** The demo handler. */
    private DemoHandler handler;

    /**
     * Creates a new demo definition.
     *
     * @param name  the demo name.
     * @param data  the data.
     * @param handler  the demo handler.
     */
    public DemoDefinition(final String name, final TableModel data, final DemoHandler handler)
    {
      if (name == null)
      {
        throw new NullPointerException("Name must not be null");
      }
      if (data == null)
      {
        throw new NullPointerException("TableModel must not be null");
      }
      if (handler == null)
      {
        throw new NullPointerException("Handler must not be null");
      }

      this.name = name;
      this.data = data;
      this.handler = handler;
    }

    /**
     * Returns the name of the demo.
     *
     * @return The name.
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the data for the demo.
     *
     * @return The data.
     */
    public TableModel getData()
    {
      return data;
    }

    /**
     * Returns the demo handler.
     *
     * @return The demo handler.
     */
    public DemoHandler getHandler()
    {
      return handler;
    }
  }

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
    public void actionPerformed(final ActionEvent event)
    {
      displayAbout();
    }
  }

  /** About action. */
  private AboutAction aboutAction;

  /** A frame for displaying information about the demo application. */
  private AboutFrame aboutFrame;

  /** A tabbed pane for displaying the sample data sets. */
  private final JTabbedPane tabbedPane;

  /** A list of the available demos. */
  private final List availableDemos;

  /**
   * Constructs a frame containing sample reports created using the JFreeReport Class Library.
   */
  public JFreeReportDemo()
  {
    final Object[] arguments = new Object[]{JFreeReport.getInfo().getVersion()};
    final String pattern = getResources().getString("main-frame.title.pattern");
    setTitle(MessageFormat.format(pattern, arguments));

    availableDemos = createAvailableDemos();
    createActions();

    // set up the menu
    setJMenuBar(createMenuBar());

    final JPanel content = (JPanel) getContentPane();
    final JToolBar toolbar = createToolBar();
    content.add(toolbar, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    for (int i = 0; i < availableDemos.size(); i++)
    {
      final DemoDefinition dd = (DemoDefinition) availableDemos.get(i);
      tabbedPane.addTab(dd.getName(),
          RefineryUtilities.createTablePanel(dd.getData()));
    }

    content.add(tabbedPane);

    final JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout());

    final ActionButton helpButton = new ActionButton();
    helpButton.setAction(aboutAction);
    FloatingButtonEnabler.getInstance().addButton(helpButton);
    buttons.add(helpButton);

    final ActionButton previewButton = new ActionButton();
    previewButton.setAction(getPreviewAction());
    FloatingButtonEnabler.getInstance().addButton(previewButton);
    buttons.add(previewButton);

    final ActionButton closeButton = new ActionButton();
    closeButton.setAction(getCloseAction());
    FloatingButtonEnabler.getInstance().addButton(closeButton);
    buttons.add(closeButton);

    buttons.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
    content.add(buttons, BorderLayout.SOUTH);
    setContentPane(content);
  }

  /**
   * Creates a list of the available demos.
   *
   * @return The list.
   */
  protected List createAvailableDemos()
  {
    // create a couple of sample data sets
    final TableModel data1 = new SampleData1();
    final TableModel data2 = new SampleData2();
    final TableModel data3 = new SampleData3();
    final TableModel data4 = new SampleData4();

    final ArrayList list = new ArrayList();
    list.add(new DemoDefinition(createExampleName(1), data1,
        new URLDemoHandler("/org/jfree/report/demo/report1.xml")));

    list.add(new DemoDefinition(createExampleName(2), data2,
        new URLDemoHandler("/org/jfree/report/demo/report2.xml")));

    list.add(new DemoDefinition(createExampleName(3), data3,
        new URLDemoHandler("/org/jfree/report/demo/report3.xml")));

    list.add(new DemoDefinition(createExampleName(4), data4,
        new URLDemoHandler("/org/jfree/report/demo/report4.xml")));

    list.add(new DemoDefinition("Report created by API", data1, new DemoHandler()
    {
      public void performPreview(final DemoDefinition def)
      {
        previewAPIReport(def.getData());
      }
    }));

    list.add(new DemoDefinition("Example 1 - using Extended Report Definition format", data1,
        new URLDemoHandler("/org/jfree/report/demo/report1a.xml")));

    list.add(new DemoDefinition("Example 2 - with Image-Function", data2,
        new URLDemoHandler("/org/jfree/report/demo/report2a.xml")));

    list.add(new DemoDefinition("ItemHideFunction-Demo", data2,
        new URLDemoHandler("/org/jfree/report/demo/report2b.xml")));

    list.add(new DemoDefinition("Dynamic-Demo", data2,
        new URLDemoHandler("/org/jfree/report/demo/report2c.xml")));

    list.add(new DemoDefinition("Band in Band Stacking", data2, new DemoHandler()
    {
      public void performPreview(final DemoDefinition def)
      {
        previewBandInBandStacking();
      }
    }));

    list.add(new DemoDefinition("Shape and Drawable", new DefaultTableModel(),
        new URLDemoHandler("/org/jfree/report/demo/shape-and-drawable.xml")));

    list.add(new DemoDefinition("Example 2 - table with cell borders", data2,
        new URLDemoHandler("/org/jfree/report/demo/report2d.xml")));

    return list;
  }

  /**
   * Returns a list of the available demos.
   *
   * @return The list.
   */
  public List getAvailableDemos()
  {
    return availableDemos;
  }

  /**
   * Forms the localized example string.
   *
   * @param ex  the example number.
   *
   * @return the string.
   */
  protected String createExampleName(final int ex)
  {
    return MessageFormat.format(getResources().getString("example"),
        new Object[]{new Integer(ex)});
  }

  /**
   * Handles a request to preview a report.  First determines which data set is visible, then
   * calls the appropriate preview method.
   */
  protected void attemptPreview()
  {
    final int index = tabbedPane.getSelectedIndex();
    final DemoDefinition dd = (DemoDefinition) getAvailableDemos().get(index);
    dd.getHandler().performPreview(dd);
  }

  /**
   * Preview a report created by using the API.
   *
   * @param data  the data for the report.
   */
  protected void previewAPIReport(final TableModel data)
  {
    try
    {
      final JFreeReport report1 = new SampleReport1().createReport();
      report1.setData(data);

      final PreviewFrame frame1 = new PreviewFrame(report1);
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
  protected void previewBandInBandStacking()
  {
    try
    {
      final JFreeReport report1 = new SampleReport2().createReport();
      report1.setData(new DefaultTableModel());

      final PreviewFrame frame1 = new PreviewFrame(report1);
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
  public void preview(final String urlname, final TableModel data)
  {
    final URL in = getClass().getResource(urlname);
    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
          MessageFormat.format(getResources().getString("report.definitionnotfound"),
              new Object[]{urlname}),
          getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
      return;
    }

    Log.debug("Processing Report: " + in);
    final ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      final JFreeReport report1 = gen.parseReport(in, in);
      if (report1 == null)
      {
        JOptionPane.showMessageDialog(this,
            MessageFormat.format(getResources().getString("report.definitionnull"),
                new Object[]{urlname}),
            getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
        return;
      }

      report1.setData(data);
      //report1.getReportConfiguration().setStrictErrorHandling(false);  // DG
      final PreviewFrame frame1 = new PreviewFrame(report1);
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
   * Returns the preferred size of the frame.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(440, 300);
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
    aboutAction = new DemoAboutAction();
  }

  /**
   * Creates and returns a menu-bar for the frame.
   *
   * @return the menu bar.
   */
  private JMenuBar createMenuBar()
  {

    // create the menus
    final JMenuBar menuBar = new JMenuBar();
    menuBar.setBorder(null);
    // first the file menu
    final JMenu fileMenu = createJMenuItem("menu.file");

    final JMenuItem printItem = new ActionMenuItem(getPreviewAction());
    final KeyStroke accelerator = (KeyStroke)
        getPreviewAction().getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
    {
      printItem.setAccelerator(accelerator);
    }
    fileMenu.add(printItem);

    fileMenu.add(new JSeparator());

    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());
    fileMenu.add(exitItem);

    // then the help menu
    final JMenu helpMenu = createJMenuItem("menu.help");

    final JMenuItem aboutItem = new ActionMenuItem(aboutAction);
    helpMenu.add(aboutItem);

    // finally, glue together the menu and return it
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    return menuBar;
  }

  /**
   * Creates a new button based on the action. The button will be floating enabled,
   * so that the buttons borders are only visible when the mouse has entered the button area.
   *
   * @param action  the action.
   *
   * @return a button based on the action.
   */
  protected JButton createButton(final Action action)
  {
    final ActionButton button = new ActionButton(action);
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
    final JToolBar toolbar = new JToolBar();
    toolbar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

    toolbar.add(createButton(getPreviewAction()));
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
  public static void main(final String[] args)
  {
    // initialize JFreeReport 
    Boot.start();

    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
        Log.info("Look and feel problem.");
      }

      final JFreeReportDemo frame = new JFreeReportDemo();
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
