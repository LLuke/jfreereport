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
 * Java14LogTarget.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14LogTarget.java,v 1.4 2003/09/09 10:27:59 taqua Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Initial version
 */
package org.jfree.report.ext.modules.java14log;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.util.LogTarget;
import org.jfree.JCommon;

/**
 * A log target for the JDK 1.4 logging API. This target can be used
 * in conjunction with the Log class.
 *
 * @see org.jfree.util.Log
 * @author Thomas Morgner
 */
public class Java14LogTarget implements LogTarget
{
  private Logger logger;

  public Java14LogTarget ()
  {
    logger = Logger.getLogger(JCommon.class.getName());
  }

  public void log (final int level, final Object message)
  {
    String messageText;
    if (message == null)
    {
      messageText = null;
    }
    else
    {
      messageText = String.valueOf(message);
    }
    if (level == ERROR)
    {
      logger.log(Level.SEVERE, messageText);
    }
    else if (level == WARN)
    {
      logger.log(Level.WARNING, messageText);
    }
    else if (level == INFO && logger.isLoggable(Level.INFO))
    {
      logger.log(Level.INFO, messageText);
    }
    else if (level == DEBUG && logger.isLoggable(Level.FINE))
    {
      logger.log(Level.FINE, messageText);
    }
    else
    {
      // should not happen, but make sure it does not get surpressed ..
      logger.log(Level.SEVERE, messageText);
    }
  }

  public void log (final int level, final Object message, final Exception e)
  {
    String messageText;
    if (message == null)
    {
      messageText = null;
    }
    else
    {
      messageText = String.valueOf(message);
    }

    if (level == ERROR)
    {
      logger.log(Level.SEVERE, messageText, e);
    }
    else if (level == WARN)
    {
      logger.log(Level.WARNING, messageText, e);
    }
    else if (level == INFO && logger.isLoggable(Level.INFO))
    {
      logger.log(Level.INFO, messageText, e);
    }
    else if (level == DEBUG && logger.isLoggable(Level.FINE))
    {
      logger.log(Level.FINE, messageText, e);
    }
    else
    {
      // should not happen, but make sure it does not get surpressed ..
      logger.log(Level.SEVERE, messageText, e);
    }
  }
}
