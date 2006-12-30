package org.jfree.formula.function.logical;

import java.math.BigDecimal;

import org.jfree.formula.DefaultFormulaContext;
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

public class IfFunctionTest
{
  private FormulaContext context;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
    { "IF(FALSE();7;8)", new BigDecimal(8) },
    { "IF(TRUE();7;8)", new BigDecimal(7) },
    { "IF(TRUE();\"HI\";8)",  "HI"},
    { "IF(1;7;8)", new BigDecimal(7) },
    { "IF(5;7;8)", new BigDecimal(7) },
    { "IF(0;7;8)", new BigDecimal(8) },
    { "IF(TRUE();[.B4];8)", new BigDecimal(2) },
    { "IF(TRUE();[.B4]+5;8)", new BigDecimal(7) },
    { "IF(\"x\";7;8)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT) },
    { "IF(\"1\";7;8)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT) },
    { "IF(\"\";7;8)", new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT) },
    { "IF(FALSE();7)", Boolean.FALSE },
    //TODO { "IF(FALSE();7;)", new BigDecimal(0) },
    { "IF(TRUE();4;1/0)", new BigDecimal(4) },
    { "IF(FALSE();1/0;5)", new BigDecimal(5) },
    };
  }

  @BeforeClass
  public void setup()
  {
    context = new TestFormulaContext(TestFormulaContext.testCaseDataset);
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
