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
 * --------------------------
 * OutputTargetException.java
 * --------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: OutputTargetException.java,v 1.1 2002/12/02 17:56:51 taqua Exp $
 *
 * Changes
 * -------
 * 16-May-2002 : Version 1 (DG);
 * 31-Aug-2002 : Changed PrintStackTrace implementation to reveal the parent exception
 */
package com.jrefinery.report.targets.pageable;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An OutputTargetException is thrown if a element could not be printed in the target or
 * an TargetInternalError occurred, that made proceeding impossible.
 *
 * @author DG
 */
public class OutputTargetException extends Exception
{
  /** The parent exception. */
  private Exception parent;

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public OutputTargetException (String message, Exception ex)
  {
    super (message);
    parent = ex;
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public OutputTargetException (String message)
  {
    super (message);
  }

  /**
   * Returns the parent exception (possibly null).
   *
   * @return the parent exception.
   */
  public Exception getParent ()
  {
    return parent;
  }

  /**
   * Prints the stack trace to the specified stream.
   *
   * @param stream  the output stream.
   */
  public void printStackTrace (PrintStream stream)
  {
    super.printStackTrace (stream);
    if (getParent () != null)
    {
      stream.println ("ParentException: ");
      getParent ().printStackTrace (stream);
    }
  }

  /**
   * Prints the stack trace to the specified writer.
   *
   * @param writer  the writer.
   */
  public void printStackTrace (PrintWriter writer)
  {
    super.printStackTrace (writer);
    if (getParent () != null)
    {
      writer.println ("ParentException: ");
      getParent ().printStackTrace (writer);
    }
  }

}
