/**
 * Date: Feb 12, 2003
 * Time: 3:18:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.objects;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

public class ObjectReferenceTableModel extends AbstractTableModel
{
  private class ObjectDescriptionRow
  {
    private ClassFactory classFactory;
    private Class object;
    private String paramName;
    private Class paramType;

    public ObjectDescriptionRow(ClassFactory classFactory, Class object, String paramName, Class paramType)
    {
      this.classFactory = classFactory;
      this.object = object;
      this.paramName = paramName;
      this.paramType = paramType;
    }

    public ClassFactory getClassFactory()
    {
      return classFactory;
    }

    public Class getObject()
    {
      return object;
    }

    public String getParamName()
    {
      return paramName;
    }

    public Class getParamType()
    {
      return paramType;
    }
  }

  private class ClassNameComparator implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second.
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     */
    public int compare(Object o1, Object o2)
    {
      Class c1 = (Class) o1;
      Class c2 = (Class) o2;
      return c1.getName().compareTo(c2.getName());
    }
  }

  private static final String[] COLUMN_NAMES =
      {
        "object-factory",
        "object-class",
        "parameter-name",
        "parameter-class"
      };

  private ArrayList rows;

  public ObjectReferenceTableModel(ClassFactoryCollector cf)
  {
    rows = new ArrayList();
    addClassFactoryCollector(cf);
  }

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
   *  Returns a default name for the column using spreadsheet conventions:
   *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
   *  returns an empty string.
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
   * @param	rowIndex	the row whose value is to be queried
   * @param	columnIndex 	the column whose value is to be queried
   * @return	the value Object at the specified cell
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
