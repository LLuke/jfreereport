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
 * --------------
 * LogTarget.java
 * --------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 * 11-May-2002 : Initial version
 *
 */
package com.jrefinery.report.util;

/**
 * Use the LogTarget to redirect all logging messages to a suitable consumer.
 *
 * @author TM
 */
public interface LogTarget
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

  /** Strings for the log levels. */
  public static final String[] LEVELS =
          {
            "ERROR: ",
            "WARN:  ",
            "INFO:  ",
            "DEBUG: "
          };

  /**
   * Logs a message at a specified log level.
   *
   * @param level  the log level.
   * @param message  the log message.
   */
  public void log (int level, String message);

  /**
   * Logs a message at a specified log level.
   *
   * @param level  the log level.
   * @param message  the log message.
   * @param e  the exception
   */
  public void log (int level, String message, Exception e);

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   */
  public void debug (String message);

  /**
   * A convenience method for logging a 'debug' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void debug (String message, Exception e);

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   */
  public void info (String message);

  /**
   * A convenience method for logging an 'info' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void info (String message, Exception e);

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   */
  public void warn (String message);

  /**
   * A convenience method for logging a 'warning' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void warn (String message, Exception e);

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   */
  public void error (String message);

  /**
   * A convenience method for logging an 'error' message.
   *
   * @param message  the message.
   * @param e  the exception.
   */
  public void error (String message, Exception e);

}
