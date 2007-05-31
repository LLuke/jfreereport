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
 * $Id: AndFunctionTest.java,v 1.3 2007/01/18 22:35:53 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.common.TestFormulaContext;
import org.jfree.formula.lvalues.DataTable;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * @author Cedric Pronzato
 *
 */
public class ArrayTest
{
  private FormulaContext context;

  @BeforeClass
  public void setup()
  {
    context = new TestFormulaContext(TestFormulaContext.testCaseDataset);
    LibFormulaBoot.getInstance().start();
  }

  @Test(groups="types")
  public void testRowsInlineArrays()
  {
    Formula formula = null;
    try
    {
      formula = new Formula("{3|2|1}");
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
    final TypeValuePair evaluation = formula.evaluateTyped();
    Assert.assertNotNull(evaluation);
    Assert.assertTrue(evaluation.getType().isFlagSet(Type.DATATABLE_TYPE));
    
    final DataTable table = (DataTable)evaluation.getValue();
    Assert.assertEquals(table.getColumnCount(), 1);
    Assert.assertEquals(table.getRowCount(), 3);
  }
  
  @Test(groups="types")
  public void testColumnsInlineArrays()
  {
    Formula formula = null;
    try
    {
      formula = new Formula("{3;2;1}");
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
    final TypeValuePair evaluation = formula.evaluateTyped();
    Assert.assertNotNull(evaluation);
    Assert.assertTrue(evaluation.getType().isFlagSet(Type.DATATABLE_TYPE));
    
    final DataTable table = (DataTable)evaluation.getValue();
    Assert.assertEquals(table.getColumnCount(), 3);
    Assert.assertEquals(table.getRowCount(), 1); 
  }
  
  @Test(groups="types")
  public void testInlineArrays()
  {
    Formula formula = null;
    try
    {
      formula = new Formula("{3;2;1|2;4;6}");
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
    final TypeValuePair evaluation = formula.evaluateTyped();
    Assert.assertNotNull(evaluation);
    Assert.assertTrue(evaluation.getType().isFlagSet(Type.DATATABLE_TYPE));
    
    final DataTable table = (DataTable)evaluation.getValue();
    Assert.assertEquals(table.getColumnCount(), 3);
    Assert.assertEquals(table.getRowCount(), 2); 
  }
  
  @Test(groups="types")
  public void testInvalidInlineArrays()
  {
    Formula formula = null;
    try
    {
      formula = new Formula("{3;2;1|2;6}");
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
    final Object evaluate = formula.evaluate();
    Assert.assertEquals(evaluate, LibFormulaErrorValue.ERROR_ILLEGAL_ARRAY_VALUE);
    
  }
  
  @Test(groups="types")
  public void testInvalidInlineArrays2()
  {
    Formula formula = null;
    try
    {
      formula = new Formula("{3;1|2;6;5;6}");
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
    final Object evaluate = formula.evaluate();
    Assert.assertEquals(evaluate, LibFormulaErrorValue.ERROR_ILLEGAL_ARRAY_VALUE);
    
  }
  
  public void testReferenceArrays()
  {
    
  }
}
