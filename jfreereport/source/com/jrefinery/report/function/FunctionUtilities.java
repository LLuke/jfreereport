/**
 * Date: Mar 7, 2003
 * Time: 6:22:53 PM
 *
 * $Id: FunctionUtilities.java,v 1.1 2003/03/07 18:59:39 taqua Exp $
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Element;
import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;

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
   * @return
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

  public static boolean isLayoutLevel(ReportEvent event)
  {
    if (event == null)
    {
      throw new NullPointerException("ReportEvent is null");
    }
    return (event.getState().getLevel() < 0);
  }
}
