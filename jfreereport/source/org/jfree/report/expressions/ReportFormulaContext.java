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
 * $Id: ReportFormulaContext.java,v 1.2 2006/12/03 20:24:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.expressions;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.ContextEvaluationException;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.DataFlags;
import org.jfree.report.structure.Element;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 29.11.2006, 17:54:33
 *
 * @author Thomas Morgner
 */
public class ReportFormulaContext implements FormulaContext
{
  private FormulaContext backend;
  private DataRow dataRow;
  private Element element;

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

  public boolean isReferenceDirty(Object name) throws ContextEvaluationException
  {
    try
    {
      final DataFlags flags = dataRow.getFlags(String.valueOf(name));
      return flags.isChanged();
    }
    catch(Exception e)
    {
      throw new ContextEvaluationException
          (new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_REFERENCE_NOT_RESOLVABLE));
    }
  }

  public Type resolveReferenceType(Object name)
  {
    return AnyType.TYPE;
  }

  public Object resolveReference(Object name) throws ContextEvaluationException
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
      Log.debug ("Error while resolving formula reference: ", e);
      throw new ContextEvaluationException(new LibFormulaErrorValue
          (LibFormulaErrorValue.ERROR_REFERENCE_NOT_RESOLVABLE));
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

  public Element getElement()
  {
    return element;
  }

  public void setElement(final Element element)
  {
    this.element = element;
  }
}
