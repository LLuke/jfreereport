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
 * $Id: Band.java,v 1.12 2002/07/28 13:25:24 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed band height from Number --> float (DG);
 * 05-Mar-2002 : Changed the constructors from public --> protected (DG);
 * 10-May-2002 : Declared Abstract, Removed complex constructors
 * 11-May-2002 : Bug: when adding multiple data fields referencing to the same column in the
 *               data model only the first field was filled with data on populateElements.
 * 20-May-2002 : Changed to support new drawing scheme. The state of the OutputTarget is stored
 *               before any element starts to draw and restored afterwards. This will greatly
 *               reduce sideeffects from changed fonts or paints which are not restored by the
 *               element.
 * 26-May-2002 : Elements are now stored ordered. Updated drawing to reflect new element property
 *               "Visible".
 * 04-Jun-2002 : Public methods throw exceptions on illegal values. Documentation update.
 * 04-Jul-2002 : Serializable and Cloneable
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataTarget;
import com.jrefinery.report.filter.FunctionDataSource;
import com.jrefinery.report.filter.ReportDataSource;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.report.util.HashNMap;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A report band contains a list of elements to be displayed, and represents one section of a
 * report (the report header or footer, the page header or footer, the group header or footer,
 * or the items within a group).
 * <P>
 * The elements in a report band can contain fixed values, field values from the dataset, or
 * function values. The elements are not required to have unique names.
 * <p>
 * This implementation is not synchronized, to take care that you externaly synchronize
 * it when using multiple threads.
 */
public abstract class Band implements Serializable, Cloneable
{

  /** The default font. */
  public static final transient Font DEFAULT_FONT = new Font ("Serif", Font.PLAIN, 10);

  /** The default paint. */
  public static final transient Paint DEFAULT_PAINT = Color.black;

  /** The height of the band. */
  private float height;

  /** The default font for this band. */
  private Font defaultFont;

  /** The default paint for this band. */
  private Paint defaultPaint;

  /** All the elements for this band, stored by name. */
  private List allElements;

  /** Data elements for this band, stored by field name. */
  private HashNMap dataElements;

  /** Function elements for this band, stored by function name. */
  private HashNMap functionElements;

  /**
   * Constructs a new band (initially empty).
   * @param height The height of the band.
   */
  protected Band ()
  {
    setDefaultFont (DEFAULT_FONT);
    setDefaultPaint (DEFAULT_PAINT);
    allElements = new ArrayList ();
    dataElements = new HashNMap ();
    functionElements = new HashNMap ();
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
    if (font == null) throw new NullPointerException ();
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
   * @throws NullPointerException if the given paint is null
   */
  public void setDefaultPaint (Paint paint)
  {
    if (paint == null) throw new NullPointerException ();
    this.defaultPaint = paint;
  }

  /**
   * Adds an element (display item) to the band.
   *
   * @param element The element.
   * @throws NullPointerException if the Element is null or contains Null-Values.
   * @throws IllegalArgumentException if the element violates validity rules
   */
  public void addElement (Element element)
  {
    if (element == null)
      throw new NullPointerException ("Cannot add null-Element");
    if (element.getName () == null)
      throw new IllegalArgumentException ("Element is not valid: Valid elements need a name");
    if (element.getBounds () == null)
      throw new IllegalArgumentException ("Element is not valid: Valid elements need filled bounds");

    allElements.add (element);
    DataSource ds = getLastDatasource (element);
    if (ds instanceof ReportDataSource)
    {
      ReportDataSource rds = (ReportDataSource) ds;
      if (rds.getField () == null)
        throw new IllegalArgumentException ("DataSource is not valid: ReportDataSources need a field set");
      dataElements.add (rds.getField (), rds);
    }
    else if (ds instanceof FunctionDataSource)
    {
      FunctionDataSource fe = (FunctionDataSource) ds;
      if (fe.getFunction () == null)
        throw new IllegalArgumentException ("DataSource is not valid: FunctionDataSources need a function name set");
      functionElements.add (fe.getFunction (), fe);
    }

  }

  /**
   * Adds a collection of elements to the band.
   * @param elements The element collection.
   * @throws NullPointerException if the collection given is null
   */
  public void addElements (Collection elements)
  {

    if (elements == null)
    {
      throw new NullPointerException ();
    }

    Iterator iterator = elements.iterator ();
    while (iterator.hasNext ())
    {
      Element element = (Element) iterator.next ();
      addElement (element);
    }

  }

  /**
   * Updates the elements in the band with the data and functions for the specified row.
   * @param data The data source.
   * @param row The current row.
   * @param functions The report functions.
   * @throws NullPointerException if the state given is null
   *
   public void populateElements (ReportState state)
   {
   if (state == null) throw new NullPointerException ();

   TableModel data = state.getReport ().getData ();

   if (data.getRowCount () < 1)
   return;

   int row = state.getCurrentDisplayItem ();
   row = Math.min (row, data.getRowCount () - 1);

   for (int column = 0; column < data.getColumnCount (); column++)
   {
   Object value = data.getValueAt (row, column);
   String name = data.getColumnName (column);
   Iterator elements = dataElements.getAll (name);
   if (elements == null)
   {
   // No data elements for this column in this band
   continue;
   }

   // Fill the value into all elements
   while (elements.hasNext ())
   {
   ReportDataSource element = (ReportDataSource) elements.next ();
   element.setValue (value);
   }
   }

   // iterate through the function elements
   FunctionCollection functions = state.getFunctions ();
   Enumeration enum = this.functionElements.keys ();
   while (enum.hasMoreElements ())
   {
   // elements are keyed by function name
   String name = (String) enum.nextElement ();
   Function f = functions.get (name);
   if (f == null) continue;

   // get all functionElements for this function
   Iterator functionsources = functionElements.getAll (name);
   if (functionsources == null) continue;

   Object functionValue = f.getValue ();
   while (functionsources.hasNext ())
   {
   FunctionDataSource fds = (FunctionDataSource) functionsources.next ();
   fds.setValue (functionValue);
   }
   }

   }
   */

  /**
   * Draws the band onto the specified output target.
   * @param target The output target.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   * @throws NullPointerException if the target given is null
   public void draw (OutputTarget target, float x, float y) throws OutputTargetException
   {
   if (target == null) throw new NullPointerException ();

   Rectangle2D bounds = new Rectangle2D.Float ();
   bounds.setRect (x, y, target.getUsableWidth (), getHeight ());
   target.setClippingArea (bounds);

   target.setPaint (getDefaultPaint ());
   Iterator iterator = allElements.iterator ();
   while (iterator.hasNext ())
   {
   Element e = (Element) iterator.next ();
   if (e.isVisible ())
   {
   target.getCursor ().setElementBounds (translateBounds (target, e.getBounds ()));
   try
   {
   Object state = target.saveState ();
   e.draw (target, this);
   target.restoreState (state);
   }
   catch (OutputTargetException ex)
   {
   Log.error ("Failed to draw band", ex);
   }
   }
   }
   }
   */

  public float draw (OutputTarget target, float x, float y) throws OutputTargetException
  {
    if (target == null) throw new NullPointerException ();
    float maxheight = 0;

    Rectangle2D bounds = new Rectangle2D.Float ();
    bounds.setRect (x, y, target.getUsableWidth (), getHeight ());
    target.setClippingArea (bounds);

    target.setPaint (getDefaultPaint ());
    Iterator iterator = allElements.iterator ();
    while (iterator.hasNext ())
    {
      Element e = (Element) iterator.next ();
      if (e.isVisible ())
      {
        target.getCursor ().setElementBounds (translateBounds (target, e.getBounds ()));
        try
        {
          Object state = target.saveState ();
          e.draw (target, this);
          target.restoreState (state);
        }
        catch (OutputTargetException ex)
        {
          Log.error ("Failed to draw band", ex);
        }
        double eh = target.getCursor ().getDrawBounds ().getY () +
                target.getCursor ().getDrawBounds ().getHeight ();
        if (eh > maxheight) maxheight = (float) eh;
      }
    }
    return maxheight;
  }

  /**
   * Translates the elements bounds from relative values (-100 .. 0) to absolute values.
   */
  private Rectangle2D translateBounds (OutputTarget target, Rectangle2D bounds)
  {
    float x = fixValue (bounds.getX (), target.getUsableWidth ());
    float y = fixValue (bounds.getY (), getHeight ());
    float w = fixValue (bounds.getWidth (), target.getUsableWidth ());
    float h = fixValue (bounds.getHeight (), getHeight ());
    bounds.setRect (x, y, w, h);
    return bounds;
  }

  /**
   * Helperfunction:
   * Translates the elements bounds from relative values (-100 .. 0) to absolute values.
   */
  private float fixValue (double value, double full)
  {
    if (value >= 0) return (float) value;
    float retval = (float) (value * full / -100);
    return retval;
  }

  /**
   * Returns the first element in that list which is registered by the given name
   *
   * @returns the first element found or null if there is no such element.
   * @throws NullPointerException if the given name is null
   */
  public Element getElement (String name)
  {
    if (name == null) throw new NullPointerException ();

    Iterator it = allElements.iterator ();
    while (it.hasNext ())
    {
      Element e = (Element) it.next ();
      if (e.getName ().equals (name))
      {
        return e;
      }
    }
    return null;
  }

  /**
   * @returns an immutable list of all registered elements for this band.
   */
  public List getElements ()
  {
    return Collections.unmodifiableList (allElements);
  }

  /**
   * @returns a string representation of this band and all contained elements.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append (this.getClass ().getName ());
    b.append ("={functions=");
    b.append (functionElements);
    b.append (", dataelements=");
    b.append (dataElements);
    b.append ("}");
    return b.toString ();
  }

  /**
   * Queries the last datasource in the chain of targets and filters.
   * <p>
   * The last datasource is used to feed data into the data processing chain.
   * The result of this computation is retrieved by the element using the
   * registered datasource to query the queue.
   */
  public static DataSource getLastDatasource (DataTarget e)
  {
    if (e == null) throw new NullPointerException ();
    DataSource s = e.getDataSource ();
    if (s instanceof DataTarget)
    {
      DataTarget tgt = (DataTarget) s;
      return getLastDatasource (tgt);
    }
    return s;
  }

  public Object clone () throws CloneNotSupportedException
  {
    Band b = (Band) super.clone ();
    b.allElements = new ArrayList (allElements);
    b.dataElements = (HashNMap) dataElements.clone ();
    b.functionElements = (HashNMap) functionElements.clone ();
    return b;
  }
}
