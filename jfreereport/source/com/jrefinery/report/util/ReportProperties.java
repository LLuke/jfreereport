/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------
 * ReportProperties.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 26-May-2002 : Created ReportProperties as a small scale hashtable with protected string keys.
 *               This implementation guarantees that all keys are strings.
 * 09-Jun-2002 : Documentation
 */
package com.jrefinery.report.util;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The report properties is a hashtable with string keys. ReportProperties are bound to
 * a report as an general purpose storage. ReportProperties bound to an JFreeReport-object
 * are visible to all generated report-state chains. A ReportState will inherit all
 * ReportProperties bound to the JFreeReport-object when the ReportState.Start object is created.
 * Properties bound to the report definition after the report state is created are not
 * visible to the ReportState and its children.
 * <p>
 * ReportProperties bound to a ReportState are not visible to the report definition (the
 * JFreeReport object), but are visible to all ReportStates of that ReportState-chain.
 * So when you add a property at the end of a report run to a ReportState, the value of
 * this property will be visible to all ReportStates when the report is restarted at a certain
 * point.
 * <p>
 * ReportProperties can be seen as a stateless shared report internal storage area. All functions
 * have access to the properties by using the ReportState.getProperty() and
 * ReportState.setProperty() functions.
 * <p>
 * JFreeReport has defined properties to publish the current state of the report:
 * <ul>
 * <li>JFreeReport.NAME_PROPERTY = "report.name"<p>
 * The name of the report as defined in JFreeReport.setName(). Changing this property in the
 * ReportState will not affect the ReportDefinition object.
 * <li>REPORT_DATE_PROPERTY = "report.date"<p>
 * A java.lang.Date object containing the timestamp when this reportchain was created. This
 * denotes the moment of the pagination, and changes when the report is repaginated.
 * <li>REPORT_PAGEFORMAT_PROPERTY = "report.pageformat"<p>
 * Contains the current PageFormat used for printing.
 * <li>REPORT_PAGECOUNT_PROPERTY = "report.pagecount"<p>
 * The number of pages for this report. <b>This property is not available in the prepare run.</b>
 * <li>REPORT_PREPARERUN_PROPERTY = "report.preparerun"<p>
 * The prepare run is invoked on repagination. This run collects the restart states for every
 * page of the report. When printing or displaying selected pages of the report, these saved
 * states are used as restarting points for the report generation. The prepare-run is invoked only
 * once per PageFormat. Subsequent report printings are restarted on clones of the stored page
 * states.
 *
 * @author TM
 */
public class ReportProperties implements Serializable, Cloneable
{
  /** Storage for the properties. */
  private Hashtable properties;

  /**
   * Adds a property to this properties collection. If a property with the given name
   * exist, the property will be replaced with the new value. If the
   * value is null, the property will be removed.
   *
   * @param key The key as string.
   * @param value The value.
   */
  public void put (String key, Object value)
  {
    if (value == null)
    {
      this.properties.remove (key);
    }
    else
    {
      this.properties.put (key, value);
    }
  }

  /**
   * Retrieves the value stored for a key in this properties collection.
   *
   * @param key The key as string.
   *
   * @return the stored value or null, if the key does not exist in this collection.
   */
  public Object get (String key)
  {
    return properties.get (key);
  }

  /**
   * Retrieves the value stored for a key in this properties collection, and returning
   * the default value if the key was not stored in this properties collection.
   *
   * @param key The key as string.
   * @param def the defaultvalue to be returned when the key is not stored in this properties
   * collection.
   *
   * @return the stored value or the default value, if the key does not exist in this collection.
   */
  public Object get (String key, Object def)
  {
    Object o = properties.get (key);
    if (o == null)
    {
      return def;
    }
    return o;
  }

  /**
   * Returns all property keys as enumeration.
   *
   * @return an enumeration of the property keys.
   */
  public Enumeration keys ()
  {
    return properties.keys ();
  }

  /**
   * Copy constructor.
   *
   * @param props  an existing ReportProperties instance.
   */
  public ReportProperties (ReportProperties props)
  {
    properties = new Hashtable (props.properties);
  }

  /**
   * Default constructor.
   */
  public ReportProperties ()
  {
    properties = new Hashtable ();
  }

  /**
   * Removes all properties stored in this collection.
   */
  public void clear ()
  {
    properties.clear ();
  }

  /**
   * Checks whether the given key is stored in this collection of ReportProperties.
   *
   * @param key  the property key.
   *
   * @return true, if the given key is known.
   */
  public boolean containsKey (String key)
  {
    return properties.containsKey (key);
  }

  /**
   * Clones the properties.
   *
   * @return a copy of this ReportProperties object.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    ReportProperties p = (ReportProperties) super.clone ();
    p.properties = (Hashtable) properties.clone ();
    return p;
  }
}
