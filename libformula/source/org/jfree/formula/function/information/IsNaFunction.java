package org.jfree.formula.function.information;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * This function returns true if the parameter is of error type NA.
 * 
 * @author Cedric Pronzato
 *
 */
public class IsNaFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    if(parameters.getParameterCount() != 1) {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    
    final Type type = parameters.getType(0);
    final Object value = parameters.getValue(0);
    
    if(ErrorType.TYPE.equals(type) && value instanceof LibFormulaErrorValue)
    {
      final LibFormulaErrorValue na = (LibFormulaErrorValue)value;
      if(na.getErrorCode() == LibFormulaErrorValue.ERROR_NA)
      {
        return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
      }
    }
    
    return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
  }

  public String getCanonicalName()
  {
    return "ISNA";
  }

}
