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
 * -----------------
 * PreviewFrame.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PreviewFrame.java,v 1.32 2002/09/06 17:56:40 taqua Exp $
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
 * 26-May-2002 : Added a statusline to the report to show errors and the current and total page number.
 *               Printing supports the pageable interface.
 * 08-Jun-2002 : Documentation and the pageFormat property is removed. The pageformat is defined
 *               in the JFreeReport-object and passed to the ReportPane.
 * 06-Sep-2002 : Added Dispose on Component-hide, so that this Frame can be garbageCollected. Without this
 *               Construct, the PreviewFrame would never be GarbageCollected and would cause
 *               OutOfMemoryExceptions when the program runs a longer time.
 */

package com.jrefinery.report.preview;

import com.jrefinery.layout.CenterLayout;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.RepaintManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;
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
 * When including this PreviewFrame in own programms, you should override the provided
 * createXXXAction-methods to include your customized actions.
 */
public class PreviewFrame
    extends JFrame
    implements ActionListener, PropertyChangeListener, JFreeReportConstants
{
  private class DefaultSaveAsAction extends SaveAsAction implements Runnable
  {
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
     * save the report to a PDF file
     */
    public void run()
    {
      attemptSaveAs();
    }
  }

  private class DefaultFirstPageAction extends FirstPageAction
  {
    public DefaultFirstPageAction()
    {
      super(getResources());
    }

    /**
     * Jump to the first page of the report
     *
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
      firstPage();
    }
  }

  private class DefaultNextPageAction extends NextPageAction
  {

    public DefaultNextPageAction()
    {
      super(getResources());
    }

    /**
     * show the next page of the report
     */
    public void actionPerformed(ActionEvent e)
    {
      increasePageNumber();
    }
  }

  private class DefaultPreviousPageAction extends PreviousPageAction
  {
    public DefaultPreviousPageAction()
    {
      super(getResources());
    }

    /**
     * show the previous page of the report.
     */
    public void actionPerformed(ActionEvent e)
    {
      decreasePageNumber();
    }
  }

  private class DefaultLastPageAction extends LastPageAction
  {
    public DefaultLastPageAction()
    {
      super(getResources());
    }

    /**
     * jump to the last page of the report.
     *
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
      lastPage();
    }
  }

  private class DefaultZoomInAction extends ZoomInAction
  {

    public DefaultZoomInAction()
    {
      super(getResources());
    }

    /**
     * increase zoom.
     */
    public void actionPerformed(ActionEvent e)
    {
      increaseZoom();
    }
  }

  private class DefaultZoomOutAction extends ZoomOutAction
  {
    public DefaultZoomOutAction()
    {
      super(getResources());
    }

    /**
     * decrease zoom.
     */
    public void actionPerformed(ActionEvent e)
    {
      decreaseZoom();
    }
  }

  private class DefaultPrintAction extends PrintAction implements Runnable
  {
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

    public void run()
    {
      attemptPrint();
    }
  }

  private class DefaultPageSetupAction extends PageSetupAction implements Runnable
  {
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

    public void run()
    {
      attemptPageSetup();
    }
  }

  private class DefaultCloseAction extends CloseAction
  {
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

  private class DefaultAboutAction extends AboutAction
  {
    public DefaultAboutAction()
    {
      super(getResources());
    }

    /**
     * Show the about box
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
    }
  }

  private class DefaultGotoAction extends GotoPageAction
  {
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
        return;
      try
      {
        int page = Integer.parseInt(result);
        if (page > 0 && page < reportPane.getNumberOfPages())
        {
          reportPane.setPageNumber(page);
        }
      }
      catch (Exception ex)
      {
      }
    }
  }

  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  private Action aboutAction;
  private Action saveAsAction;
  private Action pageSetupAction;
  private Action printAction;
  private Action closeAction;
  private Action firstPageAction;
  private Action lastPageAction;
  private Action nextPageAction;
  private Action previousPageAction;
  private Action zoomInAction;
  private Action zoomOutAction;
  private Action gotoAction;

  /** The available zoom factors. */
  private static final double[] zoomFactors = {0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 3.0, 4.0};

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

  //private JFreeReport report;
  private JLabel statusHolder;
  private JToolBar toolbar;
  private PDFSaveDialog pdfSaveDialog;

  /**
   * Constructs a PreviewFrame that displays the specified report, and has the specified width
   * and height (to begin with).
   *
   * @param report The report to be displayed.
   */
  public PreviewFrame(JFreeReport report)
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
        System.out.println("Window is disposed...");
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
    //reportPane.addPropertyChangeListener(this);

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

  public PDFSaveDialog getPdfSaveDialog()
  {
    return pdfSaveDialog;
  }

  /**
   * creates the ReportPane for the report
   *
   * @param report the report for this pane
   */
  protected ReportPane createReportPane(JFreeReport report)
  {
    ReportPane reportPane = new ReportPane(report);
    return reportPane;
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @returns this frames ResourceBundle.
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
   * @return The current zoom factor.
   */
  public double getZoomFactor()
  {
    return zoomFactors[zoomIndex];
  }

  /**
   * Handles user actions within the frame (menu selections and mouse clicks).
   * @param event The action event.
   */
  public void actionPerformed(ActionEvent event)
  {

    String command = event.getActionCommand();

    if (command.equals("ZoomSelect"))
    {
      setZoomFactor(zoomSelect.getSelectedIndex());
    }
  }

  /**
   * Presents a "Save As" dialog to the user, enabling him/her to save the report in PDF format.
   */
  public void attemptSaveAs()
  {
    getPdfSaveDialog().savePDF(reportPane.getReport(), reportPane.getPageFormat());
  }

  /**
   * Displays a printer page setup dialog, then updates the report pane with the new page size.
   */
  public void attemptPageSetup()
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
  public void attemptPrint()
  {
    PrinterJob pj = PrinterJob.getPrinterJob();
    pj.setPageable(reportPane);
    if (pj.printDialog())
    {
      try
      {
        pj.print();
      }
      catch (PrinterException e)
      {
        showExceptionDialog("error.printfailed", e);
      }
    }
  }

  /**
   * Shows the exception dialog by using localized messages. The message base is
   * used to construct the localisation key by appending ".title" and ".message" to the
   * base name.
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
   * Method lastPage moves to the last page
   */
  public void lastPage()
  {
    reportPane.setPageNumber(reportPane.getNumberOfPages());
    //validateButtons();
  }

  /**
   *Increases the page number.
   *
   * CHANGED FUNCTION
   */
  public void increasePageNumber()
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
  public void firstPage()
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
  public void decreasePageNumber()
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
  public void increaseZoom()
  {
    if (zoomIndex < zoomFactors.length - 1)
    {
      zoomIndex++;
    }

    zoomSelect.setSelectedIndex(zoomIndex);

    reportPane.setZoomFactor(getZoomFactor());
    //validateButtons();
  }

  /** Decreases the zoom factor for the report pane (unless it is already at the minimum zoom). */
  public void decreaseZoom()
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
   * sets the zoomfactor of the report pane.
   */
  public void setZoomFactor(int index)
  {
    zoomIndex = index;
    reportPane.setZoomFactor(getZoomFactor());
    zoomSelect.setSelectedIndex(zoomIndex);
  }

  /**
   * checks whether this action has a keystroke assigned. If it has one, the keystroke
   * is assigned to the frame
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
    registerAction(gotoAction);
    registerAction(saveAsAction);
    registerAction(pageSetupAction);
    registerAction(printAction);
    registerAction(aboutAction);
    registerAction(closeAction);
    registerAction(firstPageAction);
    registerAction(lastPageAction);
    registerAction(nextPageAction);
    registerAction(previousPageAction);
    registerAction(zoomInAction);
    registerAction(zoomOutAction);
  }

  /**
   * creates all actions by calling the createXXXAction functions and assigning them to
   * the local variables.
   */
  protected void createDefaultActions()
  {
    gotoAction = createDefaultGotoAction();
    saveAsAction = createDefaultSaveAsAction();
    pageSetupAction = createDefaultPageSetupAction();
    printAction = createDefaultPrintAction();
    aboutAction = createDefaultAboutAction();
    closeAction = createDefaultCloseAction();
    firstPageAction = createDefaultFirstPageAction();
    lastPageAction = createDefaultLastPageAction();
    nextPageAction = createDefaultNextPageAction();
    previousPageAction = createDefaultPreviousPageAction();
    zoomInAction = createDefaultZoomInAction();
    zoomOutAction = createDefaultZoomOutAction();
  }

  /**
   * creates the NextPageAction used in this previewframe.
   */
  protected Action createDefaultNextPageAction()
  {
    return new DefaultNextPageAction();
  }

  /**
   * creates the PreviousPageAction used in this previewframe.
   */
  protected Action createDefaultPreviousPageAction()
  {
    return new DefaultPreviousPageAction();
  }

  /**
   * creates the ZoomInAction used in this previewframe.
   */
  protected Action createDefaultZoomInAction()
  {
    return new DefaultZoomInAction();
  }

  /**
   * creates the ZoomOutAction used in this previewframe.
   */
  protected Action createDefaultZoomOutAction()
  {
    return new DefaultZoomOutAction();
  }

  /**
   * creates the AboutAction used in this previewframe.
   */
  protected Action createDefaultAboutAction()
  {
    return new DefaultAboutAction();
  }

  /**
   * creates the GotoPageAction used in this previewframe.
   */
  protected Action createDefaultGotoAction()
  {
    return new DefaultGotoAction();
  }

  /**
   * creates the SaveAsAction used in this previewframe.
   */
  protected Action createDefaultSaveAsAction()
  {
    return new DefaultSaveAsAction();
  }

  /**
   * creates the PageSetupAction used in this previewframe.
   */
  protected Action createDefaultPageSetupAction()
  {
    return new DefaultPageSetupAction();
  }

  /**
   * creates the PrintAction used in this previewframe.
   */
  protected Action createDefaultPrintAction()
  {
    return new DefaultPrintAction();
  }

  /**
   * creates the CloseAction used in this previewframe.
   */
  protected Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  /**
   * creates the FirstPageAction used in this previewframe.
   */
  protected Action createDefaultFirstPageAction()
  {
    return new DefaultFirstPageAction();
  }

  /**
   * creates the LastPageAction used in this previewframe.
   */
  protected Action createDefaultLastPageAction()
  {
    return new DefaultLastPageAction();
  }

  /**
   * returns the status label used to display the text
   */
  protected JLabel getStatus()
  {
    return statusHolder;
  }

  /**
   * Creates the statusbar for this frame. Use setStatus() to display text on the status bar.
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
   * @return A ready-made JMenuBar.
   */
  protected JMenuBar createMenuBar(ResourceBundle resources)
  {

    // create the menus

    JMenuBar menuBar = new JMenuBar();
    // first the file menu

    String menuName = resources.getString("menu.file.name");
    JMenu fileMenu = new JMenu(menuName);
    Character mnemonic = (Character) resources.getObject("menu.file.mnemonic");
    fileMenu.setMnemonic(mnemonic.charValue());

    JMenuItem gotoItem = new ActionMenuItem(gotoAction);
    KeyStroke accelerator = (KeyStroke) gotoAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
      gotoItem.setAccelerator(accelerator);
    fileMenu.add(gotoItem);

    fileMenu.addSeparator();

    JMenuItem saveAsItem = new ActionMenuItem(saveAsAction);
    accelerator = (KeyStroke) saveAsAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
      saveAsItem.setAccelerator(accelerator);
    fileMenu.add(saveAsItem);

    fileMenu.addSeparator();

    JMenuItem setupItem = new ActionMenuItem(pageSetupAction);
    accelerator = (KeyStroke) pageSetupAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
      setupItem.setAccelerator(accelerator);
    fileMenu.add(setupItem);

    JMenuItem printItem = new ActionMenuItem(printAction);
    accelerator = (KeyStroke) printAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
      printItem.setAccelerator(accelerator);
    fileMenu.add(printItem);

    fileMenu.add(new JSeparator());

    JMenuItem closeItem = new ActionMenuItem(closeAction);
    accelerator = (KeyStroke) closeAction.getValue(ActionDowngrade.ACCELERATOR_KEY);
    if (accelerator != null)
      closeItem.setAccelerator(accelerator);
    fileMenu.add(closeItem);

    // then the help menu

    menuName = resources.getString("menu.help.name");
    JMenu helpMenu = new JMenu(menuName);
    mnemonic = (Character) resources.getObject("menu.help.mnemonic");
    helpMenu.setMnemonic(mnemonic.charValue());

    JMenuItem aboutItem = new ActionMenuItem(aboutAction);
    helpMenu.add(aboutItem);

    // finally, glue together the menu and return it

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    return menuBar;
  }

  /**
   * Creates a button using the given actions properties for the buttons initialisation.
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

  /**
   * Creates and returns a toolbar containing controls for print, page forward and backward, zoom
   * in and out, and an about box.
   *
   * @return A completly initialized JToolBar.
   */
  protected JToolBar createToolBar()
  {
    ResourceBundle resources = getResources();

    JToolBar toolbar = new JToolBar();
    //toolbar.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

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
   * @returns true when the toolbar is floatable
   */
  public boolean isToolbarFloatable()
  {
    return toolbar.isFloatable();
  }

  /**
   * Defines whether the toolbar is floatable
   */
  public void setToolbarFloatable(boolean b)
  {
    toolbar.setFloatable(b);
  }

  /**
   * Creates a panel containing a combobox with available zoom-values.
   */
  private JComponent createZoomPane()
  {
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    for (int i = 0; i < zoomFactors.length; i++)
    {
      model.addElement(String.valueOf((int) (zoomFactors[i] * 100)) + " %");
    }
    zoomSelect = new JComboBox(model);
    zoomSelect.setActionCommand("ZoomSelect");
    zoomSelect.setSelectedIndex(DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(this);
    zoomSelect.setAlignmentX(zoomSelect.RIGHT_ALIGNMENT);

    JPanel zoomPane = new JPanel();
    zoomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    zoomPane.add(zoomSelect);

    return zoomPane;
  }

  /**
   * Listen to the assigned reportPane
   */
  public void propertyChange(PropertyChangeEvent event)
  {
    Object source = event.getSource();
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
      Log.debug ("ZoomFactor changed!");
      validateButtons();
    }
  }

  /**
   * Updates the states of all buttons to reflect the state of the assigned ReportPane.
   */
  protected void validateButtons()
  {
    int pn = reportPane.getPageNumber();
    int mp = reportPane.getNumberOfPages();

    lastPageAction.setEnabled(pn < mp);
    nextPageAction.setEnabled(pn < mp);
    previousPageAction.setEnabled(pn != 1);
    firstPageAction.setEnabled(pn != 1);

    zoomOutAction.setEnabled(zoomSelect.getSelectedIndex() != 0);
    zoomInAction.setEnabled(zoomSelect.getSelectedIndex() != (zoomFactors.length - 1));
  }

  /**
   * @see JFrame#processWindowEvent(WindowEvent)
   */
  protected void processWindowEvent(WindowEvent windowEvent)
  {
    if (windowEvent.getID() == WindowEvent.WINDOW_CLOSING)
    {
      closeAction.actionPerformed(
          new ActionEvent(this, ActionEvent.ACTION_PERFORMED, closeAction.NAME));
    }
    super.processWindowEvent(windowEvent);
  }

  public boolean isLargeIconsEnabled()
  {
    return largeIconsEnabled;
  }

  public void setLargeIconsEnabled(boolean b)
  {
    largeIconsEnabled = b;
  }

  public void dispose ()
  {
    //Log.debug (" ------------------> FRAME DISPOSED -------------------->");
    super.dispose();

    // Silly Swing keeps at least one reference in the RepaintManager to support DoubleBuffering
    // I dont want this here, as PreviewFrames are evil and resource expensive ...
    RepaintManager.setCurrentManager(null);

  }
/*
  protected void finalize() throws Throwable
  {
    Log.debug (" ------------------> FRAME FINALIZED -------------------->");
    super.finalize();
  }
*/
}
