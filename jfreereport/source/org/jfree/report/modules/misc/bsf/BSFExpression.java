/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
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
 * ------------------
 * BSHExpression.java
 * ------------------
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BSFExpression.java,v 1.2 2006/04/18 11:28:41 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.modules.misc.bsf;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.jfree.report.DataRow;
import org.jfree.report.expressions.AbstractExpression;
import org.jfree.report.expressions.Expression;
import org.jfree.report.expressions.ExpressionException;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * An expression that uses the BeanShell scripting framework to perform a scripted
 * calculation. The expression itself is contained in a function called
 * <p/>
 * <code>Object getValue()</code>
 * <p/>
 * and this function is defined in the <code>expression</code> property. You have to
 * overwrite the function <code>getValue()</code> to begin and to end your expression, but
 * you are free to add your own functions to the script.
 * <p/>
 * By default, base Java core and extension packages are imported for you. They are: <ul>
 * <li><code>java.lang<code> <li><code>java.io</code> <li><code>java.util</code>
 * <li><code>java.net</code> <li><code>java.awt</code> <li><code>java.awt.event</code>
 * <li><code>javax.swing</code> <li><code>javax.swing.event</code> </ul>
 * <p/>
 * An example in the XML format: (from report1.xml)
 * <p/>
 * <pre><expression name="expression" class="org.jfree.report.modules.misc.beanshell.BSHExpression">
 * <properties>
 * <property name="expression">
 * // you may import packages and classes or use the fully qualified name of the class
 * import org.jfree.report.*;
 * <p/>
 * String userdefinedFunction (String parameter, Date date)
 * {
 * return parameter + " - the current date is " + date);
 * }
 * <p/>
 * // use simple java code to perform the expression. You may use all classes
 * // available in your classpath as if you write "real" java code in your favourite
 * // IDE.
 * // See the www.beanshell.org site for more information ...
 * //
 * // A return value of type "Object" is alway implied ...
 * getValue ()
 * {
 * return userdefinedFunction ("Hello World :) ", new Date());
 * }
 * </property>
 * </properties>
 * </expression></pre>
 *
 * @author Thomas Morgner
 */
public class BSFExpression extends AbstractExpression implements Serializable
{
  /**
   * The beanshell-interpreter used to evaluate the expression.
   */
  private transient BSFManager interpreter;
  private transient boolean invalid;
  private transient DataRowWrapper dataRowWrapper;

  private String language;
  private String expression;

  /**
   * default constructor, create a new BeanShellExpression.
   */
  public BSFExpression ()
  {
  }

  protected BSFManager createInterpreter ()
  {
    try
    {
      final BSFManager interpreter = new BSFManager();
      initializeInterpreter(interpreter);
      return interpreter;
    }
    catch (Exception e)
    {
      Log.error("Unable to initialize the expression", e);
      return null;
    }
  }

  protected void initializeInterpreter (final BSFManager interpreter)
          throws BSFException
  {
    dataRowWrapper = new DataRowWrapper();
    interpreter.declareBean("dataRow", dataRowWrapper, DataRow.class);
  }

  /**
   * Evaluates the defined expression. If an exception or an evaluation error occures, the
   * evaluation returns null and the error is logged. The current datarow and a copy of
   * the expressions properties are set to script-internal variables. Changes to the
   * properties will not alter the expressions original properties and will be lost when
   * the evaluation is finished.
   * <p/>
   * Expressions do not maintain a state and no assumptions about the order of evaluation
   * can be made.
   *
   * @return the evaluated value or null.
   */
  public Object computeValue () throws ExpressionException
  {
    if (invalid)
    {
      return null;
    }

    if (interpreter == null)
    {
      interpreter = createInterpreter();
      if (interpreter == null)
      {
        invalid = true;
        return null;
      }
    }
    try
    {
      dataRowWrapper.setParent(getDataRow());
      return interpreter.eval
              (getLanguage(), "expression", 1, 1, getExpression());
    }
    catch (Exception e)
    {
      Log.warn(new Log.SimpleMessage("Evaluation error: ",
              e.getClass(), " - ", e.getMessage()), e);
      return null;
    }
  }

  /**
   * Return a new instance of this expression. The copy is initialized and uses
   * the same parameters as the original, but does not share any objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final BSFExpression expression = (BSFExpression) super.getInstance();
    expression.interpreter = null;
    return expression;
  }

  /**
   * Serialisation support. The transient child elements were not saved.
   *
   * @param in the input stream.
   * @throws IOException            if there is an I/O error.
   * @throws ClassNotFoundException if a serialized class is not defined on this system.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
  }

  public String getExpression ()
  {
    return expression;
  }

  protected void invalidate ()
  {
    this.interpreter = null;
  }

  public void setExpression (final String expression)
  {
    if (ObjectUtilities.equal(this.expression, expression))
    {
      return;
    }
    this.expression = expression;
    this.interpreter = null;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage(final String language)
  {
    if (ObjectUtilities.equal(this.language, language))
    {
      return;
    }
    this.language = language;
    this.interpreter = null;
  }
}
