/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id: PreviewPane.java,v 1.1 2006/11/13 19:27:45 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.swing.preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.jfree.layout.CenterLayout;
import org.jfree.layouting.modules.output.graphics.PageDrawable;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.report.modules.gui.swing.common.ActionFactory;
import org.jfree.report.modules.gui.swing.common.ActionPlugin;
import org.jfree.report.modules.gui.swing.common.ExportActionPlugin;
import org.jfree.report.modules.gui.swing.common.SwingGuiContext;
import org.jfree.report.modules.gui.swing.printing.PrintReportProcessor;
import org.jfree.report.util.Worker;
import org.jfree.ui.Drawable;
import org.jfree.ui.DrawablePanel;
import org.jfree.ui.FloatingButtonEnabler;
import org.jfree.ui.action.ActionButton;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.11.2006, 19:36:13
 *
 * @author Thomas Morgner
 */
public class PreviewPane extends JPanel
{
  private class PreviewGuiContext implements SwingGuiContext
  {
    public PreviewGuiContext()
    {
    }

    public Window getWindow()
    {
      return SwingUtilities.getWindowAncestor(PreviewPane.this);
    }

    public Locale getLocale()
    {
      JFreeReport report = getReport();
      if (report != null)
      {
        return report.getLocale();
      }
      return Locale.getDefault();
    }

    public IconTheme getIconTheme()
    {
      return PreviewPane.this.getIconTheme();
    }

    public Configuration getConfiguration()
    {
      JFreeReport report = getReport();
      if (report != null)
      {
        return report.getConfiguration();
      }
      return JFreeReportBoot.getInstance().getGlobalConfig();
    }
  }

  /**
   * A zoom select action.
   */
  private class ZoomSelectAction extends AbstractAction
  {
    private JComboBox source;

    /**
     * Creates a new action.
     */
    public ZoomSelectAction(JComboBox source)
    {
      this.source = source;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setZoom(source.getSelectedIndex());
    }
  }

  private class RepaginationRunnable implements Runnable
  {
    private PrintReportProcessor processor;

    public RepaginationRunnable(PrintReportProcessor processor)
    {
      this.processor = processor;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to
     * create a thread, starting the thread causes the object's <code>run</code>
     * method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take
     * any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run()
    {
      final UpdatePaginatingPropertyHandler startPaginationNotify =
          new UpdatePaginatingPropertyHandler(processor, true, 0);
      if (SwingUtilities.isEventDispatchThread())
      {
        startPaginationNotify.run();
      }
      else
      {
        SwingUtilities.invokeLater(startPaginationNotify);
      }

      // Perform the pagination ..
      final int pageCount = processor.getNumberOfPages();

      final UpdatePaginatingPropertyHandler endPaginationNotify =
          new UpdatePaginatingPropertyHandler(processor, false, pageCount);
      if (SwingUtilities.isEventDispatchThread())
      {
        endPaginationNotify.run();
      }
      else
      {
        SwingUtilities.invokeLater(endPaginationNotify);
      }

    }
  }

  private class UpdatePaginatingPropertyHandler implements Runnable
  {
    private boolean paginating;
    private int pageCount;
    private PrintReportProcessor processor;

    public UpdatePaginatingPropertyHandler(final PrintReportProcessor processor,
                                           final boolean paginating,
                                           final int pageCount)
    {
      this.processor = processor;
      this.paginating = paginating;
      this.pageCount = pageCount;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to
     * create a thread, starting the thread causes the object's <code>run</code>
     * method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take
     * any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run()
    {
      if (processor != getPrintReportProcessor())
      {
        Log.debug("No longer valid");
        return;
      }

      Log.debug("Pagination: " + paginating + " No. " + pageCount);
      if (paginating == false)
      {
        setNumberOfPages(pageCount);
        if (getPageNumber() < 1)
        {
          setPageNumber(1);
        }
        else if (getPageNumber() > pageCount)
        {
          setPageNumber(pageCount);
        }
      }
      setPaginating(paginating);
    }
  }

  private class PreviewUpdateHandler implements PropertyChangeListener
  {
    public PreviewUpdateHandler()
    {
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
      final String propertyName = evt.getPropertyName();
      if (PAGINATING_PROPERTY.equals(propertyName))
      {
        if (isPaginating())
        {
          drawablePanel.setDrawable(getPaginatingDrawable());
        }
        else
        {
          updateVisiblePage(getPageNumber());
        }
      }
      else if (REPORT_JOB_PROPERTY.equals(propertyName))
      {
        if (getReport() == null)
        {
          drawablePanel.setDrawable(getNoReportDrawable());
        }
        // else the paginating property will be fired anyway ..
      }
      else if (PAGE_NUMBER_PROPERTY.equals(propertyName))
      {
        if (isPaginating())
        {
          return;
        }

        updateVisiblePage(getPageNumber());
      }
    }
  }

  private static final double ZOOM_FACTORS[] = {
      50, 75, 100, 120, 150, 200
  };
  private static final int DEFAULT_ZOOM_INDEX = 2;
  public static final String STATUS_TEXT_PROPERTY = "statusText";
  public static final String STATUS_TYPE_PROPERTY = "statusType";
  public static final String REPORT_CONTROLLER_PROPERTY = "reportController";
  public static final String REPORT_JOB_PROPERTY = "reportJob";
  public static final String ZOOM_PROPERTY = "zoom";
  public static final String CLOSED_PROPERTY = "closed";
  public static final String PAGINATING_PROPERTY = "paginating";
  public static final String ICON_THEME_PROPERTY = "iconTheme";
  public static final String TITLE_PROPERTY = "title";
  public static final String MENU_PROPERTY = "menu";

  private static final String REPORT_CATEGORY = "report";
  private static final String EXPORT_CATEGORY = "export";
  private static final String HELP_CATEGORY = "help";
  private static final String GOTO_CATEGORY = "goto";



  private Drawable paginatingDrawable;
  private Drawable noReportDrawable;
  private PageBackgroundDrawable pageDrawable;

  private DrawablePanel drawablePanel;
  private ReportController reportController;
  private JMenu[] menus;
  private JToolBar toolBar;
  private String statusText;
  private String title;
  private int statusType;
  private boolean closed;
  private ReportJob reportJob;

  private int numberOfPages;
  private int pageNumber;
  private SwingGuiContext swingGuiContext;
  private IconTheme iconTheme;
  private double zoom;
  private boolean paginating;

  private PrintReportProcessor printReportProcessor;


  private Worker paginationWorker;
  private static final String PAGE_NUMBER_PROPERTY = "pageNumber";
  private static final String NUMBER_OF_PAGES_PROPERTY = "numberOfPages";
  private JPanel innerReportControllerHolder;
  private JPanel toolbarHolder;
  private JPanel outerReportControllerHolder;
  private boolean reportControllerInner;
  private String reportControllerLocation;
  private JComponent reportControllerComponent;


  /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
  public PreviewPane()
  {
    this.menus = new JMenu[0];
    setLayout(new BorderLayout());

    pageDrawable = new PageBackgroundDrawable();

    drawablePanel = new DrawablePanel();
    drawablePanel.setOpaque(false);
    drawablePanel.setBackground(Color.green);

    swingGuiContext = new PreviewGuiContext();

    final JPanel reportPaneHolder = new JPanel(new CenterLayout());
    reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    reportPaneHolder.add(drawablePanel);

    final JScrollPane s1 = new JScrollPane(reportPaneHolder);
    s1.getVerticalScrollBar().setUnitIncrement(20);

    innerReportControllerHolder = new JPanel();
    innerReportControllerHolder.setLayout(new BorderLayout());
    innerReportControllerHolder.add(s1, BorderLayout.CENTER);

    toolbarHolder = new JPanel();
    toolbarHolder.setLayout(new BorderLayout());
    toolbarHolder.add(innerReportControllerHolder, BorderLayout.CENTER);

    outerReportControllerHolder = new JPanel();
    outerReportControllerHolder.setLayout(new BorderLayout());
    outerReportControllerHolder.add(toolbarHolder, BorderLayout.CENTER);

    add(outerReportControllerHolder, BorderLayout.CENTER);

    addPropertyChangeListener(new PreviewUpdateHandler());
  }

  public synchronized PrintReportProcessor getPrintReportProcessor()
  {
    return printReportProcessor;
  }

  protected synchronized void setPrintReportProcessor(final PrintReportProcessor printReportProcessor)
  {
    this.printReportProcessor = printReportProcessor;
  }

  public JMenu[] getMenu()
  {
    return menus;
  }

  protected void setMenu(final JMenu[] menus)
  {
    if (menus == null)
    {
      throw new NullPointerException();
    }
    final JMenu[] oldmenu = this.menus;
    this.menus = (JMenu[]) menus.clone();
    firePropertyChange(MENU_PROPERTY, oldmenu, this.menus);
  }

  public JToolBar getToolBar()
  {
    return toolBar;
  }

  public String getStatusText()
  {
    return statusText;
  }

  public void setStatusText(final String statusText)
  {
    String oldStatus = this.statusText;
    this.statusText = statusText;

    firePropertyChange(STATUS_TEXT_PROPERTY, oldStatus, statusText);
  }

  public int getStatusType()
  {
    return statusType;
  }

  public void setStatusType(final int statusType)
  {
    int oldType = this.statusType;
    this.statusType = statusType;

    firePropertyChange(STATUS_TYPE_PROPERTY, oldType, statusType);
  }

  public ReportController getReportController()
  {
    return reportController;
  }

  public void setReportController(final ReportController reportController)
  {
    ReportController oldController = this.reportController;
    this.reportController = reportController;
    firePropertyChange(REPORT_CONTROLLER_PROPERTY, oldController, reportController);

    // Now add the controller to the GUI ..
    if (reportController != null)
    {
      final JComponent rcp = reportController.getControlPanel();
      if (reportControllerComponent != rcp ||
          reportControllerInner != reportController.isInnerComponent() ||
          ObjectUtilities.equal(reportControllerLocation,
              reportController.getControlerLocation()))
      {
        if (reportControllerComponent != null)
        {
          outerReportControllerHolder.remove(reportControllerComponent);
          innerReportControllerHolder.remove(reportControllerComponent);
        }
        final String sanLocation = sanitizeLocation(
            reportController.getControlerLocation());
        final boolean innerComponent = reportController.isInnerComponent();
        if (rcp != null)
        {
          if (innerComponent)
          {
            innerReportControllerHolder.add(rcp, sanLocation);
          }
          else
          {
            outerReportControllerHolder.add(rcp, sanLocation);
          }
        }
        reportControllerComponent = rcp;
        reportControllerLocation = sanLocation;
        reportControllerInner = innerComponent;
      }
    }
    else
    {
      if (reportControllerComponent != null)
      {
        outerReportControllerHolder.remove(reportControllerComponent);
        innerReportControllerHolder.remove(reportControllerComponent);
      }
      reportControllerComponent = null;
    }

  }


  private String sanitizeLocation(final String location)
  {
    if (BorderLayout.NORTH.equals(location))
    {
      return BorderLayout.NORTH;
    }
    if (BorderLayout.SOUTH.equals(location))
    {
      return BorderLayout.SOUTH;
    }
    if (BorderLayout.WEST.equals(location))
    {
      return BorderLayout.WEST;
    }
    if (BorderLayout.EAST.equals(location))
    {
      return BorderLayout.EAST;
    }
    return BorderLayout.NORTH;
  }

  public ReportJob getReportJob()
  {
    return reportJob;
  }

  public void setReportJob(final ReportJob reportJob)
  {
    ReportJob oldJob = this.reportJob;
    this.reportJob = reportJob;

    firePropertyChange(REPORT_JOB_PROPERTY, oldJob, reportJob);
    if (reportJob == null)
    {
      initializeWithoutJob();
    }
    else
    {
      initializeFromReport();
    }

  }

  public JFreeReport getReport()
  {
    if (reportJob == null)
    {
      return null;
    }
    return reportJob.getReport();
  }

  public void setReport(final JFreeReport report)
  {
    setReportJob(new ReportJob(report));
  }

  public double getZoom()
  {
    return zoom;
  }

  public void setZoom(final double zoom)
  {
    double oldZoom = this.zoom;
    this.zoom = zoom;
    firePropertyChange(ZOOM_PROPERTY, oldZoom, zoom);
  }

  public boolean isClosed()
  {
    return closed;
  }

  public void setClosed(final boolean closed)
  {
    boolean oldClosed = this.closed;
    this.closed = closed;
    firePropertyChange(CLOSED_PROPERTY, oldClosed, closed);
    if (closed)
    {
      prepareShutdown();
    }
  }

  private void prepareShutdown()
  {
    synchronized (this)
    {
      if (paginationWorker != null)
      {
        paginationWorker.finish();
      }
      if (printReportProcessor != null)
      {
        printReportProcessor.close();
        printReportProcessor = null;
      }
      closeToolbar();
    }
  }

  private HashMap loadActions()
  {
    HashMap actions = new HashMap();

    final Configuration configuration = swingGuiContext.getConfiguration();
    final ActionFactory factory = PreviewPaneUtilities.createActionFactory(configuration);
    actions.put(REPORT_CATEGORY, factory.getActions(swingGuiContext, REPORT_CATEGORY));
    actions.put(EXPORT_CATEGORY, factory.getActions(swingGuiContext, EXPORT_CATEGORY));
    actions.put(GOTO_CATEGORY, factory.getActions(swingGuiContext, GOTO_CATEGORY));
    actions.put(HELP_CATEGORY, factory.getActions(swingGuiContext, HELP_CATEGORY));
    return actions;
  }

  public Locale getLocale()
  {
    JFreeReport report = getReport();
    if (report != null)
    {
      return report.getLocale();
    }
    return super.getLocale();
  }

  public int getNumberOfPages()
  {
    return numberOfPages;
  }

  public void setNumberOfPages(final int numberOfPages)
  {
    final int oldPageNumber = this.numberOfPages;
    this.numberOfPages = numberOfPages;
    firePropertyChange(NUMBER_OF_PAGES_PROPERTY, oldPageNumber, numberOfPages);
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(final int pageNumber)
  {
    final int oldPageNumber = this.pageNumber;
    this.pageNumber = pageNumber;
    Log.debug ("Setting PageNumber: " + pageNumber);
    firePropertyChange(PAGE_NUMBER_PROPERTY, oldPageNumber, pageNumber);
  }

  public IconTheme getIconTheme()
  {
    return iconTheme;
  }

  protected void setIconTheme(IconTheme theme)
  {
    IconTheme oldTheme = this.iconTheme;
    this.iconTheme = theme;
    firePropertyChange(ICON_THEME_PROPERTY, oldTheme, theme);
  }

  protected void initializeFromReport()
  {
    setIconTheme(PreviewPaneUtilities.createIconTheme(reportJob.getConfiguration()));

    HashMap actions = loadActions();
    buildMenu(actions);

    if (toolBar != null)
    {
      toolbarHolder.remove(toolBar);
    }
    toolBar = buildToolbar(actions);
    if (toolBar != null)
    {
      toolbarHolder.add(toolBar, BorderLayout.NORTH);
    }

    startPagination();
  }

  /**
   * Creates a button using the given action properties for the button's
   * initialisation.
   *
   * @param action the action used to set up the button.
   * @return a button based on the supplied action.
   */
  protected JButton createButton(final Action action)
  {
    final JButton button = new ActionButton(action);
    boolean needText = true;
    if (isLargeButtonsEnabled())
    {
      final Icon icon = (Icon) action.getValue("ICON24");
      if (icon != null && (icon.getIconHeight() > 1 && icon.getIconHeight() > 1))
      {
        button.setIcon(icon);
        needText = false;
      }
    }
    else
    {
      final Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
      if (icon != null && (icon.getIconHeight() > 1 && icon.getIconHeight() > 1))
      {
        button.setIcon(icon);
        needText = false;
      }
    }

    if (needText)
    {
      final Object value = action.getValue(Action.NAME);
      if (value != null)
      {
        button.setText(String.valueOf(value));
      }
    }
    else
    {
      button.setText(null);
      button.setMargin(new Insets(0, 0, 0, 0));
    }

    FloatingButtonEnabler.getInstance().addButton(button);
    return button;
  }

  private boolean isLargeButtonsEnabled()
  {
    final Configuration configuration = swingGuiContext.getConfiguration();
    if ("true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.gui.swing.preview.LargeIcons")))
    {
      return true;
    }
    return false;
  }

  private boolean isToolbarZoomEnabled()
  {
    final Configuration configuration = swingGuiContext.getConfiguration();
    if ("true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.gui.swing.preview.EnableToolBarZoom")))
    {
      return true;
    }
    return false;
  }

  private JToolBar buildToolbar(final HashMap actions)
  {
    JToolBar toolBar = new JToolBar();

    addActionsToToolBar(toolBar, (ActionPlugin[]) actions.get(REPORT_CATEGORY));
    addActionsToToolBar(toolBar, (ActionPlugin[]) actions.get(EXPORT_CATEGORY));
    addActionsToToolBar(toolBar, (ActionPlugin[]) actions.get(GOTO_CATEGORY));

    if (isToolbarZoomEnabled())
    {
      toolBar.add(createButton(new ZoomOutAction(this)));
      toolBar.add(createButton(new ZoomInAction(this)));
      toolBar.addSeparator();

      final JPanel zoomPane = new JPanel();
      zoomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
      zoomPane.add(createZoomSelector());
      toolBar.add(zoomPane);

      toolBar.addSeparator();
    }

    addActionsToToolBar(toolBar, (ActionPlugin[]) actions.get(HELP_CATEGORY));
    return toolBar;
  }

  private void closeToolbar()
  {
    if (toolBar.getParent() != toolbarHolder)
    {
      // ha!, we detected that the toolbar is floating ...
      // Log.debug (currentToolbar.getParent());
      final Window w = SwingUtilities.windowForComponent(toolBar);
      if (w != null)
      {
        w.setVisible(false);
        w.dispose();
      }
    }
    toolBar.setVisible(false);
  }

  protected JComboBox createZoomSelector()
  {
    final DefaultComboBoxModel model = new DefaultComboBoxModel();
    final NumberFormat numberFormat =
        NumberFormat.getPercentInstance(getLocale());
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      model.addElement(numberFormat.format(ZOOM_FACTORS[i] / 100.0));
    }

    final JComboBox zoomSelect = new JComboBox(model);
    zoomSelect.setSelectedIndex(DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(new ZoomSelectAction(zoomSelect));
    zoomSelect.setAlignmentX(Component.RIGHT_ALIGNMENT);
    return zoomSelect;
  }

  private void addActionsToToolBar(final JToolBar toolBar,
                                   final ActionPlugin[] reportActions)
  {
    if (reportActions == null)
    {
      return;
    }

    boolean separatorPending = false;
    int count = 0;

    for (int i = 0; i < reportActions.length; i++)
    {
      ActionPlugin actionPlugin = reportActions[i];
      if (actionPlugin.isAddToToolbar() == false)
      {
        continue;
      }

      if (count > 0 && separatorPending)
      {
        toolBar.addSeparator();
        separatorPending = false;
      }

      if (actionPlugin instanceof ExportActionPlugin)
      {
        final ExportActionPlugin exportPlugin = (ExportActionPlugin) actionPlugin;
        final ExportAction action = new ExportAction(exportPlugin, this);
        toolBar.add(createButton(action));
        count += 1;
      }
      else if (actionPlugin instanceof ControlActionPlugin)
      {
        final ControlActionPlugin controlPlugin = (ControlActionPlugin) actionPlugin;
        final ControlAction action = new ControlAction(controlPlugin, this);
        toolBar.add(createButton(action));
        count += 1;
      }

      if (actionPlugin.isSeparated())
      {
        separatorPending = true;
      }
    }
  }

  private void buildMenu(final HashMap actions)
  {
    final ArrayList menus = new ArrayList();

    final ActionPlugin[] reportActions = (ActionPlugin[]) actions.get(REPORT_CATEGORY);
    if (reportActions != null && reportActions.length != 0)
    {
      JMenu reportMenu = new JMenu("Report");
      int count = PreviewPaneUtilities.buildMenu(reportMenu, reportActions, this);
      if (count > 0)
      {
        menus.add(reportMenu);
      }
    }

    final ActionPlugin[] exportActions = (ActionPlugin[]) actions.get(EXPORT_CATEGORY);
    if (exportActions != null && exportActions.length != 0)
    {
      JMenu exportMenu = new JMenu("Export");
      int count = PreviewPaneUtilities.buildMenu(exportMenu, exportActions, this);
      if (count > 0)
      {
        menus.add(exportMenu);
      }
    }

    final ActionPlugin[] gotoActions = (ActionPlugin[]) actions.get(GOTO_CATEGORY);
    if (gotoActions != null && gotoActions.length != 0)
    {
      JMenu gotoMenu = new JMenu("Go To");
      int count = PreviewPaneUtilities.buildMenu(gotoMenu, gotoActions, this);
      if (count > 0)
      {
        menus.add(gotoMenu);
      }
    }

    menus.add(createViewMenu());

    ReportController controller = getReportController();
    if (controller != null)
    {
      controller.initialize(this);
      final JMenu[] controlerMenus = controller.getMenus();
      for (int i = 0; i < controlerMenus.length; i++)
      {
        menus.add(controlerMenus[i]);
      }
    }

    final ActionPlugin[] helpActions = (ActionPlugin[]) actions.get(HELP_CATEGORY);
    if (helpActions != null && helpActions.length != 0)
    {
      JMenu helpMenu = new JMenu("Help");
      int count = PreviewPaneUtilities.buildMenu(helpMenu, helpActions, this);
      if (count > 0)
      {
        menus.add(helpMenu);
      }
    }

    setMenu((JMenu[]) menus.toArray(new JMenu[menus.size()]));
  }

  private JMenu createViewMenu()
  {
    JMenu zoom = new JMenu("Zoom");
    zoom.add(new ActionMenuItem(new ZoomOutAction(this)));
    zoom.add(new ActionMenuItem(new ZoomInAction(this)));
    zoom.addSeparator();

    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      double factor = ZOOM_FACTORS[i];
      zoom.add(new ActionMenuItem(new ZoomAction(factor, this)));
    }

    zoom.addSeparator();
    zoom.add(new ActionMenuItem(new ZoomCustomAction(this)));

    JMenu menu = new JMenu("View");
    menu.add(zoom);
    menu.addSeparator();
    menu.add(new ActionMenuItem("Paginated"));
    menu.add(new ActionMenuItem("Flow"));
    return menu;
  }

  protected void initializeWithoutJob()
  {
    final Configuration globalConfig =
        JFreeReportBoot.getInstance().getGlobalConfig();
    setIconTheme(PreviewPaneUtilities.createIconTheme(globalConfig));

    HashMap actions = loadActions();
    buildMenu(actions);
    if (toolBar != null)
    {
      toolbarHolder.remove(toolBar);
    }
    toolBar = buildToolbar(actions);
    if (toolBar != null)
    {
      toolbarHolder.add(toolBar, BorderLayout.NORTH);
    }

  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(final String title)
  {
    String oldTitle = this.title;
    this.title = title;
    firePropertyChange(TITLE_PROPERTY, oldTitle, title);
  }

  public double[] getZoomFactors()
  {
    return (double[]) ZOOM_FACTORS.clone();
  }

  public boolean isPaginating()
  {
    return paginating;
  }

  public void setPaginating(final boolean paginating)
  {
    boolean oldPaginating = this.paginating;
    this.paginating = paginating;
    firePropertyChange(PAGINATING_PROPERTY, oldPaginating, paginating);
  }

  private synchronized void startPagination()
  {
    if (paginationWorker != null)
    {
      // make sure that old pagination handler does not run longer than
      // necessary..
      paginationWorker.finish();
    }
    if (printReportProcessor != null)
    {
      printReportProcessor.close();
      printReportProcessor = null;
    }

    final ReportJob reportJob = getReportJob();
    printReportProcessor = new PrintReportProcessor(reportJob.derive());

    paginationWorker = new Worker();
    paginationWorker.setWorkload
        (new RepaginationRunnable(printReportProcessor));
  }

  public Drawable getNoReportDrawable()
  {
    return noReportDrawable;
  }

  public void setNoReportDrawable(final Drawable noReportDrawable)
  {
    this.noReportDrawable = noReportDrawable;
  }

  public Drawable getPaginatingDrawable()
  {
    return paginatingDrawable;
  }

  public void setPaginatingDrawable(final Drawable paginatingDrawable)
  {
    this.paginatingDrawable = paginatingDrawable;
  }

  protected void updateVisiblePage(int pageNumber)
  {
    //
    if (printReportProcessor == null)
    {
      throw new IllegalStateException();
    }

    // todo: This can be very expensive - so we better move this off the event-dispatcher
    final int pageIndex = getPageNumber() - 1;
    if (pageIndex < 0 || pageIndex >= printReportProcessor.getNumberOfPages())
    {
      drawablePanel.setDrawable(null);
      pageDrawable.setBackend(null);
    }
    else
    {
      final PageDrawable drawable = printReportProcessor.getPageDrawable(pageIndex);
      Log.debug("Drawable: " + drawable);
      this.pageDrawable.setBackend(drawable);
      this.drawablePanel.setDrawable(pageDrawable);
    }
  }
}
