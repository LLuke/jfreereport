/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Log.java,v 1.15 2003/02/04 17:56:33 taqua Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Created a simple logging schema.
 * 12-Nov-2002 : Removed redundant import (DG).
 * 10-Dec-2002 : Updated Javadocs (DG);
 * 17-Dec-2002 : Removed LEVELS since it is not used (it is also declared in LogTarget) (DG);
 * 05-Feb-2002 : Interface cleanUp, switched from ArrayList to LogTarget[] 
 */

package com.jrefinery.report.util;

import java.util.Arrays;
import java.util.List;

/**
 * A simple logging facility. Create a class implementing the {@link LogTarget} interface to use
 * this feature.
 *
 * @author Thomas Morgner
 */
public final class Log
{
  /**
   * A helper class to print memory usage message if needed.
   */
  public static class MemoryUsageMessage
  {
    /** The message. */
    private String message;

    /**
     * Creates a new message.
     *
     * @param message  the message.
     */
    public MemoryUsageMessage(String message)
    {
      this.message = message;
    }

    /**
     * Returns a string representation of the message (useful for debugging).
     *
     * @return the string.
     */
    public String toString ()
    {
      return (message
                + "Free: " + Runtime.getRuntime().freeMemory() + "; "
                + "Total: " + Runtime.getRuntime().totalMemory());
    }
  }

  /**
   * A simple message class.
   */
  public static class SimpleMessage
  {
    /** The message. */
    private String message;

    /** The parameters. */
    private Object[] param;

    /**
     * Creates a new message.
     *
     * @param message  the message text.
     * @param param1  parameter 1.
     */
    public SimpleMessage(String message, Object param1)
    {
      this.message = message;
      this.param = new Object[] {param1};
    }

    /**
     * Creates a new message.
     *
     * @param message  the message text.
     * @param param1  parameter 1.
     * @param param2  parameter 2.
     */
    public SimpleMessage(String message, Object param1, Object param2)
    {
      this.message = message;
      this.param = new Object[] {param1, param2};
    }

    /**
     * Creates a new message.
     *
     * @param message  the message text.
     * @param param1  parameter 1.
     * @param param2  parameter 2.
     * @param param3  parameter 3.
     */
    public SimpleMessage(String message, Object param1, Object param2, Object param3)
    {
      this.message = message;
      this.param = new Object[] {param1, param2, param3};
    }

    /**
     * Creates a new message.
     *
     * @param message  the message text.
     * @param param1  parameter 1.
     * @param param2  parameter 2.
     * @param param3  parameter 3.
     * @param param4  parameter 4.
     */
    public SimpleMessage(String message, Object param1, Object param2, Object param3, Object param4)
    {
      this.message = message;
      this.param = new Object[] {param1, param2, param3, param4};
    }

    /**
     * Creates a new message.
     *
     * @param message  the message text.
     * @param param  the parameters.
     */
    public SimpleMessage(String message, Object[] param)
    {
      this.message = message;
      this.param = param;
    }

    /**
     * Returns a string representation of the message (useful for debugging).
     *
     * @return the string.
     */
    public String toString ()
    {
      StringBuffer b = new StringBuffer();
      b.append(message);
      if (param != null)
      {
        for (int i = 0; i < param.length; i++)
        {
          b.append(param[i]);
        }
      }
      return b.toString();
    }
  }

  /** The log level for error messages. */
  public static final int ERROR = 0;

  /** The log level for warning messages. */
  public static final int WARN = 1;

  /** The log level for information messages. */
  public static final int INFO = 2;

  /** The log level for debug messages. */
  public static final int DEBUG = 3;

  /** The logging threshold. */
  private static int debuglevel = 100;

  /** Storage for the log targets. */
  private static LogTarget[] logTargets;

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
        Class c = ReportConfiguration.getGlobalConfig().getClass().
            getClassLoader().loadClass(className);
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
  public static synchronized void addTarget (LogTarget target)
  {
    if (target == null)
      throw new NullPointerException();

    LogTarget[] data = new LogTarget[logTargets.length + 1];
    System.arraycopy(logTargets, 0, data, 0, logTargets.length);
    data[logTargets.length] = target;
    logTargets = data;
  }

  /**
   * Removes a log target from this facility.
   *
   * @param target  the target to remove.
   */
  public synchronized static void removeTarget (LogTarget target)
  {
    if (target == null)
      throw new NullPointerException();

    List l = Arrays.asList(logTargets);
    l.remove(target);

    LogTarget[] targets = new LogTarget[l.size()];
    logTargets = (LogTarget[]) l.toArray(targets);
  }

  /**
   * Logs a message to the main log stream.  All attached log targets will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level  log level of the message.
   * @param message  text to be logged.
   */
  public static void log (int level, Object message)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.length; i++)
      {
        LogTarget t = logTargets[i];
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
  public static void log (int level, Object message, Exception e)
  {
    if (level > 3)
    {
      level = 3;
    }
    if (level <= debuglevel)
    {
      for (int i = 0; i < logTargets.length; i++)
      {
        LogTarget t = logTargets[i];
        t.log (level, message, e);
      }
    }
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   */
  public static void debug (Object message)
  {
    log (LogTarget.DEBUG, message);
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void debug (Object message, Exception e)
  {
    log (LogTarget.DEBUG, message, e);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   */
  public static void info (Object message)
  {
    log (LogTarget.INFO, message);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void info (Object message, Exception e)
  {
    log (LogTarget.INFO, message, e);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   */
  public static void warn (Object message)
  {
    log (LogTarget.WARN, message);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void warn (Object message, Exception e)
  {
    log (LogTarget.WARN, message, e);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   */
  public static void error (Object message)
  {
    log (LogTarget.ERROR, message);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void error (Object message, Exception e)
  {
    log (LogTarget.ERROR, message, e);
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
