/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * StyleChangeListener.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StyleChangeListener.java,v 1.3 2004/05/07 08:14:24 mungady Exp $
 *
 * Changes
 * -------
 * 18.03.2003 : Initial version
 */
package org.jfree.report.style;

/**
 * The interface that must be supported by objects that wish to receive notification of
 * style change events.
 *
 * @author Thomas Morgner
 */
public interface StyleChangeListener
{
  /**
   * Receives notification that a style has changed.
   *
   * @param source the source of the change.
   * @param key    the style key.
   * @param value  the value.
   */
  public void styleChanged (ElementStyleSheet source, StyleKey key, Object value);

  /**
   * Receives notification that a style has been removed.
   *
   * @param source the source of the change.
   * @param key    the style key.
   */
  public void styleRemoved (ElementStyleSheet source, StyleKey key);
}
