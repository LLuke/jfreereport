package org.jfree.formula.util;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;

public class NumberUtil
{

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
}
