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
 * --------
 * Log.java
 * --------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: Log.java,v 1.7 2002/11/07 21:45:29 taqua Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Created a simple logging schema.
 */
package com.jrefinery.report.util;

import java.util.ArrayList;

/**
 * A simple logging facility. Create a class implementing the LogTarget interface to use
 * this feature.
 *
 * @author TM
 */
public final class Log
{

  /**
   * Loglevel ERROR
   */
  public static final int ERROR = 0;

  /**
   * Loglevel WARN
   */
  public static final int WARN = 1;

  /**
   * Loglevel INFO
   */
  public static final int INFO = 2;

  /**
   * Loglevel DEBUG
   */
  public static final int DEBUG = 3;

  /**
   * String representations of the log levels.
   */
  public static final String[] LEVELS =
          {
            "ERROR: ",
            "WARN:  ",
            "INFO:  ",
            "DEBUG: "
          };

  /** The debug level. */
  private static int debuglevel = 100;

  /** Storage for the log targets. */
  private static ArrayList logTargets = new ArrayList ();

  /**
   * Private to prevent creating instances.
   */
  private Log ()
  {
  }

  static
  {
    if (ReportConfiguration.getGlobalConfig().isDisableLogging() == false)
    {
      String className = ReportConfiguration.getGlobalConfig().getLogTarget();
      try
      {
        Class c = Class.forName(className);
        LogTarget lt = (LogTarget) c.newInstance();
        addTarget(lt);
      }
      catch (Exception e)
      {
        // unable to handle that case, log it anyway.
        e.printStackTrace();
      }
    }
    String logLevel = ReportConfiguration.getGlobalConfig().getLogLevel();
    if (logLevel.equalsIgnoreCase("error"))
    {
      debuglevel = ERROR;
    }
    else
    if (logLevel.equalsIgnoreCase("warn"))
    {
      debuglevel = WARN;
    }
    else
    if (logLevel.equalsIgnoreCase("info"))
    {
      debuglevel = INFO;
    }
    else
    if (logLevel.equalsIgnoreCase("debug"))
    {
      debuglevel = DEBUG;
    }
  }

  /**
   * Adds a log target to this facility. Log targets get informed, via the LogTarget interface,
   * whenever a message is logged with this class.
   *
   * @param target  the target.
   */
  public static void addTarget (LogTarget target)
  {
    logTargets.add (target);
  }

  /**
   * Removes a log target from this facility.
   *
   * @param target  the target to remove.
   */
  public static void removeTarget (LogTarget target)
  {
    logTargets.remove (target);
  }

  /**
   * Logs a message to the main log stream.  All attached log targets will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level  log level of the message.
   * @param message  text to be logged.
   */
  public static void log (int level, String message)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.size (); i++)
      {
        LogTarget t = (LogTarget) logTargets.get (i);
        t.log (level, message);
      }
    }
  }

  /**
   * Logs a message to the main log stream. All attached logTargets will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * The exception's stacktrace will be appended to the log-stream
   *
   * @param level  log level of the message.
   * @param message  text to be logged.
   * @param e  the exception, which should be logged.
   */
  public static void log (int level, String message, Exception e)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.size (); i++)
      {
        LogTarget t = (LogTarget) logTargets.get (i);
        t.log (level, message, e);
      }
    }
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   */
  public static void debug (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.debug (message);
    }
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void debug (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.debug (message, e);
    }
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   */
  public static void info (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.info (message);
    }
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void info (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.info (message, e);
    }
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   */
  public static void warn (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.warn (message);
    }
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void warn (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.warn (message, e);
    }
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   */
  public static void error (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.error (message);
    }
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void error (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.get (i);
      t.error (message, e);
    }
  }

  /**
   * Returns the debug level.  If the log level is not less than this, then the message will be
   * ignored.
   *
   * @return the debug level.
   */
  public static int getDebugLevel ()
  {
    return debuglevel;
  }
}
