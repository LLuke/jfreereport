/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: DateTimeType.java,v 1.2 2007/05/07 22:57:01 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.typing.coretypes;

import org.jfree.formula.typing.DefaultType;
import org.jfree.formula.typing.Type;

/**
 *
 * @author Cedric Pronzato
 */
public class DateTimeType extends DefaultType
{
  public static final DateTimeType DATETIME_TYPE;
  public static final DateTimeType DATE_TYPE;
  public static final DateTimeType TIME_TYPE;
  
  static
  {
    DATE_TYPE = new DateTimeType();
    DATE_TYPE.addFlag(Type.DATE_TYPE);
    DATE_TYPE.lock();
    
    TIME_TYPE = new DateTimeType();
    TIME_TYPE.addFlag(Type.TIME_TYPE);
    TIME_TYPE.lock();
    
    DATETIME_TYPE = new DateTimeType();
    DATETIME_TYPE.addFlag(Type.DATETIME_TYPE);
    DATE_TYPE.lock();
  }

  private DateTimeType()
  {
    addFlag(Type.NUMERIC_TYPE);
    addFlag(Type.SCALAR_TYPE);
  }
}
