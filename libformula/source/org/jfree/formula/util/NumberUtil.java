package org.jfree.formula.util;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;

public class NumberUtil
{
  public static final BigDecimal DELTA = new BigDecimal("0.000000000000000000000000000005");
  
  public static BigDecimal getAsBigDecimal(Number number) throws EvaluationException
  {
    if (number == null)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
    
    if(number instanceof BigDecimal)
    {
      return (BigDecimal)number;
    }
    else
    {
      return new BigDecimal(number.toString());
    }
  }
  
  public static Integer performIntRounding(BigDecimal n)
  {
    BigDecimal round = null;
    
    try
    {
      // no need to go further if the value is already an integer
      n.setScale(0);
      return new Integer(n.intValue());
    }
    catch(ArithmeticException e)
    {
      //ignore and continue
    }
    
    if(n.signum()<0)
    {
      n = n.subtract(DELTA);
      round = n.setScale(0, BigDecimal.ROUND_UP);
    }
    else
    {
      n = n.add(DELTA);
      round = n.setScale(1, BigDecimal.ROUND_DOWN);
    }
    return new Integer(round.intValue());
  }
}
