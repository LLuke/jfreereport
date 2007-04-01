/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula;

import java.util.Locale;

/**
 * Creation-Date: 31.10.2006, 13:07:37
 *
 * @author Thomas Morgner
 */
public class LibFormulaErrorValue implements ErrorValue
{
  public static final int ERROR_REFERENCE_NOT_RESOLVABLE = 499;

  /** A parse error */
  public static final int ERROR_INVALID_CHARACTER = 501;
  /** Function name is invalid error code */
  public static final int ERROR_INVALID_FUNCTION = 505;
  /** Function name is invalid error */
  public static final LibFormulaErrorValue ERROR_INVALID_FUNCTION_VALUE = new LibFormulaErrorValue(ERROR_INVALID_FUNCTION);
  /** Parameter types are invalid error code */
  public static final int ERROR_INVALID_ARGUMENT = 502;
  /** Parameter types are invalid error */
  public static final LibFormulaErrorValue ERROR_INVALID_ARGUMENT_VALUE = new LibFormulaErrorValue(ERROR_INVALID_ARGUMENT);
  /** Number arithmetic error code */
  public static final int ERROR_ARITHMETIC = 503;
  /** Number arithmetic error */
  public static final LibFormulaErrorValue ERROR_ARITHMETIC_VALUE = new LibFormulaErrorValue(ERROR_ARITHMETIC);
  /** Invalid number of arguments error code*/
  public static final int ERROR_ARGUMENTS = 1;
  /** Invalid number of arguments error */
  public static final LibFormulaErrorValue ERROR_ARGUMENTS_VALUE = new LibFormulaErrorValue(ERROR_ARGUMENTS);
  /** Occurence not found error code */
  public static final int ERROR_NOT_FOUND = 504;
  /** Occurence not found error */
  public static final LibFormulaErrorValue ERROR_NOT_FOUND_VALUE = new LibFormulaErrorValue(ERROR_NOT_FOUND);
  /** NA error code*/
  public static final int ERROR_NA = 7;
  /** NA error*/
  public static final LibFormulaErrorValue ERROR_NA_VALUE = new LibFormulaErrorValue(ERROR_NA);
  /** Unexpected error code */
  public static final int ERROR_UNEXPECTED = 0;
  /** Unexpected error */
  public static final LibFormulaErrorValue ERROR_UNEXPECTED_VALUE = new LibFormulaErrorValue(ERROR_UNEXPECTED);

  private int errorCode;

  public LibFormulaErrorValue(final int errorCode)
  {
    this.errorCode = errorCode;
  }

  public String getNamespace()
  {
    return "http://jfreereport.sourceforge.net/libformula";
  }

  public int getErrorCode()
  {
    return errorCode;
  }

  public String getErrorMessage(Locale locale)
  {
    return "TODO";
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof LibFormulaErrorValue)
    {
      final LibFormulaErrorValue error = (LibFormulaErrorValue)obj;
      return this.errorCode == error.getErrorCode();
    }

    return false;
  }

  public String toString()
  {
    return "LibFormulaErrorValue{" +
        "errorCode=" + errorCode +
        '}';
  }
}
