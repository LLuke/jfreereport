/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * BSHExpression.java
 * ------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BSHExpression.java,v 1.3 2003/07/23 16:02:20 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.modules.misc.beanshell;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

import bsh.Interpreter;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.Expression;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.util.Log;

/**
 * An expression that uses the BeanShell scripting framework to perform a scripted calculation.
 * The expression itself is contained in a function called
 * <p>
 * <code>Object getValue()</code>
 * <p>
 * and this function is defined in the <code>expression</code> property. You have to overwrite
 * the function <code>getValue()</code> to begin and to end your expression, but you are free to
 * add your own functions to the script.
 * <p>
 * By default, base Java core and extension packages are imported for you. They are:
 * <ul>
 * <li><code>java.lang<code>
 * <li><code>java.io</code>
 * <li><code>java.util</code>
 * <li><code>java.net</code>
 * <li><code>java.awt</code>
 * <li><code>java.awt.event</code>
 * <li><code>javax.swing</code>
 * <li><code>javax.swing.event</code>
 * </ul>
 * <p>
 * An example in the XML format: (from report1.xml)
 * <p>
 * <pre><expression name="expression" class="org.jfree.report.modules.misc.beanshell.BSHExpression">
 <properties>
 <property name="expression">
 // you may import packages and classes or use the fully qualified name of the class
 import org.jfree.report.*;

 String userdefinedFunction (String parameter, Date date)
 {
 return parameter + " - the current date is " + date);
 }

 // use simple java code to perform the expression. You may use all classes
 // available in your classpath as if you write "real" java code in your favourite
 // IDE.
 // See the www.beanshell.org site for more information ...
 //
 // A return value of type "Object" is alway implied ...
 getValue ()
 {
 return userdefinedFunction ("Hello World :) ", new Date());
 }
 </property>
 </properties>
 </expression></pre>
 *
 * @author Thomas Morgner
 */
public class BSHExpression extends AbstractExpression implements Serializable
{
  /** The headerfile with the default initialisations.*/
  public static final String BSHHEADERFILE =
      "org/jfree/report/modules/misc/beanshell/BSHExpressionHeader.txt";

  /** The beanshell-interpreter used to evaluate the expression. */
  private Interpreter interpreter;

  /**
   * default constructor, create a new BeanShellExpression.
   */
  public BSHExpression()
  {
    interpreter = new Interpreter();
  }

  /**
   * Evaluates the defined expression. If an exception or an evaluation error occures, the
   * evaluation returns null and the error is logged. The current datarow and a copy of the
   * expressions properties are set to script-internal variables. Changes to the properties will
   * not alter the expressions original properties and will be lost when the evaluation is
   * finished.
   * <p>
   * Expressions do not maintain a state and no assumptions about the order of evaluation can be
   * made.
   *
   * @return the evaluated value or null.
   */
  public Object getValue()
  {
    try
    {
      interpreter.set("properties", getProperties());
      interpreter.set("dataRow", getDataRow());
      return interpreter.eval("getValue ();");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Log.error(new Log.SimpleMessage("Evaluation error: ", 
          e.getClass(), " - ", e.getMessage()));
      throw new NullPointerException();
    }
    //return null;
  }

  /**
   * Initializes the expression by executing the header file and the expression. The expression
   * should not call getValue() by itself, as the dataRow and the properties are not initialized
   * yet.
   * <p>
   * Initialisations of the script can be put at the end of the script:
   * <pre>
   ...
   <property name="expression">
   ...
   getValue ()
   {
   return userdefinedFunction ("Hello World :) ", new Date());
   }
   ...

   // script initialisations here
   System.out.println ("Script initialized @ " + new Date());
   </property>
   ...
   </pre>
   *
   * @throws FunctionInitializeException if the expression has not been initialized correctly.
   */
  public void initialize() throws FunctionInitializeException
  {
    if (getName() == null)
    {
      throw new FunctionInitializeException("No null name allowed");
    }
    final InputStream in = this.getClass().getClassLoader().getResourceAsStream(BSHHEADERFILE);
    if (in == null)
    {
      throw new FunctionInitializeException("Unable to locate BSHHeaderFile");
    }

    try
    {
      // read the header, creates a skeleton
      final Reader r = new InputStreamReader(new BufferedInputStream(in));
      interpreter.eval(r);
      r.close();

      // now add the userdefined expression
      // the expression is given in form of a function with the signature of:
      //
      // Object getValue ()
      //
      final String expression = getProperty("expression");
      if (expression == null)
      {
        throw new FunctionInitializeException("No expression set");
      }

      // do no longer evaluate the expression without having a valid datarow.
      interpreter.eval(expression);
    }
    catch (Exception e)
    {
      Log.error("Unable to initialize the expression", e);
      throw new FunctionInitializeException("Unable to initialize the expression", e);
    }
  }

  /**
   * Clones the expression and reinitializes the script.
   *
   * @return  a clone of the expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final BSHExpression expression = (BSHExpression) super.clone();
    try
    {
      expression.interpreter = new Interpreter();
      expression.initialize();
    }
    catch (FunctionInitializeException fe)
    {
      throw new CloneNotSupportedException();
    }
    return expression;
  }

  /**
   * Return a new instance of this expression. The copy is initialized
   * and uses the same parameters as the original, but does not share any
   * objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final BSHExpression expression = (BSHExpression) super.getInstance();
    return expression;
  }
}
