/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: Group.java,v 1.14 2002/09/08 13:18:56 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 11-May-2002 : Fields are now in a list, order is important for comparision. The field list
 *               not modifyable outside this list. Header and footer are no longer allowed to
 *               contain null values. Is lastItemInGroup returns always true, if the end of the
 *               data had been reached.
 * 03-Jul-2002 : Serializable and cloneable, replaces own ReadOnlyList with standard implementation
 * 26-Jul-2002 : Introduced DataRowBackend as replacement for the raw data access
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A report group.  Reports can contain any number of groups.
 * The order of the fields is important.
 *
 * @see GroupList
 *
 * @author DG
 *
 * @todo cache the fieldname to indexPos.
 */
public class Group implements Serializable, Cloneable
{

  /** The name of the group. */
  private String name;

  /** The fields that define the group (can be empty). */
  private ArrayList fields;

  /** The group header (optional). */
  private GroupHeader header;

  /** The group footer (optional). */
  private GroupFooter footer;

  /**
   * Constructs a group with no fields, and no header or footer.
   */
  public Group ()
  {
    fields = new ArrayList ();
    setFooter (new GroupFooter ());
    setHeader (new GroupHeader ());
  }

  /**
   * Defines the name for this group. The name must not be empty and must be unique within
   * the GroupList.
   *
   * @param name  the group name (null not permitted).
   */
  public void setName (String name)
  {
    if (name == null)
    {
      throw new NullPointerException ("Name must not be null");
    }

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
   * @param header  the header.
   */
  public void setHeader (GroupHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException ("Header must not be null");
    }
    this.header = header;
  }

  /**
   * Returns the group footer.
   *
   * @return the footer.
   */
  public GroupFooter getFooter ()
  {
    return footer;
  }

  /**
   * Sets the footer for the group (null forbidden).
   *
   * @param footer  the footer.
   */
  public void setFooter (GroupFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException ("The footer must not be null");
    }
    this.footer = footer;
  }

  /**
   * Sets the fields for this group. The given list should contain Strings defining the
   * needed fields from the data model.
   *
   * @param c  the list containing strings.
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
   * @param name  the field name.
   */
  public void addField (String name)
  {
    fields.add (name);
  }

  /**
   * Returns the list of fields for this group.
   *
   * @return a list (unmodifiable) of fields for the group.
   */
  public List getFields ()
  {
    return Collections.unmodifiableList (fields);
  }

  /**
   * Compares two objects without crashing if one or both are null.
   *
   * @param item1  the first object for comparison.
   * @param item2  the second object for comparison.
   *
   * @return true, if both objects are null or both objects are equal, false otherwise.
   */
  private boolean secureEquals (Object item1, Object item2)
  {
    if ((item1 == null) && (item2 == null))
    {
      return true;
    }
    if (item1 == null)
    {
      return false;
    }
    if (item2 == null)
    {
      return false;
    }
    return item1.equals (item2);
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never be thrown.
   */
  public Object clone () throws CloneNotSupportedException
  {
    Group g = (Group) super.clone ();
    g.fields = (ArrayList) fields.clone ();
    g.footer = (GroupFooter) footer.clone ();
    g.header = (GroupHeader) header.clone ();
    return g;
  }


  /**
   * Returns true if this is the last item in the group, and false otherwise.
   *
   * @param lastDataRow  the last data row.
   * @param currentDataRow   the current data row.
   *
   * @return A flag indicating whether or not the current item is the last in its group.
   */
  public boolean isLastItemInGroup (DataRowBackend lastDataRow, DataRowBackend currentDataRow)
  {
    // return true if this is the last row in the model.
    if (currentDataRow.isLastRow ())
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
        int column = currentDataRow.findColumn (field);
        if (column == -1)
        {
          continue;
        }

        Object item1 = lastDataRow.get (column);
        Object item2 = currentDataRow.get (column);
        if (!(secureEquals (item1, item2)))
        {
          return true;
        }
      }
      return last;
    }
  }

}
