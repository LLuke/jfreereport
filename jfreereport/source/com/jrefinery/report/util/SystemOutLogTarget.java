/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * 11-May-2002 : Initial version
 */
package com.jrefinery.report.util;

import java.io.Serializable;

/**
 * prints all log-messages to System.out stream.
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
    if (level > 3) level = 3;
    if (level <= Log.getDebugLevel ())
    {
      System.out.println (levels[level] + message);
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
    if (level > 3) level = 3;
    if (level <= Log.getDebugLevel ())
    {
      System.out.println (levels[level] + message);
      e.printStackTrace (System.out);
    }
  }

  public void debug (String message)
  {
    log (DEBUG, message);
  }

  public void debug (String message, Exception e)
  {
    log (DEBUG, message, e);
  }

  public void info (String message)
  {
    log (INFO, message);
  }

  public void info (String message, Exception e)
  {
    log (INFO, message, e);
  }

  public void warn (String message)
  {
    log (WARN, message);
  }

  public void warn (String message, Exception e)
  {
    log (WARN, message, e);
  }

  public void error (String message)
  {
    log (ERROR, message);
  }

  public void error (String message, Exception e)
  {
    log (ERROR, message, e);
  }

  private static SystemOutLogTarget defaultTarget;

  public static void activate ()
  {
    if (defaultTarget == null)
    {
      defaultTarget = new SystemOutLogTarget ();
      Log.addTarget (defaultTarget);
    }
  }

  public static void deactivate ()
  {
    if (defaultTarget != null)
    {
      Log.removeTarget (defaultTarget);
      defaultTarget = null;
    }
  }
}
