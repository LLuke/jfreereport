/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * -------------
 * StyleKey.java
 * -------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleKey.java,v 1.3 2002/12/06 17:37:37 mungady Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.style;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * A style key represents a (key, class) pair.  Style keys are used to access style attributes
 * defined in a <code>BandStyleSheet</code> or an <code>ElementStyleSheet</code>
 * <p>
 * Note that this class also defines a static Hashtable in which all defined keys are 
 * stored.
 *
 * @see BandStyleSheet
 * @see ElementStyleSheet
 *
 * @author Thomas Morgner
 */
public class StyleKey implements Serializable, Cloneable
{
  /** Shared storage for the defined keys. */
  private static Hashtable definedKeys;

  /** The name of the style key. */
  private String name;
  
  /** The class of the value. */
  private Class valueType;

  /**
   * Creates a new style key.
   *
   * @param name  the name.
   * @param valueType  the class of the value for this key.
   */
  private StyleKey(String name, Class valueType)
  {
    setName(name);
    setValueType(valueType);
  }

  /**
   * Returns the name of the key.
   *
   * @return the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the key.
   *
   * @param name  the name (null not permitted).
   */
  private void setName(String name)
  {
    if (name == null)
    {
      throw new NullPointerException("StyleKey.setName(...): null not permitted.");
    }
    this.name = name;
  }

  /**
   * Returns the class of the value for this key.
   *
   * @return the class.
   */
  public Class getValueType()
  {
    return valueType;
  }

  /**
   * Sets the class of the value for this key.
   *
   * @param valueType  the class.
   */
  private void setValueType(Class valueType)
  {
    if (valueType == null) 
    {
      throw new NullPointerException("ValueType must not be null");
    }
    this.valueType = valueType;
  }

  /**
   * Returns the key with the specified name.
   *
   * @param name  the name.
   * @param valueType  the class.
   * 
   * @return the style key.
   */
  public static StyleKey getStyleKey (String name, Class valueType)
  {
    if (definedKeys == null)
    {
      definedKeys = new Hashtable();
    }
    StyleKey key = (StyleKey) definedKeys.get (name);
    if (key == null)
    {
      key = new StyleKey(name, valueType);
      definedKeys.put (name, key);
    }
    return key;
  }

  /**
   * Returns the key with the specified name.
   *
   * @param name  the name.
   * 
   * @return the style key.
   */
  public static StyleKey getStyleKey (String name)
  {
    if (definedKeys == null)
    {
      return null;
    }
    else
    {
      return (StyleKey) definedKeys.get (name);
    }
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param   o  the reference object with which to compare.
   * @return  <code>true</code> if this object is the same as the obj
   *          argument; <code>false</code> otherwise.
   */
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof StyleKey)) return false;

    final StyleKey key = (StyleKey) o;

    if (!name.equals(key.name)) return false;
    if (!valueType.equals(key.valueType)) return false;

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * <p>
   *
   * @return  a hash code value for this object.
   */
  public int hashCode()
  {
    int result;
    result = name.hashCode();
    result = 29 * result + valueType.hashCode();
    return result;
  }

  /**
   * Replaces the automaticly generated instance with one of the defined
   * stylekey instances or creates a new stylekey.
   *
   * @return the resolved element
   * @throws java.io.ObjectStreamException if the element could not be resolved.
   */
  protected Object readResolve() throws ObjectStreamException
  {
    StyleKey key = getStyleKey(name);
    if (key != null) return key;
    return getStyleKey(name, valueType);
  }
}
