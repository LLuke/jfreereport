package com.jrefinery.report.demo;

import java.awt.Component;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A table cell renderer that centers information in each cell.
 */
public class ImageCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    protected ImageIcon icon = new ImageIcon();

    /**
     * Default constructor - builds a renderer that right justifies the contents of a table cell.
     */
    public ImageCellRenderer() {
        super();
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setIcon(icon);
    }

    /**
     * Returns itself as the renderer. Supports the TableCellRenderer interface.
     * @param table The table;
     * @param value The data to be rendered;
     * @param isSelected A boolean that indicates whether or not the cell is selected;
     * @param hasFocus A boolean that indicates whether or not the cell has the focus;
     * @param row The (zero-based) row index;
     * @param column The (zero-based) column index;
     * @return The component that can render the contents of the cell;
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                 boolean hasFocus, int row, int column) {

        setFont(null);
        icon.setImage((Image)value);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        }
        else {
            setBackground(null);
        }
        return this;
    }

}
