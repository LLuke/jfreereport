/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * ---------------
 * StackableException.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: StackableException.java,v 1.1 2002/12/06 19:28:05 taqua Exp $
 *
 * Changes
 * -------
 * 06-Dec-2002 : Initial version
 */
package com.jrefinery.report.util;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A baseclass for exceptions, which could have parent exceptions. These parent exceptions
 * are raised in a subclass and are now wrapped into a subclass of this Exception.
 * <p>
 * The parents are printed when this exception is printed. This class exists mainly for
 * debugging reasons, as with them it is easier to detect the root cause of an error.
 * <!-- In a perfect world ther would be no need for such a class :)-->
 */
public abstract class StackableException extends Exception
{
  /** The parent exception. */
  private Exception parent;

  /**
   * Creates a StackableRuntimeException with no message and no parent.
   */
  public StackableException()
  {}

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   * @param ex  the parent exception.
   */
  public StackableException (String message, Exception ex)
  {
    super (message);
    parent = ex;
  }

  /**
   * Creates an exception.
   *
   * @param message  the exception message.
   */
  public StackableException (String message)
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
