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
 * $Id: SystemOutLogTarget.java,v 1.4 2003/09/02 15:05:34 taqua Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Initial version
 * 17-Dec-2002 : Javadoc updates (DG);
 * 05-Feb-2003 : Interface cleanup - removed unnecessary methods.
 */

package org.jfree.report.util;

import java.io.Serializable;
import java.io.PrintStream;

import org.jfree.util.LogTarget;

/**
 * A log target that sends all log messages to the <code>System.out</code> stream.
 *
 * @author Thomas Morgner
 */
public class SystemOutLogTarget implements LogTarget, Serializable
{
  /** The printstream we use .. */
  private PrintStream printStream;

  /**
   * The default constructor. Initializes this target with the system.out stream.
   * <p>
   * All {@link org.jfree.util.LogTarget} implementations need a default constructor.
   */
  public SystemOutLogTarget()
  {
    this (System.out);
  }

  /**
   * The default constructor. Initializes this target with the given stream.
   * <p>
   * @param printStream the print stream that is used to write the content.
   */
  public SystemOutLogTarget(PrintStream printStream)
  {
    if (printStream == null)
    {
      throw new NullPointerException();
    }
    this.printStream = printStream;
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
    printStream.print(LEVELS[level]);
    printStream.println(message);
    if (level < 3)
    {
      System.out.flush();
    }
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
    printStream.print(LEVELS[level]);
    printStream.println(message);
    e.printStackTrace(printStream);
    if (level < 3)
    {
      System.out.flush();
    }
  }
}
