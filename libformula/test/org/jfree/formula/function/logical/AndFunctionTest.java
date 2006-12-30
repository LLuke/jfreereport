package org.jfree.formula.function.logical;

import org.jfree.formula.DefaultFormulaContext;
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


public class AndFunctionTest
{
  private FormulaContext context;
  
  @DataProvider(name="defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
        {"AND(FALSE();FALSE())", Boolean.FALSE},
        {"AND(FALSE();TRUE())", Boolean.FALSE},
        {"AND(TRUE();FALSE())", Boolean.FALSE},
        {"AND(TRUE();TRUE())", Boolean.TRUE},
        //define NA first {new Formula("AND(TRUE();NA())"), },
        {"AND(1;TRUE())", Boolean.TRUE},
        {"AND(0;TRUE())", Boolean.FALSE},
        {"AND(TRUE();TRUE();TRUE())", Boolean.TRUE},
        {"AND(TRUE())", Boolean.TRUE},
    };
  }
  
  @BeforeClass
  public void setup() {
    context = new TestFormulaContext();
    LibFormulaBoot.getInstance().start();
  }
  
  @Test(dataProvider="defaultTestCase", groups="functions")
  public void test(String formul, Boolean result)
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
    Assert.assertEquals(formula.evaluate(), result);
  }
}
