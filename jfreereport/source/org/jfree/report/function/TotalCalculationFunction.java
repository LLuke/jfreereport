/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * TextFormatExpression.java
 * -------------------------
 * (C)opyright 2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TotalCalculationFunction.java,v 1.7 2003/11/10 20:01:59 taqua Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Initial version
 *
 */
package org.jfree.report.function;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.jfree.report.event.ReportEvent;

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
public class TotalCalculationFunction
    extends AbstractFunction implements Serializable
{
  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** A list of results. */
  private transient ArrayList storedResults;

  /** The current index. */
  private int currentIndex;

  /** The current object. */
  private transient Object currentObject;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalCalculationFunction()
  {
    storedResults = new ArrayList();
  }


  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    currentIndex = -1;
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      storedResults.clear();
    }
    else
    {
      if (getGroup() == null)
      {
        storedResults.get (0);
      }
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
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
  public void groupFinished(final ReportEvent event)
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
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    if (getGroup() == null)
    {
      storedResults.add (currentObject);
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
  public void setGroup(final String group)
  {
    setProperty(GROUP_PROPERTY, group);
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getField()
  {
    return getProperty(FIELD_PROPERTY);
  }

  /**
   * Defines the name of the group to be totalled.
   * If the name is null, all groups are totalled.
   *
   * @param field the field name.
   */
  public void setField(final String field)
  {
    setProperty(FIELD_PROPERTY, field);
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

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws java.io.IOException when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object
   * could not be found.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    storedResults = new ArrayList();
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
    if (getField() == null)
    {
      throw new FunctionInitializeException("Field must be defined.");
    }
  }

}
