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
 * $Id: TimeFunctionTest.java,v 1.1 2007/04/27 22:00:47 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.math.BigDecimal;

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
public class TimeFunctionTest
{
  private FormulaContext context;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
//        { "TIME(0;0;0)=0", Boolean.TRUE},
        { "TIME(23;59;59)*60*60*24", new BigDecimal(86399)},
        /*{ "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
        { "", Boolean.TRUE },
*/
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
    Assert.assertEquals(eval, result);
  }
}
