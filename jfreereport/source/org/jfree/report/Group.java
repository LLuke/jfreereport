/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Group.java,v 1.11 2005/02/23 19:31:35 taqua Exp $
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
 * 09-Sep-2002 : Removed log messages
 * 13-Sep-2002 : Ran checkstyle against the source
 * 02-Dec-2002 : Removed To-Do item, caching of field-name to index pos is done.
 * 06-Dec-2002 : Updated changelog, removed Iterator-usage for performance reasons
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * A report group.  Reports can contain any number of (nested) groups. The order of the
 * fields is important. If the group does not contain any fields, the group spans the
 * whole report from the first to the last row.
 * <p/>
 * The group's field list should not be modified after the group was added to the group
 * list, or the results are undefined.
 * <p/>
 * Groups of the same GroupList must have a subgroup relation. The designated child group
 * must contain all fields of the direct parent plus at least one new field.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 * @see GroupList
 */
public class Group implements Serializable, Cloneable, Comparable
{
  /**
   * The name of the group.
   */
  private String name;

  /**
   * The fields that define the group (can be empty).
   */
  private TreeSet fields;

  /**
   * Cached fields.
   */
  private transient String[] fieldsCached;

  /**
   * The group header (optional).
   */
  private GroupHeader header;

  /**
   * The group footer (optional).
   */
  private GroupFooter footer;

  /**
   * The internal constant to mark anonymous group names.
   */
  public static final String ANONYMOUS_GROUP_PREFIX = "anonymousGroup@";

  private ReportDefinition reportDefinition;

  /**
   * Constructs a group with no fields, and an empty header and footer.
   */
  public Group ()
  {
    this.name = ANONYMOUS_GROUP_PREFIX + super.hashCode();
    this.fields = new TreeSet();
    this.footer = new GroupFooter();
    this.header = new GroupHeader();
  }

  /**
   * Defines the name for this group. The name must not be empty and must be unique within
   * the GroupList.
   *
   * @param name the group name (null not permitted).
   */
  public void setName (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Name must not be null");
    }

    this.name = name;
  }

  /**
   * Returns the name of the group.
   *
   * @return the group name.
   */
  public String getName ()
  {
    return this.name;
  }

  /**
   * Returns the group header. <P> The group header is a report band that contains
   * elements that should be printed at the start of a group.
   *
   * @return the group header.
   */
  public GroupHeader getHeader ()
  {
    return header;
  }

  /**
   * Sets the header for the group.
   *
   * @param header the header (null not permitted).
   * @throws NullPointerException if the given header is null
   */
  public void setHeader (final GroupHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("Header must not be null");
    }
    this.header.setReportDefinition(null);
    this.header = header;
    this.header.setReportDefinition(reportDefinition);
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
   * Sets the footer for the group.
   *
   * @param footer the footer (null not permitted).
   * @throws NullPointerException if the given footer is null.
   */
  public void setFooter (final GroupFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("The footer must not be null");
    }
    this.footer.setReportDefinition(null);
    this.footer = footer;
    this.footer.setReportDefinition(reportDefinition);
  }

  /**
   * Sets the fields for this group. The given list must contain Strings defining the
   * needed fields from the DataRow. Don't reference Function-Fields here, functions are
   * not supported in th groupfield definition.
   *
   * @param c the list containing strings.
   * @throws NullPointerException if the given list is null or the list contains
   *                              null-values.
   */
  public void setFields (final List c)
  {
    if (c == null)
    {
      throw new NullPointerException();
    }
    fields.clear();
    fieldsCached = null;
    final Iterator it = c.iterator();
    while (it.hasNext())
    {
      final String field = (String) it.next();
      addField(field);
    }
  }

  /**
   * Adds a field to the group.  The field names must correspond to the column names in
   * the report's TableModel.
   *
   * @param name the field name (null not permitted).
   * @throws NullPointerException if the name is null
   */
  public void addField (final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Group.addField(...): name is null.");
    }
    fields.add(name);
    fieldsCached = null;
  }

  /**
   * Returns the list of fields for this group.
   *
   * @return a list (unmodifiable) of fields for the group.
   */
  public List getFields ()
  {
    if (fieldsCached == null)
    {
      fieldsCached = (String[]) fields.toArray(new String[fields.size()]);
    }
    return Collections.unmodifiableList(Arrays.asList(fieldsCached));
  }

  /**
   * Returns the group fields as array.
   *
   * @return the fields as string array.
   */
  public String[] getFieldsArray ()
  {
    if (fieldsCached == null)
    {
      fieldsCached = (String[]) fields.toArray(new String[fields.size()]);
    }
    return fieldsCached;
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never be thrown.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final Group g = (Group) super.clone();
    g.fields = new TreeSet(fields);
    g.fieldsCached = fieldsCached;
    g.footer = (GroupFooter) footer.clone();
    g.header = (GroupHeader) header.clone();
    return g;
  }

  /**
   * Compares two objects (required to be instances of the Group class). The group's field
   * lists are compared, order of the fields does not matter.
   *
   * @param o the to be compared object.
   * @return an integer indicating the relative ordering of the two groups.
   */
  public int compareTo (final Object o)
  {
    final Group g = (Group) o;

    /** Remove all element, which are in both lists, they are equal */
    if (fields.size() == g.fields.size())
    {
      // both lists contain the same elements.
      if (fields.containsAll(g.fields))
      {
        return 0;
      }
      else
      {
        // groups with the same number of -, but different fields, are not compareable.
        throw new IllegalArgumentException
                ("These groups are not comparable, as they don't have any subgroup relation. " +
                " Groups of the same GroupList must have a subgroup relation. The designated " +
                " child group must contain all fields of the direct parent plus at least one " +
                " new field.");
      }
    }

    if (fields.containsAll(g.fields))
    {
      // c2 contains all elements of c1, so c1 is subgroup of c2
      return 1;
    }
    if (g.fields.containsAll(fields))
    {
      // c1 contains all elements of c2, so c2 is subgroup of c1
      return -1;
    }
    // not compareable, invalid groups
    // return 0;
    throw new IllegalArgumentException
            ("These groups are not comparable, as they don't have any subgroup relation. " +
            " Groups of the same GroupList must have a subgroup relation. The designated " +
            " child group must contain all fields of the direct parent plus at least one " +
            " new field.");
  }

  /**
   * Checks, whether the group is equal. A group is considered equal to another group, if
   * it defines the same fields as the other group.
   *
   * @param o the object to be checked
   * @return true, if the object is a group instance with the same fields, false
   *         otherwise.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof Group))
    {
      return false;
    }

    final Group group = (Group) o;

    if (!fields.equals(group.fields))
    {
      return false;
    }

    return true;
  }

  /**
   * Computes a hashcode for this group.
   *
   * @return the hashcode.
   */
  public int hashCode ()
  {
    final String[] fields = getFieldsArray();

    int hashCode = 0;
    for (int i = 0; i < fields.length; i++)
    {
      hashCode = 29 * hashCode + fields[i].hashCode();
    }
    return hashCode;
  }

  /**
   * Returns a string representation of the group (useful for debugging).
   *
   * @return A string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("Group={Name='");
    b.append(getName());
    b.append("', fields=");
    b.append(fields);
    b.append("} ");
    return b.toString();
  }

  /**
   * Assigns the report definition to the group and all bands in that group.
   *
   * @param reportDefinition the report definition (maybe null).
   */
  public void setReportDefinition (final ReportDefinition reportDefinition)
  {
    this.reportDefinition = reportDefinition;
    this.header.setReportDefinition(reportDefinition);
    this.footer.setReportDefinition(reportDefinition);
  }

  /**
   * Returns the assigned report definition of the group.
   *
   * @return the report definition (maybe null).
   */
  public ReportDefinition getReportDefinition ()
  {
    return reportDefinition;
  }
}
