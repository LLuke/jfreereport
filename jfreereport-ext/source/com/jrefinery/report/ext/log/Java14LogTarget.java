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
 * ----------------
 * Java14LogTarget.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14LogTarget.java,v 1.2 2003/04/23 17:32:37 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Initial version
 */
package com.jrefinery.report.ext.log;

import org.jfree.util.LogTarget;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A log target for the JDK 1.4 logging API. This target can be used
 * in conjunction with the Log class.
 *
 * @see org.jfree.util.Log
 * @author Thomas Morgner
 */
public class Java14LogTarget implements LogTarget
{
  /** the log category receives the generated log statements. */
  private Logger logger;

  /**
   * Creates a new Java 1.4 log target, which logs all statements into the
   * category "JFreeReport".
   */
  public Java14LogTarget ()
  {
    this (Logger.getLogger("JFreeReport"));
  }

  /**
   * Creates a new Java 1.4 log target, which uses the given Logger to log
   * the generated log statements.
   *
   * @param logger the logger instance, that should be used for logging.
   */
  public Java14LogTarget (Logger logger)
  {
    if (logger == null) throw new NullPointerException("Given category is null");
    this.logger = logger;
  }

  /**
   * Logs a message at a specified log level. The message is logged using the
   * private Log4J Category.
   *
   * @param level  the log level.
   * @param message  the log message.
   */
  public void log (int level, Object message)
  {
    logger.log(translateLogLevel(level), String.valueOf(message));
  }

  /**
   * Logs a message at a specified log level. The message is logged using the
   * private Log4J Category.
   *
   * @param level  the log level.
   * @param message  the log message.
   * @param e  the exception
   */
  public void log (int level, Object message, Exception e)
  {
    logger.log(translateLogLevel(level), String.valueOf(message), e);
  }

  /**
   * Translates the JFreeReport log level into a Java1.4 log level.
   *
   * @param level the JFreeReport log level.
   * @return the Java 1.4 log level.
   */
  private Level translateLogLevel (int level)
  {
    if (level == ERROR)
    {
      return Level.SEVERE;
    }
    else if (level == WARN)
    {
      return Level.WARNING;
    }
    else if (level == INFO)
    {
      return Level.INFO;
    }
    else
    {
      return Level.FINE;
    }
  }
}
