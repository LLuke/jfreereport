package org.jfree.report.dev.printerspecs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.jfree.report.dev.printerspecs.fixedcolumntable.FixedColumnTable;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.ui.tabbedui.RootPanel;
import org.jfree.util.Log;

public class EpsonEncodingsRootEditor extends RootPanel
{
  private static final String FILE_EXTENSION = ".properties";
  private static final String FILE_DESCRIPTION = "Printer definition file";

  private class AddPrinterAction extends AbstractActionDowngrade
  {
    public AddPrinterAction ()
    {
      putValue(Action.NAME, "Add Printer ..");
      putValue(Action.ACCELERATOR_KEY,
              KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final String name = JOptionPane.showInputDialog
              (EpsonEncodingsRootEditor.this, "Enter internal printer name.");
      if (name == null)
      {
        return;
      }
      final ModifiablePrinterSpecification spec =
              new ModifiablePrinterSpecification(name, name);
      tableModel.addPrinter(spec);
    }
  }

  private class RemovePrinterAction
          extends AbstractActionDowngrade implements ListSelectionListener
  {
    public RemovePrinterAction ()
    {
      putValue(Action.NAME, "Remove Printer");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (table.getSelectedRow() == -1)
      {
        return;
      }
      tableModel.removePrinter(table.getDataTable().getSelectedColumn() +
              table.getFixedColumns());
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final ListSelectionEvent e)
    {
      if (table.getSelectedRow() == -1)
      {
        setEnabled(false);
      }
      else
      {
        setEnabled(true);
      }
    }
  }

  private class CopyPrinterAction
          extends AbstractActionDowngrade implements ListSelectionListener
  {
    public CopyPrinterAction ()
    {
      putValue(Action.NAME, "Copy Printer ..");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (table.getSelectedRow() == -1)
      {
        return;
      }

      final ModifiablePrinterSpecification original =
              tableModel.getPrinter(table.getSelectedRow());
      final String name = JOptionPane.showInputDialog
              (EpsonEncodingsRootEditor.this, "Enter internal printer name.");
      if (name == null)
      {
        return;
      }

      try
      {
        final ModifiablePrinterSpecification spec =
                (ModifiablePrinterSpecification) original.clone();
        spec.setName(name);
        spec.setDisplayName(name);
        tableModel.addPrinter(spec);
      }
      catch (CloneNotSupportedException e1)
      {
        e1.printStackTrace();
      }
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final ListSelectionEvent e)
    {
      if (table.getSelectedRow() == -1)
      {
        setEnabled(false);
      }
      else
      {
        setEnabled(true);
      }
    }
  }

  private class TableStructureListener implements TableModelListener
  {
    public TableStructureListener ()
    {
    }

    /**
     * This fine grain notification tells listeners the exact range of cells, rows, or
     * columns that changed.
     */
    public void tableChanged (final TableModelEvent e)
    {
      if (e.getType() == TableModelEvent.HEADER_ROW)
      {
        updateTableWidth(table.getDataTable());
      }
    }
  }

  private class LoadDefinitionAction extends AbstractFileSelectionAction
  {
    private File lastFile;

    public LoadDefinitionAction ()
    {
      super(EpsonEncodingsRootEditor.this);
      putValue(Action.NAME, "Load ..");
      putValue(Action.ACCELERATOR_KEY,
              KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

      final String file;
      if (isNinePinEditor)
      {
        file = PrinterEditorData.getInstance().getLruNinePinPrinterFile();
      }
      else
      {
        file = PrinterEditorData.getInstance().getLruTwentyfourPinPrinterFile();
      }
      if (file != null)
      {
        lastFile = new File(file);
      }
    }

    protected String getFileDescription ()
    {
      return FILE_DESCRIPTION;
    }

    protected String getFileExtension ()
    {
      return FILE_EXTENSION;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File selectedFile =
              performSelectFile(lastFile, JFileChooser.OPEN_DIALOG, false);
      if (selectedFile == null)
      {
        return;
      }
      try
      {
        final EditablePrinterSpecificationLoader loader =
                new EditablePrinterSpecificationLoader();
        loader.load(tableModel, selectedFile);
        if (isNinePinEditor)
        {
          PrinterEditorData.getInstance().setLruNinePinPrinterFile
                  (selectedFile.getAbsolutePath());
        }
        else
        {
          PrinterEditorData.getInstance().setLruTwentyfourPinPrinterFile
                  (selectedFile.getAbsolutePath());
        }
        lastFile = selectedFile;
      }
      catch (IOException e1)
      {
        Log.error ("Unable to load the specified file", e1);
      }
    }
  }

  private class SaveDefinitionAction extends AbstractFileSelectionAction
  {
    private File lastFile;

    public SaveDefinitionAction ()
    {
      super(EpsonEncodingsRootEditor.this);
      putValue(Action.NAME, "Save ..");
      putValue(Action.ACCELERATOR_KEY,
              KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

      final String file;
      if (isNinePinEditor)
      {
        file = PrinterEditorData.getInstance().getLruNinePinPrinterFile();
      }
      else
      {
        file = PrinterEditorData.getInstance().getLruTwentyfourPinPrinterFile();
      }
      if (file != null)
      {
        lastFile = new File(file);
      }
    }

    protected String getFileDescription ()
    {
      return FILE_DESCRIPTION;
    }

    protected String getFileExtension ()
    {
      return FILE_EXTENSION;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File selectedFile =
              performSelectFile(lastFile, JFileChooser.SAVE_DIALOG, false);
      if (selectedFile == null)
      {
        return;
      }

      try
      {
        final PrinterSpecificationWriter writer =
                new PrinterSpecificationWriter();
        writer.save(selectedFile, tableModel);
        if (isNinePinEditor)
        {
          PrinterEditorData.getInstance().setLruNinePinPrinterFile
                  (selectedFile.getAbsolutePath());
        }
        else
        {
          PrinterEditorData.getInstance().setLruTwentyfourPinPrinterFile
                  (selectedFile.getAbsolutePath());
        }
        lastFile = selectedFile;
      }
      catch (IOException e1)
      {
        Log.error ("Unable to save the specified file", e1);
      }
    }
  }

  private JMenu[] menu;
  private PrinterEncodingTableModel tableModel;
  private RemovePrinterAction removePrinterAction;
  private AddPrinterAction addPrinterAction;
  private CopyPrinterAction copyPrinterAction;
  private String tabName;
  private boolean isNinePinEditor;
  private FixedColumnTable table;
  private JToolBar toolbar;

  public EpsonEncodingsRootEditor (final boolean type)
  {
    this.isNinePinEditor = type;

    if (type == true)
    {
      this.tabName = "9 Pin Printers";
      this.tableModel = PrinterEditorData.getInstance().getNinePinPrinters();
    }
    else
    {
      this.tabName = "24 Pin Printers";
      this.tableModel = PrinterEditorData.getInstance().getTwentyfourPinPrinters();
    }

    this.removePrinterAction = new RemovePrinterAction();
    this.addPrinterAction = new AddPrinterAction();
    this.copyPrinterAction = new CopyPrinterAction();

    this.menu = new JMenu[1];
    this.menu[0] = new JMenu (tabName);
    this.menu[0].add (new ActionMenuItem (new LoadDefinitionAction()));
    this.menu[0].add (new ActionMenuItem (new SaveDefinitionAction()));
    this.menu[0].addSeparator();
    this.menu[0].add (new ActionMenuItem (addPrinterAction));
    this.menu[0].add (new ActionMenuItem (copyPrinterAction));
    this.menu[0].addSeparator();
    this.menu[0].add (new ActionMenuItem (removePrinterAction));

    this.tableModel.addTableModelListener(new TableStructureListener());
    this.table = new FixedColumnTable(tableModel, 1, 250);
    configureTable(table.getDataTable());
    configureTable(table.getFixedTable());
    updateTableWidth(table.getDataTable());
    table.getDataTable().setColumnSelectionAllowed(true);

    setLayout(new BorderLayout());

    final JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add (table, BorderLayout.CENTER);

    add(panel, BorderLayout.CENTER);

    toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(new ActionMenuItem (addPrinterAction));
    toolbar.add(new ActionMenuItem (copyPrinterAction));
    toolbar.addSeparator();
    toolbar.add(new LoadDefinitionAction());
    toolbar.add(new SaveDefinitionAction());
  }

  private void configureTable (final JTable table)
  {
    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getSelectionModel().addListSelectionListener(removePrinterAction);
    table.getSelectionModel().addListSelectionListener(copyPrinterAction);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.getTableHeader().setResizingAllowed(true);
    table.getTableHeader().setReorderingAllowed(false);
  }

  /**
   * Returns the editor name.
   *
   * @return The editor name.
   */
  public String getEditorName ()
  {
    return tabName;
  }

  /**
   * Returns the menus.
   *
   * @return The menus.
   */
  public JMenu[] getMenus ()
  {
    return menu;
  }

  /**
   * Returns the toolbar. This default implementation return null, to indicate that no
   * toolbar is used.
   *
   * @return The toolbar.
   */
  public JComponent getToolbar ()
  {
    return toolbar;
  }

  private void updateTableWidth (final JTable table)
  {
    for (int i = 0; i < table.getColumnCount(); i++)
    {
      final TableColumn column = table.getColumnModel().getColumn(i);
      column.setMinWidth(100);
    }
  }
}
