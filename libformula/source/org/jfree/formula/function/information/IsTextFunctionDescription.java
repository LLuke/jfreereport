package org.jfree.formula.function.information;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.formula.typing.coretypes.LogicalType;

public class IsTextFunctionDescription extends AbstractFunctionDescription
{

  public IsTextFunctionDescription()
  {
    super("org.jfree.formula.function.information.IsText-Function");
  }
  
  public FunctionCategory getCategory()
  {
    return InformationFunctionCategory.CATEGORY;
  }

  public int getParameterCount()
  {
    return 1;
  }

  public Type getParameterType(int position)
  {
    return AnyType.TYPE;
  }

  public Type getValueType()
  {
    return LogicalType.TYPE;
  }

  public boolean isParameterMandatory(int position)
  {
    return true;
  }

}
