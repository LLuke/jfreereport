/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 12.08.2002
 * Time: 20:53:29
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.function;

import bsh.Interpreter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.jrefinery.report.util.Log;

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
      return interpreter.eval ("getValue ();");
    }
    catch (Exception e)
    {
      Log.error ("Evaluation error ", e);
    }
    return null;
  }

  public void initialize() throws FunctionInitializeException
  {
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
      Log.error ("Unable to initialize the expression", e);
      throw new FunctionInitializeException(e.getMessage());
    }
  }

  public static void main (String [] args) throws Exception
  {
    BSHExpression expr = new BSHExpression();

    expr.setProperty("expression", "Object getValue () { return \"arrrggghhh :)\"; } ");
    expr.initialize();

    System.out.println (expr.getValue());
  }
}
