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
 * -----------------
 * ReportEventException.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportEventException.java,v 1.8 2002/12/06 17:18:33 mungady Exp $
 *
 * Changes
 * -------------------------
 * 04-Mar-2003 : Initial version
 */
package com.jrefinery.report;

import java.util.List;
import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * The ReportEventException is thrown, if there were unrecoverable exceptions
 * during the report processing.
 */
public class ReportEventException extends ReportProcessingException
{
  /** the collected child exceptions. */
  private List childExceptions;

  /**
   * Creates an ReportEventException to handle exceptions, that occured
   * during the event dispatching.
   *
   * @param message  the exception message.
   * @param childExceptions the collected exceptions.
   */
  public ReportEventException(String message, List childExceptions)
  {
    super(message);
    if (childExceptions == null)
      throw new NullPointerException();

    this.childExceptions = childExceptions;
  }

  /**
   * Gets the collected child exceptions, that occured during the event
   * dispatching.
   *
   * @return the collected child exceptions.
   */
  public List getChildExceptions()
  {
    return childExceptions;
  }

  /**
   * Returns the errort message string of this throwable object.
   *
   * @return  the error message string of this <code>Throwable</code>
   *          object if it was {@link #Throwable(String) created} with an
   *          error message string; or <code>null</code> if it was
   *          {@link #Throwable() created} with no error message.
   *
   */
  public String getMessage()
  {
    return super.getMessage() + ": " + childExceptions.size() + " exceptions occured.";
  }

  /**
   * Prints the stack trace to the specified writer.
   *
   * @param writer  the writer.
   */
  public void printStackTrace(PrintWriter writer)
  {
    super.printStackTrace(writer);
    for (int i = 0; i < childExceptions.size(); i++)
    {
      writer.print("Exception #");
      writer.println(i);
      Exception ex = (Exception) childExceptions.get(i);
      if (ex != null)
      {
        ex.printStackTrace(writer);
      }
      else
      {
        writer.println("<not defined>");
      }
    }
  }

  /**
   * Prints the stack trace to the specified stream.
   *
   * @param stream  the output stream.
   */
  public void printStackTrace(PrintStream stream)
  {
    super.printStackTrace(stream);
    for (int i = 0; i < childExceptions.size(); i++)
    {
      stream.print("Exception #");
      stream.println(i);
      Exception ex = (Exception) childExceptions.get(i);
      if (ex != null)
      {
        ex.printStackTrace(stream);
      }
      else
      {
        stream.println("<not defined>");
      }
    }
  }
}
