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
 * ----------
 * Group.java
 * ----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: Group.java,v 1.1.1.1 2002/04/25 17:02:26 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 11-May-2002 : Fields are now in a list, order is important for comparision. The field list
 *               not modifyable outside this list. Header and footer are no longer allowed to
 *               contain null values. Is lastItemInGroup returns always true, if the end of the
 *               data had been reached.
 */

package com.jrefinery.report;

import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/**
 * A report group.  Reports can contain any number of groups.
 * The order of he fields is important.
 *
 * @see GroupList
 * @todo cache the fieldname to indexPos.
 */
public class Group
{
  /**
   * A readOnly list iterator that will ignore all operations which could modify its contents.
   */
  private class ReadOnlyListIterator implements ListIterator
  {
    private ListIterator it;

    public ReadOnlyListIterator (ListIterator it)
    {
      this.it = it;
    }

    public void add (Object o)
    {
    }

    public boolean hasNext ()
    {
      return it.hasNext ();
    }

    public Object next ()
    {
      return it.next ();
    }

    public boolean hasPrevious ()
    {
      return it.hasPrevious ();
    }

    public Object previous ()
    {
      return it.previous ();
    }

    public int nextIndex ()
    {
      return it.nextIndex ();
    }

    public int previousIndex ()
    {
      return it.previousIndex ();
    }

    public void remove ()
    {
    }

    public void set (Object o)
    {
    }
  }

  /**
   * A readOnly iterator that will ignore all operations which could modify its contents.
   */
  private class ReadOnlyIterator implements Iterator
  {
    private Iterator it;

    public ReadOnlyIterator (Iterator it)
    {
      this.it = it;
    }

    public boolean hasNext ()
    {
      return it.hasNext ();
    }

    public Object next ()
    {
      return it.next ();
    }

    public void remove ()
    {
    }
  }

  /**
   * A readOnly list that will ignore all operations which could modify its contents.
   */
  private class ReadOnlyList implements List
  {
    private List parent;

    public ReadOnlyList (List parent)
    {
      this.parent = parent;
    }

    public int size ()
    {
      return parent.size ();
    }

    public boolean isEmpty ()
    {
      return parent.isEmpty ();
    }

    public boolean contains (Object o)
    {
      return parent.contains (o);
    }

    public Iterator iterator ()
    {
      return new ReadOnlyIterator (parent.iterator ());
    }

    public Object[] toArray ()
    {
      return parent.toArray ();
    }

    public Object[] toArray (Object[] objects)
    {
      return parent.toArray ();
    }

    public boolean add (Object o)
    {
      return false;
    }

    public boolean remove (Object o)
    {
      return false;
    }

    public boolean containsAll (Collection collection)
    {
      return parent.containsAll (collection);
    }

    public boolean addAll (Collection collection)
    {
      return false;
    }

    public boolean addAll (int i, Collection collection)
    {
      return false;
    }

    public boolean removeAll (Collection collection)
    {
      return false;
    }

    public boolean retainAll (Collection collection)
    {
      return false;
    }

    public void clear ()
    {
    }

    public Object get (int i)
    {
      return parent.get (i);
    }

    public Object set (int i, Object o)
    {
      return null;
    }

    public void add (int i, Object o)
    {
    }

    public Object remove (int i)
    {
      return null;
    }

    public int indexOf (Object o)
    {
      return parent.indexOf (o);
    }

    public int lastIndexOf (Object o)
    {
      return parent.lastIndexOf (o);
    }

    public ListIterator listIterator ()
    {
      return new ReadOnlyListIterator (parent.listIterator ());
    }

    public ListIterator listIterator (int i)
    {
      return new ReadOnlyListIterator (parent.listIterator (i));
    }

    public List subList (int i, int i1)
    {
      return new ReadOnlyList (parent.subList (i, i1));
    }
  }

  /** The name of the group. */
  private String name;

  /** The fields that define the group (can be empty). */
  private List fields;

  /** The group header (optional). */
  private GroupHeader header;

  /** The group footer (optional). */
  private GroupFooter footer;

  /**
   * Constructs a group with no fields, and no header or footer.
   */
  public Group ()
  {
    fields = new Vector ();
    setFooter (new GroupFooter ());
    setHeader (new GroupHeader ());
  }

  /**
   * defines the name for this group. The name must not be empty and must be uniqe within
   * the GroupList.
   */
  public void setName (String name)
  {
    if (name == null)
      throw new NullPointerException ("Name must not be null");

    this.name = name;
  }

  /**
   * Returns the name of the group.
   *
   * @return The group name.
   */
  public String getName ()
  {
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
  public GroupHeader getHeader ()
  {
    return header;
  }

  /**
   * Sets the header for the group (null forbidden).
   *
   * @param header The header.
   */
  public void setHeader (GroupHeader header)
  {
    if (header == null)
      throw new NullPointerException ("Header must not be null");
    this.header = header;
  }

  /**
   * Returns the group footer.
   *
   * @return The footer.
   */
  public GroupFooter getFooter ()
  {
    return footer;
  }

  /**
   * Sets the footer for the group (null forbidden).
   *
   * @param footer The footer.
   */
  public void setFooter (GroupFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("The footer must not be null");
    this.footer = footer;
  }

  /**
   * Sets the fields for the�s group. The given list should contain Strings defining the
   * needed fields from the data model.
   *
   * @param c The list containing strings.
   */
  public void setFields (List c)
  {
    fields.clear ();
    Iterator it = c.iterator ();
    while (it.hasNext ())
    {
      addField (it.next ().toString ());
    }
  }

  /**
   * Adds a field to the group.  The field names must correspond to the column names in the
   * report's TableModel.
   *
   * @param name The field name.
   */
  public void addField (String name)
  {

    fields.add (name);

  }

  /**
   * returns the list of fields for this group. Do not modify this list!
   */
  public List getFields ()
  {
    return new ReadOnlyList (fields);
  }

  /**
   * Returns true if the specified item is the last item in the group, and false otherwise.
   * @param data The data.
   *
   * @param row The current item/row.
   * @return A flag indicating whether or not the current item is the last in its group.
   */
  public boolean isLastItemInGroup (TableModel data, int row)
  {
    // return true if this is the last row in the model.
    if (row > (data.getRowCount () - 2))
    {
      return true;
    }
    else
    { // compare item and item+1, if any field differs, then item==last in group
      boolean last = false;
      Iterator iterator = fields.iterator ();
      while (iterator.hasNext ())
      {
        String field = (String) iterator.next ();
        int column = fieldNameToColumnIndex (data, field);
        if (column == -1)
        {
          System.err.println ("Field " + field + " is not defined");
          continue;
        }

        Object item1 = data.getValueAt (row, column);
        Object item2 = data.getValueAt (row + 1, column);
        if (!(secureEquals (item1, item2)))
        {
          return true;
        }
      }
      return last;
    }
  }

  /**
   * Compares two objects without crashing if one or both are null.
   */
  private boolean secureEquals (Object item1, Object item2)
  {
    if ((item1 == null) && (item2 == null)) return true;
    if (item1 == null) return false;
    if (item2 == null) return false;
    return item1.equals (item2);
  }

  /**
   * Converts a field name to a TableModel column index, by matching the field name to the column
   * name.
   *
   * @param data The data.
   * @param name The field name (or column name).
   * @return The column index.
   */
  private int fieldNameToColumnIndex (TableModel data, String name)
  {

    int columns = data.getColumnCount ();
    for (int c = 0; c < columns; c++)
    {
      if (name.equals (data.getColumnName (c)))
      {
        return c;
      }
    }
    return -1;  // no field with that name

  }

}