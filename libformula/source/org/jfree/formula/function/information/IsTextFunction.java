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

public class IsTextFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }

    final Type type1 = parameters.getType(0);
    final Object value1 = parameters.getValue(0);
    if(type1.isFlagSet(Type.TEXT_TYPE) || value1 instanceof String)
    {
      return new TypeValuePair(LogicalType.TYPE, true);
    }
    
    return new TypeValuePair(LogicalType.TYPE, false);
  }

  public String getCanonicalName()
  {
    return "ISTEXT";
  }

}
