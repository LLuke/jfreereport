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
 * $Id: PreviewFrame.java,v 1.8 2002/05/26 13:47:39 taqua Exp $
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
 */

package com.jrefinery.report.preview;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import javax.swing.JLabel;

import com.jrefinery.layout.CenterLayout;
import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.action.AboutAction;
import com.jrefinery.report.action.CloseAction;
import com.jrefinery.report.action.PageSetupAction;
import com.jrefinery.report.action.PrintAction;
import com.jrefinery.report.action.SaveAsAction;
import com.jrefinery.report.targets.PDFOutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.ui.ExtensionFileFilter;

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
  implements ActionListener, PropertyChangeListener, JFreeReportConstants, KeyListener
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

    public void run()
    {
      handleSaveAs();
    }
  }

  private class DefaultPrintAction extends PrintAction implements Runnable
  {
    public DefaultPrintAction()
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
     * Closes the preview frame.
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
     * Closes the preview frame.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      dispose();
    }
  }

  private class DefaultAboutAction extends AboutAction
  {
    public DefaultAboutAction()
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
    }
  }

  public static final String BASE_RESOURCE_CLASS =
    "com.jrefinery.report.resources.JFreeReportResources";

  private Action aboutAction;
  private Action saveAsAction;
  private Action pageSetupAction;
  private Action printAction;
  private Action closeAction;

  /** The available zoom factors. */
  private static final double[] zoomFactors = { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 4.0 };

  /** The default zoom index (corresponds to a zoomFactor of 1.0. */
  private static final int DEFAULT_ZOOM_INDEX = 3;

  /** Internal reference to enable and disable this button */
  private JButton back;

  /** Internal reference to enable and disable this button */
  private JButton forward;

  private JButton zoomIn;

  private JButton zoomOut;

  /** The combobox enables a direct selection of the desired zoomFactor */
  private JComboBox zoomSelect;

  /** The current zoom index (indexes into the zoomFactors array). */
  private int zoomIndex;

  /** The pane that displays the report within the frame. */
  private ReportPane reportPane;

  /** The preferred size of the frame. */

  //protected Dimension preferredSize;

  /** Locale-specific resources. */
  private ResourceBundle resources;

  private PageFormat pageFormat;
  private JFreeReport report;
  private JLabel statusHolder;

  /**
   * Constructs a PreviewFrame that displays the specified report, and has the specified width
   * and height (to begin with).
   *
   * @param report The report to be displayed.
   * @param width The width of the frame.
   * @param height The height of the frame.
   */
  public PreviewFrame(JFreeReport report)
  {
    // get a locale-specific resource bundle...

    setResources(getDefaultResources());
    setReport(report);

    addKeyListener(this);

    ResourceBundle resources = getResources();

    this.setTitle(resources.getString("preview-frame.title"));
    this.zoomIndex = DEFAULT_ZOOM_INDEX;

    createDefaultActions();

    // set up the menu
    setJMenuBar(createMenuBar(resources));

    // set up the content with a toolbar and a report pane
    JPanel content = new JPanel(new BorderLayout());
    JToolBar toolbar = createToolBar();
    content.add(toolbar, BorderLayout.NORTH);

    reportPane = createReportPane();

    JPanel reportPaneHolder = new JPanel(new CenterLayout());
    reportPaneHolder.add(reportPane);
    reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane s1 = new JScrollPane(reportPaneHolder);
    JPanel scrollPaneHolder = new JPanel ();
    scrollPaneHolder.setLayout(new BorderLayout());
    scrollPaneHolder.add (s1, BorderLayout.CENTER);
    scrollPaneHolder.add (createStatusBar (), BorderLayout.SOUTH);
    content.add(scrollPaneHolder);
    setContentPane(content);
  }

  protected PageFormat getDefaultPageFormat()
  {
    PrinterJob pj = PrinterJob.getPrinterJob();
    PageFormat pf = pj.defaultPage();
    pf = pj.validatePage(pf);
    return pf;
  }

  public void setReport(JFreeReport report)
  {
    if (report == null)
      throw new NullPointerException("Report must not be null");

    this.report = report;
  }

  protected JFreeReport getReport()
  {
    return report;
  }

  protected ReportPane createReportPane()
  {
    JFreeReport report = getReport();
    ReportPane reportPane = new ReportPane(report, new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), getDefaultPageFormat()));
    reportPane.addPropertyChangeListener(ReportPane.NUMBER_OF_PAGES_PROPERTY, this);
    reportPane.addPropertyChangeListener(ReportPane.PAGENUMBER_PROPERTY, this);
    return reportPane;
  }

  protected void setResources(ResourceBundle bundle)
  {
    if (bundle == null)
      throw new NullPointerException("NullResource is not allowed");

    this.resources = bundle;
  }

  public ResourceBundle getResources()
  {
    return resources;
  }

  protected ResourceBundle getDefaultResources()
  {
    return ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
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

    if (command.equals("Back"))
    {
      decreasePageNumber();
    }
    else if (command.equals("Forward"))
    {
      increasePageNumber();
    }
    else if (command.equals("ZoomIn"))
    {
      increaseZoom();
    }
    else if (command.equals("ZoomOut"))
    {
      decreaseZoom();
    }
    else if (command.equals("ZoomSelect"))
    {
      setZoomFactor(zoomSelect.getSelectedIndex());
    }
  }

  /**
   * Saves the file to PDF, reporting any exceptions to System.err.
   */
  public void handleSaveAs()
  {
    try
    {
      doSaveAs();
    }
    catch (IOException e)
    {
      System.err.print(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Presents a "Save As" dialog to the user, enabling him/her to save the report in PDF format.
   */
  public void doSaveAs() throws IOException
  {
    JFileChooser fileChooser = new JFileChooser();
    ExtensionFileFilter filter = new ExtensionFileFilter("PDF Documents", ".pdf");
    fileChooser.addChoosableFileFilter(filter);

    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION)
    {
      PrinterJob pj = PrinterJob.getPrinterJob();
      PageFormat pf = pj.validatePage(reportPane.getPageFormat());
      File selFile = fileChooser.getSelectedFile();
      String selFileName = selFile.getAbsolutePath();

      // Test if ends of pdf

      if (selFileName.toUpperCase().endsWith(".PDF") == false)
      {
        selFileName = selFileName + ".pdf";
      }
      try
      {
        OutputStream out = new FileOutputStream(new File(selFileName));
        PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
        target.open("Title", "Author");
        getReport().processReport(target, true);
        target.close();
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog(
          this,
          "Error on saving PDF: " + ioe.getMessage());
      }
      catch (OutputTargetException re)
      {
        JOptionPane.showMessageDialog(
          this,
          "Error on processing this report: " + re.getMessage());
      }
      catch (ReportProcessingException re)
      {
        JOptionPane.showMessageDialog(
          this,
          "Error on processing this report: " + re.getMessage());
      }
    }
  }

  /**
   * Displays a printer page setup dialog, then updates the report pane with the new page size.
   */
  public void attemptPageSetup()
  {
    PrinterJob pj = PrinterJob.getPrinterJob();
    PageFormat pf = pj.pageDialog(reportPane.getOutputTarget().getPageFormat());
    reportPane.setPageFormat(pf);
    validate();
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
        JOptionPane.showMessageDialog(this, e);
      }
    }
  }

  /**
   * Method lastPage moves to the last page
   */
  public void lastPage()
  {
    reportPane.setPageNumber(reportPane.getNumberOfPages());
    validate();
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
      validate();
    }
  }

  /**
   * Method firstPage changes to the first page if not already on the first page
   *
   */
  public void firstPage()
  {
    if (reportPane.getPageNumber() != 1)
    {
      reportPane.setPageNumber(1);
      validate();
    }
  }

  /**
   * Decreases the page number.
   *
   * CHANGED FUNCTION
   */
  public void decreasePageNumber()
  {
    int pn = reportPane.getPageNumber();
    if (pn > 1)
    {
      reportPane.setPageNumber(pn - 1);
      validate();
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
    validate();
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
    validate();
  }

  /** !NEW FUNCTION */
  public void setZoomFactor(int index)
  {
    zoomIndex = index;
    reportPane.setZoomFactor(getZoomFactor());
    zoomSelect.setSelectedIndex(zoomIndex);
    validate();
  }

  protected void createDefaultActions()
  {
    saveAsAction = createDefaultSaveAsAction();
    pageSetupAction = createDefaultPageSetupAction();
    printAction = createDefaultPrintAction();
    aboutAction = createDefaultAboutAction();
    closeAction = createDefaultCloseAction();
  }

  protected Action createDefaultAboutAction()
  {
    return new DefaultAboutAction();
  }

  protected Action createDefaultSaveAsAction()
  {
    return new DefaultSaveAsAction();
  }

  protected Action createDefaultPageSetupAction()
  {
    return new DefaultPageSetupAction();
  }

  protected Action createDefaultPrintAction()
  {
    return new DefaultPrintAction();
  }

  protected Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  public JLabel getStatus ()
  {
    return statusHolder;
  }

  protected JPanel createStatusBar ()
  {
    JPanel statusPane = new JPanel ();
    statusPane.setLayout(new BorderLayout());
    statusPane.setBorder(BorderFactory.createLineBorder(UIManager.getDefaults().getColor("controlShadow")));
    statusHolder = new JLabel (" ");
    statusPane.setMinimumSize(statusHolder.getPreferredSize());
    statusPane.add (statusHolder, BorderLayout.WEST);
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

    JMenuItem saveAsItem = new JMenuItem(saveAsAction);
    KeyStroke accelerator = (KeyStroke) saveAsAction.getValue(Action.ACCELERATOR_KEY);
    saveAsItem.setAccelerator(accelerator);
    fileMenu.add(saveAsItem);

    fileMenu.addSeparator();

    JMenuItem setupItem = new JMenuItem(pageSetupAction);
    accelerator = (KeyStroke) pageSetupAction.getValue(Action.ACCELERATOR_KEY);
    if (accelerator != null)
      setupItem.setAccelerator(accelerator);
    fileMenu.add(setupItem);

    JMenuItem printItem = new JMenuItem(printAction);
    accelerator = (KeyStroke) printAction.getValue(Action.ACCELERATOR_KEY);
    if (accelerator != null)
      printItem.setAccelerator(accelerator);
    fileMenu.add(printItem);

    fileMenu.add(new JSeparator());

    JMenuItem closeItem = new JMenuItem(closeAction);
    accelerator = (KeyStroke) closeAction.getValue(Action.ACCELERATOR_KEY);
    if (accelerator != null)
      closeItem.setAccelerator(accelerator);
    fileMenu.add(closeItem);

    // then the help menu

    menuName = resources.getString("menu.help.name");
    JMenu helpMenu = new JMenu(menuName);
    mnemonic = (Character) resources.getObject("menu.help.mnemonic");
    helpMenu.setMnemonic(mnemonic.charValue());

    JMenuItem aboutItem = new JMenuItem(aboutAction);
    helpMenu.add(aboutItem);

    // finally, glue together the menu and return it

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    return menuBar;
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
    JButton sa = toolbar.add(saveAsAction);
    sa.setIcon((ImageIcon) saveAsAction.getValue("ICON24"));
    // reset Mnemonic
    sa.setMnemonic(0);
    sa = toolbar.add(printAction);
    sa.setIcon((ImageIcon) printAction.getValue("ICON24"));
    // reset Mnemonic
    sa.setMnemonic(0);

    ImageIcon icon3 = (ImageIcon) resources.getObject("action.back.icon");
    back = new JButton(icon3);
    back.setActionCommand("Back");
    back.addActionListener(this);
    toolbar.add(back);

    ImageIcon icon4 = (ImageIcon) resources.getObject("action.forward.icon");
    forward = new JButton(icon4);
    forward.setActionCommand("Forward");
    forward.addActionListener(this);
    toolbar.add(forward);
    toolbar.addSeparator();

    ImageIcon icon5 = (ImageIcon) resources.getObject("action.zoomIn.icon");
    zoomIn = new JButton(icon5);
    zoomIn.setActionCommand("ZoomIn");
    zoomIn.addActionListener(this);
    toolbar.add(zoomIn);

    ImageIcon icon6 = (ImageIcon) resources.getObject("action.zoomOut.icon");
    zoomOut = new JButton(icon6);
    zoomOut.setActionCommand("ZoomOut");
    zoomOut.addActionListener(this);
    toolbar.add(zoomOut);
    toolbar.addSeparator();

    toolbar.add(createZoomPane());
    toolbar.addSeparator();

    ImageIcon icon7 = (ImageIcon) resources.getObject("action.information.icon");
    toolbar.add(new JButton(icon7));

    return toolbar;
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
      getStatus().setText("Page " + reportPane.getPageNumber() + " of " + reportPane.getNumberOfPages());
      validate();
    }
    else if (property.equals(ReportPane.ERROR_PROPERTY))
    {
      if (reportPane.hasError())
      {
        Exception ex = reportPane.getError();
        getStatus().setText("Report generation prduced an error: " + ex.getMessage());
      }
    }
  }

  /**
   * Updates the states of all buttons to reflect the state of the assigned ReportPane.
   */
  public void validate()
  {
    int pn = reportPane.getPageNumber();
    int mp = reportPane.getNumberOfPages();

    if (pn < mp)
    {
      forward.setEnabled(true);
    }
    else
    {
      forward.setEnabled(false);
    }

    if (pn == 1)
    {
      back.setEnabled(false);
    }
    else
    {
      back.setEnabled(true);
    }

    if (zoomSelect.getSelectedIndex() == 0)
    {
      zoomOut.setEnabled(false);
    }
    else
    {
      zoomOut.setEnabled(true);
    }

    if (zoomSelect.getSelectedIndex() == (zoomFactors.length - 1))
    {
      zoomIn.setEnabled(false);
    }
    else
    {
      zoomIn.setEnabled(true);
    }

    super.validate();
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
  /**
   * @see KeyListener#keyPressed(KeyEvent)
   */
  public void keyPressed(KeyEvent ke)
  {
    if (ke.getKeyCode() == ke.VK_PAGE_DOWN)
    {
      increasePageNumber();
    }
    else if (ke.getKeyCode() == ke.VK_PAGE_UP)
    {
      decreasePageNumber();
    }
    else if (ke.getKeyCode() == ke.VK_HOME)
    {
      firstPage();
    }
    else if (ke.getKeyCode() == ke.VK_END)
    {
      lastPage();
    }
    else if (ke.getKeyChar() == '+')
    {
      increaseZoom();
    }
    else if (ke.getKeyChar() == '-')
    {
      decreaseZoom();
    }
  }

  /**
   * @see KeyListener#keyReleased(KeyEvent)
   */
  public void keyReleased(KeyEvent arg0)
  {
  }

  /**
   * @see KeyListener#keyTyped(KeyEvent)
   */
  public void keyTyped(KeyEvent arg0)
  {
  }
}