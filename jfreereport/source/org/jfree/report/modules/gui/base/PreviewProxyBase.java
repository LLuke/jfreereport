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
 * ---------------------
 * PreviewProxyBase.java
 * ---------------------
 * (C)opyright 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewProxyBase.java,v 1.18 2003/09/12 18:46:18 taqua Exp $
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
import javax.swing.UIManager;

import org.jfree.layout.CenterLayout;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionButton;
import org.jfree.report.modules.gui.base.components.ActionConcentrator;
import org.jfree.report.modules.gui.base.components.ActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionMenuItem;
import org.jfree.report.modules.gui.base.components.ExceptionDialog;
import org.jfree.report.modules.gui.base.components.FloatingButtonEnabler;
import org.jfree.report.modules.gui.base.resources.JFreeReportResources;
import org.jfree.report.util.Log;
import org.jfree.report.util.Worker;
import org.jfree.report.util.ImageUtils;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ParserUtil;

/**
 * A preview proxy.
 *
 * @author Thomas Morgner.
 */
public class PreviewProxyBase extends JComponent
{
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

  /** A configuration key to define whether large toolbar icons are enabled. */
  public static final String LARGE_ICONS_ENABLED_PROPERTY
      = "org.jfree.report.modules.gui.base.LargeIcons";

  /** A configuration key to define whether the toolbar is floatable. */
  public static final String TOOLBAR_FLOATABLE_PROPERTY
      = "org.jfree.report.modules.gui.base.ToolbarFloatable";

  /**
   * A wrapper action.
   */
  private class WrapperAction implements Action
  {
    /** The parent action. */
    private Action parent;

    /**
     * Creates a new action.
     *
     * @param parent  the parent action (null not permitted).
     */
    public WrapperAction(final Action parent)
    {
      setParent(parent);
    }

    /**
     * Returns the parent action.
     *
     * @return the parent action.
     */
    public Action getParent()
    {
      return parent;
    }

    /**
     * Sets the parent action.
     *
     * @param parent  the parent action (null not permitted).
     */
    public void setParent(final Action parent)
    {
      if (parent == null)
      {
        throw new NullPointerException();
      }
      this.parent = parent;
      registerAction(parent);
    }

    /**
     * Gets one of this object's properties using the associated key.
     *
     * @param key  the key.
     *
     * @return the property value.
     *
     * @see #putValue
     */
    public Object getValue(final String key)
    {
      return parent.getValue(key);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e  the event.
     */
    public void actionPerformed(final ActionEvent e)
    {
      parent.actionPerformed(e);
    }

    /**
     * Sets one of this object's properties
     * using the associated key. If the value has
     * changed, a <code>PropertyChangeEvent</code> is sent
     * to listeners.
     *
     * @param key    a <code>String</code> containing the key.
     * @param value  an <code>Object</code> value.
     */
    public void putValue(final String key, final Object value)
    {
      parent.putValue(key, value);
    }

    /**
     * Sets the enabled state of the <code>Action</code>.  When enabled,
     * any component associated with this object is active and
     * able to fire this object's <code>actionPerformed</code> method.
     * If the value has changed, a <code>PropertyChangeEvent</code> is sent
     * to listeners.
     *
     * @param  b true to enable this <code>Action</code>, false to disable it.
     */
    public void setEnabled(final boolean b)
    {
      parent.setEnabled(b);
    }

    /**
     * Returns the enabled state of the <code>Action</code>. When enabled,
     * any component associated with this object is active and
     * able to fire this object's <code>actionPerformed</code> method.
     *
     * @return true if this <code>Action</code> is enabled.
     */
    public boolean isEnabled()
    {
      return parent.isEnabled();
    }

    /**
     * Adds a <code>PropertyChange</code> listener. Containers and attached
     * components use these methods to register interest in this
     * <code>Action</code> object. When its enabled state or other property
     * changes, the registered listeners are informed of the change.
     *
     * @param listener  a <code>PropertyChangeListener</code> object.
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener)
    {
      parent.addPropertyChangeListener(listener);
    }

    /**
     * Removes a <code>PropertyChange</code> listener.
     *
     * @param listener  a <code>PropertyChangeListener</code> object
     * @see #addPropertyChangeListener
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener)
    {
      parent.removePropertyChangeListener(listener);
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
      setZoomFactor(zoomSelect.getSelectedIndex());
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
   * Returns the worker.
   *
   * @return The worker.
   */
  protected Worker getWorker()
  {
    if (paginationWorker == null)
    {
      paginationWorker = new Worker();
      paginationWorker.setPriority(Thread.MIN_PRIORITY);
      paginationWorker.setName("Repagination-Worker");
    }
    return paginationWorker;
  }

  /** The base class for localised resources. */
  public static final String BASE_RESOURCE_CLASS =
      JFreeReportResources.class.getName();

  /** The worker thread which is used to perform the repagination. */
  private Worker paginationWorker;

  /** The worker thread which is used to perform the repagination. */
  private Worker exportWorker;

  /** The 'about' action. */
  private WrapperAction aboutAction;

  /** The 'close' action. */
  private WrapperAction closeAction;

  /** The 'first page' action. */
  private WrapperAction firstPageAction;

  /** The 'last page' action. */
  private WrapperAction lastPageAction;

  /** The 'next page' action. */
  private WrapperAction nextPageAction;

  /** The 'previous page' action. */
  private WrapperAction previousPageAction;

  /** The 'zoom in' action. */
  private WrapperAction zoomInAction;

  /** The 'zoom out' action. */
  private WrapperAction zoomOutAction;

  /** The 'goto' action. */
  private WrapperAction gotoAction;

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

  /** The maximum size. */
  private Dimension maximumSize;

  /** The preferred size. */
  private Dimension preferredSize;

  /** A preview proxy. */
  private PreviewProxy proxy;

  /** A list of all export plugins known to this preview proxy base. */
  private ArrayList exportPlugIns;

  /** A collection of actions, keyed by the export plugin. */
  private HashMap pluginActions;

  /** The progress monitor dialog used to visualize the pagination progress. */
  private ReportProgressDialog progressDialog;
  /** A flag to define whether the interface should be the locked state. */
  private boolean lockInterface;
  /** The action concentrator used to lock or unlock the interface. */
  private ActionConcentrator zoomActionConcentrator;
  /** A flag that defines, whether the preview component is closed. */ 
  private boolean closed;


  /**
   * Creates a preview proxy.
   *
   * @param proxy  the proxy.
   */
  public PreviewProxyBase(final PreviewProxy proxy)
  {
    this.proxy = proxy;
    this.progressDialog = new ReportProgressDialog();
    this.progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
  }

  /**
   * Initialises the preview dialog.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public void init(final JFreeReport report) throws ReportProcessingException
  {
    this.zoomActionConcentrator = new ActionConcentrator();
    this.exportWorker = new Worker();
    this.exportWorker.setName("preview-dialog-export-worker: report: " + report.getName());

    final boolean largeIconsProperty =
        report.getReportConfiguration().getConfigProperty
        (LARGE_ICONS_ENABLED_PROPERTY, "true").equals("true");
    setLargeIconsEnabled(largeIconsProperty);

    final ExportPluginFactory factory = ExportPluginFactory.getInstance();
    exportPlugIns = factory.createExportPlugIns
      (proxy, report.getReportConfiguration(), exportWorker);
    pluginActions = new HashMap(exportPlugIns.size());
    final Iterator it = exportPlugIns.iterator();
    while (it.hasNext())
    {
      final ExportPlugin ep = (ExportPlugin) it.next();
      final ExportAction ea = new ExportAction(ep);
      ea.setReport(report);
      pluginActions.put(ep, ea);
    }

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

    // get a locale-specific resource bundle...
    final ResourceBundle resources = getResources();

    proxy.setTitle(resources.getString("preview-frame.title"));
    this.zoomIndex = DEFAULT_ZOOM_INDEX;

    createDefaultActions(proxy.createDefaultCloseAction());

    // set up the menu
    proxy.setJMenuBar(createMenuBar());

    // set up the content with a toolbar and a report pane
    setLayout(new BorderLayout());
    setDoubleBuffered(false);

    final boolean toolbarFloatableProperty =
        report.getReportConfiguration().getConfigProperty
        (TOOLBAR_FLOATABLE_PROPERTY, "true").equals("true");
    toolbar = createToolBar(toolbarFloatableProperty);
    add(toolbar, BorderLayout.NORTH);

    reportPane = createReportPane(report);
    reportPane.addPropertyChangeListener(createReportPanePropertyChangeListener());
    reportPane.setVisible(false);

    final JPanel reportPaneHolder = new JPanel(new CenterLayout());
    reportPaneHolder.add(reportPane);
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

    applyDefinedDimension(report);

    performPagination(report.getDefaultPageFormat());
    Log.info("Dialog started pagination ...");
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
   * Returns the maximum size of this container.
   *
   * @see #getPreferredSize
   * @return the maximum size of the dialog
   */
  public Dimension getMaximumSize()
  {
    if (maximumSize == null)
    {
      return super.getMaximumSize();
    }
    return maximumSize;
  }

  /**
   * defines the maximum size of this container.
   *
   * @see #setPreferredSize
   * @param maximumSize the maximum size of the dialog
   */
  public void setMaximumSize(final Dimension maximumSize)
  {
    this.maximumSize = maximumSize;
  }

  /**
   * Returns the preferred size of this container.
   * @return    an instance of <code>Dimension</code> that represents
   *                the preferred size of this container.
   * @see       #getMinimumSize
   * @see       #getLayout
   * @see       java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
   * @see       java.awt.Component#getPreferredSize
   */
  public Dimension getPreferredSize()
  {
    if (preferredSize == null)
    {
      return super.getPreferredSize();
    }
    return preferredSize;
  }

  /**
   * defines the preferred size of this container.
   *
   * @see #setPreferredSize
   * @param preferredSize defines the preferred size for the PreviewComponent.
   */
  public void setPreferredSize(final Dimension preferredSize)
  {
    this.preferredSize = preferredSize;
  }

  /**
   * Creates a report pane listener.
   *
   * @return the listener.
   */
  protected ReportPanePropertyChangeListener createReportPanePropertyChangeListener()
  {
    return new ReportPanePropertyChangeListener();
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
  public ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
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
  protected ReportPane getReportPane()
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
   * @param defaultCloseAction  the default close action.
   */
  private void createDefaultActions(final Action defaultCloseAction)
  {
    gotoAction = new WrapperAction(createDefaultGotoAction());
    aboutAction = new WrapperAction(createDefaultAboutAction());
    closeAction = new WrapperAction(defaultCloseAction);
    firstPageAction = new WrapperAction(createDefaultFirstPageAction());
    lastPageAction = new WrapperAction(createDefaultLastPageAction());
    nextPageAction = new WrapperAction(createDefaultNextPageAction());
    previousPageAction = new WrapperAction(createDefaultPreviousPageAction());
    zoomInAction = new WrapperAction(createDefaultZoomInAction());
    zoomOutAction = new WrapperAction(createDefaultZoomOutAction());
    zoomActionConcentrator.addAction(zoomInAction);
    zoomActionConcentrator.addAction(zoomOutAction);
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
    statusPane.add(statusHolder, BorderLayout.WEST);

    return statusPane;
  }

  /**
   * Creates and returns a menu-bar for the frame.
   *
   * @return A ready-made JMenuBar.
   */
  protected JMenuBar createMenuBar()
  {
    final ResourceBundle resources = getResources();
    // create the menus
    final JMenuBar menuBar = new JMenuBar();
    // first the file menu

    final JMenu fileMenu = new JMenu(resources.getString("menu.file.name"));
    Character mnemonic = (Character) resources.getObject("menu.file.mnemonic");
    fileMenu.setMnemonic(mnemonic.charValue());

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

    fileMenu.add(createMenuItem(closeAction));

    // the navigation menu ...
    final JMenu navMenu = new JMenu(resources.getString("menu.navigation.name"));
    mnemonic = (Character) resources.getObject("menu.navigation.mnemonic");
    navMenu.setMnemonic(mnemonic.charValue());

    navMenu.add(createMenuItem(gotoAction));
    navMenu.addSeparator();
    navMenu.add(createMenuItem(firstPageAction));
    navMenu.add(createMenuItem(previousPageAction));
    navMenu.add(createMenuItem(nextPageAction));
    navMenu.add(createMenuItem(lastPageAction));

    // the navigation menu ...
    final JMenu zoomMenu = new JMenu(resources.getString("menu.zoom.name"));
    mnemonic = (Character) resources.getObject("menu.zoom.mnemonic");
    zoomMenu.setMnemonic(mnemonic.charValue());

    zoomMenu.add(createMenuItem(zoomInAction));
    zoomMenu.add(createMenuItem(zoomOutAction));
    zoomMenu.add(new JSeparator());
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      final Action action = new ZoomSetAction(i);
      zoomActionConcentrator.addAction(action);
      zoomMenu.add(createMenuItem(action));
    }

    // then the help menu
    final JMenu helpMenu = new JMenu(resources.getString("menu.help.name"));
    mnemonic = (Character) resources.getObject("menu.help.mnemonic");
    helpMenu.setMnemonic(mnemonic.charValue());
    helpMenu.add(createMenuItem(aboutAction));

    // finally, glue together the menu and return it

    menuBar.add(fileMenu);
    menuBar.add(navMenu);
    menuBar.add(zoomMenu);
    menuBar.add(helpMenu);

    return menuBar;
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
   * Creates and returns a toolbar containing controls for print, page forward and backward, zoom
   * in and out, and an about box.
   *
   * @param floatable defines, whether the toolbar will be initially floatable.
   * @return A completely initialized JToolBar.
   */
  protected JToolBar createToolBar(final boolean floatable)
  {
    final JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(floatable);

    final Iterator it = exportPlugIns.iterator();
    final boolean addedItem = it.hasNext();
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

    toolbar.add(createButton(firstPageAction));
    toolbar.add(createButton(previousPageAction));
    toolbar.add(createButton(nextPageAction));
    toolbar.add(createButton(lastPageAction));
    toolbar.addSeparator();
    toolbar.add(createButton(zoomInAction));
    toolbar.add(createButton(zoomOutAction));
    toolbar.addSeparator();
    toolbar.add(createZoomPane());
    toolbar.addSeparator();
    toolbar.add(createButton(aboutAction));

    return toolbar;
  }

  /**
   * Returns true, if the toolbar is floatable, false otherwise.
   *
   * @return true when the toolbar is floatable.
   */
  public boolean isToolbarFloatable()
  {
    return toolbar.isFloatable();
  }

  /**
   * Defines whether the toolbar is floatable.
   *
   * @param b  a flag that indicates whether or not the toolbar is floatable.
   */
  public void setToolbarFloatable(final boolean b)
  {
    toolbar.setFloatable(b);
  }

  /**
   * Creates a panel containing a combobox with available zoom-values.
   *
   * @return a panel containing a combobox with zoom values.
   */
  protected JComponent createZoomPane()
  {
    final DefaultComboBoxModel model = new DefaultComboBoxModel();
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      model.addElement(String.valueOf((int) (ZOOM_FACTORS[i] * 100)) + " %");
    }
    zoomSelect = new JComboBox(model);
    zoomSelect.setActionCommand("ZoomSelect");
    zoomSelect.setSelectedIndex(DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(createZoomSelectAction());
    zoomSelect.setAlignmentX(Component.RIGHT_ALIGNMENT);

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
      if (ep.isControlPlugin())
      {
        ea.setEnabled(true);
      }
      else
      {
        ea.setEnabled(mp > 0);
      }
    }

    zoomSelect.setEnabled(true);
    zoomActionConcentrator.setEnabled(true);
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
    zoomActionConcentrator.setEnabled(false);
    zoomSelect.setEnabled(false);

    final Iterator it = pluginActions.values().iterator();
    while (it.hasNext())
    {
      final ExportAction ea = (ExportAction) it.next();
      ea.setEnabled(false);
    }
  }

  /**
   * Returns the action concentrator used to collect all zoom-related
   * actions.
   *
   * @return the zoom action concentrator.
   */
  protected ActionConcentrator getZoomActionConcentrator()
  {
    return zoomActionConcentrator;
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
    largeIconsEnabled = b;
  }

  /**
   * Disposes the preview frame.
   */
  public void dispose()
  {
    try
    {
      // make sure that the pagination worker does no longer waste our time.
      // this won't kill the export worker ...
      paginationWorker.interrupt();
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

    // Silly Swing keeps at least one reference in the RepaintManager to support DoubleBuffering
    // I dont want this here, as PreviewFrames are evil and resource expensive ...

    // I hope this helps as well ...
    RepaintManager.currentManager(this).removeInvalidComponent(this);
    RepaintManager.currentManager(this).markCompletelyClean(this);
    //RepaintManager.setCurrentManager(null);
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
    exportWorker.finish();
    paginationWorker.finish();
    if (progressDialog.isVisible())
    {
      progressDialog.setVisible(false);
      progressDialog.dispose();
    }
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
    exportWorker.finish();
    paginationWorker.finish();
  }

  /**
   * Returns the 'About' action.
   *
   * @return the 'About' action.
   */
  public Action getAboutAction()
  {
    return aboutAction.getParent();
  }

  /**
   * Sets the 'About' action.
   *
   * @param aboutAction  the 'About' action.
   */
  public void setAboutAction(final Action aboutAction)
  {
    this.aboutAction.setParent(aboutAction);
  }

  /**
   * Returns the 'Close' action.
   *
   * @return the 'Close' action.
   */
  public Action getCloseAction()
  {
    return closeAction.getParent();
  }

  /**
   * Sets the 'Close' action.
   *
   * @param closeAction  the 'Close' action.
   */
  public void setCloseAction(final Action closeAction)
  {
    this.closeAction.setParent(closeAction);
  }

  /**
   * Returns the 'First Page' action.
   *
   * @return the 'First Page' action.
   */
  public Action getFirstPageAction()
  {
    return firstPageAction.getParent();
  }

  /**
   * Sets the 'First Page' action.
   *
   * @param firstPageAction  the 'First Page' action.
   */
  public void setFirstPageAction(final Action firstPageAction)
  {
    this.firstPageAction.setParent(firstPageAction);
  }

  /**
   * Returns the 'Last Page' action.
   *
   * @return the 'Last Page' action.
   */
  public Action getLastPageAction()
  {
    return lastPageAction.getParent();
  }

  /**
   * Sets the 'Last Page' action.
   *
   * @param lastPageAction  the 'Last Page' action.
   */
  public void setLastPageAction(final Action lastPageAction)
  {
    this.lastPageAction.setParent(lastPageAction);
  }

  /**
   * Returns the 'Next Page' action.
   *
   * @return the 'Next Page' action.
   */
  public Action getNextPageAction()
  {
    return nextPageAction.getParent();
  }

  /**
   * Sets the 'Next Page' action.
   *
   * @param nextPageAction  the 'Next Page' action.
   */
  public void setNextPageAction(final Action nextPageAction)
  {
    this.nextPageAction.setParent(nextPageAction);
  }

  /**
   * Returns the 'Previous Page' action.
   *
   * @return the 'Previous Page' action.
   */
  public Action getPreviousPageAction()
  {
    return previousPageAction.getParent();
  }

  /**
   * Sets the 'Previous Page' action.
   *
   * @param previousPageAction  the 'Previous Page' action.
   */
  public void setPreviousPageAction(final Action previousPageAction)
  {
    this.previousPageAction.setParent(previousPageAction);
  }

  /**
   * Returns the 'Zoom In' action.
   *
   * @return the 'Zoom In' action.
   */
  public Action getZoomInAction()
  {
    return zoomInAction.getParent();
  }

  /**
   * Sets the 'Zoom In' action.
   *
   * @param zoomInAction  the 'Zoom In' action.
   */
  public void setZoomInAction(final Action zoomInAction)
  {
    this.zoomInAction.setParent(zoomInAction);
  }

  /**
   * Returns the 'Zoom Out' action.
   *
   * @return the 'Zoom Out' action.
   */
  public Action getZoomOutAction()
  {
    return zoomOutAction.getParent();
  }

  /**
   * Sets the 'Zoom Out' action.
   *
   * @param zoomOutAction  the 'Zoom Out' action.
   */
  public void setZoomOutAction(final Action zoomOutAction)
  {
    this.zoomOutAction.setParent(zoomOutAction);
  }

  /**
   * Returns the 'Goto' action.
   *
   * @return the 'Goto' action.
   */
  public Action getGotoAction()
  {
    return gotoAction.getParent();
  }

  /**
   * Sets the 'Goto' action.
   *
   * @param gotoAction  the 'Goto' action.
   */
  public void setGotoAction(final Action gotoAction)
  {
    this.gotoAction.setParent(gotoAction);
  }

  /**
   * Updates the pageformat of the ReportPane.
   *
   * @param pf the new page format object.
   */
  public void updatePageFormat(final PageFormat pf)
  {
    if (pf == null)
    {
      throw new NullPointerException("The given pageformat is null.");
    }
    reportPane.setVisible(false);
    performPagination(pf);
  }

  /**
   * Paginates the report.
   * 
   * @param format the new page format for the report.
   */
  protected void performPagination(final PageFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException("The given pageformat is null.");
    }
    setLockInterface(true);
    setStatusText(getResources().getString("statusline.repaginate"));
    progressDialog.setTitle(getResources().getString("statusline.repaginate"));
    progressDialog.setMessage(getResources().getString("statusline.repaginate"));
    progressDialog.pack();

    final Worker worker = getWorker();
    synchronized (worker)
    {
      while (worker.isAvailable() == false)
      {
        // wait until the worker is done with his current job
        try
        {
          worker.wait();
        }
        catch (InterruptedException ie)
        {
          // ignored
        }
      }
      getWorker().setWorkload(new Runnable()
      {
        public void run()
        {
          final ReportPane reportPane = getReportPane();
          try
          {
            long startTime = System.currentTimeMillis();
            reportPane.addRepaginationListener(progressDialog);
            RefineryUtilities.positionFrameRandomly(progressDialog);
            progressDialog.setVisible(true);
            reportPane.setHandleInterruptedState(true);
            reportPane.setVisible(false);
            reportPane.setPageFormat(format);
            reportPane.repaginate();
            reportPane.setVisible(true);
            reportPane.setHandleInterruptedState(false);
            progressDialog.setVisible(false);
            reportPane.removeRepaginationListener(progressDialog);
            setLockInterface(false);
            Log.debug ("Pagination done: " + 
              ((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
          }
          catch (ReportInterruptedException re)
          {
            Log.info ("Repagination aborted.");
          }
          catch (Exception e)
          {
            Log.warn("Failed to repaginate", e);
            reportPane.setError(e);
          }
        }
      });
    }
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
}
