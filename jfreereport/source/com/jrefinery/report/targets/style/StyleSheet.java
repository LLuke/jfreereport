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
 * ---------------
 * StyleSheet.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheet.java,v 1.1 2002/12/02 17:57:08 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 */
package com.jrefinery.report.targets.style;

/**
 * A style-sheet interface.
 *
 * @author Thomas Morgner
 */
public interface StyleSheet
{
  /**
   * Returns the name of the style-sheet.
   *
   * @return the name.
   */
  public String getName ();

  /**
   * Returns the style property value for a given key.
   *
   * @param key  the style key.
   *
   * @return the property value.
   */
  public Object getStyleProperty (StyleKey key);
  
  /**
   * Returns the style property value for a given key.  If the key is not found, the default value 
   * is returned.
   *
   * @param key  the style key.
   * @param value  the default value.
   *
   * @return the property value.
   */
  public Object getStyleProperty (StyleKey key, Object value);
  
}
