/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------
 * SystemOutLogTarget.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SystemOutLogTarget.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Initial version
 * 17-Dec-2002 : Javadoc updates (DG);
 * 05-Feb-2003 : Interface cleanup - removed unnecessary methods.
 */

package org.jfree.report.util;

import java.io.Serializable;

import org.jfree.util.LogTarget;

/**
 * A log target that sends all log messages to the <code>System.out</code> stream.
 *
 * @author Thomas Morgner
 */
public class SystemOutLogTarget implements LogTarget, Serializable
{
  /**
   * The default constructor.
   * <p>
   * All {@link org.jfree.util.LogTarget} implementations need a default constructor.
   */
  public SystemOutLogTarget()
  {
  }

  /**
   * Logs a message to the main log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level log level of the message.
   * @param message text to be logged.
   */
  public void log(int level, final Object message)
  {
    if (level > 3)
    {
      level = 3;
    }
    System.out.print(LEVELS[level]);
    System.out.println(message);
  }

  /**
   * logs an message to the main-log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * The exception's stacktrace will be appended to the log-stream
   *
   * @param level log level of the message.
   * @param message text to be logged.
   * @param e the exception, which should be logged.
   */
  public void log(int level, final Object message, final Exception e)
  {
    if (level > 3)
    {
      level = 3;
    }
    System.out.print(LEVELS[level]);
    System.out.println(message);
    e.printStackTrace(System.out);
  }
}
