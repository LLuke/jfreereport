/**
 * Date: Mar 7, 2003
 * Time: 6:36:32 PM
 *
 * $Id: TotalCalculationFunction.java,v 1.3 2003/05/16 17:26:42 taqua Exp $
 */
package com.jrefinery.report.function;

import java.util.ArrayList;

import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that stores the result of a calculation for a group or the
 * complete report. The field value, that was read when the group finished, is
 * stored and returned when the group gets active again in a higher processing level.
 * <p>
 * This function can be used to calculate total values for a group, f.I. a TotalMax
 * for the group, which returns the maximum value encountered for that field in the
 * current group. The computed values are available to all bands of the group.
 * <p>
 * The function undestands two parameters, the <code>field</code> parameter is required and
 * denotes the name of an ItemBand-field which gets summed up.
 * <p>
 * The parameter <code>group</code> denotes the name of a group. When this group is started,
 * the counter gets reseted to null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalCalculationFunction extends AbstractFunction
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** A list of results. */
  private ArrayList storedResults;

  /** The current index. */
  private int currentIndex;

  /** The current object. */
  private Object currentObject;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalCalculationFunction ()
  {
    storedResults = new ArrayList();
  }


  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportInitialized(ReportEvent event)
  {
    currentIndex = -1;
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      storedResults.clear();
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      // wrong group ...
      return;
    }

    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      currentObject = null;
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      if (event.getState().isPrepareRun() == false)
      {
        currentIndex += 1;
        currentObject = storedResults.get(currentIndex);
      }
    }
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      // wrong group ...
      return;
    }

    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      storedResults.add(currentObject);
    }
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Defines the name of the group to be totalled.
   * If the name is null, all groups are totalled.
   *
   * @param group  the group name.
   */
  public void setGroup(String group)
  {
    setProperty(GROUP_PROPERTY, group);
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
    return currentObject;
  }
}
