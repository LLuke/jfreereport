/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * -------------------
 * LGPLTextDemo.java
 * -------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: LGPLTextDemo.java,v 1.7 2003/05/02 12:39:35 taqua Exp $
 *
 * Changes
 * -------
 * 29-Nov-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.Log;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple JFreeReport demonstration.  The generated report contains
 * the complete text of the LGPL.
 *
 * @author Thomas Morgner
 */
public class LGPLTextDemo extends ApplicationFrame implements ActionListener
{

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public LGPLTextDemo(String title)
    {
        super(title);
        setJMenuBar(createMenuBar());
        setContentPane(createContent());
    }

    /**
     * Creates a menu bar.
     *
     * @return the menu bar.
     */
    private JMenuBar createMenuBar()
    {
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
     * @return a panel containing the basic user interface.
     */
    private JPanel createContent()
    {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane);
        return content;
    }

    /**
     * Handles action events.
     *
     * @param e  the event.
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals("PREVIEW"))
        {
            previewReport();
        }
        else if (command.equals("EXIT"))
        {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Displays a print preview screen for the sample report.
     */
    protected void previewReport()
    {
      try
      {
        URL in  = getClass().getResource("/com/jrefinery/report/demo/lgpl.xml");
        JFreeReport report = parseReport(in);
        if (report == null)
        {
          Log.error ("Report could not be parsed");
          return;
        }
        report.setData(new DefaultTableModel());
        PreviewFrame frame = new PreviewFrame(report);
        frame.getBase().setToolbarFloatable(true);
        frame.pack ();
        RefineryUtilities.positionFrameRandomly(frame);
        frame.setVisible(true);
        frame.requestFocus();
      }
      catch (ReportProcessingException pre)
      {
        Log.error ("Failed", pre);
      }
    }

    /**
     * Reads the report from the specified template file.
     *
     * @param templateURL  the template location.
     *
     * @return a report.
     */
    private JFreeReport parseReport(URL templateURL)
    {

        JFreeReport result = null;
        ReportGenerator generator = ReportGenerator.getInstance();
        try
        {
            result = generator.parseReport(templateURL);
        }
        catch (Exception e)
        {
            Log.error("Failed to parses", e);

        }
        return result;

    }

    /**
     * Entry point for running the demo application...
     *
     * @param args  ignored.
     */
    public static void main(String[] args)
    {
        LGPLTextDemo frame = new LGPLTextDemo("LGPL text Demo");
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

}