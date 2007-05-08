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
 * $Id: NowFunction.java,v 1.2 2007/04/01 13:51:52 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.formula.function.datetime;

import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.DateType;

/**
 * Return the serial number of the current date and time. This returns the current day and time serial number, using the
 * current locale. If you want only the serial number of the current day, use TODAY.
 *
 * @author Thomas Morgner
 * @since 23.03.2007
 */
public class NowFunction implements Function
{
  public NowFunction()
  {
  }

  public String getCanonicalName()
  {
    return "NOW";
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters)
      throws EvaluationException
  {
    //System.out.println("DEGUG Y:"+n1+" M:"+n2+"["+value+"] D:"+n3);
    final LocalizationContext localizationContext = context.getLocalizationContext();
    final GregorianCalendar gc = new GregorianCalendar
        (localizationContext.getTimeZone(), localizationContext.getLocale());

    final Date date = gc.getTime();
    return new TypeValuePair(DateType.TYPE, date);
  }
}
