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
