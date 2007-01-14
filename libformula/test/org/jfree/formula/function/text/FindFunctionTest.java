package org.jfree.formula.function.text;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.common.TestFormulaContext;
import org.jfree.formula.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FindFunctionTest
{
  private FormulaContext context;
  
  @DataProvider(name="defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
        {"FIND(\"b\";\"abcabc\")", new BigDecimal(2)},
        {"FIND(\"b\";\"abcabcabc\"; 3)", new BigDecimal(5)},
        {"FIND(\"b\";\"ABC\";1)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_NOT_FOUND)},
        {"FIND(\"b\";\"bbbb\")", new BigDecimal(1)},
        {"FIND(\"b\";\"bbbb\";2)", new BigDecimal(2)},
        {"FIND(\"b\";\"bbbb\";2.9)", new BigDecimal(2)},
        {"FIND(\"b\";\"bbbb\";0)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS)},
        {"FIND(\"b\";\"bbbb\";0.9)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS)},
    };
  }  
  
  @BeforeClass
  public void setup() {
    context = new TestFormulaContext();
    LibFormulaBoot.getInstance().start();
  }
  
  @Test(dataProvider="defaultTestCase", groups="functions")
  public void test(String formul, Object result)
  {
    Formula formula = null;
    try
    {
      formula = new Formula(formul);
    } catch (ParseException e1)
    {
      Assert.fail("Error while parsing the formula", e1);
    }
    try
    {
      formula.initialize(context);
    } catch (EvaluationException e)
    {
      Assert.fail("Initialization Error", e);
    }
    Object eval = formula.evaluate();
    Assert.assertEquals(eval, result);
  }
}
