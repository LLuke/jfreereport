/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ----------------------
 * FunctionUtilities.java
 * ----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Function.java,v 1.13 2002/12/12 12:26:56 mungady Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Version 1;
 * 
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;

/**
 * A collection of utility methods relating to functions.
 * 
 * @author Thomas Morgner.
 */
public class FunctionUtilities
{
  /**
   * Try to find the defined element in the last active root-band.
   *
   * @param band the band that is suspected to contain the element.
   * @return the found element or null, if no element could be found.
   */
  public static Element findElement (Band band, String element)
  {
    if (element == null)
    {
      throw new NullPointerException("Element name must not be null");
    }

    Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (e instanceof Band)
      {
        return findElement((Band) e, element);
      }
      else if (e.getName().equals(element))
      {
        return e;
      }
    }
    return null;
  }

  /**
   * Returns true if ??.
   * 
   * @param groupName  the group name.
   * @param event  the report event.
   */
  public static boolean isGroupInGroup (String groupName, ReportEvent event)
  {
    if (groupName == null)
    {
      return false;
    }

    Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    if (groupName.equals(group.getName()))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns true, if the current run level is defined for the given function and
   * this is a prepare run.
   *  
   * @param f
   * @param event
   * 
   * @return A boolean.
   */
  public static boolean isDefinedPrepareRunLevel (Function f, ReportEvent event)
  {
    if (f == null)
    {
      throw new NullPointerException("Function is null");
    }

    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }

    if (event.getState().isPrepareRun() == false)
    {
      return false;
    }
    return (event.getState().getLevel() == f.getDependencyLevel());
  }

  /**
   * Returns true or false.
   * 
   * @param event  the report event.
   */
  public static boolean isLayoutLevel(ReportEvent event)
  {
    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }
    return (event.getState().getLevel() < 0);
  }
}
