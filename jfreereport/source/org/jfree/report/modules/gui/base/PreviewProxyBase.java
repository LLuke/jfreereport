/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * PreviewProxyBase.java
 * ---------------------
 * (C)opyright 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PreviewProxyBase.java,v 1.35 2004/03/16 15:09:23 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.layout.CenterLayout;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionMenuItem;
import org.jfree.report.modules.gui.base.components.DowngradeActionMap;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.gui.base.components.FloatingButtonEnabler;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.Worker;
import org.jfree.report.util.WorkerPool;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ParserUtil;

/**
 * A preview proxy. This class is the backend for all preview components.
 *
 * @author Thomas Morgner.
 */
public class PreviewProxyBase extends JComponent
{
  /** The property name for the largeIconsEnabled property for the toolbar. */
  public static final String LARGE_ICONS_PROPERTY = "largeIconsEnabled";
  /** A key to query an action from the action map. */
  public static final String GOTO_ACTION_KEY = "GotoAction";
  /** A key to query an action from the action map. */
  public static final String ABOUT_ACTION_KEY = "AboutAction";
  /** A key to query an action from the action map. */
  public static final String CLOSE_ACTION_KEY = "CloseAction";
  /** A key to query an action from the action map. */
  public static final String FIRSTPAGE_ACTION_KEY = "FirstPageAction";
  /** A key to query an action from the action map. */
  public static final String LASTPAGE_ACTION_KEY = "LastPageAction";
  /** A key to query an action from the action map. */
  public static final String NEXT_PAGE_ACTION_KEY = "NextPageAction";
  /** A key to query an action from the action map. */
  public static final String PREV_PAGE_ACTION_KEY = "PreviousPageAction";
  /** A key to query an action from the action map. */
  public static final String ZOOM_IN_ACTION_KEY = "ZoomInAction";
  /** A key to query an action from the action map. */
  public static final String ZOOM_OUT_ACTION_KEY = "ZoomOutAction";

  /** The default width of the report pane. */
  public static final int DEFAULT_REPORT_PANE_WIDTH = 640;

  /** The default height of the report pane. */
  public static final int DEFAULT_REPORT_PANE_HEIGHT = 480;

  /** The preferred width key. */
  public static final String PREVIEW_PREFERRED_WIDTH
      = "org.jfree.report.modules.gui.base.PreferredWidth";

  /** The preferred height key. */
  public static final String PREVIEW_PREFERRED_HEIGHT
      = "org.jfree.report.modules.gui.base.PreferredHeight";

  /** The maximum width key. */
  public static final String PREVIEW_MAXIMUM_WIDTH
      = "org.jfree.report.modules.gui.base.MaximumWidth";

  /** The maximum height key. */
  public static final String PREVIEW_MAXIMUM_HEIGHT
      = "org.jfree.report.modules.gui.base.MaximumHeight";

  /** A configuration key to define whether the toolbar is floatable. */
  public static final String PROGRESS_BAR_ENABLE_PROPERTY
      = "org.jfree.report.modules.gui.base.ProgressBarEnabled";

  /** A configuration key to define whether the toolbar is floatable. */
  public static final String PROGRESS_DIALOG_ENABLE_PROPERTY
      = "org.jfree.report.modules.gui.base.ProgressDialogEnabled";

  /** A configuration key to define whether large toolbar icons are enabled. */
  public static final String LARGE_ICONS_ENABLED_PROPERTY
      = "org.jfree.report.modules.gui.base.LargeIcons";

  /** A configuration key to define whether the toolbar is floatable. */
  public static final String TOOLBAR_FLOATABLE_PROPERTY
      = "org.jfree.report.modules.gui.base.ToolbarFloatable";

  /** A configuration key to define whether the toolbar is created. */
  public static final String CREATE_TOOLBAR_PROPERTY
      = "org.jfree.report.modules.gui.base.ToolbarAvailable";

  /** A configuration key to define whether the toolbar is floatable. */
  public static final String CREATE_MENUBAR_PROPERTY
      = "org.jfree.report.modules.gui.base.MenuBarAvailable";

  /** The property name for the toolbarFloatable property. */
  public static final String TOOLBAR_FLOATABLE_PROPERTYNAME
      = "toolbarFloatable";

  /**
   * A configuration key to define whether and how the about action
   * should be included. Can be set to "disable", "menu", "toolbar", "enable"
   */
  public static final String ACTION_ABOUT_PROPERTY
      = "org.jfree.report.modules.gui.base.About";

  /**
   * A configuration key to define whether and how the zoom actions
   * should be included. Can be set to "disable", "menu", "toolbar", "enable"
   */
  public static final String ACTION_ZOOM_PROPERTY
      = "org.jfree.report.modules.gui.base.Zoom";

  /**
   * A configuration key to define whether and how the navigation actions
   * should be included. Can be set to "disable", "menu", "toolbar", "enable"
   */
  public static final String ACTION_NAVIGATION_PROPERTY
      = "org.jfree.report.modules.gui.base.Navigate";

  /**
   * A configuration key to define whether and how the close action
   * should be included. Can be set to "disable", "menu", "toolbar", "enable"
   */
  private static final String ACTION_CLOSE_PROPERTY
      = "org.jfree.report.modules.gui.base.Close";

  /**
   * An property change handler for the toolbar. Handles the toolbarFloatable
   * and largeIconsEnabled properties.
   */
  private class ToolbarPropertyChangeListener implements PropertyChangeListener
  {
    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *        and the property that has changed.
     */
    public void propertyChange(final PropertyChangeEvent evt)
    {
      if (evt.getSource() == getToolbar())
      {
        if ("floatable".equals(evt.getPropertyName()))
        {
          setToolbarFloatable(getToolbar().isFloatable());
        }
      }
      else if (evt.getSource() == PreviewProxyBase.this)
      {
        if (TOOLBAR_FLOATABLE_PROPERTYNAME.equals(evt.getPropertyName()))
        {
          getToolbar().setFloatable(isToolbarFloatable());
        }
        else if (LARGE_ICONS_ENABLED_PROPERTY.equals(evt.getPropertyName()))
        {
          reinitialize();
        }
      }
    }
  }

  /**
   * A property change listener.
   */
  private class ReportPanePropertyChangeListener implements PropertyChangeListener
  {
    /**
     * Creates a new listener.
     */
    public ReportPanePropertyChangeListener()
    {
    }

    /**
     * Listen to the assigned reportPane.
     *
     * @param event  the property change event.
     */
    public void propertyChange(final PropertyChangeEvent event)
    {
      final String property = event.getPropertyName();
      final ReportPane reportPane = getReportPane();

      if (property.equals(ReportPane.PAGINATED_PROPERTY))
      {
        if (reportPane.isPaginated())
        {
          // is paginated ...
          final Object[] params = new Object[]{
            new Integer(reportPane.getPageNumber()),
            new Integer(reportPane.getNumberOfPages())
          };
          setStatusText(
              MessageFormat.format(
                  getResources().getString("statusline.pages"),
                  params
              )
          );
          validateButtons();
        }
        else
        {
          // is not paginated ... ignore event ...
        }
      }
      else if (property.equals(ReportPane.PAGENUMBER_PROPERTY)
          || property.equals(ReportPane.NUMBER_OF_PAGES_PROPERTY))
      {

        final Object[] params = new Object[]{
          new Integer(reportPane.getPageNumber()),
          new Integer(reportPane.getNumberOfPages())
        };
        setStatusText(
            MessageFormat.format(
                getResources().getString("statusline.pages"),
                params
            )
        );
        validateButtons();
      }
      else if (property.equals(ReportPane.ERROR_PROPERTY))
      {
        if (reportPane.hasError())
        {
          final Exception ex = reportPane.getError();

          ex.printStackTrace();
          setStatusText(
              MessageFormat.format(
                  getResources().getString("statusline.error"),
                  new Object[]{ex.getMessage()}
              )
          );
        }
        validateButtons();
      }
      else if (property.equals(ReportPane.ZOOMFACTOR_PROPERTY))
      {
        validateButtons();
        validate();
      }
    }
  }

  /**
   * Default 'first page' action.
   */
  private class DefaultFirstPageAction extends FirstPageAction
  {
    /**
     * Creates a 'first page' action.
     */
    public DefaultFirstPageAction()
    {
      super(getResources());
    }

    /**
     * Jump to the first page of the report.
     *
     * @param e The action event.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
      firstPage();
    }
  }

  /**
   * Default 'next page' action for the frame.
   */
  private class DefaultNextPageAction extends NextPageAction
  {

    /**
     * Creates a 'next page' action.
     */
    public DefaultNextPageAction()
    {
      super(getResources());
    }

    /**
     * show the next page of the report.
     *
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      increasePageNumber();
    }
  }

  /**
   * Default 'previous page' action for the frame.
   */
  private class DefaultPreviousPageAction extends PreviousPageAction
  {
    /**
     * Creates a 'previous page' action.
     */
    public DefaultPreviousPageAction()
    {
      super(getResources());
    }

    /**
     * show the previous page of the report.
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      decreasePageNumber();
    }
  }

  /**
   * Default 'last page' action for the frame.
   */
  private class DefaultLastPageAction extends LastPageAction
  {
    /**
     * Creates a 'last page' action.
     */
    public DefaultLastPageAction()
    {
      super(getResources());
    }

    /**
     * jump to the last page of the report.
     *
     * @param e The action event.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
      lastPage();
    }
  }

  /**
   * Default 'zoom in' action for the frame.
   */
  private class DefaultZoomInAction extends ZoomInAction
  {

    /**
     * Creates a 'zoom in' action.
     */
    public DefaultZoomInAction()
    {
      super(getResources());
    }

    /**
     * increase zoom.
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      increaseZoom();
    }
  }

  /**
   * Default 'zoom out' action for the frame.
   */
  private class DefaultZoomOutAction extends ZoomOutAction
  {
    /**
     * Creates a 'zoom out' action.
     */
    public DefaultZoomOutAction()
    {
      super(getResources());
    }

    /**
     * decrease zoom.
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      decreaseZoom();
    }
  }

  /**
   * Default 'about' action (does nothing).
   */
  private class DefaultAboutAction extends AboutAction
  {
    /**
     * Creates an 'about' action.
     */
    public DefaultAboutAction()
    {
      super(getResources());
    }

    /**
     * Does nothing (should show an 'about' dialog).
     *
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
    }
  }

  /**
   * Default 'goto' action for the frame.
   */
  private class DefaultGotoAction extends GotoPageAction
  {
    /**
     * Creates a 'goto' action.
     */
    public DefaultGotoAction()
    {
      super(getResources());
    }

    /**
     * Jump to a page.
     *
     * @param e The action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      final String result = JOptionPane.showInputDialog(PreviewProxyBase.this,
          getResources().getString("dialog.gotopage.title"),
          getResources().getString("dialog.gotopage.message"),
          JOptionPane.OK_CANCEL_OPTION);
      if (result == null)
      {
        return;
      }
      try
      {
        final int page = Integer.parseInt(result);

        final ReportPane reportPane = getReportPane();
        if (page > 0 && page <= reportPane.getNumberOfPages())
        {
          reportPane.setPageNumber(page);
        }
      }
      catch (Exception ex)
      {
        Log.info("DefaultGotoAction: swallowed an exception");
      }
    }
  }

  /**
   * A zoom select action.
   */
  private class ZoomSelectAction extends AbstractAction
  {
    /**
     * Creates a new action.
     */
    public ZoomSelectAction()
    {
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setZoomFactor(getZoomSelect().getSelectedIndex());
    }
  }

  /**
   * A zoom set action.
   */
  protected class ZoomSetAction extends AbstractActionDowngrade
  {
    /** The zoom factor index. */
    private final int zoomFactor;

    /**
     * Creates a new action.
     *
     * @param factorIndex  the zoom factor index.
     */
    public ZoomSetAction(final int factorIndex)
    {
      zoomFactor = factorIndex;
      this.putValue(Action.NAME, String.valueOf((int) (ZOOM_FACTORS[factorIndex] * 100)) + " %");
      this.putValue(SMALL_ICON, ImageUtils.createTransparentIcon(16, 16));
      this.putValue("ICON24", ImageUtils.createTransparentIcon(24, 24));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      setZoomFactor(zoomFactor);
    }
  }

  /**
   * A runnable for the Swing EventDispatcher thread. Shows or hides the
   * report pane.
   */
  private class ShowHidePaginationRunnable implements Runnable
  {
    private boolean visible;
    private boolean cancel;
    private ReportPane reportPane;
    private ReportProgressDialog progressDialog;
    private ReportProgressBar progressBar;

    public ShowHidePaginationRunnable
        (final ReportPane reportPane, final ReportProgressDialog progressDialog,
         final ReportProgressBar progressBar, final boolean visible)
    {
      this.reportPane = reportPane;
      this.progressDialog = progressDialog;
      this.progressBar = progressBar;
      this.visible = visible;
      this.cancel = false;
    }

    public void cancel()
    {
      cancel = true;
    }

    public void run()
    {
      if (visible == PREPARE_REPAGINATION)
      {
        // if task is not already completed ...
        if (cancel == false)
        {
          if (isProgressDialogEnabled())
          {
            reportPane.addRepaginationListener(progressDialog);
            RefineryUtilities.positionFrameRandomly(progressDialog);
            // show if report pane not visible
            progressDialog.setVisible(true);
          }
          if (isProgressBarEnabled())
          {
            reportPane.addRepaginationListener(progressBar);
            // show if report pane not visible
            progressBar.setVisible(true);
          }
        }
        reportPane.setHandleInterruptedState(true);
        reportPane.setVisible(false);
      }
      else
      {
        progressDialog.setVisible(false);
        progressBar.setVisible(false);
        reportPane.removeRepaginationListener(progressDialog);
        reportPane.removeRepaginationListener(progressBar);
        reportPane.setVisible(true);
        reportPane.setHandleInterruptedState(false);
        setLockInterface(false);
      }
    }
  }

  private class RepaginationRunnable implements Runnable
  {
    public RepaginationRunnable()
    {
    }

    public void run()
    {
      final ReportPane reportPane = getReportPane();
      final ReportProgressDialog progressDialog = getProgressDialog();
      final ReportProgressBar progressBar = getProgressBar();

      try
      {
        final long startTime = System.currentTimeMillis();

        // it will cause trouble if not called from within the
        // event handler thread
        ShowHidePaginationRunnable hidePane =
            new ShowHidePaginationRunnable(reportPane, progressDialog, progressBar, PREPARE_REPAGINATION);
        if (SwingUtilities.isEventDispatchThread())
        {
          hidePane.run();
        }
        else
        {
          SwingUtilities.invokeLater(hidePane);
        }

        reportPane.repaginate();

        ShowHidePaginationRunnable showPane =
            new ShowHidePaginationRunnable(reportPane, progressDialog, progressBar, POST_REPAGINATION);
        if (SwingUtilities.isEventDispatchThread())
        {
          showPane.run();
        }
        else
        {
          hidePane.cancel();
          SwingUtilities.invokeLater(showPane);
        }

        // it will cause trouble if not called from within the
        // event handler thread
        Log.debug("Pagination done: " +
            ((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
      }
      catch (ReportInterruptedException re)
      {
        progressDialog.setVisible(false);
        reportPane.removeRepaginationListener(progressDialog);
        Log.info("Repagination aborted [ReportInterruptedException]");
      }
      catch (Exception e)
      {
        progressDialog.setVisible(false);
        reportPane.removeRepaginationListener(progressDialog);
        Log.warn("Failed to repaginate", e);
        reportPane.setError(e);
      }
    }
  }

  /** A private constant used to mark the start of a pagination process. */
  private static final boolean PREPARE_REPAGINATION = false;
  /** A private constant used to mark the ending of a pagination process. */
  private static final boolean POST_REPAGINATION = true;

  public static final String CONF_TOOLBAR_ENABLED = "toolbar";
  public static final String CONF_ALL_ENABLED = "enable";
  public static final String CONF_ALL_DISABLED = "disable";
  public static final String CONF_MENUBAR_ENABLED = "menubar";
  public static final String REPORT_PANE_PROPERTY = "reportPane";

  /** The base class for localised resources. */
  public static final String RESOURCES_BASE_NAME =
      "org.jfree.report.modules.gui.base.resources.jfreereport-resources";

  /** The worker thread which is used to perform the repagination. */
  private Worker repaginationWorker;

  /** The worker thread which is used to perform the repagination. */
  private WorkerPool exportWorkerPool;
  /** An action map storing all basic actions. */
  private final DowngradeActionMap baseActionMap;
  /** An action map storing all navigation related actions. */
  private final DowngradeActionMap navigationActionMap;
  /** An action map storing all zoom related actions. */
  private final DowngradeActionMap zoomActionMap;
  /** An action map storing all export related actions. */
  private final DowngradeActionMap exportActionMap;
  /** An action map storing all custom toolbar actions. */
  private final DowngradeActionMap customActionMap;

  /** The available zoom factors. */
  protected static final float[]
      ZOOM_FACTORS = {0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 3.0f, 4.0f};

  /** The default zoom index (corresponds to a zoomFactor of 1.0. */
  private static final int DEFAULT_ZOOM_INDEX = 3;

  /** The combobox enables a direct selection of the desired zoomFactor. */
  private JComboBox zoomSelect;

  /** The current zoom index (indexes into the zoomFactors array). */
  private int zoomIndex;

  /** The pane that displays the report within the frame. */
  private ReportPane reportPane;

  /** Locale-specific resources. */
  private ResourceBundle resources;

  /** Defines whether to use 24x24 icons or 16x16 icons. */
  private boolean largeIconsEnabled;

  /** Label for status. */
  private JLabel statusHolder;

  /** Toolbar. */
  private JToolBar toolbar;
  /** Whether the toolbar will be floatable. */
  private boolean toolbarFloatable;

  /** A preview proxy. */
  private final PreviewProxy proxy;

  /** A list of all export plugins known to this preview proxy base. */
  private ArrayList exportPlugIns;

  /** A collection of actions, keyed by the export plugin. */
  private HashMap pluginActions;

  /** The progress monitor dialog used to visualize the pagination progress. */
  private final ReportProgressDialog progressDialog;
  /** The progress monitor component used to visualize the pagination progress. */
  private ReportProgressBar progressBar;
  /** A flag to define whether the interface should be the locked state. */
  private boolean lockInterface;
  /** A flag that defines, whether the preview component is closed. */
  private boolean closed;

  private JPanel reportPaneHolder;
  private ReportPanePropertyChangeListener reportPanePropertyChangeListener;
  private boolean progressDialogEnabled;
  private boolean progressBarEnabled;

  /**
   * Creates a preview proxy.
   *
   * @param proxy  the proxy.
   */
  public PreviewProxyBase(final PreviewProxy proxy)
  {
    if (proxy == null)
    {
      throw new NullPointerException("Proxy must not be null.");
    }
    this.baseActionMap = new DowngradeActionMap();
    this.customActionMap = new DowngradeActionMap();

    this.exportActionMap = new DowngradeActionMap();
    this.exportActionMap.setParent(baseActionMap);

    this.navigationActionMap = new DowngradeActionMap();
    this.navigationActionMap.setParent(baseActionMap);

    this.zoomActionMap = new DowngradeActionMap();
    this.zoomActionMap.setParent(baseActionMap);

    this.proxy = proxy;
    this.progressDialog = new ReportProgressDialog();
    this.progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    this.reportPanePropertyChangeListener = new ReportPanePropertyChangeListener();

    progressBar = new ReportProgressBar();

    setLayout(new BorderLayout());
    setDoubleBuffered(false);

    // handle a JDK bug: windows are not garbage collected if dispose is not called manually.
    // DisposedState is undone when show() or pack() is called, so this does no harm.
    proxy.addComponentListener(new ComponentAdapter()
    {
      public void componentHidden(final ComponentEvent e)
      {
        final Component c = e.getComponent();
        if (c instanceof Window)
        {
          final Window w = (Window) c;
          w.dispose();
          dispose();
        }
      }
    });

    this.exportWorkerPool = new WorkerPool
        (10, "preview-dialog-export-worker");

    createDefaultActions();

    reportPaneHolder = new JPanel(new CenterLayout());
    reportPaneHolder.setDoubleBuffered(false);
    reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    final JScrollPane s1 = new JScrollPane(reportPaneHolder);
    s1.setDoubleBuffered(false);
    s1.getVerticalScrollBar().setUnitIncrement(20);

    final JPanel scrollPaneHolder = new JPanel();
    scrollPaneHolder.setLayout(new BorderLayout());
    scrollPaneHolder.add(s1, BorderLayout.CENTER);
    scrollPaneHolder.setDoubleBuffered(false);

    scrollPaneHolder.add(createStatusBar(), BorderLayout.SOUTH);
    add(scrollPaneHolder);

    zoomSelect = createZoomSelector();
  }

  /**
   * Builds the export plugins and fills the plug in collections.
   *
   * @param report the report for which to build the plugins
   */
  private void buildExportPlugins(final JFreeReport report)
  {
    final ExportPluginFactory factory = ExportPluginFactory.getInstance();
    exportPlugIns = factory.createExportPlugIns
        (proxy, report.getReportConfiguration(), exportWorkerPool);
    pluginActions = new HashMap(exportPlugIns.size());
    final Iterator it = exportPlugIns.iterator();
    while (it.hasNext())
    {
      final ExportPlugin ep = (ExportPlugin) it.next();
      final ExportAction ea = new ExportAction(ep);
      ea.setReport(report);
      pluginActions.put(ep, ea);
    }
  }

  /**
   * Returns the list of export plugins available to the report.
   *
   * @return the list of export plugins.
   */
  protected List getExportPlugins()
  {
    return exportPlugIns;
  }

  /**
   * Returns the map of export plugins available to the report.
   * Using a plugin as key you can query the assigned action for that
   * plugin.
   *
   * @return the list of export plugins.
   */
  protected HashMap getExportActions()
  {
    return pluginActions;
  }

  /**
   * Returns the export worker used for exporting.
   *
   * @return the worker.
   */
  protected WorkerPool getExportWorkerPool()
  {
    return exportWorkerPool;
  }

  /**
   * Returns the pagination worker used when paginating the report.
   *
   * @return the worker.
   */
  protected Worker getRepaginationWorker()
  {
    if (repaginationWorker == null)
    {
      repaginationWorker = new Worker();
      repaginationWorker.setPriority(Thread.MIN_PRIORITY);
      repaginationWorker.setName("Repagination-Worker");
    }
    return repaginationWorker;
  }

  private void closeToolbar()
  {
    if (toolbar.getParent() != this)
    {
      // ha!, we detected that the toolbar is floating ...
      // Log.debug (currentToolbar.getParent());
      final Window w = SwingUtilities.windowForComponent(toolbar);
      if (w != null)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            w.setVisible(false);
            w.dispose();
          }
        });
      }
    }
    toolbar.setVisible(false);
  }


  /**
   * Initialises the preview dialog.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   * @deprecated use setReport(..) instead.
   */
  public void init(final JFreeReport report) throws ReportProcessingException
  {
    setReport(report);
  }

  /**
   * Returns the boolean value for a given report configuration property.
   *
   * @param property the property name
   * @param defaultValue the default value if not set.
   * @return
   */
  private boolean isPropertySet(String property, boolean defaultValue)
  {
    String value =
        ReportConfiguration.getGlobalConfig().getConfigProperty(property);
    if (value == null)
    {
      return defaultValue;
    }
    return (value.equals(String.valueOf(defaultValue)));
  }

  /**
   * Call this method, whenever actions have changed. The menu and toolbar
   * will be rebuild.
   */
  protected void reinitialize()
  {
    initializeToolBar();
    // set up the menu
    if (isPropertySet(CREATE_MENUBAR_PROPERTY, true))
    {
      proxy.setJMenuBar(createMenuBar());
    }
  }

  /**
   * Creates an empty toolbar. The toolbar will be inizialized later by
   * calling inizializeToolbar().
   *
   * @return the created toolbar.
   */
  protected JToolBar createToolBar()
  {
    final JToolBar toolbar = new JToolBar();
    return toolbar;
  }

  protected boolean isMenuActionEnabled(String property)
  {
    String value = ReportConfiguration.getGlobalConfig().getConfigProperty(property);
    if (value == null)
    {
      return true;
    }
    if (value.equals(CONF_MENUBAR_ENABLED))
    {
      return true;
    }
    if (value.equals(CONF_ALL_ENABLED))
    {
      return true;
    }
    return false;
  }

  protected boolean isToolbarActionEnabled(String property)
  {
    String value = ReportConfiguration.getGlobalConfig().getConfigProperty(property);
    if (value == null)
    {
      return true;
    }
    if (value.equals(CONF_TOOLBAR_ENABLED))
    {
      return true;
    }
    if (value.equals(CONF_ALL_ENABLED))
    {
      return true;
    }
    return false;
  }

  /**
   * Read the defined dimensions from the report's configuration and set them to
   * the Dialog. If a maximum size is defined, add a WindowSizeLimiter to check the
   * maximum size
   *
   * @param report the report of this dialog.
   */
  private void applyDefinedDimension(final JFreeReport report)
  {
    String width = report.getReportConfiguration().getConfigProperty(
        PREVIEW_PREFERRED_WIDTH);
    String height = report.getReportConfiguration().getConfigProperty(
        PREVIEW_PREFERRED_HEIGHT);

    // only apply if both values are set.
    if (width != null && height != null)
    {
      try
      {
        final Dimension pref = createCorrectedDimensions
            (Integer.parseInt(width), Integer.parseInt(height));
        setPreferredSize(pref);
      }
      catch (Exception nfe)
      {
        Log.warn("Preferred viewport size is defined, but the specified values are invalid.");
      }
    }

    width = report.getReportConfiguration().getConfigProperty(
        PREVIEW_MAXIMUM_WIDTH);
    height = report.getReportConfiguration().getConfigProperty(
        PREVIEW_MAXIMUM_HEIGHT);

    // only apply if at least one value is set.
    if (width != null || height != null)
    {
      try
      {
        final int iWidth = (width == null)
            ? Short.MAX_VALUE : (int) ParserUtil.parseRelativeFloat(width, "");
        final int iHeight = (height == null)
            ? Short.MAX_VALUE : (int) ParserUtil.parseRelativeFloat(height, "");
        final Dimension pref = createCorrectedDimensions(iWidth, iHeight);
        setMaximumSize(pref);
        addComponentListener(new org.jfree.report.modules.gui.base.components.WindowSizeLimiter());
      }
      catch (Exception nfe)
      {
        Log.warn("Maximum viewport size is defined, but the specified values are invalid.");
      }
    }
  }

  /**
   * Correct the given width and height. If the values are negative, the height and
   * width is considered a proportional value where -100 corresponds to 100%.
   * The proportional attributes are given is relation to the screen width and height.
   *
   * @param w the to be corrected width
   * @param h the height that should be corrected
   * @return the dimension of width and height, where all relative values are normalized.
   */
  private Dimension createCorrectedDimensions(int w, int h)
  {
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (w < 0)
    {
      w = (w * screenSize.width / -100);
    }
    if (h < 0)
    {
      h = (h * screenSize.height / -100);
    }
    return new Dimension(w, h);
  }

  /**
   * Creates the ReportPane for the report.
   *
   * @param report the report for this pane.
   *
   * @return the report pane.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  protected ReportPane createReportPane(final JFreeReport report) throws ReportProcessingException
  {
    final ReportPane reportPane = new ReportPane(report);
    return reportPane;
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  protected ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(RESOURCES_BASE_NAME);
    }
    return resources;
  }

  /**
   * Returns the current zoom factor.
   *
   * @return The current zoom factor.
   */
  public float getZoomFactor()
  {
    return ZOOM_FACTORS[zoomIndex];
  }

  /**
   * Returns the report pane, which implements the Pageable interface.
   *
   * @return the report pane.
   */
  public Pageable getPageable()
  {
    return reportPane;
  }

  /**
   * Returns the report pane, which implements the Printable interface.
   *
   * @return the report pane.
   */
  public Printable getPrintable()
  {
    return reportPane;
  }

  /**
   * Returns the report pane used to preview the report.
   *
   * @return the report pane.
   */
  public ReportPane getReportPane()
  {
    return reportPane;
  }

  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
   *
   * @param localisationBase  the resource key prefix.
   * @param e  the exception.
   */
  protected void showExceptionDialog(final String localisationBase, final Exception e)
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
   * Method lastPage moves to the last page.
   */
  protected void lastPage()
  {
    reportPane.setPageNumber(reportPane.getNumberOfPages());
  }

  /**
   * Increases the page number.
   */
  protected void increasePageNumber()
  {
    final int pn = reportPane.getPageNumber();
    final int mp = reportPane.getNumberOfPages();

    if (pn < mp)
    {
      reportPane.setPageNumber(pn + 1);
    }
  }

  /**
   * Activates the display of the first page, if not already on the first page.
   */
  protected void firstPage()
  {
    if (reportPane.getPageNumber() != 1)
    {
      reportPane.setPageNumber(1);
    }
  }

  /**
   * Decreases the page number.
   */
  protected void decreasePageNumber()
  {
    final int pn = reportPane.getPageNumber();
    if (pn > 1)
    {
      reportPane.setPageNumber(pn - 1);
    }
  }

  /**
   * Increases the zoom factor for the report pane (unless it is already at maximum zoom).
   */
  protected void increaseZoom()
  {
    if (zoomIndex < ZOOM_FACTORS.length - 1)
    {
      zoomIndex++;
    }

    zoomSelect.setSelectedIndex(zoomIndex);

    reportPane.setZoomFactor(getZoomFactor());
    //validateButtons();
  }

  /**
   * Decreases the zoom factor for the report pane (unless it is already at the minimum zoom).
   * */
  protected void decreaseZoom()
  {
    if (zoomIndex > 0)
    {
      zoomIndex--;
    }

    zoomSelect.setSelectedIndex(zoomIndex);

    reportPane.setZoomFactor(getZoomFactor());
    //validateButtons();
  }

  /**
   * Sets the zoomfactor of the report pane.
   *
   * @param index  the index into the array of standard zoom factors.
   */
  public void setZoomFactor(final int index)
  {
    zoomIndex = index;
    reportPane.setZoomFactor(getZoomFactor());
    zoomSelect.setSelectedIndex(zoomIndex);
  }

  /**
   * Checks whether this action has a keystroke assigned. If it has one, the keystroke
   * is assigned to the frame.
   *
   * @param action  the action.
   */
  protected void registerAction(final Action action)
  {
    final KeyStroke key = (KeyStroke) action.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (key != null)
    {
      registerKeyboardAction(action, key, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
  }

  /**
   * Creates all actions by calling the createXXXAction functions and assigning them to
   * the local variables.
   *
   */
  private void createDefaultActions()
  {
    navigationActionMap.put(GOTO_ACTION_KEY, createDefaultGotoAction());
    baseActionMap.put(ABOUT_ACTION_KEY, createDefaultAboutAction());
    navigationActionMap.put(FIRSTPAGE_ACTION_KEY, createDefaultFirstPageAction());
    navigationActionMap.put(LASTPAGE_ACTION_KEY, createDefaultLastPageAction());
    navigationActionMap.put(NEXT_PAGE_ACTION_KEY, createDefaultNextPageAction());
    navigationActionMap.put(PREV_PAGE_ACTION_KEY, createDefaultPreviousPageAction());
    final Action zoomInAction = createDefaultZoomInAction();
    final Action zoomOutAction = createDefaultZoomOutAction();
    zoomActionMap.put(ZOOM_IN_ACTION_KEY, zoomInAction);
    zoomActionMap.put(ZOOM_OUT_ACTION_KEY, zoomOutAction);
  }

  /**
   * Creates the NextPageAction used in this previewframe.
   *
   * @return the 'next page' action.
   */
  protected Action createDefaultNextPageAction()
  {
    return new DefaultNextPageAction();
  }

  /**
   * Creates the PreviousPageAction used in this previewframe.
   *
   * @return the 'previous page' action.
   */
  protected Action createDefaultPreviousPageAction()
  {
    return new DefaultPreviousPageAction();
  }

  /**
   * Creates the ZoomInAction used in this previewframe.
   *
   * @return the 'zoom in' action.
   */
  protected Action createDefaultZoomInAction()
  {
    return new DefaultZoomInAction();
  }

  /**
   * Creates the ZoomOutAction used in this previewframe.
   *
   * @return the 'zoom out' action.
   */
  protected Action createDefaultZoomOutAction()
  {
    return new DefaultZoomOutAction();
  }

  /**
   * Creates the AboutAction used in this previewframe.
   * <P>
   * If you subclass PreviewFrame, and override this method, you can display your own 'about'
   * dialog.
   *
   * @return the 'about' action.
   */
  protected Action createDefaultAboutAction()
  {
    return new DefaultAboutAction();
  }

  /**
   * Creates a zoom select action.
   *
   * @return the action.
   */
  protected Action createZoomSelectAction()
  {
    return new ZoomSelectAction();
  }

  /**
   * Creates the GotoPageAction used in this previewframe.
   *
   * @return the 'goto' action.
   */
  protected Action createDefaultGotoAction()
  {
    return new DefaultGotoAction();
  }

  /**
   * Creates the FirstPageAction used in this previewframe.
   *
   * @return the 'first page' action.
   */
  protected Action createDefaultFirstPageAction()
  {
    return new DefaultFirstPageAction();
  }

  /**
   * Creates the LastPageAction used in this previewframe.
   *
   * @return the 'last page' action.
   */
  protected Action createDefaultLastPageAction()
  {
    return new DefaultLastPageAction();
  }

  /**
   * Returns the status text of the status line.
   *
   * @return the status text.
   */
  public String getStatusText()
  {
    return statusHolder.getText();
  }

  /**
   * Defines the text of the status line.
   *
   * @param text the new text of the status line.
   */
  public void setStatusText(final String text)
  {
    statusHolder.setText(text);
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
    statusPane.add(statusHolder, BorderLayout.CENTER);
    statusPane.add(progressBar, BorderLayout.EAST);
    return statusPane;
  }

  /**
   * Creates and returns a menu-bar for the frame.
   *
   * @return A ready-made JMenuBar.
   */
  protected JMenuBar createMenuBar()
  {
    // create the menus
    final JMenuBar menuBar = new JMenuBar();
    menuBar.add(createFileMenu());

    if (isMenuActionEnabled(ACTION_NAVIGATION_PROPERTY))
    {
      menuBar.add(createNavigationMenu());
    }
    if (isMenuActionEnabled(ACTION_ZOOM_PROPERTY))
    {
      menuBar.add(createZoomMenu());
    }
    if (isMenuActionEnabled(ACTION_ABOUT_PROPERTY))
    {
      menuBar.add(createHelpMenu());
    }
    return menuBar;
  }

  /**
   * Creates and returns the file menu of the preview base. The actions must
   * be assigned in this method.
   *
   * @return A ready-made FileMenu.
   */
  protected JMenu createFileMenu()
  {
    final ResourceBundle resources = getResources();
    final JMenu fileMenu = new JMenu(resources.getString("menu.file.name"));
    Integer mnemonic = ResourceBundleUtils.createMnemonic
        (resources.getString("menu.file.mnemonic"));
    fileMenu.setMnemonic(mnemonic.intValue());

    final Iterator it = exportPlugIns.iterator();
    final boolean addedItem = it.hasNext();
    while (it.hasNext())
    {
      final ExportPlugin plugIn = (ExportPlugin) it.next();
      final ExportAction action = (ExportAction) pluginActions.get(plugIn);
      if (plugIn.isSeparated())
      {
        fileMenu.addSeparator();
      }
      fileMenu.add(createMenuItem(action));
    }
    if (addedItem == true)
    {
      fileMenu.addSeparator();
    }

    fileMenu.add(createMenuItem(getCloseAction()));
    return fileMenu;
  }

  /**
   * Creates and returns the navigation menu of the preview base. The actions must
   * be assigned in this method.
   *
   * @return A ready-made navigation Menu.
   */
  protected JMenu createNavigationMenu()
  {
    final ResourceBundle resources = getResources();
    // the navigation menu ...
    final JMenu navMenu = new JMenu(resources.getString("menu.navigation.name"));
    final Integer mnemonic = ResourceBundleUtils.createMnemonic
        (resources.getString("menu.navigation.mnemonic"));
    navMenu.setMnemonic(mnemonic.intValue());

    navMenu.add(createMenuItem(getGotoAction()));
    navMenu.addSeparator();
    navMenu.add(createMenuItem(getFirstPageAction()));
    navMenu.add(createMenuItem(getPreviousPageAction()));
    navMenu.add(createMenuItem(getNextPageAction()));
    navMenu.add(createMenuItem(getLastPageAction()));
    return navMenu;
  }

  /**
   * Creates and returns the zoom menu of the preview base. The actions must
   * be assigned in this method.
   *
   * @return A ready-made zoom menu.
   */
  protected JMenu createZoomMenu()
  {
    final ResourceBundle resources = getResources();
    // the navigation menu ...
    final JMenu zoomMenu = new JMenu(resources.getString("menu.zoom.name"));
    Integer mnemonic = ResourceBundleUtils.createMnemonic
        (resources.getString("menu.zoom.mnemonic"));
    zoomMenu.setMnemonic(mnemonic.intValue());

    zoomMenu.add(createMenuItem(getZoomInAction()));
    zoomMenu.add(createMenuItem(getZoomOutAction()));
    zoomMenu.add(new JSeparator());

    final DowngradeActionMap zoomActionMap = getZoomActionMap();
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      final Action action = new ZoomSetAction(i);
      zoomActionMap.put(action.getValue(Action.NAME), action);
      zoomMenu.add(createMenuItem(action));
    }
    return zoomMenu;
  }

  /**
   * Creates and returns the help menu of the preview base. The actions must
   * be assigned in this method.
   *
   * @return A ready-made help menu.
   */
  protected JMenu createHelpMenu()
  {
    final ResourceBundle resources = getResources();
    // then the help menu
    final JMenu helpMenu = new JMenu(resources.getString("menu.help.name"));
    Integer mnemonic = ResourceBundleUtils.createMnemonic
        (resources.getString("menu.help.mnemonic"));
    helpMenu.setMnemonic(mnemonic.intValue());
    helpMenu.add(createMenuItem(getAboutAction()));
    return helpMenu;
  }

  /**
   * Creates a button using the given action properties for the button's initialisation.
   *
   * @param action  the action used to set up the button.
   *
   * @return a button based on the supplied action.
   */
  protected JButton createButton(final Action action)
  {
    final JButton button = new ActionButton(action);
    if (isLargeIconsEnabled())
    {
      final Icon icon = (Icon) action.getValue("ICON24");
      if (icon != null)
      {
        button.setIcon(icon);
      }
    }
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setText(null);
    FloatingButtonEnabler.getInstance().addButton(button);
    return button;
  }

  /**
   * Creates a menu item based on the supplied action.
   *
   * @param action  the action.
   *
   * @return the menu item.
   */
  protected JMenuItem createMenuItem(final Action action)
  {
    final JMenuItem menuItem = new ActionMenuItem(action);
    final KeyStroke accelerator = (KeyStroke) action.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
    {
      menuItem.setAccelerator(accelerator);
    }
    return menuItem;
  }

  /**
   * Returns the toolbar used in this component. If the toolbar is disabled
   * in the global report configuration, then <code>null</code> is returned.
   *
   * @return the toolbar of this component or null
   */
  protected final JToolBar getToolbar()
  {
    return toolbar;
  }

  /**
   * Creates and returns a toolbar containing controls for print, page forward and backward, zoom
   * in and out, and an about box.
   */
  protected void initializeToolBar()
  {
    // Maybe the toolbar is disabled, then we don't want to touch it.
    if (toolbar == null)
    {
      return;
    }

    final Iterator it = exportPlugIns.iterator();
    final boolean addedItem = it.hasNext();
    if (isToolbarActionEnabled(ACTION_CLOSE_PROPERTY))
    {
      toolbar.add(createButton(getCloseAction()));
      toolbar.addSeparator();
    }
    while (it.hasNext())
    {
      final ExportPlugin plugIn = (ExportPlugin) it.next();
      if (plugIn.isAddToToolbar() == false)
      {
        continue;
      }
      final ExportAction action = (ExportAction) pluginActions.get(plugIn);
      if (plugIn.isSeparated())
      {
        toolbar.addSeparator();
      }
      toolbar.add(createButton(action));
    }
    if (addedItem == true)
    {
      toolbar.addSeparator();
    }
    if (isToolbarActionEnabled(ACTION_NAVIGATION_PROPERTY))
    {
      toolbar.add(createButton(getFirstPageAction()));
      toolbar.add(createButton(getPreviousPageAction()));
      toolbar.add(createButton(getNextPageAction()));
      toolbar.add(createButton(getLastPageAction()));
      toolbar.addSeparator();
    }
    if (isToolbarActionEnabled(ACTION_ZOOM_PROPERTY))
    {
      toolbar.add(createButton(getZoomInAction()));
      toolbar.add(createButton(getZoomOutAction()));
      toolbar.addSeparator();
      toolbar.add(createZoomPane());
      toolbar.addSeparator();
    }
    Object[] keys = getCustomActionMap().keys();
    for (int i = 0; i < keys.length; i++)
    {
      Action a = getCustomActionMap().get(keys[i]);
      toolbar.add(createButton(a));
    }
    if (keys.length > 0)
    {
      toolbar.addSeparator();
    }

    if (isToolbarActionEnabled(ACTION_ABOUT_PROPERTY))
    {
      toolbar.add(createButton(getAboutAction()));
    }
  }

  /**
   * Returns true, if the toolbar is floatable, false otherwise.
   *
   * @return true when the toolbar is floatable.
   */
  public boolean isToolbarFloatable()
  {
    return toolbarFloatable;
  }

  /**
   * Defines whether the toolbar is floatable.
   *
   * @param b  a flag that indicates whether or not the toolbar is floatable.
   */
  public void setToolbarFloatable(final boolean b)
  {
    final boolean oldValue = this.toolbarFloatable;
    this.toolbarFloatable = b;
    firePropertyChange("toolbarFloatable", oldValue, b);
  }

  protected JComboBox createZoomSelector()
  {
    final DefaultComboBoxModel model = new DefaultComboBoxModel();
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      model.addElement(String.valueOf((int) (ZOOM_FACTORS[i] * 100)) + " %");
    }
    JComboBox zoomSelect = new JComboBox(model);
    zoomSelect.setActionCommand("ZoomSelect");
    zoomSelect.setSelectedIndex(DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(createZoomSelectAction());
    zoomSelect.setAlignmentX(Component.RIGHT_ALIGNMENT);
    return zoomSelect;
  }

  /**
   * Creates a panel containing a combobox with available zoom-values.
   *
   * @return a panel containing a combobox with zoom values.
   */
  protected JComponent createZoomPane()
  {
    final JPanel zoomPane = new JPanel();
    zoomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    zoomPane.add(zoomSelect);

    return zoomPane;
  }

  /**
   * Updates the states of all buttons to reflect the state of the assigned ReportPane.
   */
  protected void validateButtons()
  {
    if (lockInterface == true)
    {
      // do not reenable any buttons.
      // but make sure they remain locked.
      disableButtons();
      return;
    }

    final int pn = reportPane.getPageNumber();
    final int mp = reportPane.getNumberOfPages();

    getLastPageAction().setEnabled(pn < mp);
    getNextPageAction().setEnabled(pn < mp);

    // no previous page, so dont enable
    getPreviousPageAction().setEnabled(pn > 1);
    getFirstPageAction().setEnabled(pn > 1);
    // no goto, if there is only one page
    getGotoAction().setEnabled(mp > 1);

    final Iterator it = exportPlugIns.iterator();
    while (it.hasNext())
    {
      final ExportPlugin ep = (ExportPlugin) it.next();
      final ExportAction ea = (ExportAction) pluginActions.get(ep);
      if (ep.isControlPlugin() == false)
      {
        ea.setEnabled(mp > 0);
      }
    }

    zoomSelect.setEnabled(true);
    final Object[] keys = zoomActionMap.keys();
    for (int i = 0; i < keys.length; i++)
    {
      zoomActionMap.get(keys[i]).setEnabled(true);
    }
    getZoomOutAction().setEnabled(zoomSelect.getSelectedIndex() != 0);
    getZoomInAction().setEnabled(zoomSelect.getSelectedIndex() != (ZOOM_FACTORS.length - 1));
  }

  /**
   * Returns the zoom selection combobox. Use this to enable or diable
   * it, but dont modify it, or be doomed.
   * @return the zoom selection combobox.
   */
  protected JComboBox getZoomSelect()
  {
    return zoomSelect;
  }

  /**
   * Disables the buttons.
   */
  protected void disableButtons()
  {
    getGotoAction().setEnabled(false);
    getLastPageAction().setEnabled(false);
    getNextPageAction().setEnabled(false);
    getPreviousPageAction().setEnabled(false);
    getFirstPageAction().setEnabled(false);
    final Object[] keys = zoomActionMap.keys();
    for (int i = 0; i < keys.length; i++)
    {
      zoomActionMap.get(keys[i]).setEnabled(false);
    }
    zoomSelect.setEnabled(false);

    final Iterator it = pluginActions.values().iterator();
    while (it.hasNext())
    {
      final ExportAction ea = (ExportAction) it.next();
      ea.setEnabled(false);
    }
  }

  /**
   * Returns the export action map containing all export related actions.
   * This map contains the actions for the export plugins.
   *
   * @return the export action map.
   */
  public DowngradeActionMap getExportActionMap()
  {
    return exportActionMap;
  }

  /**
   * Returns the base action map containing all basic actions.
   * This map contains such actions like the Close-Action or the
   * About-Action.
   *
   * @return the export action map.
   */
  public DowngradeActionMap getBaseActionMap()
  {
    return baseActionMap;
  }

  /**
   * Returns the navigation action map containing all navigation related
   * actions. This map contains the various "Goto..." actions.
   *
   * @return the export action map.
   */
  public DowngradeActionMap getNavigationActionMap()
  {
    return navigationActionMap;
  }

  /**
   * Returns the zoom action map containing all zoom related actions.
   * This map contains actions controling the zoom level of the report pane.
   *
   * @return the export action map.
   */
  public DowngradeActionMap getZoomActionMap()
  {
    return zoomActionMap;
  }

  /**
   * Returns the repagination report progress dialog.
   * @return the repaginiation progress dialog.
   */
  protected ReportProgressDialog getProgressDialog()
  {
    return progressDialog;
  }

  /**
   * Returns true if large icons are enabled for the toolbar.
   *
   * @return true if large icons are enabled.
   */
  public boolean isLargeIconsEnabled()
  {
    return largeIconsEnabled;
  }

  /**
   * Sets a flag that controls whether or not large icons are used in the toolbar.
   *
   * @param b  the new value of the flag.
   */
  public void setLargeIconsEnabled(final boolean b)
  {
    final boolean oldValue = largeIconsEnabled;
    largeIconsEnabled = b;
    firePropertyChange(LARGE_ICONS_PROPERTY, oldValue, b);
  }

  /**
   * Disposes the preview frame.
   */
  public void dispose()
  {
    freeResources();
    // Silly Swing keeps at least one reference in the RepaintManager to support DoubleBuffering
    // I dont want this here, as PreviewFrames are evil and resource expensive ...

    // I hope this helps as well ...
    // Setting the repaint manager to null is invalid, as the silly swing
    // seems to loose all update requests.
    //
    // So we have to choose beween a memory leak or an invalid repaint operation
    RepaintManager.currentManager(this).removeInvalidComponent(this);
    RepaintManager.currentManager(this).markCompletelyClean(this);
  }

  /**
   * Performs a minor dispose operation and interrupts the repagination
   * worker.
   */
  protected final void freeResources()
  {
    try
    {
      // make sure that the pagination worker does no longer waste our time.
      // this won't kill the export worker ...
      repaginationWorker.interrupt();
    }
    catch (SecurityException se)
    {
      // ignore
    }
    if (progressDialog.isVisible())
    {
      progressDialog.setVisible(false);
      progressDialog.dispose();
    }
    // cleanup the report pane, removes some cached resources ...
    reportPane.dispose();
  }

  /**
   * Checks, wether the preview frame was finally closed. Closing the
   * frame does not just mean to make it invisible, it also kills all
   * worker threads. Once the preview is closed, it is not possible to
   * reactivate it again.
   *
   * @return true, if the preview is closed, false otherwise
   */
  public boolean isClosed()
  {
    return closed;
  }

  /**
   * Shuts down the preview component. Once the component is closed, it
   * cannot be reactivated anymore. Calling this method will abort all
   * worker threads, will close the progress dialog and dispose all
   * components.
   */
  public void close()
  {
    closed = true;
    exportWorkerPool.finishAll();
    repaginationWorker.finish();
    if (progressDialog.isVisible())
    {
      progressDialog.setVisible(false);
      progressDialog.dispose();
    }
    closeToolbar();
    dispose();
  }

  /**
   * Called by the garbage collector on an object when garbage collection
   * determines that there are no more references to the object.
   * A subclass overrides the <code>finalize</code> method to dispose of
   * system resources or to perform other cleanup.
   *
   * @throws Throwable the <code>Exception</code> raised by this method
   */
  public void finalize() throws Throwable
  {
    if (isClosed() == false)
    {
      close();
    }
    super.finalize();
    exportWorkerPool.finishAll();
    repaginationWorker.finish();
  }

  /**
   * Returns the 'About' action.
   *
   * @return the 'About' action.
   */
  public Action getAboutAction()
  {
    return baseActionMap.get(ABOUT_ACTION_KEY);
  }

  /**
   * Sets the 'About' action.
   *
   * @param aboutAction  the 'About' action.
   */
  public void setAboutAction(final Action aboutAction)
  {
    baseActionMap.put(ABOUT_ACTION_KEY, aboutAction);
  }

  /**
   * Returns the 'Close' action.
   *
   * @return the 'Close' action.
   */
  public Action getCloseAction()
  {
    return baseActionMap.get(CLOSE_ACTION_KEY);
  }

  /**
   * Sets the 'Close' action.
   *
   * @param closeAction  the 'Close' action.
   */
  public void setCloseAction(final Action closeAction)
  {
    baseActionMap.put(CLOSE_ACTION_KEY, closeAction);
  }

  /**
   * Returns the 'First Page' action.
   *
   * @return the 'First Page' action.
   */
  public Action getFirstPageAction()
  {
    return navigationActionMap.get(FIRSTPAGE_ACTION_KEY);
  }

  /**
   * Sets the 'First Page' action.
   *
   * @param firstPageAction  the 'First Page' action.
   */
  public void setFirstPageAction(final Action firstPageAction)
  {
    navigationActionMap.put(FIRSTPAGE_ACTION_KEY, firstPageAction);
  }

  /**
   * Returns the 'Last Page' action.
   *
   * @return the 'Last Page' action.
   */
  public Action getLastPageAction()
  {
    return navigationActionMap.get(LASTPAGE_ACTION_KEY);
  }

  /**
   * Sets the 'Last Page' action.
   *
   * @param lastPageAction  the 'Last Page' action.
   */
  public void setLastPageAction(final Action lastPageAction)
  {
    navigationActionMap.put(LASTPAGE_ACTION_KEY, lastPageAction);
  }

  /**
   * Returns the 'Next Page' action.
   *
   * @return the 'Next Page' action.
   */
  public Action getNextPageAction()
  {
    return navigationActionMap.get(NEXT_PAGE_ACTION_KEY);
  }

  /**
   * Sets the 'Next Page' action.
   *
   * @param nextPageAction  the 'Next Page' action.
   */
  public void setNextPageAction(final Action nextPageAction)
  {
    navigationActionMap.put(NEXT_PAGE_ACTION_KEY, nextPageAction);
  }

  /**
   * Returns the 'Previous Page' action.
   *
   * @return the 'Previous Page' action.
   */
  public Action getPreviousPageAction()
  {
    return navigationActionMap.get(PREV_PAGE_ACTION_KEY);
  }

  /**
   * Sets the 'Previous Page' action.
   *
   * @param previousPageAction  the 'Previous Page' action.
   */
  public void setPreviousPageAction(final Action previousPageAction)
  {
    navigationActionMap.put(PREV_PAGE_ACTION_KEY, previousPageAction);
  }

  /**
   * Returns the 'Zoom In' action.
   *
   * @return the 'Zoom In' action.
   */
  public Action getZoomInAction()
  {
    return zoomActionMap.get(ZOOM_IN_ACTION_KEY);
  }

  /**
   * Sets the 'Zoom In' action.
   *
   * @param zoomInAction  the 'Zoom In' action.
   */
  public void setZoomInAction(final Action zoomInAction)
  {
    zoomActionMap.put(ZOOM_IN_ACTION_KEY, zoomInAction);
  }

  /**
   * Returns the 'Zoom Out' action.
   *
   * @return the 'Zoom Out' action.
   */
  public Action getZoomOutAction()
  {
    return zoomActionMap.get(ZOOM_OUT_ACTION_KEY);
  }

  /**
   * Sets the 'Zoom Out' action.
   *
   * @param zoomOutAction  the 'Zoom Out' action.
   */
  public void setZoomOutAction(final Action zoomOutAction)
  {
    zoomActionMap.put(ZOOM_OUT_ACTION_KEY, zoomOutAction);
  }

  /**
   * Returns the 'Goto' action.
   *
   * @return the 'Goto' action.
   */
  public Action getGotoAction()
  {
    return navigationActionMap.get(GOTO_ACTION_KEY);
  }

  /**
   * Sets the 'Goto' action.
   *
   * @param gotoAction  the 'Goto' action.
   */
  public void setGotoAction(final Action gotoAction)
  {
    navigationActionMap.put(GOTO_ACTION_KEY, gotoAction);
  }

  /**
   * Updates the pageformat of the ReportPane.
   *
   * @param pf the new page format object.
   */
  public void updatePageFormat(final PageFormat pf)
      throws ReportProcessingException
  {
    if (pf == null)
    {
      throw new NullPointerException("The given pageformat is null.");
    }
    JFreeReport report = getReport();
    report.setPageDefinition(new SimplePageDefinition (pf));
    setReport(report);
  }

  /**
   * Checks, whether the interface is locked. A locked interface has all
   * actions disabled and waits for a certain task to be completed. The only
   * actions that are always enabled are teh help and the exit actions.
   *
   * @return true, if the interface is in the locked state, or false otherwise.
   */
  public boolean isLockInterface()
  {
    return lockInterface;
  }

  /**
   * Defines, whether the interface is locked. A locked interface has all
   * actions disabled and waits for a certain task to be completed. The only
   * actions that are always enabled are teh help and the exit actions.
   *
   * @param lockInterface set to true, if the interface should be set into the
   * locked state, or false otherwise.
   */
  public void setLockInterface(final boolean lockInterface)
  {
    this.lockInterface = lockInterface;
    if (lockInterface == true)
    {
      disableButtons();
    }
    else
    {
      validateButtons();
    }
  }

  /**
   * Adds a repagination listener to this component. The listener will be
   * informed about the pagination progress.
   *
   * @param listener the listener to be added.
   */
  public void addRepaginationListener(final RepaginationListener listener)
  {
    reportPane.addRepaginationListener(listener);
  }

  /**
   * Removes the specified repagination listener from this component.
   *
   * @param listener the listener to be removed.
   */
  public void removeRepaginationListener(final RepaginationListener listener)
  {
    reportPane.removeRepaginationListener(listener);
  }

  public void setReport(JFreeReport report) throws ReportProcessingException
  {
    ReportPane oldPane = this.reportPane;

    if (reportPane != null)
    {
      // interrupts the Pagination worker and hides the progress dialog.
      freeResources();
      reportPane.removeRepaginationListener(progressDialog);
      reportPane.removePropertyChangeListener(reportPanePropertyChangeListener);

      reportPaneHolder.remove(reportPane);
      reportPane = null;
    }

    setLargeIconsEnabled
        (report.getReportConfiguration().getConfigProperty
        (LARGE_ICONS_ENABLED_PROPERTY, "true").equals("true"));

    setToolbarFloatable
        (report.getReportConfiguration().getConfigProperty
        (TOOLBAR_FLOATABLE_PROPERTY, "true").equals("true"));

    setProgressBarEnabled
        (report.getReportConfiguration().getConfigProperty
        (PROGRESS_BAR_ENABLE_PROPERTY, "true").equals("true"));

    setProgressDialogEnabled
        (report.getReportConfiguration().getConfigProperty
         (PROGRESS_DIALOG_ENABLE_PROPERTY, "true").equals("true"));

    proxy.setTitle(report.getName() + " - " + getResources().getString("preview-frame.title"));

    // set up the content with a toolbar and a report pane
    baseActionMap.put(CLOSE_ACTION_KEY, proxy.createDefaultCloseAction());

    this.zoomIndex = DEFAULT_ZOOM_INDEX;

    // we have an old toolbar floating around ... lets remove it
    if (toolbar != null)
    {
      closeToolbar();
      remove(toolbar);
    }

    if (isPropertySet(CREATE_TOOLBAR_PROPERTY, true))
    {
      toolbar = createToolBar();
      toolbar.setFloatable(isToolbarFloatable());
      toolbar.addPropertyChangeListener(new ToolbarPropertyChangeListener());
      add(toolbar, BorderLayout.NORTH);
    }

    reportPane = createReportPane(report);
    reportPane.addPropertyChangeListener(reportPanePropertyChangeListener);
    reportPane.setVisible(false); // initially hidden
    reportPaneHolder.add(reportPane);

    buildExportPlugins(report);
    reinitialize();

    applyDefinedDimension(report);

    firePropertyChange(REPORT_PANE_PROPERTY, oldPane, reportPane);

    performPagination();

    // inform all listeners, that we have a new reportpane
    Log.info("setReport(..): started pagination ...");
  }

  /**
   * Paginates the report. This method should be called from the event dispatcher
   * thread, or funny things could happen.
   */
  protected void performPagination()
  {
    setLockInterface(true);
    setStatusText(getResources().getString("statusline.repaginate"));
    progressDialog.setTitle(getResources().getString("statusline.repaginate"));
    progressDialog.setMessage(getResources().getString("statusline.repaginate"));
    progressDialog.pack();

    final Worker worker = getRepaginationWorker();
    //Log.debug ("Worker is unavailable (preSync) ... " + worker.isAvailable());
    synchronized (worker)
    {
      while (worker.isAvailable() == false)
      {
        // wait until the worker is done with his current job
        //Log.debug ("Worker is unavailable ... ");
        try
        {
          worker.wait();
        }
        catch (InterruptedException ie)
        {
          // ignored
        }
      }
      getRepaginationWorker().setWorkload(new RepaginationRunnable());
    }
    //Log.debug ("Worker is on the way... ");
  }

  public JFreeReport getReport()
  {
    return reportPane.getReport();
  }

  public void refresh() throws ReportProcessingException
  {
    JFreeReport report = reportPane.getReport();
    setReport(report);
  }

  public DowngradeActionMap getCustomActionMap()
  {
    return customActionMap;
  }

  public boolean isProgressBarEnabled()
  {
    return progressBarEnabled;
  }

  public void setProgressBarEnabled(boolean progressBarEnabled)
  {
    this.progressBarEnabled = progressBarEnabled;
  }

  public boolean isProgressDialogEnabled()
  {
    return progressDialogEnabled;
  }

  public void setProgressDialogEnabled(boolean progressDialogEnabled)
  {
    this.progressDialogEnabled = progressDialogEnabled;
  }

  protected ReportProgressBar getProgressBar()
  {
    return progressBar;
  }
}
