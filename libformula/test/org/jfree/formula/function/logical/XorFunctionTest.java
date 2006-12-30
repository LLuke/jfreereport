package org.jfree.formula.function.logical;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.common.TestFormulaContext;
import org.jfree.formula.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XorFunctionTest
{
  private FormulaContext context;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
      { "XOR(FALSE();FALSE())", Boolean.FALSE },
      { "XOR(FALSE();TRUE())", Boolean.TRUE },
      { "XOR(TRUE();FALSE())", Boolean.TRUE },
      { "XOR(TRUE();TRUE())", Boolean.FALSE },
      //TODO { "XOR(FALSE();NA())", Boolean. },
      { "XOR(FALSE();FALSE();TRUE())", Boolean.TRUE },
      { "XOR(FALSE();TRUE();TRUE())", Boolean.FALSE },
      { "XOR(TRUE())", Boolean.TRUE },
    };
  }

  @BeforeClass
  public void setup()
  {
    context = new TestFormulaContext();
    LibFormulaBoot.getInstance().start();
  }

  @Test(dataProvider = "defaultTestCase", groups = "functions")
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
