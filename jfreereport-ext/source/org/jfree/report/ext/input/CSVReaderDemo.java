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
 * ------------------------
 * CSVReaderDemo.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Mimil;
 *
 * $Id$
 *
 * $Log$
 */

package org.jfree.report.ext.input;

import org.jfree.report.*;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionMenuItem;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;


public class CSVReaderDemo extends AbstractDemoFrame {

    private TableModel data;
    private boolean columnfirst = false;

    private JFreeReport report;
    String text = "R0C0; R0C1; R0C2;\n"
            + "R1C0; R1C1; R1C2;\n"
            + "R2C0; R2C1; R2C2;";
    String textColumn = "C0; C1; C2;\n";


    public CSVReaderDemo() {
        this.setJMenuBar(this.createMenuBar());
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Text:"));
        panel.add(new JTextArea(this.text));
        setContentPane(panel);
        this.setTitle("CSVReader Demos");
    }

    protected void attemptPreview() {
        this.report = new JFreeReport();

        this.report.setName("CSVReader API demos");
        this.report.setData(this.data);

        this.configurePageHeader(this.report.getPageHeader());
        this.configureItemBand(this.report.getItemBand());

        try {
            final PreviewFrame frame = new PreviewFrame(report);
            frame.getBase().setToolbarFloatable(true);
            frame.pack();
            RefineryUtilities.positionFrameRandomly(frame);
            frame.setVisible(true);
            frame.requestFocus();

        } catch (ReportProcessingException rpe) {
            showExceptionDialog("report.previewfailure", rpe);
        }
    }

    protected void attemptStringApiPreview() {
        this.data = new CSVReader(new BufferedReader(new StringReader(text))).getTableModel();
        this.attemptPreview();
    }

    protected void attemptStringDefApiPreview() {
        CSVReader csv = new CSVReader(new BufferedReader(new StringReader(textColumn + text)));
        csv.setColumnNameFirstLine(true);
        this.data = csv.getTableModel();
        this.attemptPreview();
    }

    protected void attemptFileApiPreview() {
        try {
            this.data = new CSVReader(getClass().getResource("/org/jfree/report/ext/input/file.csv").openStream()).getTableModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.attemptPreview();
    }

    private void configureItemBand(final ItemBand band) {
        final ElementStyleSheet ess = band.getBandDefaults();
        ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 10));

        TextFieldElementFactory facto = new TextFieldElementFactory();
        facto.setColor(Color.blue);
        facto.setNullString("-");

        for (int i = 0; i < this.data.getColumnCount(); i++) {
            facto.setFieldname(this.data.getColumnName(i));
            facto.setName(this.data.getColumnName(i));
            facto.setAbsolutePosition(new Point2D.Float(i * 70, 0));
            facto.setMinimumSize(new Dimension(70, 10));
            band.addElement(facto.createElement());
        }
    }

    private void configurePageHeader(final PageHeader header) {
        LabelElementFactory title = new LabelElementFactory();
        title.setText("CSVReader Demos by Mimil");
        title.setHorizontalAlignment(ElementAlignment.CENTER);
        title.setBold(Boolean.TRUE);
        title.setFontSize(new Integer(20));
        title.setColor(Color.black);
        title.setMinimumSize(new Dimension(-100, 70));
        title.setAbsolutePosition(new Point2D.Float(0, 0));
        title.setName("Title");

        header.addElement(title.createElement());

        if (this.columnfirst) {
            final ElementStyleSheet ess = header.getBandDefaults();
            ess.setFontDefinitionProperty(new FontDefinition("SansSerif", 10));

            LabelElementFactory lbl = new LabelElementFactory();
            lbl.setColor(Color.RED);

            for (int i = 0; i < this.data.getColumnCount(); i++) {
                lbl.setText(this.data.getColumnName(i));
                lbl.setName(this.data.getColumnName(i));
                lbl.setAbsolutePosition(new Point2D.Float(i * 70, 70));
                lbl.setMinimumSize(new Dimension(70, 10));
                header.addElement(lbl.createElement());
            }
        }
    }


    /**
     * Creates a menu bar.
     *
     * @return the menu bar.
     */
    public JMenuBar createMenuBar() {
        final JMenuBar mb = new JMenuBar();
        final JMenu fileMenu = new JMenu("Demos");


        final JMenuItem exitItem = new ActionMenuItem(getCloseAction());
        final JMenuItem api = new ActionMenuItem(new CSVReaderStringApiAction());
        final JMenuItem file = new ActionMenuItem(new CSVReaderFileApiAction());
        final JMenuItem autoApi = new ActionMenuItem(new CSVReaderStringAutoApiAction());
        final JMenuItem DefApi = new ActionMenuItem(new CSVReaderStringDefApiAction());

        fileMenu.add(api);
        fileMenu.add(autoApi);
        fileMenu.add(DefApi);
        fileMenu.add(file);
        fileMenu.addSeparator();

        fileMenu.add(exitItem);
        mb.add(fileMenu);
        return mb;
    }

    protected class CSVReaderStringAutoApiAction extends AbstractActionDowngrade {

        public CSVReaderStringAutoApiAction() {
            putValue(NAME, "String Preview (column names auto)");
        }

        public void actionPerformed(ActionEvent e) {
            columnfirst = true;
            attemptStringApiPreview();
        }
    }

    protected class CSVReaderStringDefApiAction extends AbstractActionDowngrade {

        public CSVReaderStringDefApiAction() {
            putValue(NAME, "String Preview (column names on first line)");
        }

        public void actionPerformed(ActionEvent e) {
            columnfirst = true;
            attemptStringDefApiPreview();
        }
    }

    protected class CSVReaderStringApiAction extends AbstractActionDowngrade {

        public CSVReaderStringApiAction() {
            putValue(NAME, "String Preview");
        }

        public void actionPerformed(ActionEvent e) {
            attemptStringApiPreview();
        }
    }

    protected class CSVReaderFileApiAction extends AbstractActionDowngrade {

        public CSVReaderFileApiAction() {
            putValue(NAME, "File Preview");
        }

        public void actionPerformed(ActionEvent e) {
            attemptFileApiPreview();
        }
    }


    public static void main(String args[]) {
        CSVReaderDemo demo = new CSVReaderDemo();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
