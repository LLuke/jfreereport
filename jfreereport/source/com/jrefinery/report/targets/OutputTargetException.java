/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * OutputTargetException.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 16-May-2002 : Version 1 (DG);
 * 31-Aug-2002 : Changed PrintStackTrace implementation to reveal the parent exception
 */
package com.jrefinery.report.targets;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An OutputTargetException is thrown if a element could not be printed in the target or
 * an TargetInternalError occured, that made proceeding impossible.
 */
public class OutputTargetException extends Exception
{
  private Exception parent;

  public OutputTargetException (String message, Exception ex)
  {
    super (message);
    parent = ex;
  }

  public OutputTargetException (String message)
  {
    super (message);
  }

  public Exception getParent ()
  {
    return parent;
  }

  public void printStackTrace (PrintStream stream)
  {
    super.printStackTrace (stream);
    if (getParent () != null)
    {
      stream.println ("ParentException: ");
      getParent ().printStackTrace (stream);
    }
  }

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
