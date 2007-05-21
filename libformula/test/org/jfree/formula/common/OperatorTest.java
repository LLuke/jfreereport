/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2007, by Pentaho Corporation and Contributors.
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
 * $Id: OperatorTest.java,v 1.2 2007/05/17 21:29:48 mimil Exp $
 * ------------
 * (C) Copyright 2007, by Pentaho Corporation.
 */
package org.jfree.formula.common;

import java.math.BigDecimal;

import org.jfree.formula.ContextEvaluationException;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OperatorTest
{
  private FormulaContext context;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
    { "1 + 1", new BigDecimal(2) },
    { "1 - 1", new BigDecimal(0) },
    { "1 = 1", Boolean.TRUE },
    { "1 <> 1", Boolean.FALSE },
    { "1 < 1", Boolean.FALSE },
    { "1 > 1", Boolean.FALSE },
    { "1 >= 1", Boolean.TRUE },
    { "1 <= 1", Boolean.TRUE },
    { "1 * 1", new BigDecimal(1) },
    { "1 ^ 1", new Double(1.0) },
    { "1 / 1", new BigDecimal(1) },
    { "1%", new BigDecimal("0.01") },
    { "0.1%", new BigDecimal("0.001") } };
  }

  @BeforeClass(alwaysRun = true)
  public void setup()
  {
    context = new TestFormulaContext(TestFormulaContext.testCaseDataset);
    LibFormulaBoot.getInstance().start();
  }

  @Test(dataProvider = "defaultTestCase", groups = "operators")
  public void test(String formul, Object result)
  {
    Formula formula = null;
    try
    {
      formula = new Formula(formul);
    }
    catch (ParseException e1)
    {
      Assert.fail("Error while parsing the formula", e1);
    }
    try
    {
      formula.initialize(context);
    }
    catch (EvaluationException e)
    {
      Assert.fail("Initialization Error", e);
    }
    Assert.assertEquals(formula.evaluate().getClass(), result.getClass());
    Assert.assertEquals(formula.evaluate(), result);
  }

  @Test(groups = "operators")
  public void testRangeOperator()
  {

    try
    {
      final Object resolveReference = context.resolveReference(".B4:.B5");
      Assert.assertNotNull(resolveReference, "Reference should not be null");
//      Assert.assertEquals(resolveReference, new Object[]
//      { context.resolveReference(".B4"), context.resolveReference(".B5") },
//          "Wrong references returned");

    }
    catch (ContextEvaluationException e)
    {
      Assert.fail("Error while resolving a range reference", e);
    }
  }
}
