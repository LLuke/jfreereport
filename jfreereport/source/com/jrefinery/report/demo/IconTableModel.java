package com.jrefinery.report.demo;

import java.awt.Image;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class IconTableModel extends AbstractTableModel {

    protected List data;

    public IconTableModel() {
        this.data = new java.util.ArrayList();
    }

    public void addIconEntry(String name, String category, Image icon, Long size) {
        Object[] item = new Object[4];
        item[0] = name;
        item[1] = category;
        item[2] = icon;
        item[3] = size;
        data.add(item);
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int column) {
        Object[] item = (Object[])data.get(row);
        return item[column];
    }

    public Class getColumnClass(int column) {
        if (column==2) {
            return java.awt.Image.class;
        }
        else return Object.class;
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0 : return "Name";
            case 1 : return "Category";
            case 2 : return "Icon";
            case 3 : return "Size";
            default: return "Error";
        }
    }

}