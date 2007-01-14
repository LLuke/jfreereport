package org.jfree.formula.function.text;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.NumberType;

public class FindFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 2 || parameterCount > 3)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    
    final Type searchType = parameters.getType(0);
    final Object searchValue = parameters.getValue(0);
    final Type textType = parameters.getType(1);
    final Object textValue = parameters.getValue(1);
    Type indexType = null;
    Object indexValue = null;
    
    if(parameterCount == 3) {
      indexType = parameters.getType(2);
      indexValue = parameters.getValue(2);
    }

    final String search = typeRegistry.convertToText(searchType, searchValue);
    final String text = typeRegistry.convertToText(textType, textValue);
    
    if(search == null || text == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }
    int indexFrom = 0;
    
    if(indexType != null && indexValue != null)
    {
      final Number n = typeRegistry.convertToNumber(indexType, indexValue);
      if(n.intValue() >= 1) 
      {
        indexFrom = n.intValue()-1;
      }
      else
      {
        return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
      }
    }
    
    int index = text.indexOf(search, indexFrom);
    if(index < 0) {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_NOT_FOUND));
    }

    return new TypeValuePair(NumberType.GENERIC_NUMBER, new BigDecimal(index+1));
  }

  public String getCanonicalName()
  {
    return "FIND";
  }

}