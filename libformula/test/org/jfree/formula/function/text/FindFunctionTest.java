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
 * $Id: FindFunctionTest.java,v 1.3 2007/01/18 22:35:53 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
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

/**
 * 
 * @author Cedric Pronzato
 *
 */
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
        {"FIND(\"b\";\"ABC\";1)", LibFormulaErrorValue.ERROR_NOT_FOUND_VALUE},
        {"FIND(\"b\";\"bbbb\")", new BigDecimal(1)},
        {"FIND(\"b\";\"bbbb\";2)", new BigDecimal(2)},
        {"FIND(\"b\";\"bbbb\";2.9)", new BigDecimal(2)},
        {"FIND(\"b\";\"bbbb\";0)", LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE},
        {"FIND(\"b\";\"bbbb\";0.9)", LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE},
    };
  }  
  
  @BeforeClass(alwaysRun=true)
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
