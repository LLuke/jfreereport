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
 * ------------------------------
 * ReportBuilderHints.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportBuilderHints.java,v 1.6 2003/08/18 21:36:24 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jul-2003 : Initial version
 *  
 */

package org.jfree.report;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.report.util.NullOutputStream;
import org.jfree.report.util.ReportConfiguration;

/**
 * The report builder hints can be used to store extra information for an
 * JFreeReport instance. This information can be used to support automated
 * processes on the JFreeReport definition.
 * <p>
 * The XML parsers use this collection to store comments and other metadata
 * for their reports.
 * <p>
 * The report builder hints are not copied when a report is cloned and are
 * not available during the report processing.
 * 
 * @author Thomas Morgner
 */
public class ReportBuilderHints implements Serializable
{
  /** 
   * Defines whether the report builder should be paranoid when accepting 
   * objects for storage. 
   */
  private static final boolean PARANOID_CHECKS = 
    ReportConfiguration.getGlobalConfig().getConfigProperty
      ("org.jfree.report.ReportBuilderHint.ParanoidChecks", "false").equals("true");
  
  /**
   * The parser hint key is a compound key to combine a string key name
   * with an arbitary target object. 
   */
  public static class ParserHintKey implements Serializable
  {
    /** The object for which to store an hint. */
    private Object primaryKey;
    /** The name of the hint. */
    private String hintKey;

    /**
     * Creates a new parser hint key for the given primary key and the given
     * key name.
     * 
     * @param primaryKey the target object for that a hint should be stored. 
     * @param hintKey the name of the hint.
     */
    public ParserHintKey(Serializable primaryKey, String hintKey)
    {
      if (primaryKey == null)
      {
        throw new NullPointerException();
      }
      if (hintKey == null)
      {
        throw new NullPointerException();
      }
      this.primaryKey = primaryKey;
      this.hintKey = hintKey;
    }

    /**
     * Returns the primary key object for that hint key.
     * 
     * @return the key object.
     */
    public Object getPrimaryKey()
    {
      return primaryKey;
    }
    
    /**
     * Returns the hint name.
     * 
     * @return the name of the hint.
     */
    public String getHintKey()
    {
      return hintKey;
    }

    /**
     * Compares whether this key points to the same object and refers to the
     * same key name.
     *   
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     * @param o the object to compare
     * @return true, if the given object is equal to this object.
     */
    public boolean equals(Object o)
    {
      if (this == o)
      { 
        return true;
      }
      if (!(o instanceof ParserHintKey))
      { 
        return false;
      }

      final ParserHintKey parserHintKey = (ParserHintKey) o;

      if (!primaryKey.equals(parserHintKey.primaryKey))
      { 
        return false;
      }
      if (!hintKey.equals(parserHintKey.hintKey))
      { 
        return false;
      }

      return true;
    }

    /**
     * Computes an hashcode for this key.
     *  
     * @see java.lang.Object#hashCode()
     * 
     * @return the hashcode.
     */
    public int hashCode()
    {
      int result;
      result = primaryKey.hashCode();
      result = 29 * result + hintKey.hashCode();
      return result;
    }

    /**
     * Prints this key as string. This is a debug function, dont depend on it.
     *  
     * @see java.lang.Object#toString()
     * 
     * @return the string representation of this object.
     */
    public String toString()
    {
      StringBuffer b = new StringBuffer();
      b.append ("ParserHintKey={");
      b.append (primaryKey);
      b.append ("; ");
      b.append (hintKey);
      b.append ("}");
      return b.toString();
    }
  }

  /** The hashmap which stores the hints. */
  private HashMap map;

  /**
   * Creates a new ReportBuilderHints instance.
   */
  public ReportBuilderHints()
  {
    this.map = new HashMap();
  }

  /**
   * Stores a new hint, replacing all previously stored hints for that key.
   * 
   * @param target the target object, to which the hint is assigned.
   * @param hint the name of the hint.
   * @param hintValue the value of the hint.
   */
  public void putHint (Serializable target, String hint, Serializable hintValue)
  {
    ParserHintKey pHint = new ParserHintKey(target, hint);
    if (hintValue == null)
    {
      map.remove(pHint);
    }
    else
    {
      map.put(pHint, hintValue);
    }
  }

  /**
   * Queries the object to get a stored hint from this collcetion.
   * 
   * @param target the target object, to which the hint is assigned.
   * @param hint the name of the hint.
   * @return the stored hint or null, if no such hint is stored for that object.
   */
  public Object getHint (Serializable target, String hint)
  {
    return map.get(new ParserHintKey(target, hint));
  }

  /**
   * Queries the object to get a stored hint from this collcetion and checks
   * that the target object has the correct type. This will return null, if the
   * hintobject is not assignable from the given object type. 
   * 
   * @param target the target object, to which the hint is assigned.
   * @param hint the name of the hint.
   * @param objectType the expected type of the stored hint. 
   * @return the stored hint or null, if no such hint is stored for that object.
   */
  public Object getHint (Serializable target, String hint, Class objectType)
  {
    Object o = map.get(new ParserHintKey(target, hint));
    if (o == null)
    {
      return null;
    }
    if (objectType.isAssignableFrom(o.getClass()))
    {
      return o;
    }
    return null;
  }

  /**
   * Serializes an given object to test whether this object is a valid serializable 
   * implementation.
   * 
   * @param o the object that should be tested.
   */
  private void serialize (Object o)
  {
    try
    {
      ObjectOutputStream out = new ObjectOutputStream(new NullOutputStream());
      out.writeObject(o);
    }
    catch (IOException ipoe)
    {
      ipoe.printStackTrace();
      throw new IllegalArgumentException("Not serializable:" + o);
    }
  }

  /**
   * Adds an hint into an ArrayList. If the hint is no list hint, a
   * IllegalArgumentException is thrown. If the speocified hint value is
   * already contained in that list, no action is taken.
   *
   * @param target the target object for which the hint is specified.
   * @param hint the hint name
   * @param hintValue the hint value (not null)
   * @throws java.lang.IllegalArgumentException if the specified hint is no list type.
   */
  public void addHintList (Serializable target, String hint, Serializable hintValue)
  {
    this.addHintList(target, hint, hintValue, true);
  }

  /**
   * Adds an hint into an ArrayList. If the hint is no list hint, a
   * IllegalArgumentException is thrown. If the speocified hint value is
   * already contained in that list, no action is taken.
   *
   * @param target the target object for which the hint is specified.
   * @param hint the hint name
   * @param hintValue the hint value (not null)
   * @param unique true, if the value should be unique within the list
   * @throws java.lang.IllegalArgumentException if the specified hint is no list type.
   */
  public void addHintList (Serializable target, String hint, Serializable hintValue, boolean unique)
  {
    if (hintValue == null)
    {
      throw new NullPointerException("Hintvalue is null.");
    }

    if (PARANOID_CHECKS)
    {
      serialize(target);
      serialize(hintValue);
    }

    Object o = getHint(target, hint);
    List hintList = null;
    if (o != null)
    {
      if (o instanceof List == false)
      {
        throw new IllegalArgumentException("The parser hint " + hint + " is no list type.");
      }
      hintList = (List) o;
    }
    else
    {
      hintList = new ArrayList();
      putHint(target, hint, (Serializable) hintList);
    }
    if (unique == false || hintList.contains(hintValue) == false)
    {
      hintList.add(hintValue);
    }
  }
}
