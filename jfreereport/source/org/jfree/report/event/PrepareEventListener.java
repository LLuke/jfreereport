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
 * -------------------------
 * PrepareEventListener.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PrepareEventListener.java,v 1.3 2004/05/07 08:03:37 mungady Exp $
 *
 * Changes
 * -------
 * 26.03.2003 : Initial version
 */
package org.jfree.report.event;

import java.util.EventListener;

/**
 * An interface for receiving notification of prepare events.
 *
 * @author Thomas Morgner.
 */
public interface PrepareEventListener extends EventListener
{
  /**
   * Receives notification of a prepare event.
   *
   * @param event the event.
   */
  public void prepareEvent (ReportEvent event);
}
