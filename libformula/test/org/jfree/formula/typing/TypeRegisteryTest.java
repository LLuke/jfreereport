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
 * $Id: TypeRegisteryTest.java,v 1.7 2007/05/07 22:57:01 mimil Exp $
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
import org.jfree.formula.typing.coretypes.DateTimeType;
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
  public void testZeroDateConvertion()
  {
    final Calendar cal = new GregorianCalendar
        (context.getLocalizationContext().getTimeZone(),
            context.getLocalizationContext().getLocale());
    cal.set(Calendar.MILLISECOND, 0);

    final Date d = cal.getTime();
    
    Number n = null;
    try
    {
      n = context.getTypeRegistry().convertToNumber(DateTimeType.DATETIME_TYPE, d);
      Assert.assertNotNull(n, "The date has not been converted to a number");
    } catch (TypeConversionException e)
    {
      Assert.fail("Conversion failed", e);
    }
    
    Date d1 = null;
    try
    {
      d1 = context.getTypeRegistry().convertToDate(DateTimeType.DATETIME_TYPE, n);
      Assert.assertNotNull(d1, "The number has not been converted to a date");
    } catch (TypeConversionException e)
    {
      Assert.fail("Conversion failed", e);
    }
    Assert.assertEquals(d1, d, "dates are differents");
  }

//  @Test(enabled=false)
//  public void testNowDateConvertion()
//  {
//    final Calendar cal = new GregorianCalendar
//        (context.getLocalizationContext().getTimeZone(),
//            context.getLocalizationContext().getLocale());
//
//    final Date d = cal.getTime();
//    Number n = null;
//    try
//    {
//      n = context.getTypeRegistry().convertToNumber(DateType.TYPE, d);
//      Assert.assertNotNull(n, "The date has not been converted to a number");
//    } catch (TypeConversionException e)
//    {
//      Assert.fail("Conversion failed", e);
//    }
//    
//    Date d1 = null;
//    try
//    {
//      d1 = context.getTypeRegistry().convertToDate(NumberType.GENERIC_NUMBER, n);
//      Assert.assertNotNull(d1, "The number has not been converted to a date");
//    } catch (TypeConversionException e)
//    {
//      Assert.fail("Conversion failed", e);
//    }
//    
//    Assert.assertEquals(d1, d, "dates are differents");
//  }
  
//  @Test
//  public void testStringDateConversion () throws TypeConversionException
//  {
//    final Date d = TestFormulaContext.createDate1(2004, GregorianCalendar.JANUARY, 1, 0, 0, 0, 0);
//    final TypeRegistry typeRegistry = context.getTypeRegistry();
//    final Number n = typeRegistry.convertToNumber(DateType.TYPE, d);
//    final Date d1 = typeRegistry.convertToDate(TextType.TYPE, "2004-01-01");
//
//    if (d1.getTime() != d.getTime())
//    {
//      final Number n2 = typeRegistry.convertToNumber(DateType.TYPE, d);
//      final Date dx = typeRegistry.convertToDate(TextType.TYPE, "2004-01-01");
//    }
//
//    Assert.assertEquals(d1.getTime(), d.getTime(), "dates are different");
//  }
//
//
//  @Test
//  public void testStringNumberConversion () throws TypeConversionException
//  {
//    final Number d = new Double(2000.5);
//    final TypeRegistry typeRegistry = context.getTypeRegistry();
//    final Number n = typeRegistry.convertToNumber(NumberType.GENERIC_NUMBER, d);
//    final Number d1 = typeRegistry.convertToNumber(TextType.TYPE, "2000.5");
//
//    if (d1.doubleValue() != d.doubleValue())
//    {
//      final Number n2 = typeRegistry.convertToNumber(DateType.TYPE, d);
//      final Date dx = typeRegistry.convertToDate(TextType.TYPE, "2004-01-01");
//    }
//
//    Assert.assertEquals(d1.doubleValue(), d.doubleValue(), "dates are different");
//  }
}
