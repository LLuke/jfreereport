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
 * CSVReader.java
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

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;


public class CSVReader {
    private BufferedReader reader;
    private String separator = ";";
    private CSVTableModel tableModel;
    private boolean columnNameFirst = false;

    public CSVReader(InputStream in) {
        if (in != null) {
            this.reader = new BufferedReader(new InputStreamReader(in));
            this.tableModel = new CSVTableModel();

        } else {
            throw new NullPointerException("The input stream must not be null");
        }
    }

    public CSVReader(String filename) {
        try {
            this.reader = new BufferedReader(new FileReader(filename));
            this.tableModel = new CSVTableModel();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CSVReader(BufferedReader r) {
        if (r != null) {
            this.reader = r;
            this.tableModel = new CSVTableModel();

        } else {
            throw new NullPointerException("The input stream must not be null");
        }
    }

    public void parse() {

        try {
            if (this.columnNameFirst == true) {   //read the fisrt line
                String first = this.reader.readLine();

                if (first != null) {
                    this.tableModel.columnNames = first.split(this.separator);
                }
            }

            String line;
            while ((line = this.reader.readLine()) != null) {

                String[] tbl = line.split(this.separator);

                this.tableModel.rowCount++;
                this.tableModel.setMaxColumnCount(tbl.length);
                this.tableModel.array.add(tbl);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public AbstractTableModel getTableModel() {
        this.parse();
        return tableModel;
    }

    public boolean isColumnNameFirstLine() {
        return columnNameFirst;
    }

    public void setColumnNameFirstLine(boolean columnNameFirst) {
        this.columnNameFirst = columnNameFirst;
    }

    public class CSVTableModel extends AbstractTableModel {

        protected String[] columnNames = null;
        protected int rowCount = 0;
        private int maxColumnCount = 0;
        protected ArrayList array;

        public CSVTableModel() {
            this.array = new ArrayList();
        }

        public int getColumnCount() {
            if (this.columnNames != null) {
                return columnNames.length;
            }

            return this.maxColumnCount;
        }

        public int getRowCount() {
            return this.rowCount;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Object[] line = (Object[]) this.array.get(rowIndex);

            if (line.length < columnIndex) {
                return null;
            } else {
                return line[columnIndex];
            }
        }

        public void setMaxColumnCount(int maxColumnCount) {
            if (this.maxColumnCount < maxColumnCount) {
                this.maxColumnCount = maxColumnCount;
            }
        }

        public String getColumnName(int column) {
            if (this.columnNames != null) {
                return this.columnNames[column];
            } else {    //todo: an exception if column > maxColumnCount
                return "COLUMN_" + column;
            }
        }
    }
}
