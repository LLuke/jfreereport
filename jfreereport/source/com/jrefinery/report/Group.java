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
 * ----------
 * Group.java
 * ----------
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

package com.jrefinery.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.table.TableModel;

/**
 * A report group.  Reports can contain any number of groups.
 */
public class Group {

    /** The name of the group. */
    protected String name;

    /** The fields that define the group (can be empty). */
    protected Collection fields;

    /** The group header (optional). */
    protected GroupHeader header;

    /** The group footer (optional). */
    protected GroupFooter footer;

    /**
     * Constructs a group with no fields, and no header or footer.
     *
     * @param name The group name.
     */
    public Group(String name) {
        this(name, null, null, null);
    }

    /**
     * Constructs a group with the specified fields, and no header or footer.
     *
     * @param name The group name.
     * @param fields The field names.
     */
    public Group(String name, Collection fields) {
        this(name, fields, null, null);
    }

    /**
     * Constructs a group.
     *
     * @param name The group name.
     * @param fields The fields that define the group.
     * @param header The group header (null permitted).
     * @param footer The group footer (null permitted).
     */
    public Group(String name, Collection fields, GroupHeader header, GroupFooter footer) {

        this.name = name;
        this.header = header;
        this.footer = footer;
        if (fields!=null) {
            this.fields = fields;
        }
        else {
            this.fields = new ArrayList();
        }

    }

    /**
     * Returns the name of the group.
     *
     * @return The group name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the group header (possibly null).
     * <P>
     * The group header is a report band that contains elements that should be printed at the
     * start of a group.
     *
     * @return The group header.
     */
    public GroupHeader getHeader() {
        return header;
    }

    /**
     * Sets the header for the group (null permitted).
     *
     * @param header The header.
     */
    public void setHeader(GroupHeader header) {
        this.header = header;
    }

    /**
     * Returns the group footer (possibly null).
     *
     * @return The footer.
     */
    public GroupFooter getFooter() {
        return footer;
    }

    /**
     * Sets the footer for the group (null permitted).
     *
     * @param footer The footer.
     */
    public void setFooter(GroupFooter footer) {

        this.footer = footer;

    }

    /**
     * Adds a field to the group.  The field names must correspond to the column names in the
     * report's TableModel.
     *
     * @param name The field name.
     */
    public void addField(String name) {

        fields.add(name);

    }

    /**
     * Returns true if the specified item is the last item in the group, and false otherwise.
     * @param data The data.
     *
     * @param row The current item/row.
     * @return A flag indicating whether or not the current item is the last in its group.
     */
    public boolean lastItemInGroup(TableModel data, int row) {

        if (row==(data.getRowCount()-1)) 
        {
            return true;
        }
        else { // compare item and item+1, if any field differs, then item==last in group
            boolean last = false;
            Iterator iterator = fields.iterator();
            while (iterator.hasNext()) {
                String field = (String)iterator.next();
                int column = fieldNameToColumnIndex(data, field);
                Object item1 = data.getValueAt(row, column);
                Object item2 = data.getValueAt(row+1, column);
                if (!(item1.equals(item2))) {
                    last = true;
                }
            }
            return last;
        }

    }

    /**
     * Converts a field name to a TableModel column index, by matching the field name to the column
     * name.
     *
     * @param data The data.
     * @param name The field name (or column name).
     * @return The column index.
     */
    private int fieldNameToColumnIndex(TableModel data, String name) {

        int columns = data.getColumnCount();
        for (int c=0; c<columns; c++) {
            if (name.equals(data.getColumnName(c))) {
                return c;
            }
        }
        return -1;  // no field with that name

    }

}