/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ClassConfigDescriptionEntry.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ClassConfigDescriptionEntry.java,v 1.6 2004/05/07 14:29:24 mungady Exp $
 *
 * Changes 
 * -------------------------
 * 26-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

/**
 * A config description entry that describes class name configurations. The specified
 * class in the configuration is forced to be a subclass of the specified base class.
 *
 * @author Thomas Morgner
 */
public class ClassConfigDescriptionEntry extends ConfigDescriptionEntry
{
  /**
   * The base class for the configuration value.
   */
  private Class baseClass;

  /**
   * Creates a new config description entry.
   *
   * @param keyName the full name of the key.
   */
  public ClassConfigDescriptionEntry (final String keyName)
  {
    super(keyName);
    baseClass = Object.class;
  }

  /**
   * Returns the base class used to verify the configuration values.
   *
   * @return the base class or Object.class if not specified otherwise.
   */
  public Class getBaseClass ()
  {
    return baseClass;
  }

  /**
   * Defines the base class for this configuration entry.
   *
   * @param baseClass the base class, never null.
   */
  public void setBaseClass (final Class baseClass)
  {
    if (baseClass == null)
    {
      throw new NullPointerException();
    }
    this.baseClass = baseClass;
  }
}
