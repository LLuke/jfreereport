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
 * --------
 * Log.java
 * --------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Log.java,v 1.6 2003/08/24 15:13:23 taqua Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Created a simple logging schema.
 * 12-Nov-2002 : Removed redundant import (DG).
 * 10-Dec-2002 : Updated Javadocs (DG);
 * 17-Dec-2002 : Removed LEVELS since it is not used (it is also declared in LogTarget) (DG);
 * 05-Feb-2003 : Interface cleanUp, switched from ArrayList to LogTarget[]
 * 07-Feb-2003 : BugFix, last cleanup caused a NullPointer, I removed too much ;(
 */

package org.jfree.report.util;

import org.jfree.util.LogTarget;

/**
 * A simple logging facility. Create a class implementing the
 * {@link org.jfree.util.LogTarget} interface to use
 * this feature.
 *
 * @author Thomas Morgner
 */
public final class Log extends org.jfree.util.Log
{
  /**
   * A helper class to print memory usage message if needed.
   */
  public static class MemoryUsageMessage
  {
    /** The message. */
    private final String message;

    /**
     * Creates a new message.
     *
     * @param message  the message.
     */
    public MemoryUsageMessage(final String message)
    {
      this.message = message;
    }

    /**
     * Returns a string representation of the message (useful for debugging).
     *
     * @return the string.
     */
    public String toString()
    {
      return (message
          + "Free: " + Runtime.getRuntime().freeMemory() + "; "
          + "Total: " + Runtime.getRuntime().totalMemory());
    }
  }

  /** The log level for error messages. */
  public static final int ERROR = LogTarget.ERROR;

  /** The log level for warning messages. */
  public static final int WARN = LogTarget.WARN;

  /** The log level for information messages. */
  public static final int INFO = LogTarget.INFO;

  /** The log level for debug messages. */
  public static final int DEBUG = LogTarget.DEBUG;

  /** The default log target. */
  private static final SystemOutLogTarget DEFAULT_LOG_TARGET = new SystemOutLogTarget();

  /** The JFreeReport log instance. */
  private static final Log jfreeReportLog;

  /**
   * Private to prevent creating instances.
   */
  private Log()
  {
  }

  static
  {
    jfreeReportLog = new Log();
    org.jfree.util.Log.defineLog(jfreeReportLog);
    jfreeReportLog.addTarget(Log.DEFAULT_LOG_TARGET);
    jfreeReportLog.setDebuglevel(DEBUG);
  }

  /**
   * Returns the JFreeReport log instance.
   *
   * @return the log object.
   */
  public static Log getJFreeReportLog()
  {
    return jfreeReportLog;
  }

  /**
   * Initializes the log system after the log module was loaded and a log target
   * was defined. This is the second step of the log initialisation.
   */
  public void init()
  {
    removeTarget(DEFAULT_LOG_TARGET);
    final String logLevel = ReportConfiguration.getGlobalConfig().getLogLevel();
    if (logLevel.equalsIgnoreCase("error"))
    {
      setDebuglevel(ERROR);
    }
    else if (logLevel.equalsIgnoreCase("warn"))
    {
      setDebuglevel(WARN);
    }
    else if (logLevel.equalsIgnoreCase("info"))
    {
      setDebuglevel(INFO);
    }
    else if (logLevel.equalsIgnoreCase("debug"))
    {
      setDebuglevel(DEBUG);
    }
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   */
  public static void debug(final Object message)
  {
    log(LogTarget.DEBUG, message);
  }

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void debug(final Object message, final Exception e)
  {
    log(LogTarget.DEBUG, message, e);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   */
  public static void info(final Object message)
  {
    log(LogTarget.INFO, message);
  }

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void info(final Object message, final Exception e)
  {
    log(LogTarget.INFO, message, e);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   */
  public static void warn(final Object message)
  {
    log(LogTarget.WARN, message);
  }

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void warn(final Object message, final Exception e)
  {
    log(LogTarget.WARN, message, e);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   */
  public static void error(final Object message)
  {
    log(LogTarget.ERROR, message);
  }

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public static void error(final Object message, final Exception e)
  {
    log(LogTarget.ERROR, message, e);
  }

  /**
   * Logs a message to the main log stream.  All attached log targets will also
   * receive this message. If the given log-level is higher than the given debug-level
   * in the main config file, no logging will be done.
   *
   * @param level  log level of the message.
   * @param message  text to be logged.
   */
  public static void log(final int level, final Object message)
  {
    org.jfree.util.Log.log(level, message);
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
  public static void log(final int level, final Object message, final Exception e)
  {
    org.jfree.util.Log.log(level, message, e);
  }
}
