/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------------
 * ElementVisiblitySwitchFunction.java
 * -----------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.5 2002/05/28 19:36:41 taqua Exp $
 *
 * Changes (since 5-Jun-2002)
 * --------------------------
 * 05-Jun-2002 : Changed name from ElementVisiblity --> ElementVisibility.  Updated Javadoc
 *               comments (DG);
 */
package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.Element;

/**
 * A function that alternates between true and false for each item within a group.
 */
public class ElementVisiblitySwitchFunction extends AbstractFunction
{

  /** The function value. */
  private boolean trigger;

  /**
   * Default constructor.
   */
  public ElementVisiblitySwitchFunction ()
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
  public void itemsStarted (ReportEvent event)
  {
    trigger = false;
  }

  /**
   * Maps the pageStarted-method to the legacy function startPage (int).
   */
  public void itemsAdvanced (ReportEvent event)
  {
    if (event.getState().isPrepareRun()) return;

    trigger = (!trigger);
    Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      e.setVisible(trigger);
    }
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   */
  public void initialize () throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty("element") == null)
      throw new FunctionInitializeException("Element name must be specified");
  }

  /**
   * Sets the element name.
   *
   * @param name The element name.
   */
  public void setElement (String name)
  {
    setProperty("element", name);
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   */
  public String getElement ()
  {
    return getProperty("element", "");
  }

  public Object getValue ()
  {
    if (trigger)
      return Boolean.TRUE;
    else
      return Boolean.FALSE;
  }
}
