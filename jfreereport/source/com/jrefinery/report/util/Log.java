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
 * Log.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 11-May-2002 : Created a simple logging schema.
 */
package com.jrefinery.report.util;

import java.util.Vector;

/**
 * A simple logging facility. Create a class implementing the LogTarget interface to use
 * this feature.
 */
public final class Log
{
  private boolean logSystemOut;

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

  public static final String[] levels =
          {
            "ERROR: ",
            "WARN:  ",
            "INFO:  ",
            "DEBUG: "
          };

  private static int debuglevel = 100;
  private static Vector logTargets = new Vector ();

  private Log ()
  {
  }

  static
  {
    if (Boolean.getBoolean ("com.jrefinery.report.NoDefaultDebug") == false)
    {
      SystemOutLogTarget.activate ();
    }
  }

  /**
   * Adds a logtarget to this facillity. Logtargets get informed whenever a event occured.
   */
  public static void addTarget (LogTarget target)
  {
    logTargets.add (target);
  }

  /**
   * removes a logtarget from this facillity.
   */
  public static void removeTarget (LogTarget target)
  {
    logTargets.remove (target);
  }

  /**
   * logs an message to the main-log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level log level of the message.
   * @param message text to be logged.
   */
  public static void log (int level, String message)
  {
    if (level > 3) level = 3;
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.size (); i++)
      {
        LogTarget t = (LogTarget) logTargets.elementAt (i);
        t.log (level, message);
      }
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
  public static void log (int level, String message, Exception e)
  {
    if (level > 3) level = 3;
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.size (); i++)
      {
        LogTarget t = (LogTarget) logTargets.elementAt (i);
        t.log (level, message, e);
      }
    }
  }

  public static void debug (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.debug (message);
    }
  }

  public static void debug (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.debug (message, e);
    }
  }

  public static void info (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.info (message);
    }
  }

  public static void info (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.info (message, e);
    }
  }

  public static void warn (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.warn (message);
    }
  }

  public static void warn (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.warn (message, e);
    }
  }

  public static void error (String message)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.error (message);
    }
  }

  public static void error (String message, Exception e)
  {
    for (int i = 0; i < logTargets.size (); i++)
    {
      LogTarget t = (LogTarget) logTargets.elementAt (i);
      t.error (message, e);
    }
  }

  public static int getDebugLevel ()
  {
    return debuglevel;
  }
}
