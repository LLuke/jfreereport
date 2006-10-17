/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * Loggers.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: Loggers.java,v 1.2 2006/07/29 18:57:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import org.jfree.util.Log;
import org.jfree.util.LogContext;

/**
 * Creation-Date: 27.07.2006, 14:35:31
 *
 * @author Thomas Morgner
 */
public class Loggers
{
  private static class NullLogContext extends LogContext
  {
    public NullLogContext()
    {
      super("");
    }

    /**
     * Logs a message to the main log stream. All attached logTargets will also
     * receive this message. If the given log-level is higher than the given
     * debug-level in the main config file, no logging will be done.
     * <p/>
     * The exception's stacktrace will be appended to the log-stream
     *
     * @param level   log level of the message.
     * @param message text to be logged.
     * @param e       the exception, which should be logged.
     */
    public void log(final int level, final Object message, final Exception e)
    {
    }

    /**
     * Logs a message to the main log stream.  All attached log targets will also
     * receive this message. If the given log-level is higher than the given
     * debug-level in the main config file, no logging will be done.
     *
     * @param level   log level of the message.
     * @param message text to be logged.
     */
    public void log(final int level, final Object message)
    {
    }
  }

  private Loggers ()
  {
  }
  
  public static final LogContext VALIDATION = new NullLogContext();
          //Log.createContext("Renderer.validation");
  public static final LogContext STATECHANGE =  new NullLogContext(); 
                  //Log.createContext("Renderer.statechange");
  public static final LogContext SPLITSTRATEGY = Log.createContext("Renderer.split");
}
