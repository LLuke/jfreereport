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

public class BSHExpression extends AbstractExpression
{
  private static final String BSHHEADERFILE = "com/jrefinery/report/function/BSHExpressionHeader.txt";
  private Interpreter interpreter;

  public BSHExpression()
  {
    interpreter = new Interpreter();
  }

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
