/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultLocalizationContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Creation-Date: 03.11.2006, 14:28:12
 *
 * @author Thomas Morgner
 */
public class DefaultLocalizationContext implements LocalizationContext
{
  public DefaultLocalizationContext()
  {
  }

  public Locale getLocale()
  {
    return Locale.getDefault();
  }

  public ResourceBundle getBundle(String id)
  {
    return ResourceBundle.getBundle(id, getLocale());
  }

  public TimeZone getTimeZone()
  {
    return TimeZone.getDefault();
  }
}
