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
 * $Id: Log.java,v 1.20 2003/02/26 13:58:04 mungady Exp $
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

package com.jrefinery.report.util;

import com.jrefinery.util.LogTarget;

/**
 * A simple logging facility. Create a class implementing the {@link com.jrefinery.util.LogTarget} interface to use
 * this feature.
 *
 * @author Thomas Morgner
 */
public final class Log extends com.jrefinery.util.Log
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

  /** The log level for error messages. */
  public static final int ERROR = LogTarget.ERROR;

  /** The log level for warning messages. */
  public static final int WARN = LogTarget.WARN;

  /** The log level for information messages. */
  public static final int INFO = LogTarget.INFO;

  /** The log level for debug messages. */
  public static final int DEBUG = LogTarget.DEBUG;

  /**
   * Private to prevent creating instances.
   */
  private Log ()
  {
  }

  static
  {
    Log l = new Log();
    Log.defineLog(l);

    if (ReportConfiguration.getGlobalConfig().isDisableLogging() == false)
    {
      String className = ReportConfiguration.getGlobalConfig().getLogTarget();

      try
      {
        Class c = ReportConfiguration.getGlobalConfig().getClass().
            getClassLoader().loadClass(className);
        LogTarget lt = (LogTarget) c.newInstance();
        l.addTarget(lt);
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
      l.setDebuglevel(ERROR);
    }
    else
    if (logLevel.equalsIgnoreCase("warn"))
    {
      l.setDebuglevel(WARN);
    }
    else
    if (logLevel.equalsIgnoreCase("info"))
    {
      l.setDebuglevel(INFO);
    }
    else
    if (logLevel.equalsIgnoreCase("debug"))
    {
      l.setDebuglevel(DEBUG);
    }
  }


}
