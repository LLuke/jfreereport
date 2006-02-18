/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------
 * Log4JLogTarget.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Log4JLogTarget.java,v 1.5 2005/11/25 16:07:46 taqua Exp $
 *
 * Changes
 * -------
 * 23-Jun-2002 : Initial version
 */
package org.jfree.report.ext.modules.log4jlog;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.jfree.JCommon;
import org.jfree.util.LogTarget;

/**
 * The Log4J Target can be used to redirect the logging output into the
 * Log4J system. When loaded using the default constructor, the Category
 * is set the "JFreeReport".
 *
 * @see org.jfree.util.Log
 * @author Thomas Morgner
 */
public class Log4JLogTarget implements LogTarget
{
  private Logger logger;

  public Log4JLogTarget ()
  {
    logger = Logger.getLogger(JCommon.class);
  }

  public void log (final int level, final Object message)
  {
    if (level == ERROR)
    {
      logger.log(Priority.ERROR, message);
    }
    else if (level == WARN)
    {
      logger.log(Priority.WARN, message);
    }
    else if (level == INFO && logger.isInfoEnabled())
    {
      logger.log(Priority.INFO, message);
    }
    else if (level == DEBUG && logger.isDebugEnabled())
    {
      logger.log(Priority.DEBUG, message);
    }
    else
    {
      // should not happen, but make sure it does not get surpressed ..
      logger.log(Priority.FATAL, message);
    }
  }

  public void log (final int level, final Object message, final Exception e)
  {
    if (level == ERROR)
    {
      logger.log(Priority.ERROR, message, e);
    }
    else if (level == WARN)
    {
      logger.log(Priority.WARN, message, e);
    }
    else if (level == INFO && logger.isInfoEnabled())
    {
      logger.log(Priority.INFO, message, e);
    }
    else if (level == DEBUG && logger.isDebugEnabled())
    {
      logger.log(Priority.DEBUG, message, e);
    }
    else
    {
      // should not happen, but make sure it does not get surpressed ..
      logger.log(Priority.FATAL, message, e);
    }
  }
}
