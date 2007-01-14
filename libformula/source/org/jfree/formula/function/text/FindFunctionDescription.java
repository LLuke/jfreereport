package org.jfree.formula.function.text;

import org.jfree.formula.function.AbstractFunctionDescription;
import org.jfree.formula.function.FunctionCategory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.typing.coretypes.TextType;

public class FindFunctionDescription extends AbstractFunctionDescription
{
  public FindFunctionDescription()
  {
    super("org.jfree.formula.function.text.Find-Function");
  }

  public FunctionCategory getCategory()
  {
    return TextFunctionCategory.CATEGORY;
  }

  public int getParameterCount()
  {
    return 3;
  }

  public Type getParameterType(int position)
  {
    if(position == 0 || position == 1)
    {
      return TextType.TYPE;
    }
    if(position == 2)
    {
      return NumberType.GENERIC_NUMBER;
    }
    return null;
  }

  public Type getValueType()
  {
    return NumberType.GENERIC_NUMBER;
  }

  public boolean isParameterMandatory(int position)
  {
    if(position == 0 || position == 1)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
