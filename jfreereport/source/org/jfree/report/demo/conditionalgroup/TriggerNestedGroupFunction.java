/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * TriggerNestedGroupFunction.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TriggerNestedGroupFunction.java,v 1.4 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.conditionalgroup;

import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;
import org.jfree.util.Log;

public class TriggerNestedGroupFunction extends AbstractFunction
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public TriggerNestedGroupFunction ()
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    // first check, if this event activates the correct group
    if (FunctionUtilities.isDefinedGroup("conditional-level-group", event) == false)
    {
      return;
    }
    // and if so, then get a reference to that group
    final Group group = FunctionUtilities.getCurrentGroup(event);

    // check, if all required bands are defined and return if one
    // of them is missing.
    final Element normalItemBand =
            event.getReport().getItemBand().getElement("FirstLevel");
    final Element nestedItemBand =
            event.getReport().getItemBand().getElement("SecondLevel");
    if (normalItemBand == null || nestedItemBand == null)
    {
      Log.warn("IllegalState: Child-Itembands not found.");
      return;
    }

    // and now apply the visiblity to all bands affected
    final boolean isNestedGroup =
            (event.getDataRow().get("level-two-account") != null);
    //Log.warn("Is Nested Group: " + event.getDataRow().get("level-one-account") + " -> " + isNestedGroup);
    if (isNestedGroup)
    {
      normalItemBand.setVisible(false);
      nestedItemBand.setVisible(true);
      group.getHeader().setVisible(true);
      group.getFooter().setVisible(true);
    }
    else
    {
      normalItemBand.setVisible(true);
      nestedItemBand.setVisible(false);
      group.getHeader().setVisible(false);
      group.getFooter().setVisible(false);
    }
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
}
