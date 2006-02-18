/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * Log4JLogContext.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.02.2006 : Initial version
 */
package org.jfree.report.ext.modules.java14log;


import java.util.logging.Logger;
import java.util.logging.Level;

import org.jfree.util.LogContext;
import org.jfree.util.LogTarget;

public class Java14LogContext extends LogContext
{
  private Logger logger;

  public Java14LogContext (final String contextPrefix)
  {
    super(contextPrefix);
    logger = Logger.getLogger(contextPrefix);
  }

  /**
   * Returns true, if the log level allows debug messages to be printed.
   *
   * @return true, if messages with an log level of DEBUG are allowed.
   */
  public boolean isDebugEnabled ()
  {
    return logger.isLoggable(Level.FINE);
  }

  /**
   * Returns true, if the log level allows error messages to be printed.
   *
   * @return true, if messages with an log level of ERROR are allowed.
   */
  public boolean isErrorEnabled ()
  {
    return logger.isLoggable(Level.SEVERE);
  }

  /**
   * Returns true, if the log level allows informational messages to be printed.
   *
   * @return true, if messages with an log level of INFO are allowed.
   */
  public boolean isInfoEnabled ()
  {
    return logger.isLoggable(Level.INFO);
  }

  /**
   * Returns true, if the log level allows warning messages to be printed.
   *
   * @return true, if messages with an log level of WARN are allowed.
   */
  public boolean isWarningEnabled ()
  {
    return logger.isLoggable(Level.WARNING);
  }

  /**
   * Logs a message to the main log stream.  All attached log targets will also receive
   * this message. If the given log-level is higher than the given debug-level in the main
   * config file, no logging will be done.
   *
   * @param level   log level of the message.
   * @param message text to be logged.
   */
  public void log (final int level, final Object message)
  {
    logger.log(translatePriority(level), message == null ? null : message.toString());
  }

  protected final Level translatePriority (final int level)
  {
    if (level == LogTarget.DEBUG)
    {
      return Level.SEVERE;
    }
    if (level == LogTarget.INFO)
    {
      return Level.INFO;
    }
    if (level == LogTarget.WARN)
    {
      return Level.SEVERE;
    }
    if (level == LogTarget.ERROR)
    {
      return Level.SEVERE;
    }
    return Level.ALL;
  }

  /**
   * Logs a message to the main log stream. All attached logTargets will also receive this
   * message. If the given log-level is higher than the given debug-level in the main
   * config file, no logging will be done.
   * <p/>
   * The exception's stacktrace will be appended to the log-stream
   *
   * @param level   log level of the message.
   * @param message text to be logged.
   * @param e       the exception, which should be logged.
   */
  public void log (final int level, final Object message, final Exception e)
  {
    logger.log(translatePriority(level), message == null ? null : message.toString(), e);
  }
}
