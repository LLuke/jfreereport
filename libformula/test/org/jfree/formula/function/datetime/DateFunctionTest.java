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
 * $Id: DateFunction.java,v 1.6 2006/12/30 14:54:38 taqua Exp $
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
public class DateFunctionTest
{
  private FormulaContext context;

  @DataProvider(name = "defaultTestCase")
  public Object[][] createDataTest()
  {
    return new Object[][]
    {
    { "DATE(2005;1;31)=[.C7]", Boolean.TRUE },
    { "DATE(2005;12;31)-DATE(1904;1;1)", new BigDecimal(37255) },
    { "DATE(2004;2;29)=DATE(2004;2;28)+1", Boolean.TRUE },
    { "DATE(2000;2;29)=DATE(2000;2;28)+1", Boolean.TRUE },
    { "DATE(2005;3;1)=DATE(2005;2;28)+1", Boolean.TRUE },
    { "DATE(2017.5; 1;2)=DATE(2017; 1; 2)", Boolean.TRUE },
    { "DATE(2006; 2.5;3)=DATE(2006; 2; 3)", Boolean.TRUE },
    { "DATE(2006; 1;3.5)=DATE(2006; 1; 3)", Boolean.TRUE },
    { "DATE(2006; 13; 3)=DATE(2007;1; 3)", Boolean.TRUE },
    { "DATE(2006; 1; 32)=DATE(2006;2; 1)", Boolean.TRUE },
    { "DATE(2006; 25;34)=DATE(2008;2;3)", Boolean.TRUE },
    { "DATE(2006;-1;1)=DATE(2005;11;1)", Boolean.TRUE },
    { "DATE(2006;4;-1)=DATE(2006;3;30)", Boolean.TRUE },
    { "DATE(2006;-4;-1)=DATE(2005;7;30)", Boolean.TRUE },
    { "DATE(2003;2;29)=DATE(2003;3;1)", Boolean.TRUE },
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
