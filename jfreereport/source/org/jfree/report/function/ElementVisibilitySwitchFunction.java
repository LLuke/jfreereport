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
 * ------------------------------------
 * ElementVisibilitySwitchFunction.java
 * ------------------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementVisibilitySwitchFunction.java,v 1.11 2005/09/19 15:38:45 taqua Exp $
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
import org.jfree.util.Log;

/**
 * A function that alternates between true and false for each item within a group. The
 * functions value affects a defined elements visibility. If the function evaluates to
 * true, the named element is visible, else the element is invisible.
 * <p/>
 * Elements in JFreeReport do not define their own background color attribute. To create a
 * background, you would place a rectangle shape element behind the element.
 * <p/>
 * The ElementVisibilitySwitchFunction is used to trigger the visibility of an named
 * element. If the element is your background, you will get the alternating effect.
 * <p/>
 * The ElementVisibilitySwitchFunction defines two parameters: <ul> <li>element <p>The
 * name of the element in the itemband that should be modified. The element must be named
 * using the "name" attribute, only the first occurence of that named element will be
 * modfied.</p> <li>initial-state <p>The initial state of the function. (true or false)
 * defaults to false. This is the revers of the element's visiblity (set to false to start
 * with an visible element, set to true to hide the element in the first itemrow).</p>
 * </ul>
 *
 * @author Thomas Morgner
 */
public class ElementVisibilitySwitchFunction extends AbstractFunction
        implements Serializable, PageEventListener
{
  /**
   * The function value.
   */
  private transient boolean trigger;
  private transient int count;

  private int togglecount;
  /**
   * A flag indicating whether a warning has been printed.
   */
  private boolean warned;
  /**
   * A flag indicating whether a pagebreak was encountered.
   */
  private boolean pagebreak;

  private int numberOfElements;
  private String element;
  private boolean initialState;
  private boolean newPageState;

  /**
   * Default constructor.
   */
  public ElementVisibilitySwitchFunction ()
  {
    warned = false;
    numberOfElements = 1;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    pagebreak = false;
    trigger = newPageState;
    togglecount = getNumberOfElements();
    count = 0;
    triggerVisibleState(event);
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor. This method is
   * called, when a page was removed from the report after it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled (final ReportEvent event)
  {
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished (final ReportEvent event)
  {
  }

  /**
   * Receives notification that the items are being processed.  Sets the function value to
   * false. <P> Following this event, there will be a sequence of itemsAdvanced events
   * until the itemsFinished event is raised.
   *
   * @param event Information about the event.
   */
  public void itemsStarted (final ReportEvent event)
  {
    pagebreak = false;
    trigger = !getInitialState();
    togglecount = getNumberOfElements();
    count = 0;
  }

  /**
   * Triggers the visibility of an element. If the named element was visible at the last
   * itemsAdvanced call, it gets now invisible and vice versa. This creates the effect,
   * that an element is printed every other line.
   *
   * @param event the report event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    if (pagebreak)
    {
      trigger = !getInitialState();
      togglecount = getNumberOfElements();
      pagebreak = false;
    }
    triggerVisibleState(event);
  }

  /**
   * Triggers the visible state of the specified itemband element. If the named element
   * was visible at the last call, it gets now invisible and vice versa. This creates the
   * effect, that an element is printed every other line.
   *
   * @param event the current report event.
   */
  private void triggerVisibleState (final ReportEvent event)
  {
    if ((count % togglecount) == 0)
    {
      trigger = (!trigger);
    }
    count += 1;
    if (element == null)
    {
      return;
    }

    final Element[] e = FunctionUtilities.findAllElements(event.getReport().getItemBand(), getElement());
    if (e.length > 0)
    {
      for (int i = 0; i < e.length; i++)
      {
        e[i].setVisible(trigger);
      }
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

  public int getNumberOfElements ()
  {
    return numberOfElements;
  }

  public void setNumberOfElements (final int numberOfElements)
  {
    this.numberOfElements = numberOfElements;
  }

  public boolean isNewPageState()
  {
    return newPageState;
  }

  public void setNewPageState(final boolean newPageState)
  {
    this.newPageState = newPageState;
  }

  /**
   * Gets the initial value for the visible trigger, either "true" or "false".
   *
   * @return the initial value for the trigger.
   * @deprecated use getInitialState instead.
   */
  public boolean getInitialTriggerValue ()
  {
    return initialState;
  }

  /**
   * Gets the initial value for the visible trigger, either "true" or "false".
   *
   * @return the initial value for the trigger.
   */
  public boolean getInitialState ()
  {
    return initialState;
  }


  /**
   * Sets the element name. The name denotes an element within the item band. The element
   * will be retrieved using the getElement(String) function.
   *
   * @param name The element name.
   * @see org.jfree.report.Band#getElement(String)
   */
  public void setElement (final String name)
  {
    element = name;
  }

  public void setInitialState (final boolean initialState)
  {
    this.initialState = initialState;
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   */
  public String getElement ()
  {
    return element;
  }

  /**
   * Returns the defined visibility of the element. Returns either true or false as
   * java.lang.Boolean.
   *
   * @return the visibility of the element, either Boolean.TRUE or Boolean.FALSE.
   */
  public Object getValue ()
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

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (final ReportEvent event)
  {
    // although it should not matter, we do that for completeness
    // next event will be a pageFinished event ..
    count -= 1;
  }
}
