package org.jfree.report.demo.helper;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;

import org.jfree.ui.NumberCellRenderer;

/**
 * Creation-Date: 27.08.2005, 12:56:17
 *
 * @author: Thomas Morgner
 */
public abstract class AbstractDemoHandler implements InternalDemoHandler
{
  private DemoControler controler;

  public AbstractDemoHandler()
  {
  }


  protected JComponent createDefaultTable(final TableModel data) {
    final JTable table = new JTable(data);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    for (int columnIndex = 0; columnIndex < data.getColumnCount(); columnIndex++) {
        final TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(50);
        final Class c = data.getColumnClass(columnIndex);
        if (c.equals(Number.class)) {
            column.setCellRenderer(new NumberCellRenderer());
        }
    }

    return new JScrollPane
            (table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  public DemoControler getControler()
  {
    return controler;
  }

  public void setControler(final DemoControler controler)
  {
    this.controler = controler;
  }

  public PreviewHandler getPreviewHandler()
  {
    return new DefaultPreviewHandler(this);
  }
}
