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
 * $Id$
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

    protected TableModel data;

    protected JFreeReport report;

    public First(String title) {
        super(title);
        setJMenuBar(createMenuBar());
        setContentPane(createContent());
    }

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

    public JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        //content.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
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
     */
    private TableModel readData() {

        IconTableModel result = new IconTableModel();

        // find the file on the classpath...
        File f = FileUtilities.findFileOnClassPath("jlfgr-1_0.jar");
        // File f = new File("/home/dgilbert/jars/jlfgr_1
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
                    System.out.println("File: "+ze+":"+category+":"+name+"("+bytes.toString()+")");

                }
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

        return result;

    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.toString());
        String command = e.getActionCommand();
        if (command.equals("PREVIEW")) {
            previewReport();
        }
    }

    protected void previewReport() {

        if (this.report==null) {
            this.report = parseReport("/home/dgilbert/jrefinery/working/jfreereport/source/com/jrefinery/report/demo/first.xml");
            this.report.setData(this.data);
        }

        if (this.report!=null) {
            PreviewFrame frame = new PreviewFrame(this.report);
            frame.pack ();
            RefineryUtilities.positionFrameRandomly(frame);
            frame.setVisible(true);
            frame.requestFocus();
        }

    }

    private JFreeReport parseReport(String xmlTemplateFileName) {

        JFreeReport result = null;
        ReportGenerator generator = ReportGenerator.getInstance();
        try {
            result = generator.parseReport(xmlTemplateFileName);
        }
        catch (Exception e) {
            System.out.println(e.toString());

        }
        return result;

    }

    private Image getImage(ZipFile file, ZipEntry entry) {

        Image result = null;
        try {
            InputStream in = new BufferedInputStream(file.getInputStream(entry));
            byte[] bytes = new byte[(int)entry.getSize()];
            int count = in.read(bytes);
            System.out.println("Bytes = "+count);
            ImageIcon temp = new ImageIcon(bytes);
            result = temp.getImage();
            //result = Toolkit.getDefaultToolkit().createImage(bytes);
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

    public static void main(String[] args) {
        First frame = new First("Icons");
        frame.pack();
        frame.setVisible(true);
    }

}