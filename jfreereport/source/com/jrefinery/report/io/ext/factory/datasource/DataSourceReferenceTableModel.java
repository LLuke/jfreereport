/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------------------
 * StyleKeyReferenceTableModel.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSourceReferenceTableModel.java,v 1.2 2003/06/15 21:26:29 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.factory.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

import org.jfree.xml.factory.objects.ObjectDescription;

/**
 * A table model for the style key reference generator.
 *
 * @author Thomas Morgner
 */
public class DataSourceReferenceTableModel extends AbstractTableModel
{
  /**
   * Represents a row in the table model.
   */
  private class DataSourceDescriptionRow
  {
    /** The factory. */
    private DataSourceFactory datasourceFactory;

    /** The key. */
    private String datasourceName;

    /** The implementing class for the datasource name. */
    private Class implementingClass;

    /**
     * Creates a new row.
     *
     * @param datasourceFactory the datasource factory
     * @param name the name of the datasource within the factory.
     * @param implementingClass the class that implements the named datasource.
     */
    public DataSourceDescriptionRow(DataSourceFactory datasourceFactory,
                                    String name, Class implementingClass)
    {
      this.datasourceFactory = datasourceFactory;
      this.datasourceName = name;
      this.implementingClass = implementingClass;
    }

    /**
     * Returns the factory.
     *
     * @return The factory.
     */
    public DataSourceFactory getFactory()
    {
      return datasourceFactory;
    }

    /**
     * Returns the datasource name.
     *
     * @return The datasource name.
     */
    public String getName()
    {
      return datasourceName;
    }

    /**
     * Returns the class object for the datasource.
     *
     * @return the datasource class.
     */
    public Class getImplementingClass()
    {
      return implementingClass;
    }
  }

  /** The column names. */
  private static final String[] COLUMN_NAMES =
      {
        "datasource-factory",
        "datasource-name",
        "datasource-class"
      };

  /** Storage for the rows. */
  private ArrayList rows;

  /**
   * Creates a new table model.
   *
   * @param cf  the factory collection.
   */
  public DataSourceReferenceTableModel(DataSourceCollector cf)
  {
    rows = new ArrayList();
    addFactoryCollector(cf);
  }

  /**
   * Adds a factory.
   *
   * @param cf  the factory.
   */
  private void addFactoryCollector(DataSourceCollector cf)
  {
    Iterator it = cf.getFactories();
    while (it.hasNext())
    {
      DataSourceFactory cfact = (DataSourceFactory) it.next();
      if (cfact instanceof DataSourceCollector)
      {
        addFactoryCollector((DataSourceCollector) cfact);
      }
      else
      {
        addDataSourceFactory(cfact);
      }
    }
  }

  /**
   * Adds a factory.
   *
   * @param cf  the factory.
   */
  private void addDataSourceFactory(DataSourceFactory cf)
  {
    Iterator it = cf.getRegisteredNames();
    ArrayList factories = new ArrayList();

    while (it.hasNext())
    {
      String c = (String) it.next();
      factories.add(c);
    }

    Collections.sort(factories);
    it = factories.iterator();

    while (it.hasNext())
    {
      String keyName = (String) it.next();
      ObjectDescription od = cf.getDataSourceDescription(keyName);
      rows.add(new DataSourceDescriptionRow(cf, keyName, od.getObjectClass()));
    }
  }

  /**
   * Returns the number of rows in the model. A
   * <code>JTable</code> uses this method to determine how many rows it
   * should display.  This method should be quick, as it
   * is called frequently during rendering.
   *
   * @return the number of rows in the model
   * @see #getColumnCount
   */
  public int getRowCount()
  {
    return rows.size();
  }

  /**
   * Returns the number of columns in the model. A
   * <code>JTable</code> uses this method to determine how many columns it
   * should create and display by default.
   *
   * @return the number of columns in the model
   * @see #getRowCount
   */
  public int getColumnCount()
  {
    return COLUMN_NAMES.length;
  }

  /**
   * Returns the column name.
   *
   * @param column  the column being queried
   * @return a string containing the default name of <code>column</code>
   */
  public String getColumnName(int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
   *  Returns <code>String.class</code> regardless of <code>columnIndex</code>.
   *
   *  @param columnIndex  the column being queried
   *  @return the Object.class
   */
  public Class getColumnClass(int columnIndex)
  {
    return String.class;
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param rowIndex  the row whose value is to be queried
   * @param columnIndex  the column whose value is to be queried
   *
   * @return the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    DataSourceDescriptionRow or = (DataSourceDescriptionRow) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return String.valueOf(or.getFactory().getClass().getName());
      case 1:
        return String.valueOf(or.getName());
      case 2:
        return String.valueOf(or.getImplementingClass().getName());
      default:
        return null;
    }
  }
}
