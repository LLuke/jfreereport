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
package org.jfree.formula.common;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.formula.ContextEvaluationException;
import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.util.Configuration;

/**
 * 
 * @author Cedric Pronzato
 *
 */
public class TestFormulaContext implements FormulaContext
{
  private FormulaContext formulaContext;
  private TableModel model;
  /*public static final TableModel testCaseDataset;
  static {
    testCaseDataset = new DefaultTableModel(18, 2);
    testCaseDataset.setValueAt(aValue, rowIndex, columnIndex)
  }*/
  
/*
id B C
3 ="7"
4 =2 4
5 =3 5
6 =1=1 7
7 ="Hello" 2005-01-31
8 2006-01-31
9 =1/0 02:00:00
10 =0 23:00:00
11 3 5
12 4 6
13 2005-01-31T01:00:00 8
14 1 4
15 2 3
16 3 2
17 4 1
 */
  public static final TableModel testCaseDataset = new AbstractTableModel()
  {
    private Date createDate1() {
      Calendar cal = GregorianCalendar.getInstance();
      cal.set(GregorianCalendar.YEAR, 2005);
      cal.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
      cal.set(GregorianCalendar.DAY_OF_MONTH, 31);
      
      return cal.getTime();
    }
    private Date createDate2() {
      return null;
    }
    private Object[][] data = new Object[][] {
        {null, null},
        {null, null},
        {null, null},
        {"7", null},
        {new BigDecimal(2), new BigDecimal(4)},
        {new BigDecimal(3), new BigDecimal(5)},
        {"1=1", new BigDecimal(7)},
        {"Hello", createDate1()},
        {null, createDate2()},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
    };

    public int getColumnCount()
    {
      return 2;
    }
    
    public String getColumnName(int column)
    {
      if(column==0)
      {
        return "B";
      }
      else if(column==1)
      {
        return "C";
      }
      return null;
    }
    
    public int getRowCount()
    {
      return 18;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
     return data[rowIndex][columnIndex];
    }
    
  };
  
  public TestFormulaContext()
  {
    this(new DefaultTableModel());
  }
  
  public TestFormulaContext(TableModel model)
  {
    formulaContext = new DefaultFormulaContext();
    this.model = model;
  }
  
  public Configuration getConfiguration()
  {
    return formulaContext.getConfiguration();
  }

  public FunctionRegistry getFunctionRegistry()
  {
    return formulaContext.getFunctionRegistry();
  }

  public LocalizationContext getLocalizationContext()
  {
    return formulaContext.getLocalizationContext();
  }

  public OperatorFactory getOperatorFactory()
  {
    return formulaContext.getOperatorFactory();
  }

  public TypeRegistry getTypeRegistry()
  {
    return formulaContext.getTypeRegistry();
  }

  public boolean isReferenceDirty(Object name) throws ContextEvaluationException
  {
    return formulaContext.isReferenceDirty(name);
  }

  public Object resolveReference(Object name) throws ContextEvaluationException
  {
    if(name instanceof String)
    {
      String ref = ((String)name);
      final String columnName = ref.substring(1,2);
      int col = -1;
      for(int i=0; i<model.getColumnCount(); i++) {
        if(columnName.equalsIgnoreCase(model.getColumnName(i))) {
          col = i;
        }
      }
      final int row = Integer.parseInt(ref.substring(2));
      //System.out.println("Fetching row:"+row+", col:"+col+"["+columnName+"]");
      return model.getValueAt(row, col);
    }
    return null;
  }

  public Type resolveReferenceType(Object name) throws ContextEvaluationException
  {
    // TODO Auto-generated method stub
    return null;
  }

}
