/* =============================================================
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
 * $Id: PreviewFrame.java,v 1.1.1.1 2002/04/25 17:02:22 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 24-Apr-2002 : Corrected the structure to better interact with ReportPane. Uses PropertyChange
 *               Events to pass information from the panel. Zoom and Page-Buttons maintain
 *               their state according to the informations from the ReportPane.
 *               SaveToPDF appends a ".pdf" extension to the filename, if not given.
 */

package com.jrefinery.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JSeparator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import com.jrefinery.report.action.SaveAsAction;
import com.jrefinery.report.action.PageSetupAction;
import com.jrefinery.report.action.PrintAction;
import com.jrefinery.report.action.CloseAction;
import com.lowagie.text.Rectangle;
import com.jrefinery.layout.CenterLayout;
import com.jrefinery.ui.ExtensionFileFilter;
import com.jrefinery.report.pdf.PDFOutputTarget;

/**
 * A standard print preview frame for any JFreeReport.  Allows the user to page back and forward
 * through the report, zoom in and out, and send the output to the printer.
 * <P>
 * You can also save the report in PDF format (thanks to the iText library).
 */
public class PreviewFrame extends JFrame implements ActionListener, PropertyChangeListener {

    protected Action saveAsAction;
    protected Action pageSetupAction;
    protected Action printAction;
    protected Action closeAction;

    /** Command for saving the report. */
    protected static final String SAVE_AS_COMMAND = "SAVE_AS";

    /** Comand for page setup. */
    protected static final String PAGE_SETUP_COMMAND = "PAGE_SETUP";

    /** Comand for printing the report. */
    protected static final String PRINT_COMMAND = "PRINT";

    /** Command for closing the preview frame. */
    protected static final String CLOSE_COMMAND = "CLOSE";

    /** The available zoom factors. */
    private static final double[] zoomFactors = {0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 4.0};

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
    protected int zoomIndex;

    /** The pane that displays the report within the frame. */
    protected ReportPane reportPane;

    /** The preferred size of the frame. */
    protected Dimension preferredSize;

    /** Locale-specific resources. */
    protected ResourceBundle resources;

    /**
     * Constructs a PreviewFrame that displays the specified report, and has the specified width
     * and height (to begin with).
     *
     * @param report The report to be displayed.
     * @param width The width of the frame.
     * @param height The height of the frame.
     */
    public PreviewFrame(JFreeReport report, int width, int height) {

        // get a locale-specific resource bundle...
        String baseResourceClass = "com.jrefinery.report.resources.JFreeReportResources";
        this.resources = ResourceBundle.getBundle(baseResourceClass);

        String title = this.resources.getString("preview-frame.title");
        this.setTitle(title);
        this.zoomIndex = DEFAULT_ZOOM_INDEX;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });


        this.createActions(resources);

        // set up the menu
        JMenuBar menuBar = createMenuBar(this.resources);
        setJMenuBar(menuBar);

        // set up the content with a toolbar and a report pane
        JPanel content = new JPanel(new BorderLayout());
        JToolBar toolbar = createToolBar(this.resources);
        content.add(toolbar, BorderLayout.NORTH);

        JPanel reportPaneHolder = new JPanel(new CenterLayout());
        reportPaneHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        pf = pj.validatePage(pf);

        reportPane = new ReportPane(report, new G2OutputTarget(null, pf));
        reportPane.addPropertyChangeListener (ReportPane.PAGECOUNT_PROPERTY, this);
        reportPane.addPropertyChangeListener (ReportPane.PAGENUMBER_PROPERTY, this);
        reportPaneHolder.add(reportPane);

        JScrollPane s1 = new JScrollPane(reportPaneHolder);
        content.add(s1);
        setContentPane(content);
        preferredSize = new Dimension(width, height);

    }

    /**
     * Returns the current zoom factor.
     * @return The current zoom factor.
     */
    double zoomFactor() {
        return zoomFactors[zoomIndex];
    }

    /**
     * Handles user actions within the frame (menu selections and mouse clicks).
     * @param event The action event.
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command.equals("Back")) {
            decreasePageNumber();
        }
        else if (command.equals("Forward")) {
            increasePageNumber();
        }
        else if (command.equals("ZoomIn")) {
            increaseZoom();
        }
        else if (command.equals("ZoomOut")) {
            decreaseZoom();
        }
        else if (command.equals("ZoomSelect"))
        {
          setZoomFactor (zoomSelect.getSelectedIndex ());
        }

    }

    /**
     * Saves the file to PDF, reporting any exceptions to System.err.
     */
    public void handleSaveAs() {

        try {
            doSaveAs();
        }
        catch (IOException e) {
            System.err.print(e.getMessage());
        }

    }

    /**
     * Presents a "Save As" dialog to the user, enabling him/her to save the report in PDF format.
     */
    public void doSaveAs() throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        ExtensionFileFilter filter = new ExtensionFileFilter("PDF Documents", ".pdf");
        fileChooser.addChoosableFileFilter(filter);

        int option = fileChooser.showSaveDialog(this);
        if (option==JFileChooser.APPROVE_OPTION) {

            PrinterJob pj = PrinterJob.getPrinterJob();
            PageFormat pf = pj.defaultPage();
            pf = pj.validatePage(pf);
            File selFile = fileChooser.getSelectedFile();
            String selFileName = selFile.getAbsolutePath();

            // Test if ends of pdf
            if (selFileName.toUpperCase().endsWith(".PDF") == false)
            {
              selFileName = selFileName + ".pdf";
            }
            System.out.println ("FileName : " + selFileName);
            OutputStream out = new FileOutputStream(new File (selFileName));
            PDFOutputTarget target = new PDFOutputTarget(out, pf, true);
            target.open("Title", "Author");
            this.reportPane.getReport().processReport(target, true);
            target.close();

        }

    }

    /**
     * Displays a printer page setup dialog, then updates the report pane with the new page size.
     */
    public void attemptPageSetup() {

        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.pageDialog(reportPane.getOutputTarget().getPageFormat());
        reportPane.setPageFormat(pf);
        validate();

    }

    /**
     * Prints the report.
     */
    public void attemptPrint() {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.validatePage(reportPane.getOutputTarget().getPageFormat());
        pj.setPrintable(this.reportPane, reportPane.getOutputTarget().getPageFormat());
        if (pj.printDialog()) {
            try {
                pj.print();
            }
            catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }

    }

    /**
     *Increases the page number.
     *
     * CHANGED FUNCTION
     */
    public void increasePageNumber()
    {
      int pn = reportPane.getPageNumber();
      int mp = reportPane.getCurrentPageCount ();

      if (pn < mp)
      {
        reportPane.setPageNumber(reportPane.getPageNumber()+1);
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

    /** Increases the zoom factor for the report pane (unless it is already at maximum zoom). */
    public void increaseZoom() {
      if (zoomIndex<zoomFactors.length-1) {
        zoomIndex++;
      }

      zoomSelect.setSelectedIndex (zoomIndex);

      reportPane.setZoomFactor(zoomFactors[zoomIndex]);
      validate();
    }

    /** Decreases the zoom factor for the report pane (unless it is already at the minimum zoom). */
    public void decreaseZoom() {
      if (zoomIndex>0)  {
        zoomIndex--;
      }

      zoomSelect.setSelectedIndex (zoomIndex);

      reportPane.setZoomFactor(zoomFactors[zoomIndex]);
      validate();
    }


    /** !NEW FUNCTION */
    public void setZoomFactor (int index)
    {
      zoomIndex = index;
      reportPane.setZoomFactor(zoomFactors[zoomIndex]);
      zoomSelect.setSelectedIndex (zoomIndex);
      validate();
    }


    /**
     * Returns the preferred size of the report preview frame - this size is set in the
     * constructor.
     * @return The preferred size of the frame.
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    protected void createActions(ResourceBundle resources) {

        this.saveAsAction = new SaveAsAction(this, resources);
        this.pageSetupAction = new PageSetupAction(this, resources);
        this.printAction = new PrintAction(this, resources);
        this.closeAction = new CloseAction(this, resources);

    }

    /**
     * Creates and returns a menu-bar for the frame.
     *
     * @param resources A resource bundle containing localised resources for the menu.
     * @return A ready-made JMenuBar.
     */
    private JMenuBar createMenuBar(ResourceBundle resources) {

        // create the menus
        JMenuBar menuBar = new JMenuBar();

        // first the file menu
        String menuName = resources.getString("menu.file.name");
        JMenu fileMenu = new JMenu(menuName);
        Character mnemonic = (Character)resources.getObject("menu.file.mnemonic");
        fileMenu.setMnemonic(mnemonic.charValue());

        JMenuItem saveAsItem = new JMenuItem(saveAsAction);
        KeyStroke accelerator = (KeyStroke)saveAsAction.getValue(Action.ACCELERATOR_KEY);
        saveAsItem.setAccelerator(accelerator);
        fileMenu.add(saveAsItem);

        fileMenu.addSeparator();

        JMenuItem setupItem = new JMenuItem(pageSetupAction);
        accelerator = (KeyStroke)pageSetupAction.getValue(Action.ACCELERATOR_KEY);
        if (accelerator!=null) setupItem.setAccelerator(accelerator);
        fileMenu.add(setupItem);

        JMenuItem printItem = new JMenuItem(printAction);
        accelerator = (KeyStroke)printAction.getValue(Action.ACCELERATOR_KEY);
        if (accelerator!=null) printItem.setAccelerator(accelerator);
        fileMenu.add(printItem);

        fileMenu.add(new JSeparator());

        JMenuItem closeItem = new JMenuItem(closeAction);
        accelerator = (KeyStroke)closeAction.getValue(Action.ACCELERATOR_KEY);
        if (accelerator!=null) closeItem.setAccelerator(accelerator);
        fileMenu.add(closeItem);

        // then the help menu
        menuName = resources.getString("menu.help.name");
        JMenu helpMenu = new JMenu(menuName);
        mnemonic = (Character)resources.getObject("menu.help.mnemonic");
        helpMenu.setMnemonic(mnemonic.charValue());

        JMenuItem aboutItem = new JMenuItem("About...", 'A');
        aboutItem.setActionCommand("About");
        aboutItem.addActionListener(this);
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
     * @return A ready-made JToolBar.
     * CHANGED FUNCTION
     */
    private JToolBar createToolBar(ResourceBundle resources) {

        JToolBar toolbar = new JToolBar();

        JButton saveAs = new JButton(saveAsAction);
        saveAs.setText(null);
        ImageIcon icon = (ImageIcon)saveAsAction.getValue("ICON24");
        saveAs.setIcon(icon);
        saveAs.setActionCommand(SAVE_AS_COMMAND);
        saveAs.addActionListener(this);
        toolbar.add(saveAs);
        toolbar.addSeparator();

        JButton print = new JButton(printAction);
        print.setText(null);
        icon = (ImageIcon)printAction.getValue("ICON24");
        print.setIcon(icon);
        print.setActionCommand(PRINT_COMMAND);
        print.addActionListener(this);
        toolbar.add(print);
        toolbar.addSeparator();

        ImageIcon icon3 = secureResourceLoad ("Back24.gif");
        back = new JButton(icon3);
        back.setActionCommand("Back");
        back.addActionListener(this);
        toolbar.add(back);

        ImageIcon icon4 = secureResourceLoad ("Forward24.gif");
        forward = new JButton(icon4);
        forward.setActionCommand("Forward");
        forward.addActionListener(this);
        toolbar.add(forward);
        toolbar.addSeparator();

        ImageIcon icon5 = secureResourceLoad ("ZoomIn24.gif");
        zoomIn = new JButton(icon5);
        zoomIn.setActionCommand("ZoomIn");
        zoomIn.addActionListener(this);
        toolbar.add(zoomIn);

        ImageIcon icon6 = secureResourceLoad ("ZoomOut24.gif");
        zoomOut = new JButton(icon6);
        zoomOut.setActionCommand("ZoomOut");
        zoomOut.addActionListener(this);
        toolbar.add(zoomOut);
        toolbar.addSeparator();

        toolbar.add (createZoomPane ());
        toolbar.addSeparator();

        ImageIcon icon7 = secureResourceLoad ("Information24.gif");
        toolbar.add(new JButton(icon7));

        return toolbar;

    }


  /** NEW FUNCTION */
  private JComponent createZoomPane ()
  {
    DefaultComboBoxModel model = new DefaultComboBoxModel ();
    for (int i = 0; i < zoomFactors.length; i++)
    {
      model.addElement (new Double (zoomFactors [i]));
    }
    zoomSelect = new JComboBox (model);
    zoomSelect.setActionCommand("ZoomSelect");
    zoomSelect.setSelectedIndex (DEFAULT_ZOOM_INDEX);
    zoomSelect.addActionListener(this);

    JPanel zoomPane = new JPanel ();
    zoomPane.setLayout (new BorderLayout (0, 0));
    zoomPane.add (zoomSelect, BorderLayout.WEST);

    return zoomPane;
  }

  /** NEW FUNCTION */
  public void propertyChange (PropertyChangeEvent event)
  {
    Object source = event.getSource ();
    String property = event.getPropertyName ();

    if (property.equals (ReportPane.PAGENUMBER_PROPERTY) ||
        property.equals (ReportPane.PAGECOUNT_PROPERTY))
    {
        validate ();
    }
    else
    if (property.equals (ReportPane.ERROR_PROPERTY))
    {
      if (reportPane.hasError ())
      {
        Exception ex = reportPane.getError ();
        JOptionPane.showMessageDialog (this,
          "Error" + ex.getMessage (),
          "Report has an error",
          JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /** NEW FUNCTION */
  public void validate ()
  {
        int pn = reportPane.getPageNumber();
        int mp = reportPane.getCurrentPageCount ();

        if (pn < mp)
        {
                forward.setEnabled (true);
        }
        else
        {
                forward.setEnabled (false);
        }

        if (pn == 1)
        {
                back.setEnabled (false);
        }
        else
        {
                back.setEnabled (true);
        }

    if (zoomSelect.getSelectedIndex () == 0)
    {
      zoomOut.setEnabled (false);
    }
    else
    {
      zoomOut.setEnabled (true);
    }

    if (zoomSelect.getSelectedIndex () == (zoomFactors.length - 1))
    {
      zoomIn.setEnabled (false);
    }
    else
    {
      zoomIn.setEnabled (true);
    }

    super.validate ();
  }

  /** NEW FUNCTION */
  public static ImageIcon secureResourceLoad (String filename)
  {

    URL in = ClassLoader.getSystemResource(filename);
    if (in == null)
    {
      System.out.println ("File " + filename + " is not on classpath");

      System.out.println (System.getProperty ("java.class.path"));

      return new ImageIcon ();
    }
    return new ImageIcon (in);
  }

}
