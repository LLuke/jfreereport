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
 * EnumConfigDescriptionEntry.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: EnumConfigDescriptionEntry.java,v 1.5 2004/05/07 14:29:24 mungady Exp $
 *
 * Changes 
 * -------------------------
 * 26-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

/**
 * The enumeration config description entry represents an configuration key, where users
 * may select a valid value from a predefined list of elements. Such an key will not allow
 * free-form text.
 *
 * @author Thomas Morgner
 */
public class EnumConfigDescriptionEntry extends ConfigDescriptionEntry
{
  /**
   * The list of available options in this entry.
   */
  private String[] options;

  /**
   * Creates a new enumeration description entry for the given configuration key.
   *
   * @param keyName the keyname of this entry.
   */
  public EnumConfigDescriptionEntry (final String keyName)
  {
    super(keyName);
    this.options = new String[0];
  }

  /**
   * Returns all options from this entry as array.
   *
   * @return the options as array.
   */
  public synchronized String[] getOptions ()
  {
    final String[] retval = new String[options.length];
    System.arraycopy(options, 0, retval, 0, options.length);
    return retval;
  }

  /**
   * Defines all options for this entry.
   *
   * @param options the selectable values for this entry.
   */
  public synchronized void setOptions (final String[] options)
  {
    this.options = new String[options.length];
    System.arraycopy(options, 0, this.options, 0, options.length);
  }
}
