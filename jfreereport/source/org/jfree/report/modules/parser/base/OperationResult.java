/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * OperationResult.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationResult.java,v 1.1 2003/08/26 17:37:28 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import java.io.Serializable;

/**
 * The OperationResult class provides the possibility to monitor the result of 
 * the parsing progress and to handle warnings and errors.
 *  
 * @author Thomas Morgner
 */
public class OperationResult implements Serializable
{
  /** The message of the result object. */
  private String message;
  /** The severity level of the message. */
  private SeverityLevel severity;
  /** The column in the xml file where the result was generated. */
  private int column;
  /** The line in the xml file where the result was generated. */
  private int line;

  /**
   * Creates a new operation result with the given message and severity.
   * 
   * @param message the message of this result object.
   * @param severity the assigned severity.
   * @throws NullPointerException if one of the parameters is null.
   */
  public OperationResult(String message, SeverityLevel severity)
  {
    this (message, severity, -1, -1);
  }

  /**
   * Creates a new operation result with the given message, severity and
   * parse position.
   * 
   * @param message the message of this result object.
   * @param severity the assigned severity.
   * @param column the column of the parse position
   * @param line the line of the parse position.
   * @throws NullPointerException if one of the parameters is null.
   */
  public OperationResult(String message, SeverityLevel severity, int column, int line)
  {
    if (message == null)
    {
      throw new NullPointerException("message must not be null.");
    }
    if (severity == null)
    {
      throw new NullPointerException("severity must not be null.");
    }
    this.message = message;
    this.severity = severity;
    this.column = column;
    this.line = line;
  }

  /**
   * Returns the message of the operation result. The message is never null. 
   * 
   * @return the message.
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the severity of the message. The severity is never null.
   * 
   * @return the severity.
   */
  public SeverityLevel getSeverity()
  {
    return severity;
  }

  /**
   * Returns the column of the parse position where this message occured.
   * 
   * @return the column of the parse position.
   */
  public int getColumn()
  {
    return column;
  }

  /**
   * Returns the line of the parse position where this message occured.
   * 
   * @return the line of the parse position.
   */
  public int getLine()
  {
    return line;
  }
}
