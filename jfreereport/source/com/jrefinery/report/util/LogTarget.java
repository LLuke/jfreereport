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
 * --------------
 * LogTarget.java
 * --------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: LogTarget.java,v 1.7 2002/12/18 10:13:16 mungady Exp $
 *
 * Changes
 * -------
 * 11-May-2002 : Initial version
 * 06-Dec-2002 : LogTargets now use Object-Parameters instead of Strings.
 * 05-Feb-2003 : Removed unnecessary methods.
 */
package com.jrefinery.report.util;

/**
 * An interface that defines a log target (a consumer of log messages).  Classes that implement
 * this interface can be registered with the {@link Log} class and will then receive logging
 * messages generated in the JFreeReport code.
 *
 * @author Thomas Morgner
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
  public void log (int level, Object message);

  /**
   * Logs a message at a specified log level.
   *
   * @param level  the log level.
   * @param message  the log message.
   * @param e  the exception
   */
  public void log (int level, Object message, Exception e);
}
