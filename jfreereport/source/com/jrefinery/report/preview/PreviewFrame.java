/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------
 * PreviewFrame.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PreviewFrame.java,v 1.42 2002/12/04 19:49:13 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 24-Apr-2002 : Corrected the structure to better interact with ReportPane. Uses PropertyChange
 *               Events to pass information from the panel. Zoom and Page-Buttons maintain
 *               their state according to the informations from the ReportPane.
 *               SaveToPDF appends a ".pdf" extension to the filename, if not given.
 * 10-May-2002 : Inner classes deliver default implementations for all actions. Actions are used
 *               directly to create menus and buttons.
 * 16-May-2002 : Line delimiters adjusted
 *               close behaviour unified
 *               reset the mnemonics of the toolBar buttons
 * 17-May-2002 : KeyListener for zooming and navigation
 * 26-May-2002 : Added a statusline to the report to show errors and the current and total page
 *               number.  Printing supports the pageable interface.
 * 08-Jun-2002 : Documentation and the pageFormat property is removed. The pageformat is defined
 *               in the JFreeReport-object and passed to the ReportPane.
 * 06-Sep-2002 : Added Dispose on Component-hide, so that this Frame can be garbageCollected.
 *               Without this Construct, the PreviewFrame would never be GarbageCollected and
 *               would cause OutOfMemoryExceptions when the program runs a longer time.
 */

package com.jrefinery.report.preview;

import com.jrefinery.layout.CenterLayout;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.action.AboutAction;
import com.jrefinery.report.action.CloseAction;
import com.jrefinery.report.action.FirstPageAction;
import com.jrefinery.report.action.GotoPageAction;
import com.jrefinery.report.action.LastPageAction;
import com.jrefinery.report.action.NextPageAction;
import com.jrefinery.report.action.PageSetupAction;
import com.jrefinery.report.action.PreviousPageAction;
import com.jrefinery.report.action.PrintAction;
import com.jrefinery.report.action.SaveAsAction;
import com.jrefinery.report.action.ZoomInAction;
import com.jrefinery.report.action.ZoomOutAction;
import com.jrefinery.report.util.ActionButton;
import com.jrefinery.report.util.ActionDowngrade;
import com.jrefinery.report.util.ActionMenuItem;
import com.jrefinery.report.util.ExceptionDialog;
import com.jrefinery.report.util.FloatingButtonEnabler;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.AbstractActionDowngrade;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A standard print preview frame for any JFreeReport.  Allows the user to page back and forward
 * through the report, zoom in and out, and send the output to the printer.
 * <P>
 * You can also save the report in PDF format (thanks to the iText library).
 * <p>
 * When including this PreviewFrame in own programs, you should override the provided
 * createXXXAction-methods to include your customized actions.
 *
 * @author DG
 */
public class PreviewFrame
    extends JFrame
    implements JFreeReportConstants
{
  private class WrapperAction implements Action
  {
    private Action parent;

    public WrapperAction(Action parent)
    {
      setParent(parent);
    }

    public Action getParent()
    {
      return parent;
    }

    public void setParent(Action parent)
    {
      if (parent == null) throw new NullPointerException();
      this.parent = parent;
      registerAction(parent);
    }

    /**
     * Gets one of this object's properties
     * using the associated key.
     * @see #putValue
     */
    public Object getValue(String key)
    {
      return parent.getValue(key);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      parent.actionPerformed(e);
    }

    /**
     * Sets one of this object's properties
     * using the associated key. If the value has
     * changed, a <code>PropertyChangeEvent</code> is sent
     * to listeners.
     *
     * @param key    a <code>String</code> containing the key
     * @param value  an <code>Object</code> value
     */
    public void putValue(String key, Object value)
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
     * @param  b true to enable this <code>Action</code>, false to disable it
     */
    public void setEnabled(boolean b)
    {
      parent.setEnabled(b);
    }

    /**
     * Returns the enabled state of the <code>Action</code>. When enabled,
     * any component associated with this object is active and
     * able to fire this object's <code>actionPerformed</code> method.
     *
     * @return true if this <code>Action</code> is enabled
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
     * @param listener  a <code>PropertyChangeListener</code> object
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
      parent.addPropertyChangeListener(listener);
    }

    /**
     * Removes a <code>PropertyChange</code> listener.
     *
     * @param listener  a <code>PropertyChangeListener</code> object
     * @see #addPropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
      parent.removePropertyChangeListener(listener);
    }
  }

  private class ReportPanePropertyChangeListener implements PropertyChangeListener
  {
    public ReportPanePropertyChangeListener()
    {
    }

    /**
     * Listen to the assigned reportPane
     *
     * @param event  the property change event.
     */
    public void propertyChange(PropertyChangeEvent event)
    {
      String property = event.getPropertyName();

      if (property.equals(ReportPane.PAGENUMBER_PROPERTY)
          || property.equals(ReportPane.NUMBER_OF_PAGES_PROPERTY))
      {

        Object[] params = new Object[]{
          new Integer(reportPane.getPageNumber()),
          new Integer(reportPane.getNumberOfPages())
        };
        getStatus().setText(
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
          Exception ex = reportPane.getError();

          ex.printStackTrace();
          getStatus().setText(
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
   * Default 'save as' action for the frame.
   */
  protected class DefaultSaveAsAction extends SaveAsAction implements Runnable
  {
    /**
     * Creates a 'save as' action.
     */
    public DefaultSaveAsAction()
    {
      super(getResources());
    }

    /**
     * Closes the preview frame.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      SwingUtilities.invokeLater(this);
    }

    /**
     * Passes control to the frame which will present a dialog to save the report in PDF format.
     */
    public void run()
    {
      attemptSaveAs();
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
     * Jump to the first page of the report
     *
     * @param e The action event.
     *
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
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
     * show the next page of the report
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
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
    public void actionPerformed(ActionEvent e)
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
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
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
    public void actionPerformed(ActionEvent e)
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
    public void actionPerformed(ActionEvent e)
    {
      decreaseZoom();
    }
  }

  /**
   * Default 'print' action for the frame.
   */
  private class DefaultPrintAction extends PrintAction implements Runnable
  {
    /**
     * Creates a 'print' action.
     */
    public DefaultPrintAction()
    {
      super(getResources());
    }

    /**
     * Prints the report
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      SwingUtilities.invokeLater(this);
    }

    /**
     * The workload for this action.
     */
    public void run()
    {
      try
      {
        attemptPrint();
      }
      catch (PrinterException pe)
      {
      }
    }
  }

  /**
   * Default 'page setup' action for the frame.
   */
  private class DefaultPageSetupAction extends PageSetupAction implements Runnable
  {
    /**
     * Creates a 'page setup' action.
     */
    public DefaultPageSetupAction()
    {
      super(getResources());
    }

    /**
     * perform page setup
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      SwingUtilities.invokeLater(this);
    }

    /**
     * The workload for this action.
     */
    public void run()
    {
      attemptPageSetup();
    }
  }

  /**
   * Default 'close' action for the frame.
   */
  private class DefaultCloseAction extends CloseAction
  {
    /**
     * Creates a 'close' action.
     */
    public DefaultCloseAction()
    {
      super(getResources());
    }

    /**
     * Closes the preview frame if the default close operation is set to dispose
     * so this frame is reusable.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      if (getDefaultCloseOperation() == DISPOSE_ON_CLOSE)
      {
        dispose();
      }
      else
      {
        setVisible(false);
      }
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
    public void actionPerformed(ActionEvent e)
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
    public void actionPerformed(ActionEvent e)
    {
      String result = JOptionPane.showInputDialog(PreviewFrame.this,
          getResources().getString("dialog.gotopage.title"),
          getResources().getString("dialog.gotopage.message"),
          JOptionPane.OK_CANCEL_OPTION);
      if (result == null)
      {
        return;
      }
      try
      {
        int page = Integer.parseInt(result);

        // thanks to anonymous
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

  private class ZoomSelectAction extends AbstractAction
  {
    public ZoomSelectAction()
    {
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      setZoomFactor(zoomSelect.getSelectedIndex());
    }
  }

  protected class ZoomSetAction extends AbstractActionDowngrade
  {
    private int zoomFactor;

    public ZoomSetAction(int factorIndex)
    {
      zoomFactor = factorIndex;
      this.putValue(Action.NAME, String.valueOf((int) (ZOOM_FACTORS[factorIndex] * 100)) + " %");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
      setZoomFactor(zoomFactor);
    }
  }

  /** The base class for localised resources. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  /** The 'about' action. */
  private WrapperAction aboutAction;

  /** The 'save as' action. */
  private WrapperAction saveAsAction;

  /** The 'page setup' action. */
  private WrapperAction pageSetupAction;

  /** The 'print' action. */
  private WrapperAction printAction;

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
  private static final double[] ZOOM_FACTORS = {0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 3.0, 4.0};

  /** The default zoom index (corresponds to a zoomFactor of 1.0. */
  private static final int DEFAULT_ZOOM_INDEX = 3;

  /** The combobox enables a direct selection of the desired zoomFactor */
  private JComboBox zoomSelect;

  /** The current zoom index (indexes into the zoomFactors array). */
  private int zoomIndex;

  /** The pane that displays the report within the frame. */
  private ReportPane reportPane;

  /** Locale-specific resources. */
  private ResourceBundle resources;

  /** Defines whether to use 24x24 icons or 16x16 icons */
  private boolean largeIconsEnabled;

  /** Label for status. */
  private JLabel statusHolder;

  /** Toolbar. */
  private JToolBar toolbar;

  /** A dialog for specifying PDF file properties. */
  private PDFSaveDialog pdfSaveDialog;

  /**
   * Constructs a PreviewFrame that displays the specified report, and has the specified width
   * and height (to begin with).
   *
   * @param report The report to be displayed.
   */
  public PreviewFrame(JFreeReport report) throws ReportProcessingException
  {
    // get a locale-specific resource bundle...
    setLargeIconsEnabled(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Handle a JDK-Bug: Windows are not GCd if dispose is not called manually.
    // DisposedState is undone when show() or pack() is called, so this does no harm
    addComponentListener(new ComponentAdapter()
    {
      public void componentHidden(ComponentEvent e)
      {
        ((Window) (e.getComponent())).dispose();
      }
    });

    ResourceBundle resources = getResources();

    this.setTitle(resources.getString("preview-frame.title"));
    this.zoomIndex = DEFAULT_ZOOM_INDEX;

    createDefaultActions();
    registerDefaultActions();

    // set up the menu
    setJMenuBar(createMenuBar(resources));

    // set up the content with a toolbar and a report pane
    JPanel content = new JPanel(new BorderLayout());
    content.setDoubleBuffered(false);
    toolbar = createToolBar();
    content.add(toolbar, BorderLayout.NORTH);

    reportPane = createReportPane(report);
    reportPane.addPropertyChangeListener(createReportPanePropertyChangeListener());

    JPanel reportPaneHolder = new JPanel(new CenterLayout());
    reportPaneHolder.add(reportPane);
    reportPaneHolder.setDoubleBuffered(false);
    reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane s1 = new JScrollPane(reportPaneHolder);
    s1.setDoubleBuffered(false);
    //s1.setBorder(null);
    s1.getVerticalScrollBar().setUnitIncrement(20);

    JPanel scrollPaneHolder = new JPanel();
    scrollPaneHolder.setLayout(new BorderLayout());
    scrollPaneHolder.add(s1, BorderLayout.CENTER);
    scrollPaneHolder.setDoubleBuffered(false);

    scrollPaneHolder.add(createStatusBar(), BorderLayout.SOUTH);
    content.add(scrollPaneHolder);
    setContentPane(content);

    pdfSaveDialog = new PDFSaveDialog(this);
    pdfSaveDialog.pack();
  }

  /**
   * Returns the PDF save dialog.
   *
   * @return the PDF save dialog.
   */
  public PDFSaveDialog getPdfSaveDialog()
  {
    return pdfSaveDialog;
  }

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
   */
  protected ReportPane createReportPane(JFreeReport report) throws ReportProcessingException
  {
    ReportPane reportPane = new ReportPane(report);
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
  public double getZoomFactor()
  {
    return ZOOM_FACTORS[zoomIndex];
  }

  /**
   * Presents a "Save As" dialog to the user, enabling him/her to save the report in PDF format.
   */
  protected void attemptSaveAs()
  {
    getPdfSaveDialog().savePDF(reportPane.getReport(), reportPane.getPageFormat());
  }

  /**
   * Displays a printer page setup dialog, then updates the report pane with the new page size.
   */
  protected void attemptPageSetup()
  {
    PrinterJob pj = PrinterJob.getPrinterJob();
    PageFormat pf = pj.pageDialog(reportPane.getPageFormat());

    reportPane.setPageFormat(pf);
    validateButtons();
    pack();
  }

  /**
   * Prints the report.
   */
  protected void attemptPrint() throws PrinterException
  {
    PrinterJob pj = PrinterJob.getPrinterJob();
    pj.setPageable(reportPane);
    if (pj.printDialog())
    {
      pj.print();
    }
  }

  protected Pageable getPageable ()
  {
    return reportPane;
  }

  protected Printable getPrintable ()
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
  protected void showExceptionDialog(String localisationBase, Exception e)
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
   * Method lastPage moves to the last page
   */
  protected void lastPage()
  {
    reportPane.setPageNumber(reportPane.getNumberOfPages());
    //validateButtons();
  }

  /**
   *Increases the page number.
   *
   * CHANGED FUNCTION
   */
  protected void increasePageNumber()
  {
    int pn = reportPane.getPageNumber();
    int mp = reportPane.getNumberOfPages();

    if (pn < mp)
    {
      reportPane.setPageNumber(pn + 1);
      //validateButtons();
    }
  }

  /**
   * Method firstPage changes to the first page if not already on the first page
   */
  protected void firstPage()
  {
    if (reportPane.getPageNumber() != 1)
    {
      reportPane.setPageNumber(1);
      //validateButtons();
    }
  }

  /**
   * Decreases the page number.
   */
  protected void decreasePageNumber()
  {
    int pn = reportPane.getPageNumber();
    if (pn > 1)
    {
      reportPane.setPageNumber(pn - 1);
      //validateButtons();
    }
  }

  /**
   * Increases the zoom factor for the report pane (unless it is already at maximum zoom).
   *
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
  public void setZoomFactor(int index)
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
  protected void registerAction(Action action)
  {
    JComponent cp = getRootPane();
    KeyStroke key = (KeyStroke) action.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (key != null)
    {
      cp.registerKeyboardAction(action, key, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
  }

  /**
   * registeres the keystrokes of all known actions to this frame
   */
  protected void registerDefaultActions()
  {
    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void windowClosing(WindowEvent e)
      {
        getCloseAction().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CloseFrame"));
      }
    }
    );

  }

  /**
   * creates all actions by calling the createXXXAction functions and assigning them to
   * the local variables.
   */
  private void createDefaultActions()
  {
    gotoAction = new WrapperAction(createDefaultGotoAction());
    saveAsAction = new WrapperAction(createDefaultSaveAsAction());
    pageSetupAction = new WrapperAction(createDefaultPageSetupAction());
    printAction = new WrapperAction(createDefaultPrintAction());
    aboutAction = new WrapperAction(createDefaultAboutAction());
    closeAction = new WrapperAction(createDefaultCloseAction());
    firstPageAction = new WrapperAction(createDefaultFirstPageAction());
    lastPageAction = new WrapperAction(createDefaultLastPageAction());
    nextPageAction = new WrapperAction(createDefaultNextPageAction());
    previousPageAction = new WrapperAction(createDefaultPreviousPageAction());
    zoomInAction = new WrapperAction(createDefaultZoomInAction());
    zoomOutAction = new WrapperAction(createDefaultZoomOutAction());
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
   * Creates the SaveAsAction used in this previewframe.
   *
   * @return the 'save as' action.
   */
  protected Action createDefaultSaveAsAction()
  {
    return new DefaultSaveAsAction();
  }

  /**
   * creates the PageSetupAction used in this previewframe.
   *
   * @return the 'page setup' action.
   */
  protected Action createDefaultPageSetupAction()
  {
    return new DefaultPageSetupAction();
  }

  /**
   * Creates the PrintAction used in this previewframe.
   *
   * @return the 'print' action.
   */
  protected Action createDefaultPrintAction()
  {
    return new DefaultPrintAction();
  }

  /**
   * Creates the CloseAction used in this previewframe.
   *
   * @return the 'close' action.
   */
  protected Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
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
   * Returns the status label used to display the text.
   *
   * @return the status label.
   */
  protected JLabel getStatus()
  {
    return statusHolder;
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status bar.
   *
   * @return the status bar.
   */
  protected JPanel createStatusBar()
  {
    JPanel statusPane = new JPanel();
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
   * @param resources A resource bundle containing localised resources for the menu.
   *
   * @return A ready-made JMenuBar.
   */
  protected JMenuBar createMenuBar(ResourceBundle resources)
  {

    // create the menus

    JMenuBar menuBar = new JMenuBar();
    // first the file menu

    JMenu fileMenu = new JMenu(resources.getString("menu.file.name"));
    Character mnemonic = (Character) resources.getObject("menu.file.mnemonic");
    fileMenu.setMnemonic(mnemonic.charValue());

    fileMenu.add(createMenuItem(saveAsAction));
    fileMenu.addSeparator();
    fileMenu.add(createMenuItem(pageSetupAction));
    fileMenu.add(createMenuItem(printAction));
    fileMenu.add(new JSeparator());
    fileMenu.add(createMenuItem(closeAction));

    // the navigation menu ...
    JMenu navMenu = new JMenu(resources.getString("menu.navigation.name"));
    mnemonic = (Character) resources.getObject("menu.navigation.mnemonic");
    navMenu.setMnemonic(mnemonic.charValue());

    navMenu.add(createMenuItem(gotoAction));
    navMenu.addSeparator();
    navMenu.add(createMenuItem(firstPageAction));
    navMenu.add(createMenuItem(previousPageAction));
    navMenu.add(createMenuItem(nextPageAction));
    navMenu.add(createMenuItem(lastPageAction));

    // the navigation menu ...
    JMenu zoomMenu = new JMenu(resources.getString("menu.zoom.name"));
    mnemonic = (Character) resources.getObject("menu.zoom.mnemonic");
    zoomMenu.setMnemonic(mnemonic.charValue());

    zoomMenu.add (createMenuItem(zoomInAction));
    zoomMenu.add (createMenuItem(zoomOutAction));
    zoomMenu.add(new JSeparator());
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      zoomMenu.add (createMenuItem(new ZoomSetAction(i)));
    }

    // then the help menu
    JMenu helpMenu = new JMenu(resources.getString("menu.help.name"));
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
  protected JButton createButton(Action action)
  {
    JButton button = new ActionButton(action);
    if (isLargeIconsEnabled())
    {
      Icon icon = (Icon) action.getValue("ICON24");
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

  protected JMenuItem createMenuItem (Action action)
  {
    JMenuItem menuItem = new ActionMenuItem(action);
    KeyStroke accelerator = (KeyStroke) action.getValue(ActionDowngrade.ACCELERATOR_KEY);
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
   * @return A completely initialized JToolBar.
   */
  protected JToolBar createToolBar()
  {
    JToolBar toolbar = new JToolBar();

    toolbar.add(createButton(saveAsAction));
    toolbar.add(createButton(printAction));
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
   * @return true when the toolbar is floatable
   */
  public boolean isToolbarFloatable()
  {
    return toolbar.isFloatable();
  }

  /**
   * Defines whether the toolbar is floatable
   *
   * @param b  a flag that indicates whether or not the toolbar is floatable.
   */
  public void setToolbarFloatable(boolean b)
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
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    for (int i = 0; i < ZOOM_FACTORS.length; i++)
    {
      model.addElement(String.valueOf((int) (ZOOM_FACTORS[i] * 100)) + " %");
    }
    zoomSelect = new JComboBox(model);
    zoomSelect.setActionCommand("ZoomSelect");
    zoomSelect.setSelectedIndex(DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(createZoomSelectAction());
    zoomSelect.setAlignmentX(zoomSelect.RIGHT_ALIGNMENT);

    JPanel zoomPane = new JPanel();
    zoomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    zoomPane.add(zoomSelect);

    return zoomPane;
  }

  /**
   * Updates the states of all buttons to reflect the state of the assigned ReportPane.
   */
  protected void validateButtons()
  {
    int pn = reportPane.getPageNumber();
    int mp = reportPane.getNumberOfPages();

    getLastPageAction().setEnabled(pn < mp);
    getNextPageAction().setEnabled(pn < mp);
    getPreviousPageAction().setEnabled(pn != 1);
    getFirstPageAction().setEnabled(pn != 1);

    getZoomOutAction().setEnabled(zoomSelect.getSelectedIndex() != 0);
    getZoomInAction().setEnabled(zoomSelect.getSelectedIndex() != (ZOOM_FACTORS.length - 1));
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
  public void setLargeIconsEnabled(boolean b)
  {
    largeIconsEnabled = b;
  }

  /**
   * Disposes the preview frame.
   */
  public void dispose()
  {
    // cleanup the report pane, removes some cached resources ...
    reportPane.dispose();
    pdfSaveDialog.dispose();
    super.dispose();

    // Silly Swing keeps at least one reference in the RepaintManager to support DoubleBuffering
    // I dont want this here, as PreviewFrames are evil and resource expensive ...
    RepaintManager.setCurrentManager(null);
  }

  public Action getAboutAction()
  {
    return aboutAction.getParent();
  }

  public void setAboutAction(Action aboutAction)
  {
    this.aboutAction.setParent(aboutAction);
  }

  public Action getSaveAsAction()
  {
    return saveAsAction.getParent();
  }

  public void setSaveAsAction(Action saveAsAction)
  {
    this.saveAsAction.setParent(saveAsAction);
  }

  public Action getPageSetupAction()
  {
    return pageSetupAction.getParent();
  }

  public void setPageSetupAction(Action pageSetupAction)
  {
    this.pageSetupAction.setParent(pageSetupAction);
  }

  public Action getPrintAction()
  {
    return printAction.getParent();
  }

  public void setPrintAction(Action printAction)
  {
    this.printAction.setParent(printAction);
  }

  public Action getCloseAction()
  {
    return closeAction.getParent();
  }

  public void setCloseAction(Action closeAction)
  {
    this.closeAction.setParent(closeAction);
  }

  public Action getFirstPageAction()
  {
    return firstPageAction.getParent();
  }

  public void setFirstPageAction(Action firstPageAction)
  {
    this.firstPageAction.setParent(firstPageAction);
  }

  public Action getLastPageAction()
  {
    return lastPageAction.getParent();
  }

  public void setLastPageAction(Action lastPageAction)
  {
    this.lastPageAction.setParent(lastPageAction);
  }

  public Action getNextPageAction()
  {
    return nextPageAction.getParent();
  }

  public void setNextPageAction(Action nextPageAction)
  {
    this.nextPageAction.setParent(nextPageAction);
  }

  public Action getPreviousPageAction()
  {
    return previousPageAction.getParent();
  }

  public void setPreviousPageAction(Action previousPageAction)
  {
    this.previousPageAction.setParent(previousPageAction);
  }

  public Action getZoomInAction()
  {
    return zoomInAction.getParent();
  }

  public void setZoomInAction(Action zoomInAction)
  {
    this.zoomInAction.setParent(zoomInAction);
  }

  public Action getZoomOutAction()
  {
    return zoomOutAction.getParent();
  }

  public void setZoomOutAction(Action zoomOutAction)
  {
    this.zoomOutAction.setParent(zoomOutAction);
  }

  public Action getGotoAction()
  {
    return gotoAction.getParent();
  }

  public void setGotoAction(Action gotoAction)
  {
    this.gotoAction.setParent(gotoAction);
  }
}
