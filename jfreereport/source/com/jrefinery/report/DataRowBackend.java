/**
 * =============================================================
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
 * -----------
 * DataRowBackend.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * ------------------------------
 * 27.07.2002 : Inital version
 */
package com.jrefinery.report;

import javax.swing.table.TableModel;
import java.util.Hashtable;

/**
 * The DataRow-Backend maintains the state of a datarow. Whenever a reportstate changes
 * the backend of the datarow is updated and then reconnected with the DataRowConnector.
 */
public class DataRowBackend implements Cloneable
{
  private static class DataRowPreview extends DataRowBackend
  {
    private DataRowBackend db;

    public DataRowPreview (DataRowBackend db)
    {
      this.db = db;
    }

    public boolean isPreviewMode ()
    {
      return true;
    }

    public int getCurrentRow ()
    {
      return db.getCurrentRow () + 1;
    }

    public void setCurrentRow (int currentRow)
    {
      throw new IllegalStateException ("This is a preview, not changable");
    }

    public void setFunctions (FunctionCollection functions)
    {
      throw new IllegalStateException ("This is a preview, not changable");
    }

    public void setTablemodel (TableModel tablemodel)
    {
      throw new IllegalStateException ("This is a preview, not changable");
    }

    /**
     * looks up the position of the column with the name <code>name</code>.
     * returns the position of the column or -1 if no columns could be retrieved.
     */
    public int findColumn (String name)
    {
      return db.findColumn (name);
    }

    public int getColumnCount ()
    {
      return db.getColumnCount ();
    }

    public String getColumnName (int col)
    {
      return db.getColumnName (col);
    }

    public FunctionCollection getFunctions ()
    {
      return db.getFunctions ();
    }

    public TableModel getTablemodel ()
    {
      return db.getTablemodel ();
    }

    /**
     * Create a preview backend. Such datarows will have no access to functions (all functions
     * will return null).
     */
    public DataRowBackend previewNextRow ()
    {
      throw new IllegalStateException ("Is already a preview version!");
    }
  }

  private Hashtable colcache;
  private DataRowBackend preview;

  // set by the report state
  private FunctionCollection functions;
  // set by the report state
  private TableModel tablemodel;
  // set by the report state
  private int currentRow;

  public DataRowBackend ()
  {
    colcache = new Hashtable ();
    currentRow = -1;
  }

  public FunctionCollection getFunctions ()
  {
    return functions;
  }

  public TableModel getTablemodel ()
  {
    return tablemodel;
  }

  public int getCurrentRow ()
  {
    return currentRow;
  }

  public void setCurrentRow (int currentRow)
  {
    this.currentRow = currentRow;
  }

  public void setFunctions (FunctionCollection functions)
  {
    this.functions = functions;
  }

  public void setTablemodel (TableModel tablemodel)
  {
    this.tablemodel = tablemodel;
  }

  public Object get (int col)
  {
    if (col > getColumnCount ())
      throw new IndexOutOfBoundsException ("requested " + col + " , have " + getColumnCount ());

    if (col < getTablemodel ().getColumnCount ())
    {
      // Handle Pos == BEFORE_FIRST_ROW

      if (getCurrentRow () < 0 || getCurrentRow () >= getTablemodel ().getRowCount ())
      {
        System.out.println ("Is OutOfBounds => null");
        return null;
      }
      else
      {
        return getTablemodel ().getValueAt (getCurrentRow (), col);
      }
    }

    col -= getTablemodel ().getColumnCount ();
    if (isPreviewMode ())
    {
      System.out.println ("Is Preview Mode => null");
      return null;
    }
    return getFunctions ().getFunction (col).getValue ();
  }

  public boolean isPreviewMode ()
  {
    return false;
  }

  public Object get (String name)
  {
    int idx = findColumn (name);
    if (idx == -1)
    {
      System.out.println ("Is InvalidIndex => null");
      return null;
    }
    return get (idx);
  }

  public int getColumnCount ()
  {
    return (getTablemodel ().getColumnCount () + getFunctions ().size ());
  }

  /**
   * looks up the position of the column with the name <code>name</code>.
   * returns the position of the column or -1 if no columns could be retrieved.
   */
  public int findColumn (String name)
  {
    Integer integ = (Integer) colcache.get (name);
    if (integ != null)
      return integ.intValue ();

    int size = getColumnCount ();
    for (int i = 0; i < size; i++)
    {
      String column = getColumnName (i);
      if (column.equals (name))
      {
        colcache.put (name, new Integer (i));
        return i;
      }
    }
    return -1;
  }

  public String getColumnName (int col)
  {
    if (col > getColumnCount ())
      throw new IndexOutOfBoundsException ("requested " + col + " , have " + getColumnCount ());
    if (col < getTablemodel ().getColumnCount ())
    {
      return getTablemodel ().getColumnName (col);
    }

    col -= getTablemodel ().getColumnCount ();
    return getFunctions ().getFunction (col).getName ();
  }


  public boolean isBeforeFirstRow ()
  {
    return getCurrentRow () < 0;
  }

  public boolean isLastRow ()
  {
    return getCurrentRow () > (getTablemodel ().getRowCount () - 1);
  }

  public Object clone () throws CloneNotSupportedException
  {
    DataRowBackend db = (DataRowBackend) super.clone ();
    db.preview = null;
    return db;
  }

  /**
   * Create a preview backend. Such datarows will have no access to functions (all functions
   * will return null).
   */
  public DataRowBackend previewNextRow ()
  {
    if (preview == null)
    {
      preview = new DataRowPreview (this);
    }
    return preview;
  }
}