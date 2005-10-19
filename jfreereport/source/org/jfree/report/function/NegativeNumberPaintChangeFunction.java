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
 * NegativeNumberPaintChangeFunction.java
 * -------------------------
 * (C)opyright 2005, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):
 *
 * $Id: ElementColorFunction.java,v 1.12 2005/08/29 17:56:46 taqua Exp $
 *
 * Changes
 * -------
 * 19-Oct-2005 : Initial version
 */
package org.jfree.report.function;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.SerializerHelper;

/**
 * This function changes the color of the named elements according to the
 * current value of a numeric field. If the value of the field is not
 * numeric (or null), the positive color is set.
 *
 * @author Thomas Morgner
 */
public class NegativeNumberPaintChangeFunction
  extends AbstractElementFormatFunction
{
  private String field;
  private transient Color positiveColor;
  private transient Color negativeColor;
  private transient Color zeroColor;

  public NegativeNumberPaintChangeFunction()
  {
  }

  protected void processRootBand(Band b)
  {
    final Element[] elements = FunctionUtilities.findAllElements(b, getElement());
    if (elements.length == 0)
    {
      return;
    }

    final Color color = selectColor();
    for (int i = 0; i < elements.length; i++)
    {
      elements[i].getStyle().setStyleProperty(ElementStyleSheet.PAINT, color);
    }
  }

  protected Color selectColor ()
  {
    Object o = getDataRow().get(getField());
    if (o instanceof Number == false)
    {
      return getPositiveColor();
    }
    Number n = (Number) o;
    double d = n.doubleValue();
    if (d < 0)
    {
      return getNegativeColor();
    }
    if (d > 0)
    {
      return getPositiveColor();
    }
    final Color zeroColor = getZeroColor();
    if (zeroColor == null)
    {
      return getPositiveColor();
    }
    return zeroColor;
  }

  public Color getPositiveColor()
  {
    return positiveColor;
  }

  public void setPositiveColor(final Color positiveColor)
  {
    this.positiveColor = positiveColor;
  }

  public Color getNegativeColor()
  {
    return negativeColor;
  }

  public void setNegativeColor(final Color negativeColor)
  {
    this.negativeColor = negativeColor;
  }

  public Color getZeroColor()
  {
    return zeroColor;
  }

  public void setZeroColor(final Color zeroColor)
  {
    this.zeroColor = zeroColor;
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  public Object getValue()
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
    SerializerHelper.getInstance().writeObject(positiveColor, out);
    SerializerHelper.getInstance().writeObject(negativeColor, out);
    SerializerHelper.getInstance().writeObject(zeroColor, out);
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
    positiveColor = (Color) SerializerHelper.getInstance().readObject(in);
    negativeColor = (Color) SerializerHelper.getInstance().readObject(in);
    zeroColor = (Color) SerializerHelper.getInstance().readObject(in);
  }


}
