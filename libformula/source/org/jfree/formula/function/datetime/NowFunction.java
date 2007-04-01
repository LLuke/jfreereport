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

package org.jfree.formula.function.datetime;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.typing.coretypes.DateType;

/**
 * Todo: Document me!
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

    // Time is implicitly set to the current time. All we have to do is to
    // remove the fractional part ..
    gc.set(Calendar.DAY_OF_MONTH, 0);
    gc.set(Calendar.MONTH, 0);
    gc.set(Calendar.YEAR, 0);

    final Date date = gc.getTime();
    return new TypeValuePair(DateType.TYPE, date);
  }
}