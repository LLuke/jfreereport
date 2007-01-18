/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id: IfFunctionTest.java,v 1.2 2007/01/14 18:28:57 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.logical;

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

/**
 * 
 * @author Cedric Pronzato
 *
 */
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

  @BeforeClass(alwaysRun=true)
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
