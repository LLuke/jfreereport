/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ------------------------------------
 * ElementVisibilitySwitchFunction.java
 * ------------------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementVisibilitySwitchFunction.java,v 1.9 2002/12/12 12:26:55 mungady Exp $
 *
 * Changes (since 5-Jun-2002)
 * --------------------------
 * 05-Jun-2002 : Changed name from ElementVisiblity --> ElementVisibility.  Updated Javadoc
 *               comments (DG);
 * 08-Jun-2002 : Completed Javadoc comments.
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Element;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportEvent;

/**
 * A function that alternates between true and false for each item within a group. The functions
 * value affects a defined elements visibility. If the function evaluates to true, the named
 * element is visible, else the element is invisible.
 * <p>
 * Use the property <code>element</code> to name an element contained in the ItemBand whose
 * visiblity should be affected by this function.
 *
 * @author Thomas Morgner
 */
public class ElementVisibilitySwitchFunction extends AbstractFunction
{
  /** the Property key for the name of the ItemBand element. */
  public static final String ELEMENT_PROPERTY = "element";

  /** The function value. */
  private boolean trigger;

  /**
   * Default constructor.
   */
  public ElementVisibilitySwitchFunction()
  {
  }

  /**
   * Receives notification that the items are being processed.  Sets the function value to false.
   * <P>
   * Following this event, there will be a sequence of itemsAdvanced events until the itemsFinished
   * event is raised.
   *
   * @param event Information about the event.
   */
  public void itemsStarted(ReportEvent event)
  {
    trigger = false;
  }

  /**
   * Triggers the visibility of an element. If the named element was visible at the last
   * itemsAdvanced call, it gets now invisible and vice versa. This creates the effect,
   * that an element is printed every other line.
   *
   * @param event  the report event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      return;
    }

    trigger = (!trigger);
    Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      e.setVisible(trigger);
    }
    else
    {
      Log.debug ("Element not defined in the item band");
    }
  }

  /**
   * Checks that the function has been correctly initialized. The functions name or the
   * elements name have not been set, and FunctionInitializeException is thrown.
   *
   * @throws FunctionInitializeException if required parameters were missing and initialisation
   * cannot be performed.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty(ELEMENT_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Element name must be specified");
    }
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
   * @return the visibility of the element, either Boolean.TRUE or Boolean.FALSE.
   */
  public Object getValue()
  {
    if (trigger)
    {
      return Boolean.TRUE;
    }
    else
    {
      return Boolean.FALSE;
    }
  }

}
