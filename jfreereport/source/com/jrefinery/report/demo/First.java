/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ----------
 * First.java
 * ----------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: First.java,v 1.1 2002/07/15 16:49:56 mungady Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.io.FileUtilities;
import com.jrefinery.ui.ApplicationFrame;
import com.jrefinery.ui.RefineryUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.table.TableModel;

/**
 * A demonstration application.
 * <P>
 * This demo is written up in the JFreeReport PDF Documentation.  Please notify David Gilbert
 * (david.gilbert@object-refinery.com) if you need to make changes to this file.
 * <P>
 * To run this demo, you need to have the Java Look and Feel Icons jar file on your classpath.
 * <P>
 * To do: fix hard coded path to XML report template.
 *
 */
public class First extends ApplicationFrame implements ActionListener {

    /** The data for the report. */
    protected TableModel data;

    /** The report (read from first.xml template). */
    protected JFreeReport report;

    /**
     * Constructs the demo application.
     *
     * @param title The frame title.
     */
    public First(String title) {
        super(title);
        setJMenuBar(createMenuBar());
        setContentPane(createContent());
    }

    /**
     * Creates a menu bar.
     *
     * @return The menu bar.
     */
    public JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem previewItem = new JMenuItem("Preview Report");
        previewItem.setActionCommand("PREVIEW");
        previewItem.addActionListener(this);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setActionCommand("EXIT");
        exitItem.addActionListener(this);

        fileMenu.add(previewItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        mb.add(fileMenu);
        return mb;
    }

    /**
     * Creates the content for the application frame.
     *
     * @return A panel containing the basic user interface.
     */
    public JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.data = readData();
        JTable table = new JTable(data);
        table.setDefaultRenderer(java.awt.Image.class, new ImageCellRenderer());
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane);
        return content;
    }

    /**
     * Creates a data set using Java icons.
     *
     * @return The report data.
     */
    private TableModel readData() {

        IconTableModel result = new IconTableModel();

        // find the file on the classpath...
        File f = FileUtilities.findFileOnClassPath("jlfgr-1_0.jar");
        try {
            ZipFile iconJar = new ZipFile(f);
            Enumeration e = iconJar.entries();
            while(e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry)e.nextElement();
                String fullName = ze.getName();
                if (fullName.endsWith(".gif")) {
                    String category = getCategory(fullName);
                    String name = getName(fullName);
                    Image image = getImage(iconJar, ze);
                    Long bytes = new Long(ze.getSize());
                    result.addIconEntry(name, category, image, bytes);
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

        return result;

    }

    /**
     * Handles action events.
     *
     * @param e The event.
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("PREVIEW")) {
            previewReport();
        }
        else if (command.equals("EXIT")) {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Displays a print preview screen for the sample report.
     */
    protected void previewReport() {

        if (this.report==null) {
            URL in  = getClass().getResource("/com/jrefinery/report/demo/first.xml");
            this.report = parseReport(in);
            this.report.setData(this.data);
        }

        if (this.report!=null) {
            PreviewFrame frame = new PreviewFrame(this.report);
            frame.setToolbarFloatable(true);
            frame.pack ();
            RefineryUtilities.positionFrameRandomly(frame);
            frame.setVisible(true);
            frame.requestFocus();
        }

    }

    /**
     * Reads the report from the first.xml report template.
     *
     * @param templateURL The template location.
     *
     * @return A report.
     */
    private JFreeReport parseReport(URL templateURL) {

        JFreeReport result = null;
        ReportGenerator generator = ReportGenerator.getInstance();
        try {
            result = generator.parseReport(templateURL);
        }
        catch (Exception e) {
            System.out.println(e.toString());

        }
        return result;

    }

    /**
     * Reads an icon from the zip file.
     *
     * @param file The zip file.
     * @param entry The zip entry.
     *
     * @return The image.
     */
    private Image getImage(ZipFile file, ZipEntry entry) {

        Image result = null;
        try {
            InputStream in = new BufferedInputStream(file.getInputStream(entry));
            byte[] bytes = new byte[(int)entry.getSize()];
            int count = in.read(bytes);
            ImageIcon temp = new ImageIcon(bytes);
            result = temp.getImage();
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        return result;

    }

    /**
     * For the category, use the subdirectory name.
     */
    private String getCategory(String fullName) {
        int start = fullName.indexOf("/")+1;
        int end = fullName.lastIndexOf("/");
        return fullName.substring(start, end);
    }

    /**
     * For the name, strip off the ".gif".
     */
    private String getName(String fullName) {
        int start = fullName.lastIndexOf("/")+1;
        int end = fullName.indexOf(".");
        return fullName.substring(start, end);
    }

    /**
     * Entry point for running the demo application...
     */
    public static void main(String[] args) {
        First frame = new First("First Report");
        frame.pack();
        frame.setVisible(true);
    }

}