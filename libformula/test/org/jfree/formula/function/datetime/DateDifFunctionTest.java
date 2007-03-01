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
 * $Id: DateDifFunctionTest.java,v 1.1 2007/02/22 21:34:46 mimil Exp $
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
public class DateDifFunctionTest
{
  private FormulaContext context;

  public DateDifFunctionTest() {}

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
        { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"y\")", new Integer(3)},
        { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"m\")", new Integer(43)},
        //TODO result not found in spec { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"d\")", new Integer()},
        { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"md\")", new Integer(0)},
        { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"ym\")", new Integer(7)},
        //TODO result not found in spec { "DATEDIF(DATE(1990;2;15);DATE(1993;9;15); \"yd\")", new Integer()},

        // Additional specs from
        // http://www.cpearson.com/excel/datedif.htm
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"d\")", new Integer(1626)},
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"m\")", new Integer(53)},
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"y\")", new Integer(4)},
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"ym\")", new Integer(5)},
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"yd\")", new Integer(165)},
        { "DATEDIF(DATE(1995;1;1);DATE(1999;6;15); \"md\")", new Integer(14)},
    };
  }
  
  @BeforeClass(alwaysRun=true)
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
    Assert.assertEquals(eval, result, formul);
  }
}
