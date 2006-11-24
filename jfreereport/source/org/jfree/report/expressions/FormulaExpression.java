/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.expressions;

import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.flow.ReportContext;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 04.11.2006, 19:24:04
 *
 * @author Thomas Morgner
 */
public class FormulaExpression extends AbstractExpression
{
  private static class ReportFormulaContext implements FormulaContext
  {
    private FormulaContext backend;
    private DataRow dataRow;

    public ReportFormulaContext(FormulaContext backend,
                                DataRow dataRow)
    {
      this.backend = backend;
      this.dataRow = dataRow;
    }

    public LocalizationContext getLocalizationContext()
    {
      return backend.getLocalizationContext();
    }

    public Configuration getConfiguration()
    {
      return backend.getConfiguration();
    }

    public FunctionRegistry getFunctionRegistry()
    {
      return backend.getFunctionRegistry();
    }

    public TypeRegistry getTypeRegistry()
    {
      return backend.getTypeRegistry();
    }

    public OperatorFactory getOperatorFactory()
    {
      return backend.getOperatorFactory();
    }

    public boolean isReferenceDirty(Object name)
    {
      return true;
    }

    public Type resolveReferenceType(Object name)
    {
      return AnyType.TYPE;
    }

    public Object resolveReference(Object name)
    {
      if (name == null)
      {
        throw new NullPointerException();
      }

      try
      {
        return dataRow.get(String.valueOf(name));
      }
      catch (DataSourceException e)
      {
        Log.debug("Error while resolving formula reference: ", e);
        return new LibFormulaErrorValue
            (LibFormulaErrorValue.ERROR_REFERENCE_NOT_RESOLVABLE);
      }
    }

    public DataRow getDataRow()
    {
      return dataRow;
    }

    public void setDataRow(final DataRow dataRow)
    {
      this.dataRow = dataRow;
    }
  }

  private transient Formula compiledFormula;
  private String formulaNamespace;
  private String formulaExpression;
  private String formula;

  public FormulaExpression()
  {
  }

  private synchronized FormulaContext getFormulaContext()
  {
    final ReportContext globalContext = getRuntime().getReportContext();
    return globalContext.getFormulaContext();
  }

  public String getFormula()
  {
    return formula;
  }

  public String getFormulaNamespace()
  {
    return formulaNamespace;
  }

  public String getFormulaExpression()
  {
    return formulaExpression;
  }

  public void setFormula(final String formula)
  {
    this.formula = formula;
    if (formula == null)
    {
      formulaNamespace = null;
      formulaExpression = null;
    }
    else
    {
      final int separator = formula.indexOf(':');
      if (separator <= 0 || ((separator + 1) == formula.length()))
      {
        // error: invalid formula.
        formulaNamespace = null;
        formulaExpression = null;
      }
      else
      {
        formulaNamespace = formula.substring(0, separator);
        formulaExpression = formula.substring(separator + 1);
      }
    }
    this.compiledFormula = null;
  }

  private Object computeRegularValue()
  {
    try
    {
      if (compiledFormula == null)
      {
        compiledFormula = new Formula(formulaExpression);
      }

      final FormulaExpression.ReportFormulaContext context =
          new FormulaExpression.ReportFormulaContext(getFormulaContext(), getDataRow());
      try
      {
        compiledFormula.initialize(context);
        return compiledFormula.evaluate();
      }
      finally
      {
        context.setDataRow(null);
      }
    }
    catch (Exception e)
    {
      Log.debug("Failed to compute the regular value.", e);
      return null;
    }
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object computeValue() throws DataSourceException
  {
    try
    {
      return computeRegularValue();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Clones the expression, expression should be reinitialized after the
   * cloning. <P> Expression maintain no state, cloning is done at the beginning
   * of the report processing to disconnect the used expression from any other
   * object space.
   *
   * @return A clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final FormulaExpression o = (FormulaExpression) super.clone();
    if (compiledFormula != null)
    {
      o.compiledFormula = (Formula) compiledFormula.clone();
    }
    return o;
  }
}
