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
 * ---------
 * Band.java
 * ---------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: Band.java,v 1.1.1.1 2002/04/25 17:02:25 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed band height from Number --> float (DG);
 * 05-Mar-2002 : Changed the constructors from public --> protected (DG);
 * 10-May-2002 : Declared Abstract, Removed complex constructors
 * 11-May-2002 : Bug: when adding multiple data fields referencing to the same column in the
 *               data model only the first field was filled with data on populateElements.
 */

package com.jrefinery.report;

import com.jrefinery.report.function.Function;

import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * A report band contains a list of elements to be displayed, and represents one section of a
 * report (the report header or footer, the page header or footer, the group header or footer,
 * or the items within a group).
 * <P>
 * The elements in a report band can contain fixed values, field values from the dataset, or
 * function values.
 *
 */
public abstract class Band
{

  /** The default font. */
  public static final Font DEFAULT_FONT = new Font ("Serif", Font.PLAIN, 10);

  /** The default paint. */
  public static final Paint DEFAULT_PAINT = Color.black;

  /** The height of the band. */
  private float height;

  /** The default font for this band. */
  private Font defaultFont;

  /** The default paint for this band. */
  private Paint defaultPaint;

  /** All the elements for this band, stored by name. */
  private SortedMap allElements;

  /** Data elements for this band, stored by field name. */
  private SortedMap dataElements;

  /** Function elements for this band, stored by function name. */
  private SortedMap functionElements;

  /**
   * Constructs a new band (initially empty).
   * @param height The height of the band.
   */
  protected Band ()
  {
    allElements = new TreeMap ();
    dataElements = new TreeMap ();
    functionElements = new TreeMap ();
  }

  /**
   * Returns the height of the band (in points).
   * @return The height. */
  public float getHeight ()
  {
    return height;
  }

  /**
   * defines the height of the band (in points).
   */
  public void setHeight (float height)
  {
    this.height = height;
  }

  /**
   * Returns the default font for the band.
   *
   * @return The font.
   */
  public Font getDefaultFont ()
  {
    return this.defaultFont;
  }

  /**
   * Sets the default font for the band.
   *
   * @param font The font.
   */
  public void setDefaultFont (Font font)
  {
    this.defaultFont = font;
  }

  /**
   * Returns the default paint for the band.
   *
   * @return The paint.
   */
  public Paint getDefaultPaint ()
  {
    return this.defaultPaint;
  }

  /**
   * Sets the default paint for the band.
   *
   * @param paint The paint.
   */
  public void setDefaultPaint (Paint paint)
  {
    this.defaultPaint = paint;
  }

  /**
   * Adds an element (display item) to the band.
   * @param element The element.
   */
  public void addElement (Element element)
  {

    allElements.put (element.getName (), element);

    if (element instanceof DataElement)
    {
      DataElement de = (DataElement) element;
      Vector v = (Vector) dataElements.get (de.getField());
      if (v == null)
      {
        v = new Vector ();
        dataElements.put (de.getField(), v);
      }
      v.add (de);
    }

    if (element instanceof FunctionElement)
    {
      FunctionElement fe = (FunctionElement) element;
      functionElements.put (fe.getName (), fe);
    }

  }

  /**
   * Adds a collection of elements to the band.
   * @param elements The element collection.
   */
  public void addElements (Collection elements)
  {

    if (elements != null)
    {
      Iterator iterator = elements.iterator ();
      while (iterator.hasNext ())
      {
        Element element = (Element) iterator.next ();
        addElement (element);
      }
    }

  }

  /**
   * Updates the elements in the band with the data and functions for the specified row.
   * @param data The data source.
   * @param row The current row.
   * @param functions The report functions.
   */
  public void populateElements (ReportState state)
  {
    TableModel data = state.getReport().getData();
    int row = state.getCurrentDisplayItem();
    FunctionCollection functions = state.getFunctions();

    if (data.getRowCount() < 1)
      return;

    row = Math.min (row, data.getRowCount () - 1);
    for (int column = 0; column < data.getColumnCount (); column++)
    {
      Object value = data.getValueAt (row, column);
      String name = data.getColumnName (column);
      Vector elements = (Vector) dataElements.get (name);
      if (elements == null)
      {
        // No data elements for this column in this band
        continue;
      }

      // Fill the value into all elements
      for (int i = 0; i < elements.size(); i++)
      {
        DataElement element = (DataElement) elements.elementAt(i);
        element.setValue (value);
      }
    }

    // iterate through the function elements
    Iterator iterator = this.functionElements.values ().iterator ();
    while (iterator.hasNext ())
    {
      FunctionElement fe = (FunctionElement) iterator.next ();
      Function f = functions.get (fe.getFunctionName ());
      if (f != null)
      {
        fe.setValue (f.getValue ());
      }
      else
      {
        fe.setValue ("-");
      }
    }

  }

  /**
   * Draws the band onto the specified output target.
   * @param target The output target.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public void draw (OutputTarget target, float x, float y)
  {
    target.setPaint (Color.black);
    Iterator iterator = allElements.values ().iterator ();
    while (iterator.hasNext ())
    {
      Element e = (Element) iterator.next ();
      e.draw (target, this, x, y);
    }
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append (this.getClass().getName());
    b.append ("={functions=");
    b.append (functionElements);
    b.append (", dataelements=");
    b.append (dataElements);
    b.append ("}");
    return b.toString ();
  }
}
