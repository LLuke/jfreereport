/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------------
 * ElementVisibilitySwitchFunction.java
 * ------------------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementVisibilitySwitchFunction.java,v 1.27 2003/06/29 16:59:24 taqua Exp $
 *
 * Changes (since 5-Jun-2002)
 * --------------------------
 * 05-Jun-2002 : Changed name from ElementVisiblity --> ElementVisibility.  Updated Javadoc
 *               comments (DG);
 * 08-Jun-2002 : Completed Javadoc comments.
 * 24-Mar-2003 : Bug: After page breaks, the visible state could be invalid.
 */
package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.Element;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.Log;

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
    implements Serializable, PageEventListener
{
  /** the Property key for the name of the ItemBand element. */
  public static final String ELEMENT_PROPERTY = "element";

  /** The initial state property key. */
  public static final String INITIAL_STATE_PROPERTY = "initial-state";

  /** The function value. */
  private boolean trigger;
  /** A flag indicating whether a waring has been printed. */
  private boolean warned;
  /** A flag indicating whether a pagebreak was encountered. */
  private boolean pagebreak;

  /**
   * Default constructor.
   */
  public ElementVisibilitySwitchFunction()
  {
    warned = false;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    pagebreak = false;
    trigger = (getInitialTriggerValue()); // docmark
    triggerVisibleState(event);
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   * This method is called, when a page was removed from the report after
   * it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled(ReportEvent event)
  {
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished(ReportEvent event)
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
  public void itemsStarted(final ReportEvent event)
  {
    pagebreak = false;
    trigger = (getInitialTriggerValue()); // docmark
  }

  /**
   * Triggers the visibility of an element. If the named element was visible at the last
   * itemsAdvanced call, it gets now invisible and vice versa. This creates the effect,
   * that an element is printed every other line.
   *
   * @param event  the report event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (pagebreak)
    {
      trigger = (getInitialTriggerValue()); // docmark
      pagebreak = false;
    }
    triggerVisibleState(event);
  }

  /**
   * Triggers the visible state of the specified itemband element. If the named element
   * was visible at the last call, it gets now invisible and vice versa. This creates
   * the effect, that an element is printed every other line.
   *
   * @param event the current report event.
   */
  private void triggerVisibleState(final ReportEvent event)
  {
    trigger = (!trigger);

    final Element e = FunctionUtilities.findElement(event.getReport().getItemBand(), getElement());
    if (e != null)
    {
      e.setVisible(trigger);
    }
    else
    {
      if (warned == false)
      {
        Log.warn(new Log.SimpleMessage("Element ", getElement(), "not defined in the item band"));
        warned = true;
      }
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
    pagebreak = false;
  }

  /**
   * Gets the initial value for the visible trigger, either "true" or "false".
   *
   * @return the initial value for the trigger.
   */
  public boolean getInitialTriggerValue()
  {
    return getProperty(INITIAL_STATE_PROPERTY, "false").equalsIgnoreCase("true");
  }

  /**
   * Sets the element name. The name denotes an element within the item band. The element
   * will be retrieved using the getElement(String) function.
   *
   * @param name The element name.
   * @see org.jfree.report.Band#getElement(String)
   */
  public void setElement(final String name)
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
   * Returns the defined visibility of the element. Returns either true or false as
   * java.lang.Boolean.
   *
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
