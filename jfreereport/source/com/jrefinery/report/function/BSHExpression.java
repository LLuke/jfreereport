/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ---------------------
 * BSHExpression.java
 * ---------------------
 *
 * ChangeLog
 * ---------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 */
package com.jrefinery.report.function;

import bsh.Interpreter;
import com.jrefinery.report.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The BSHExpression uses the BeanShell-Scripting framework to perform the calculation, the expression itself is contained in a function called
 * <p>
 * <code>Object getValue()</code>
 * <p>
 * and this function is defined in the expression property "expression". You have to overwrite the function
 * getValue() to begin and to end you expression, but you are free to add own function to the script.
 * <p>
 * By default, common Java core and extension packages are imported for you. They are:
 * <ul>
 * <li>java.lang
 * <li>java.io
 * <li>java.util
 * <li>java.net
 * <li>java.awt
 * <li>java.awt.event
 * <li>javax.swing
 * <li>javax.swing.event
 * </ul>
 * <p>
 * An example in the XML format: (from report1.xml)
<pre>
  <expression name="expression" class="com.jrefinery.report.function.BSHExpression">
    <properties>
      <property name="expression">
        // you may import packages and classes or use the fully qualified name of the class
        import com.jrefinery.report.*;

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
  </expression>
</pre>
 *
 */
public class BSHExpression extends AbstractExpression
{
  /** The headerfile with the default initialisations.*/
  private static final String BSHHEADERFILE = "com/jrefinery/report/function/BSHExpressionHeader.txt";

  /** The beanshell-interpreter used for evaluating the expression */
  private Interpreter interpreter;

  /**
   * default constructor, create a new BeanShellExpression.
   */
  public BSHExpression()
  {
    interpreter = new Interpreter();
  }

  /**
   * Evaluates the defined expression. If an exception or an evaluation error occures, the evaluation
   * returns null and the error is logged. The current datarow and a copy of the expressions properties
   * are set to script-internal variables. Changes to the properties will not alter the expressions original
   * properties and will be lost when the evaluation is finished.
   * <p>
   * Expressions do not maintain a state and no assumptions about the order of evaluation can be made.
   *
   * @returns the evaluated value or null.
   */
  public Object getValue()
  {
    try
    {
      interpreter.set("dataRow", getDataRow());
      interpreter.set("properties", getProperties());
      return interpreter.eval("getValue ();");
    }
    catch (Exception e)
    {
      Log.error("Evaluation error ", e);
    }
    return null;
  }

  /**
   * Initializes the expression by executing the header file and the expression. The expression
   * should not call getValue() by itself, as the dataRow and the properties are not initialized
   * yet.
   * <p>
   * Initalisations of the script can be put at the end of the script:
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
   */
  public void initialize() throws FunctionInitializeException
  {
    if (getName() == null) throw new FunctionInitializeException("No null name allowed");
    InputStream in = this.getClass().getClassLoader().getResourceAsStream(BSHHEADERFILE);
    if (in == null) throw new FunctionInitializeException("Unable to locate BSHHeaderFile");

    try
    {
      // read the header, creates a skeleton
      Reader r = new InputStreamReader(new BufferedInputStream(in));
      interpreter.eval(r);
      r.close();

      // now add the userdefined expression
      // the expression is given in form of an function with the signature of:
      //
      // Object getValue ()
      //
      String expression = getProperty("expression");
      if (expression == null) throw new FunctionInitializeException("No expression set");
      interpreter.eval(expression);

    }
    catch (Exception e)
    {
      Log.error("Unable to initialize the expression", e);
      throw new FunctionInitializeException(e.getMessage());
    }
  }

  /**
   * Clones the expression and reinitializes the script.
   */
  public Object clone() throws CloneNotSupportedException
  {
    BSHExpression expression = (BSHExpression) super.clone();
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
}
