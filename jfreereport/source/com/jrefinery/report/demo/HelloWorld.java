/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ---------------
 * HelloWorld.java
 * ---------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: HelloWorld.java,v 1.5 2003/02/25 17:20:39 mungady Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.util.Log;

/**
 * A very simple JFreeReport demo.  The purpose of this demo is to illustrate the basic steps
 * required to connect a report definition with some data and display a report preview on-screen.
 *
 * In this example, the report definition is created in code.  It is also possible to read a
 * report definition from an XML file...that is demonstrated elsewhere.
 *
 * @author David Gilbert
 */
public class HelloWorld
{
    /**
     * Creates and displays a simple report.
     */
    public HelloWorld()
    {
        
        TableModel data = createData();
        JFreeReport report = createReportDefinition();
        report.setData(data);
        try
        {
            PreviewFrame preview = new PreviewFrame(report);
            preview.pack();
            preview.setVisible(true);
        }
        catch (ReportProcessingException e)
        {
            Log.error("Failed to generate report ", e);
        }

    }

    /**
     * Creates a small dataset to use in a report.  JFreeReport always reads data from a
     * <code>TableModel</code> instance.
     *
     * @return a dataset.
     */
    private TableModel createData()
    {

        Object[] columnNames = new String[] { "Column1", "Column2" };
        DefaultTableModel result = new DefaultTableModel(columnNames, 1);
        result.setValueAt("Hello", 0, 0);
        result.setValueAt("World!", 0, 1);
        return result;

    }

    /**
     * Creates a report definition.
     *
     * @return a report definition.
     */
    private JFreeReport createReportDefinition()
    {

        JFreeReport report = new JFreeReport();
        report.setName("A Very Simple Report");

        TextElement t1 = ItemFactory.createStringElement(
            "T1",
            new Rectangle2D.Double(0.0, 0.0, 150.0, 20.0),
            Color.black,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null, // font
            "-",  // null string
            "Column1"
        );

        report.getItemBand().addElement(t1);

        TextElement t2 = ItemFactory.createStringElement(
            "T2",
            new Rectangle2D.Double(200.0, 0.0, 150.0, 20.0),
            Color.black,
            ElementAlignment.LEFT.getOldAlignment(),
            ElementAlignment.MIDDLE.getOldAlignment(),
            null, // font
            "-",  // null string
            "Column2"
        );

        report.getItemBand().addElement(t2);
        return report;

    }

    /**
     * The starting point for the "Hello World" demo application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args)
    {
        HelloWorld app = new HelloWorld();
    }

}