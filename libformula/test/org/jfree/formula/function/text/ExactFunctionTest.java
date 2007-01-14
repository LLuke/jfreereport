package org.jfree.formula.function.text;

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

public class ExactFunctionTest
{
  private FormulaContext context;
  
  @DataProvider(name="defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
        {"EXACT(\"A\";\"A\")", Boolean.TRUE},
        {"EXACT(\"A\";\"a\")", Boolean.FALSE},
        {"EXACT(1;1)", Boolean.TRUE},
//TODO        {"EXACT((1/3)*3;1)", Boolean.TRUE}, check devide operator because it will result EXACT(0;1)
        {"EXACT(TRUE();TRUE())", Boolean.TRUE},
        {"EXACT(\"1\";2)", Boolean.FALSE},
        {"EXACT(\"h\";1)", Boolean.FALSE},
        {"EXACT(\"1\";1)", Boolean.TRUE},
        {"EXACT(\" 1\";1)", Boolean.FALSE},
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
