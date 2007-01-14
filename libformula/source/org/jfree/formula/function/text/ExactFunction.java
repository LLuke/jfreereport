package org.jfree.formula.function.text;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.formula.typing.coretypes.NumberType;

public class ExactFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 2)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    
    final Type textType1 = parameters.getType(0);
    final Object textValue1 = parameters.getValue(0);
    final Type textType2 = parameters.getType(1);
    final Object textValue2 = parameters.getValue(1);
 
    final String text1 = typeRegistry.convertToText(textType1, textValue1);
    final String text2 = typeRegistry.convertToText(textType2, textValue2);
    
    if(text1 == null || text2 == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    // Numerical comparisons ignore "trivial" differences that
    // depend only on numeric precision of finite numbers.
    if((textType1.isFlagSet(Type.NUMERIC_TYPE) || textValue1 instanceof Number)
        && (textType2.isFlagSet(Type.NUMERIC_TYPE) || textValue2 instanceof Number))
    {
      final BigDecimal number1 = (BigDecimal)typeRegistry.convertToNumber(textType1, textValue1);
      final BigDecimal number2 = (BigDecimal)typeRegistry.convertToNumber(textType2, textValue2);
      
      final BigInteger n1 = number1.toBigInteger();
      final BigInteger n2 = number2.toBigInteger();
      return new TypeValuePair(LogicalType.TYPE, new Boolean(n1.equals(n2)));
    }

    return new TypeValuePair(LogicalType.TYPE, new Boolean(text1.equals(text2)));
  }

  public String getCanonicalName()
  {
    return "EXACT";
  }

}