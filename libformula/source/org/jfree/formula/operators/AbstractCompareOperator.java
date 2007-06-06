package org.jfree.formula.operators;

import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.ExtendedComparator;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * Creation-Date: 06.06.2007, 18:52:25
 *
 * @author Thomas Morgner
 */
public abstract class AbstractCompareOperator implements InfixOperator
{
  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);

  public AbstractCompareOperator()
  {
  }

  public final TypeValuePair evaluate(final FormulaContext context,
                                      final TypeValuePair value1,
                                      final TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Type type1 = value1.getType();
    final Type type2 = value2.getType();
    final Object value1Raw = value1.getValue();
    final Object value2Raw = value2.getValue();
    if (value1Raw == null || value2Raw == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_NA_VALUE);
    }

    final ExtendedComparator comparator = typeRegistry.getComparator(type1, type2);
    final Integer result = comparator.compare (type1, value1Raw, type2, value2Raw);
    if (result == null)
    {
      throw new EvaluationException
          (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    if (evaluate(result.intValue()))
    {
      return RETURN_TRUE;
    }
    return RETURN_FALSE;
  }

  protected abstract boolean evaluate(int compareResult);
}
