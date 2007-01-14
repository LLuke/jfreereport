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
 * $Id: DateFunction.java,v 1.6 2006/12/30 14:54:38 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.common.TestFormulaContext;
import org.jfree.formula.typing.coretypes.DateType;
import org.jfree.formula.typing.coretypes.NumberType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * @author Cedric Pronzato
 *
 */
public class TypeRegisteryTest
{
  private FormulaContext context;

  @BeforeClass
  public void setup()
  {
    context = new TestFormulaContext(TestFormulaContext.testCaseDataset);
    LibFormulaBoot.getInstance().start();
  }
  
  @Test
  public void testDateConvertion()
  {
    //final Date d = GregorianCalendar.getInstance().getTime();
    final Calendar cal = new GregorianCalendar(2005, 0, 31);
    //cal.set(GregorianCalendar.YEAR, 2005);
    //cal.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
    //cal.set(GregorianCalendar.DAY_OF_MONTH, 31);
    
    final Date d = cal.getTime();
    final Number n = context.getTypeRegistry().convertToNumber(DateType.TYPE, d);
    Assert.assertNotNull(n, "The date has not been converted to a number");
    final Date d1 = context.getTypeRegistry().convertToDate(NumberType.GENERIC_NUMBER, n);
    Assert.assertNotNull(d1, "The number has not been converted to a date");
    Assert.assertEquals(d, d1, "dates are differents");
  }
}
