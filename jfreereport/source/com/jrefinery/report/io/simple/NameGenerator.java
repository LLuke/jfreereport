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
 * -----------------
 * NameGenerator.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NameGenerator.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 *
 * Changes
 * -------
 * 12-Jan-2003 : Initial version.
 */
package com.jrefinery.report.io.simple;

/**
 * The name generator is used to generate unique names for elements and bands.
 */
public class NameGenerator
{
  /**
   * DefaultConstructor.
   */
  public NameGenerator()
  {
  }

  /**
   * The namecounter is used to create unique element names. After a name has been
   * created, the counter is increased by one.
   */
  private int nameCounter;

  /**
   * If a name is supplied, then this method simply returns it.  Otherwise, if name is null, then
   * a unique name is generating by appending a number to the prefix '@anonymous'.
   *
   * @param name The name.
   *
   * @return a non-null name.
   */
  public String generateName (String name)
  {
    if (name == null)
    {
      nameCounter += 1;
      return "@anonymous" + Integer.toHexString (nameCounter);
    }
    return name;
  }

}
