/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * JFreeReportStyleKeys.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12.02.2006 : Initial version
 */
package org.jfree.layouting.input.style.keys.jfreereport;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * All kind of StyleKeys needed for compatiblity with the old display model
 * of JFreeReport.
 *
 * @author Thomas Morgner
 */
public class JFreeReportStyleKeys
{
  public static final StyleKey POSITION_X =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-jfreereport-x", true, false, false);

  public static final StyleKey POSITION_Y =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-jfreereport-y", true, true, false);

  private JFreeReportStyleKeys()
  {
  }

}
