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
 * -------------------------
 * ElementColorFunction.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementColorFunction.java,v 1.5 2003/04/24 18:08:47 taqua Exp $
 *
 * Changes
 * -------
 * 07.04.2003 : Initial version
 */
package com.jrefinery.report.function;

import java.awt.Color;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.xml.factory.objects.ColorObjectDescription;

/**
 * A function that alternates between true and false for each item within a group. The functions
 * value affects a defined elements color. If the function evaluates to true, the named
 * element is painted with the elementColorTrue, else the element is painted with elementColorFalse.
 * <p>
 * Use the property <code>element</code> to name an element contained in the ItemBand whose
 * color should be affected by this function. All colors have the color 'black' by default.
 *
 * @author Thomas Morgner
 */
public class ElementColorFunction extends AbstractFunction
{
  /** the Property key for the name of the ItemBand element. */
  public static final String ELEMENT_PROPERTY = "element";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** the color if the field is TRUE */
  private Color elementColorTrue;
  
  /** the color if the field is FALSE */
  private Color elementColorFalse;
  
  /** The color object descripion. */
  private ColorObjectDescription cod;

  /**
   * Default constructor. 
   */
  public ElementColorFunction()
  {
    cod = new ColorObjectDescription();
  }

  /**
   * Sets the element name. The name denotes an element within the item band. The element
   * will be retrieved using the getElement(String) function.
   *
   * @param name The element name.
   * @see com.jrefinery.report.Band#getElement(String)
   */
  public void setElement(String name)
  {
    setProperty(ELEMENT_PROPERTY, name);
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   */
  public String getElement()
  {
    return getProperty(ELEMENT_PROPERTY, "");
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField()
  {
    return getProperty(FIELD_PROPERTY);
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Checks that the function has been correctly initialized.
   * <p>
   * The only check performed at present is to make sure the name is not <code>null</code>.
   *
   * @throws FunctionInitializeException in case the function is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    try
    {
      cod.setParameter("value", getProperty("colorTrue", "black"));
      elementColorTrue = (Color) cod.createObject();
      cod.setParameter("vaule", getProperty("colorFalse", "black"));
      elementColorFalse = (Color) cod.createObject();
    }
    catch (Exception e)
    {
      throw new FunctionInitializeException("Failed to parse colors", e);
    }
  }

  /**
   * Sets the color for true values.
   * 
   * @param elementColorTrue  the color.
   */
  public void setElementColorTrue(Color elementColorTrue)
  {
    if (elementColorTrue == null)
    {
      throw new NullPointerException();
    }
    try
    {
      cod.setParameterFromObject(elementColorTrue);
      setProperty("colorTrue", (String) cod.getParameter("value"));
    }
    catch (Exception e)
    {
      // ignore me, should never happen...
    }
    this.elementColorTrue = elementColorTrue;
  }

  /**
   * Sets the color for false values.
   * 
   * @param elementColorFalse  the color.
   */
  public void setElementColorFalse(Color elementColorFalse)
  {
    if (elementColorFalse == null)
    {
      throw new NullPointerException();
    }
    try
    {
      cod.setParameterFromObject(elementColorTrue);
      setProperty("colorFalse", (String) cod.getParameter("value"));
    }
    catch (Exception e)
    {
      // ignore me, should never happen...
    }
    this.elementColorFalse = elementColorFalse;
  }

  /**
   * Returns the color for true values.
   * 
   * @return A color.
   */
  public Color getElementColorTrue()
  {
    return elementColorTrue;
  }

  /**
   * Returns the color for false values.
   * 
   * @return A color.
   */
  public Color getElementColorFalse()
  {
    return elementColorFalse;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    Band b = event.getReport().getItemBand();
    Element e = FunctionUtilities.findElement(b, getElement());
    if (e == null)
    {
      // there is no such element ! (we searched it by its name)
      return;
    }

    Object o = event.getDataRow().get(getField());
    boolean value;
    if (o == null)
    {
      value = false;
    }
    else
    {
      value = o.equals(Boolean.TRUE);
    }
    if (value)
    {
      e.getStyle().setStyleProperty(ElementStyleSheet.PAINT, elementColorTrue);
    }
    else
    {
      e.getStyle().setStyleProperty(ElementStyleSheet.PAINT, elementColorFalse);
    }
    super.itemsAdvanced(event);
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return null;
  }
}
