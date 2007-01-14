package org.jfree.formula.function.information;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;


public class NaFunctionDescription extends AbstractFunctionDescription
{

  public NaFunctionDescription()
  {
    super("org.jfree.formula.function.information.Na-Function");
  }

  public FunctionCategory getCategory()
  {
    return InformationFunctionCategory.CATEGORY;
  }

  public int getParameterCount()
  {
    return 0;
  }

  public Type getParameterType(int position)
  {
    return null;
  }

  public Type getValueType()
  {
    return ErrorType.TYPE;
  }

  public boolean isParameterMandatory(int position)
  {
    return false;
  }

}
