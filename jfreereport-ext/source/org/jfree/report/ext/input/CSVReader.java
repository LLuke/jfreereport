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
 * $Id: CSVReader.java,v 1.1 2004/08/07 14:35:14 mimil Exp $
 *
 * $Log: CSVReader.java,v $
 * Revision 1.1  2004/08/07 14:35:14  mimil
 * Initial version
 *
 */

package org.jfree.report.ext.input;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;

/**
 * Creates a <code>TableModel</code> using a file formated in CSV for input.
 * The separation can be what ever you want (as it is an understandable regexp).
 * The default separator is a <code>;</code>.
 *
 * @author Mimil
 */
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

    /**
     * Parses the input and stores data in a TableModel.
     *
     * @see this.getTableModel()
     */
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

    /**
     * Returns the current separator used to parse the input.
     *
     * @return a regexp
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets the separator for parsing the input.
     * It can be a regexp as we use the function <code>String.split()</code>.
     * The default separator is a <code>;</code>.
     *
     * @param separator a regexp
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Creates the corrspondant TableModel of the input.
     *
     * @return the new TableModel
     */
    public AbstractTableModel getTableModel() {
        this.parse();
        return tableModel;
    }

    /**
     * Tells if the first line of the input was column names.
     *
     * @return boolean
     */
    public boolean isColumnNameFirstLine() {
        return columnNameFirst;
    }

    /**
     * Set if the first line of the input is column names or not.
     *
     * @param columnNameFirst   boolean
     */
    public void setColumnNameFirstLine(boolean columnNameFirst) {
        this.columnNameFirst = columnNameFirst;
    }

    /**
     * <code>TableModel</code> used by the <code>CSVReader</code> class.
     * It has a feature which generates the column name if it is not know.
     *
     * @see this.getColumnName()
     *
     * @author Mimil
     */
    public class CSVTableModel extends AbstractTableModel {

        protected String[] columnNames = null;
        protected int rowCount = 0;
        private int maxColumnCount = 0;
        protected ArrayList array;

        public CSVTableModel() {
            this.array = new ArrayList();
        }

        /**
         * Counts columns of this <code>TableModel</code>.
         *
         * @return the column count
         */
        public int getColumnCount() {
            if (this.columnNames != null) {
                return columnNames.length;
            }

            return this.maxColumnCount;
        }

        /**
         * Counts rows of this <code>TableModel</code>.
         *
         * @return the row count
         */
        public int getRowCount() {
            return this.rowCount;
        }

        /**
         * Gets the Object at specified row and column positions.
         *
         * @param rowIndex row index
         * @param columnIndex column index
         * @return The requested Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object[] line = (Object[]) this.array.get(rowIndex);

            if (line.length < columnIndex) {
                return null;
            } else {
                return line[columnIndex];
            }
        }

        /**
         * Sets the maximum column count if it is bigger than the current one.
         *
         * @param maxColumnCount
         */
        public void setMaxColumnCount(int maxColumnCount) {
            if (this.maxColumnCount < maxColumnCount) {
                this.maxColumnCount = maxColumnCount;
            }
        }

        /**
         * Return the column name at a specified position.
         *
         * @param column column index
         * @return the column name
         */
        public String getColumnName(int column) {
            if (this.columnNames != null) {
                return this.columnNames[column];
            } else {
                if(column >= this.maxColumnCount) {
                    throw new IllegalArgumentException("Column ("+column+") does not exist");
                } else {
                    return "COLUMN_" + column;
                }
            }
        }
    }
}
