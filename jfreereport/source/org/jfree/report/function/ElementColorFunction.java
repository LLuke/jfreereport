/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------
 * ElementColorFunction.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementColorFunction.java,v 1.11 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 07.04.2003 : Initial version
 */
package org.jfree.report.function;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.SerializerHelper;

/**
 * A function that alternates between true and false for each item within a group. The
 * functions value affects a defined elements color. If the function evaluates to true,
 * the named element is painted with the colorTrue, else the element is painted with
 * colorFalse.
 * <p/>
 * Use the property <code>element</code> to name an element contained in the ItemBand
 * whose color should be affected by this function. All colors have the color 'black' by
 * default.
 *
 * @author Thomas Morgner
 */
public class ElementColorFunction
        extends AbstractElementFormatFunction implements Serializable
{
  /**
   * the color if the field is TRUE.
   */
  private transient Color colorTrue;
  private transient Color colorFalse;

  private String field;

  /**
   * Default constructor.
   */
  public ElementColorFunction ()
  {
  }

  /**
   * Returns the field used by the function. <P> The field name corresponds to a column
   * name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return field;
  }

  /**
   * Sets the field name for the function. <P> The field name corresponds to a column name
   * in the report's TableModel.
   *
   * @param field the field name (null not permitted).
   */
  public void setField (final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    this.field = field;
  }

  /**
   * Sets the color for true values.
   *
   * @param colorTrue the color.
   */
  public void setColorTrue (final Color colorTrue)
  {
    this.colorTrue = colorTrue;
  }

  /**
   * Sets the color for false values.
   *
   * @param colorFalse the color.
   */
  public void setColorFalse (final Color colorFalse)
  {
    this.colorFalse = colorFalse;
  }

  /**
   * Returns the color for true values.
   *
   * @return A color.
   */
  public Color getColorTrue ()
  {
    return colorTrue;
  }

  /**
   * Returns the color for false values.
   *
   * @return A color.
   */
  public Color getColorFalse ()
  {
    return colorFalse;
  }

  /**
   * Process the given band and color the named element of that band if it exists.
   *
   * @param b the band that should be colored.
   */
  protected void processRootBand (final Band b)
  {
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    if (elements.length == 0)
    {
      // there is no such element ! (we searched it by its name)
      return;
    }

    final boolean value = isValueTrue();
    final Color color;
    if (value)
    {
      color = getColorTrue();
    }
    else
    {
      color = getColorFalse();
    }

    for (int i = 0; i < elements.length; i++)
    {
      elements[i].getStyle().setStyleProperty(ElementStyleSheet.PAINT, color);
    }
  }

  protected boolean isValueTrue()
  {
    final boolean value;
    final Object o = getDataRow().get(getField());
    if (o == null)
    {
      value = false;
    }
    else
    {
      value = o.equals(Boolean.TRUE);
    }
    return value;
  }


  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return null;
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject (final ObjectOutputStream out)
          throws IOException
  {
    out.defaultWriteObject();
    SerializerHelper.getInstance().writeObject(colorFalse, out);
    SerializerHelper.getInstance().writeObject(colorTrue, out);
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could
   *                                not be found.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    colorFalse = (Color) SerializerHelper.getInstance().readObject(in);
    colorTrue = (Color) SerializerHelper.getInstance().readObject(in);
  }


}
