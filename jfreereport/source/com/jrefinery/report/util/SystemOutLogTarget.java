/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------------
 * SystemOutLogTarget.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 * 11-May-2002 : Initial version
 */
package com.jrefinery.report.util;

import java.io.Serializable;

/**
 * Prints all log-messages to System.out stream.
 *
 * @author TM
 */
public class SystemOutLogTarget implements LogTarget, Serializable
{
  /**
   * logs an message to the main-log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level log level of the message.
   * @param message text to be logged.
   */
  public void log (int level, String message)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= Log.getDebugLevel ())
    {
      System.out.println (LEVELS[level] + message);
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
  public void log (int level, String message, Exception e)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= Log.getDebugLevel ())
    {
      System.out.println (LEVELS[level] + message);
      e.printStackTrace (System.out);
    }
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   */
  public void debug (String message)
  {
    log (DEBUG, message);
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void debug (String message, Exception e)
  {
    log (DEBUG, message, e);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   */
  public void info (String message)
  {
    log (INFO, message);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void info (String message, Exception e)
  {
    log (INFO, message, e);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   */
  public void warn (String message)
  {
    log (WARN, message);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void warn (String message, Exception e)
  {
    log (WARN, message, e);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   */
  public void error (String message)
  {
    log (ERROR, message);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void error (String message, Exception e)
  {
    log (ERROR, message, e);
  }
}
