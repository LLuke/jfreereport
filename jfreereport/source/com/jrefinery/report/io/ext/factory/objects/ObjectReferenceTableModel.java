/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * ObjectReferenceTableModel.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

/**
 * A table model for the objects referenced by the class factories.
 * 
 * @author Thomas Morgner.
 */
public class ObjectReferenceTableModel extends AbstractTableModel
{
  /**
   * Used to represent each row in the table model.
   */
  private class ObjectDescriptionRow
  {
    /** The class factory. */
    private ClassFactory classFactory;
    
    /** The object class. */
    private Class object;
    
    /** The parameter name. */
    private String paramName;
    
    /** The parameter type. */
    private Class paramType;

    /**
     * Creates a new row.
     * 
     * @param classFactory  the class factory.
     * @param object  the object class.
     * @param paramName  the parameter name.
     * @param paramType  the parameter type.
     */
    public ObjectDescriptionRow(ClassFactory classFactory, Class object, String paramName, 
                                Class paramType)
    {
      this.classFactory = classFactory;
      this.object = object;
      this.paramName = paramName;
      this.paramType = paramType;
    }

    /**
     * Returns the class factory.
     * 
     * @return The class factory.
     */
    public ClassFactory getClassFactory()
    {
      return classFactory;
    }

    /**
     * Returns the object class.
     * 
     * @return The class.
     */
    public Class getObject()
    {
      return object;
    }

    /**
     * Returns the parameter name.
     * 
     * @return the parameter name.
     */ 
    public String getParamName()
    {
      return paramName;
    }

    /**
     * Returns the parameter type.
     * 
     * @return the parameter type.
     */
    public Class getParamType()
    {
      return paramType;
    }
  }

  /**
   * A class name comparator.
   */
  private class ClassNameComparator implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * 
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the second.
     * 
     * @throws ClassCastException if the arguments' types prevent them from being compared by 
     *         this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      Class c1 = (Class) o1;
      Class c2 = (Class) o2;
      return c1.getName().compareTo(c2.getName());
    }
  }

  /** The table model column names. */
  private static final String[] COLUMN_NAMES =
      {
        "object-factory",
        "object-class",
        "parameter-name",
        "parameter-class"
      };

  /** Storage for the rows. */
  private ArrayList rows;

  /**
   * Creates a new table model for a set of class factories.
   * 
   * @param cf  the class factories.
   */
  public ObjectReferenceTableModel(ClassFactoryCollector cf)
  {
    rows = new ArrayList();
    addClassFactoryCollector(cf);
  }

  /**
   * Adds a class factory collector.
   * 
   * @param cf  the class factory collector.
   */
  private void addClassFactoryCollector (ClassFactoryCollector cf)
  {
    Iterator it = cf.getFactories();
    while (it.hasNext())
    {
      ClassFactory cfact = (ClassFactory) it.next();
      if (cfact instanceof ClassFactoryCollector)
      {
        addClassFactoryCollector((ClassFactoryCollector) cfact);
      }
      else
      {
        addClassFactory(cfact);
      }
    }
  }

  /**
   * Adds a class factory.
   * 
   * @param cf  the class factory.
   */
  private void addClassFactory (ClassFactory cf)
  {
    Iterator it = cf.getRegisteredClasses();
    ArrayList factories = new ArrayList();

    while (it.hasNext())
    {
      Class c = (Class) it.next();
      factories.add(c);
    }

    Collections.sort(factories, new ClassNameComparator());
    it = factories.iterator();

    while (it.hasNext())
    {
      Class c = (Class) it.next();
      ObjectDescription od = cf.getDescriptionForClass(c);
      Iterator itNames = od.getParameterNames();
      ArrayList nameList = new ArrayList();
      while (itNames.hasNext())
      {
        nameList.add(itNames.next());
      }
      // sort the parameter names
      Collections.sort(nameList);
      itNames = nameList.iterator();
      while (itNames.hasNext())
      {
        String name = (String) itNames.next();
        rows.add(new ObjectDescriptionRow(cf, c, name, od.getParameterDefinition(name)));
      }
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
   * Returns <code>String.class</code> regardless of <code>columnIndex</code>.
   *
   * @param columnIndex  the column being queried
   * @return the Object.class
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
   * @return  the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    ObjectDescriptionRow or = (ObjectDescriptionRow) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0: return String.valueOf(or.getClassFactory().getClass().getName());
      case 1: return String.valueOf(or.getObject().getName());
      case 2: return String.valueOf(or.getParamName());
      case 3: return String.valueOf(or.getParamType().getName());
    }
    return null;
  }

}
