/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * --------------------------------
 * FunctionInitializeException.java
 * --------------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -
 *
 * $Id: FunctionInitializeException.java,v 1.3 2003/10/18 19:32:12 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Version 1 (TM);
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package org.jfree.report.function;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.jfree.report.ReportInitialisationException;

/**
 * An exception that indicates that a function has not been correctly initialised.
 *
 * @author Thomas Morgner
 */
public class FunctionInitializeException extends ReportInitialisationException
{
  /** The parent exception. */
  private Exception parent;

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public FunctionInitializeException(final String message, final Exception ex)
  {
    super(message);
    parent = ex;
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public FunctionInitializeException(final String message)
  {
    super(message);
  }

  /**
   * Returns the parent exception (possibly null).
   *
   * @return the parent exception.
   */
  public Exception getParent()
  {
    return parent;
  }

  /**
   * Prints the stack trace to the specified stream.
   *
   * @param stream  the output stream.
   */
  public void printStackTrace(final PrintStream stream)
  {
    super.printStackTrace(stream);
    if (getParent() != null)
    {
      stream.println("ParentException: ");
      getParent().printStackTrace(stream);
    }
  }

  /**
   * Prints the stack trace to the specified writer.
   *
   * @param writer  the writer.
   */
  public void printStackTrace(final PrintWriter writer)
  {
    super.printStackTrace(writer);
    if (getParent() != null)
    {
      writer.println("ParentException: ");
      getParent().printStackTrace(writer);
    }
  }
}
