package org.jfree.report.dev.locales;

import java.awt.Component;
import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;

public class LocalesTableCellRenderer extends DefaultTableCellRenderer
{
  private Color selectedInheritedColor;
  private Color inheritedColor;

  public LocalesTableCellRenderer ()
  {
    inheritedColor = new Color(0xedcfcf);
    selectedInheritedColor = new Color (0xf0b8b8);
  }

  /**
   * Returns the default table cell renderer.
   *
   * @param table      the <code>JTable</code>
   * @param value      the value to assign to the cell at <code>[row, column]</code>
   * @param isSelected true if cell is selected
   * @param hasFocus   true if cell has focus
   * @param row        the row of the cell to render
   * @param column     the column of the cell to render
   * @return the default table cell renderer
   */
  public Component getTableCellRendererComponent (final JTable table,
                                                  final Object value,
                                                  final boolean isSelected,
                                                  final boolean hasFocus,
                                                  final int row, final int column)
  {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    final LocalesTableModel model = (LocalesTableModel) table.getModel();
    if (model.isInheritedValue(row))
    {
      if (isSelected)
      {
        setBackground(selectedInheritedColor);
      }
      else
      {
        setBackground(inheritedColor);
      }
    }
    else
    {
      if (isSelected)
      {
         setBackground(table.getSelectionBackground());
      }
      else
      {
         setBackground(table.getBackground());
      }
    }
    return this;
  }
}
