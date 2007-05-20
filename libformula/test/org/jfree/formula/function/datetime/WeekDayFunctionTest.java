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
 * $Id: DateFunctionTest.java,v 1.7 2007/04/27 22:00:47 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

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

/**
 * 
 * @author Cedric Pronzato
 * 
 */
public class WeekDayFunctionTest
{
  private FormulaContext context;

  private WeekDayFunction function;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
    { "WEEKDAY(DATE(2006;5;21))", new Integer(1) },
    { "WEEKDAY(DATE(2005;1;1))", new Integer(7) },
    { "WEEKDAY(DATE(2005;1;1);1)", new Integer(7) },
    { "WEEKDAY(DATE(2005;1;1);2)", new Integer(6) },
    { "WEEKDAY(DATE(2005;1;1);3)", new Integer(5) }, };
  }

  @DataProvider(name = "typeTestCase")
  public Object[][] createTypeDataTest()
  {
    return new Object[][]
    {
    { 1, 7, 6 },
    { 2, 1, 0 },
    { 3, 2, 1 },
    { 4, 3, 2 },
    { 5, 4, 3 },
    { 6, 5, 4 },
    { 7, 6, 5 }, };
  }

  @BeforeClass(alwaysRun = true)
  public void setup()
  {
    context = new TestFormulaContext();
    LibFormulaBoot.getInstance().start();
    function = new WeekDayFunction();
  }

  @Test(dataProvider="typeTestCase")
  public void testAllTypes(int type1, int type2, int type3)
  {

    Assert.assertEquals(function.convertType(type1, 1), type1,
        "Error with Type 1 conversion");
    Assert.assertEquals(function.convertType(type1, 2), type2,
        "Error with Type 2 conversion");
    Assert.assertEquals(function.convertType(type1, 3), type3,
        "Error with Type 3 conversion");
  }

  @Test(dataProvider = "defaultTestCase", groups = "functions")
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
    Object eval = formula.evaluate();
    Assert.assertEquals(eval, result, "Failure on " + eval.getClass());
  }
}
