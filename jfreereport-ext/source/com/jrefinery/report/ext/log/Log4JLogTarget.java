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
 * Log4JLogTarget.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Log4JLogTarget.java,v 1.4 2003/02/25 20:32:43 taqua Exp $
 *
 * Changes
 * -------
 * 23-Jun-2002 : Initial version
 */
package com.jrefinery.report.ext.log;

import com.jrefinery.util.LogTarget;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

/**
 * The Log4J Target can be used to redirect the logging output into the
 * Log4J system. When loaded using the default constructor, the Category
 * is set the "JFreeReport".
 */
public class Log4JLogTarget implements LogTarget
{
  /** the log category receives the generated log statements. */
  private Category cat;

  /**
   * Creates a new Log4J log target, which logs all statements into the
   * category "JFreeReport".
   */
  public Log4JLogTarget ()
  {
    this (Category.getInstance("JFreeReport"));
  }

  /**
   * Creates a new Log4J log target, which uses the given Category to log
   * the generated log statements.
   *
   * @param cat the category, that should be used for logging.
   */
  public Log4JLogTarget (Category cat)
  {
    if (cat == null) throw new NullPointerException("Given category is null");
    this.cat = cat;
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
    Priority priority = Priority.toPriority(level);
    cat.log(priority, message);
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
    Priority priority = Priority.toPriority(level);
    cat.log(priority, message, e);
  }
}