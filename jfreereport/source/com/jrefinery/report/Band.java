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
 * $Id: Band.java,v 1.3 2002/05/21 23:06:17 taqua Exp $
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
 * 26-May-2002 : Elements are not stored ordered.
 */

package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataTarget;
import com.jrefinery.report.filter.FunctionDataSource;
import com.jrefinery.report.filter.ReportDataSource;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.util.HashNMap;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.LinkedList;

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
    setDefaultFont(DEFAULT_FONT);
    setDefaultPaint(DEFAULT_PAINT);
    allElements = new LinkedList();
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
    if (font == null) throw new NullPointerException();
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
    if (paint == null) throw new NullPointerException();
    this.defaultPaint = paint;
  }

  /**
   * Adds an element (display item) to the band.
   * @param element The element.
   */
  public void addElement (Element element)
  {

    allElements.add (element);

    DataSource ds = getLastDatasource (element);
    if (ds instanceof ReportDataSource)
    {
      ReportDataSource rds = (ReportDataSource) ds;
      dataElements.add (rds.getField (), rds);
    }
    else if (ds instanceof FunctionDataSource)
    {
      FunctionDataSource fe = (FunctionDataSource) ds;
      functionElements.add (fe.getFunction (), fe);
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
    TableModel data = state.getReport ().getData ();
    int row = state.getCurrentDisplayItem ();

    if (data.getRowCount () < 1)
      return;

    row = Math.min (row, data.getRowCount () - 1);
    for (int column = 0; column < data.getColumnCount (); column++)
    {
      Object value = data.getValueAt (row, column);
      String name = data.getColumnName (column);
      Enumeration elements = dataElements.getAll (name);
      if (elements == null)
      {
        // No data elements for this column in this band
        continue;
      }

      // Fill the value into all elements
      while (elements.hasMoreElements())
      {
        ReportDataSource element = (ReportDataSource) elements.nextElement();
        element.setValue (value);
      }
    }

    // iterate through the function elements
    FunctionCollection functions = state.getFunctions ();
    Enumeration enum = this.functionElements.keys ();
    while (enum.hasMoreElements ())
    {

      String name = (String) enum.nextElement ();
      Function f = functions.get (name);
      if (f == null) continue;

      Enumeration functionsources = functionElements.getAll(name);
      if (functionsources == null) continue;

      while (functionsources.hasMoreElements())
      {
        FunctionDataSource fds = (FunctionDataSource) functionsources.nextElement();
        fds.setValue (f.getValue ());
      }
    }

  }

  /**
   * Draws the band onto the specified output target.
   * @param target The output target.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public void draw (OutputTarget target, float x, float y) throws OutputTargetException
  {
    Rectangle2D bounds = new Rectangle2D.Float();
    bounds.setRect(x,y, target.getUsableWidth(), getHeight());
    target.setClippingArea(bounds);

    target.setPaint (getDefaultPaint());
    Iterator iterator = allElements.iterator ();
    while (iterator.hasNext ())
    {
      Element e = (Element) iterator.next ();
      if (e.isVisible())
      {
        target.getCursor().setElementBounds(translateBounds(target, e.getBounds()));
        try
        {
          Object state = target.saveState();
          e.draw (target, this);
          target.restoreState(state);
        }
        catch (OutputTargetException ex)
        {
          Log.error ("Failed to draw band", ex);
        }
      }
    }
  }

  /**
   * Translates the elements bounds from relative values (-100 .. 0) to absolute values.
   */
  private Rectangle2D translateBounds (OutputTarget target, Rectangle2D bounds)
  {
    float x = fixValue(bounds.getX(), target.getUsableWidth());
    float y = fixValue(bounds.getY(), getHeight());
    float w = fixValue(bounds.getWidth(), target.getUsableWidth());
    float h = fixValue(bounds.getHeight(), getHeight());
    return new Rectangle2D.Float (x,y,w,h);
  }

  /**
   * Helperfunction:
   * Translates the elements bounds from relative values (-100 .. 0) to absolute values.
   */
  private float fixValue (double value, double full)
  {
    if (value >= 0) return (float) value;
    float retval = (float) (value * full / -100);
    Log.debug ("Adjusted Relative value: " + retval);
    return retval;
  }

  public Element getElement (String name)
  {
    Iterator it  = allElements.iterator();
    while (it.hasNext())
    {
      Element e = (Element) it.next();
      if (e.getName().equals(name))
      {
        return e;
      }
    }
    return null;
  }

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
   */
  public static DataSource getLastDatasource (DataTarget e)
  {
    DataSource s = e.getDataSource ();
    if (s instanceof DataTarget)
    {
      DataTarget tgt = (DataTarget) s;
      return getLastDatasource (tgt);
    }
    return s;
  }
}
