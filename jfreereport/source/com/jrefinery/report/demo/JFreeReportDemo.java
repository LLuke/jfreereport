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
 * --------------------
 * JFreeReportDemo.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JButton;
import com.jrefinery.io.FileUtilities;
import com.jrefinery.ui.JRefineryUtilities;
import com.jrefinery.ui.L1R2ButtonPanel;
import com.jrefinery.ui.about.AboutFrame;
import com.jrefinery.report.*;
import com.jrefinery.report.io.*;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.ItemCountFunction;
import com.jrefinery.report.function.ItemSumFunction;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.jrefinery.report.io.ReportDefinitionContentHandler;


/**
 * The main frame in the report demonstration application.
 */
public class JFreeReportDemo extends JFrame implements ActionListener, WindowListener {

    public static final String ABOUT_COMMAND = "ABOUT";
    public static final String PRINT_PREVIEW_COMMAND = "PRINT_PREVIEW";
    public static final String EXIT_COMMAND = "EXIT";

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

    /**
     * Constructs a frame containing sample reports created using the JFreeReport Class Library.
     */
    public JFreeReportDemo() {

        super("JFreeReport "+JFreeReport.VERSION+" Demo");

        // create a couple of sample data sets
        data1 = new SampleData1();
        data2 = new SampleData2();

        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            dispose();
            System.exit(0);
          }
        });

        // set up the menu
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        JPanel content = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane();

        tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tabbedPane.addTab("Example 1", JRefineryUtilities.createTablePanel(data1));
        tabbedPane.addTab("Example 2", JRefineryUtilities.createTablePanel(data2));

        content.add(tabbedPane);

        L1R2ButtonPanel buttons = new L1R2ButtonPanel("Help", "Preview", "Close");

        JButton helpButton = buttons.getLeftButton();
        helpButton.setActionCommand("Help");
        helpButton.addActionListener(this);

        JButton previewButton = buttons.getRightButton1();
        previewButton.setActionCommand(PRINT_PREVIEW_COMMAND);
        previewButton.addActionListener(this);

        JButton closeButton = buttons.getRightButton2();
        closeButton.setActionCommand(EXIT_COMMAND);
        closeButton.addActionListener(this);

        buttons.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        content.add(buttons, BorderLayout.SOUTH);
        setContentPane(content);

    }

    /**
     * Handles menu selections by passing control to an appropriate method.
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
//        if (command.equals("Help")) {
//            attemptHelp();
//        }
        //else
        if (command.equals(PRINT_PREVIEW_COMMAND)) {
            attemptPreview();
        }
        else if (command.equals(EXIT_COMMAND)) {
            attemptExit();
        }
        else if (command.equals(ABOUT_COMMAND)) {
            displayAbout();
        }

    }

    /**
     * Handles a request to preview a report.  First determines which data set is visible, then
     * calls the appropriate preview method.
     */
    public void attemptPreview() {

        int index = tabbedPane.getSelectedIndex();
        if (index==0) {
            preview1();
        }
        else preview2();

    }

    /**
     * Displays a preview frame for report one.  If the preview frame already exists, it is brought
     * to the front.
     */
    public void preview1() {

        if (frame1==null) {
            //try {
                //File file = Files.findFileOnClassPath("reports.zip");
                //ZipFile zip = new ZipFile(file);
                //ZipEntry entry = zip.getEntry("SampleReport1.txt");
                //report1=readReport(zip.getInputStream(entry));

                //report1 = createReport1();

                File file1 = FileUtilities.findFileOnClassPath("report1.xml");
                if (file1 == null)
                {
                  JOptionPane.showMessageDialog(this, "ReportDefinition report1.xml not found on classpath");
                  return;
                }
                ReportGenerator gen = ReportGenerator.getInstance();
                
                try
                {
                  report1 = gen.parseReport (file1);
                }
                catch (Exception ioe)
                {
                  JOptionPane.showMessageDialog(this, ioe.getMessage(), "Error: " + ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                  return;
                }

                ItemBand band = report1.getItemBand();
                report1.setData(data1);

                frame1 = new PreviewFrame(report1, 640, 400);
                frame1.addWindowListener(this);
                frame1.pack();
                JRefineryUtilities.positionFrameRandomly(frame1);
                frame1.show();

        }
        else {
            frame1.requestFocus();
        }

    }


    /**
     * Displays a preview frame for report two.  If the preview frame already exists, it is brought
     * to the front.
     */
    public void preview2() {

        if (frame2==null) {

                File file1 = FileUtilities.findFileOnClassPath("report2.xml");
                if (file1 == null)
                {
                  JOptionPane.showMessageDialog(this, "ReportDefinition report1.xml not found on classpath");
                  return;
                }
                ReportGenerator gen = ReportGenerator.getInstance();
                
                try
                {
                  report2 = gen.parseReport (file1);
                }
                catch (Exception ioe)
                {
                  JOptionPane.showMessageDialog(this, ioe.getMessage(), "Error: " + ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                  return;
                }


                report2.setData(data2);
                frame2 = new PreviewFrame(report2, 640, 400);
                frame2.addWindowListener(this);
                frame2.pack();
                JRefineryUtilities.positionFrameRandomly(frame2);
                frame2.show();
        }
        else {
            frame2.requestFocus();
        }

    }

//    /**
//     * Displays a dialog explaining that help information has not yet been written.
//     */
//    public void attemptHelp() {
//
//       JOptionPane.showMessageDialog(null,
//                                     "No help has been implemented!",
//                                     "Important Message...",
//                                     JOptionPane.INFORMATION_MESSAGE);
//
//    }

    /**
     * Returns the preferred size of the frame.
     */
    public Dimension getPreferredSize() {
        return new Dimension(440, 300);
    }

    /**
     * Exits the application, but only if the user agrees.
     */
    private void attemptExit() {

       int result = JOptionPane.showConfirmDialog(this,
                                                  "Are you sure you want to exit?",
                                                  "Confirmation...",
                                                  JOptionPane.YES_NO_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE);
        if (result==JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }

    }


    /**
     * Displays information about the application.
     */
    private void displayAbout() {

        if (aboutFrame==null) {

            aboutFrame = new AboutFrame("About...",
                                        JFreeReport.NAME,
                                        "Version "+JFreeReport.VERSION,
                                        JFreeReport.INFO,
                                        JFreeReport.COPYRIGHT,
                                        JFreeReport.LICENCE,
                                        JFreeReport.CONTRIBUTORS,
                                        JFreeReport.LIBRARIES);

            aboutFrame.pack();
            JRefineryUtilities.centerFrameOnScreen(aboutFrame);
        }
        aboutFrame.setVisible(true);
        aboutFrame.requestFocus();

    }

    /**
     * Creates and returns a menu-bar for the frame.
     */
    private JMenuBar createMenuBar() {

        // create the menus
        JMenuBar menuBar = new JMenuBar();

        // first the file menu
        JMenu fileMenu = new JMenu("File", true);
        fileMenu.setMnemonic('F');
        JMenuItem printItem = new JMenuItem("Preview Report...", 'P');
        printItem.setActionCommand(PRINT_PREVIEW_COMMAND);
        printItem.addActionListener(this);
        fileMenu.add(printItem);

        fileMenu.add(new JSeparator());

        JMenuItem exitItem = new JMenuItem("Exit", 'x');
        exitItem.setActionCommand(EXIT_COMMAND);
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        // then the help menu
        JMenu helpMenu = new JMenu("Help");
//        helpMenu.setMnemonic('H');
//
//        JMenuItem helpItem = new JMenuItem("Help...", 'H');
//        helpItem.setActionCommand("Help");
//        helpItem.addActionListener(this);
//        helpMenu.add(helpItem);
//
//        helpMenu.addSeparator();

        JMenuItem aboutItem = new JMenuItem("About...", 'A');
        aboutItem.setActionCommand(ABOUT_COMMAND);
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        // finally, glue together the menu and return it
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;

    }

    /**
     * Clears the reference to the print preview frames when they are closed.
     */
    public void windowClosed(WindowEvent e) {

        if (e.getWindow()==this.frame1) {
            frame1=null;
        }
        else if (e.getWindow()==this.frame2) {
            frame2=null;
        }
        else if (e.getWindow()==this.infoFrame) {
            infoFrame=null;
        }
        else if (e.getWindow()==this.aboutFrame) {
            aboutFrame=null;
        }

    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowClosing(WindowEvent e) {
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     */
    public void windowOpened(WindowEvent e) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The starting point for the demonstration application.
     */
    public static void main(String[] args) {
        JFreeReportDemo f = new JFreeReportDemo();
        try
        {
        f.pack();
        JRefineryUtilities.centerFrameOnScreen(f);
        f.setVisible(true);
//        f.preview2();
        }
        finally
        {
//         System.exit (0);
        }
    }

}