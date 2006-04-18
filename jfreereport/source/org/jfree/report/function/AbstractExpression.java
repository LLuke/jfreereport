/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * AbstractExpression.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractExpression.java,v 1.11 2006/01/24 18:58:29 taqua Exp $
 *
 * Changes
 * -------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 18-Dec-2002 : More Javadoc updates (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * An abstract base class for implementing new report expressions.
 * <p/>
 * Expressions are stateless functions which have access to the report's {@link
 * DataRow}. All expressions are named and the defined names have to be unique
 * within the report's expressions, functions and fields of the datasource.
 * Expressions are configured using properties.
 * <p/>
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExpression implements Expression, Serializable
{
  /** The expression name. */
  private String name;

  /** The data row. */
  private transient ExpressionRuntime runtime;

  private boolean precompute;
  private boolean deepTraversal;
  private boolean exported;

  /**
   * Creates an unnamed expression. Make sure the name of the expression is set
   * using {@link #setName} before the expression is added to the report's
   * expression collection.
   */
  protected AbstractExpression()
  {
    name = super.toString();
  }

  /**
   * Returns the name of the expression.
   *
   * @return the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the expression. <P> The name should be unique among: <ul>
   * <li>the functions and expressions for the report; <li>the columns in the
   * report's <code>TableModel</code>; </ul> This allows the expression to be
   * referenced by name from any report element.
   * <p/>
   * You should not change the name of an expression once it has been added to
   * the report's expression collection.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException(
              "AbstractExpression.setName(...) : name is null.");
    }
    this.name = name;
  }

  /**
   * Returns the current {@link MasterDataRow}.
   *
   * @return the data row.
   */
  public DataRow getDataRow()
  {
    if (runtime == null)
    {
      return null;
    }
    return runtime.getDataRow();
  }

  /**
   * Clones the expression.  The expression should be reinitialized after the
   * cloning. <P> Expressions maintain no state, cloning is done at the
   * beginning of the report processing to disconnect the expression from any
   * other object space.
   *
   * @return a clone of this expression.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
          throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer
   * share any changeable objects with the original function. Only the datarow
   * may be shared.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    try
    {
      return (Expression) clone();
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }

  protected ResourceBundleFactory getResourceBundleFactory()
  {
    if (runtime == null)
    {
      return null;
    }
    return runtime.getResourceBundleFactory();
  }

  protected  Configuration getReportConfiguration()
  {
    if (runtime == null)
    {
      return null;
    }
    return runtime.getConfiguration();
  }

  protected Locale getParentLocale ()
  {
    if (runtime == null)
    {
      return null;
    }
    return runtime.getDeclaringParent().getLocale();
  }

  /**
   * Defines the DataRow used in this expression. The dataRow is set when the
   * report processing starts and can be used to access the values of functions,
   * expressions and the reports datasource.
   *
   * @param runtime the runtime information for the expression
   */
  public void setRuntime(ExpressionRuntime runtime)
  {
    this.runtime = runtime;
  }

  protected ExpressionRuntime getRuntime()
  {
    return runtime;
  }

  public boolean isPrecompute()
  {
    return precompute;
  }

  public void setPrecompute(final boolean precompute)
  {
    this.precompute = precompute;
  }

  public boolean isDeepTraversal()
  {
    return deepTraversal;
  }

  public void setDeepTraversal(final boolean deepTraversal)
  {
    this.deepTraversal = deepTraversal;
  }

  public boolean isExported()
  {
    return exported;
  }

  public void setExported(final boolean exported)
  {
    this.exported = exported;
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    info.setDeepTraversal(deepTraversal);
    info.setExported(exported);
    info.setPrecompute(precompute);
  }
}
