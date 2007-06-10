/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: FormulaParser.java,v 1.7 2007/04/01 13:51:54 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.parser;

import java.io.StringReader;

import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.operators.DefaultOperatorFactory;
import org.jfree.formula.operators.OperatorFactory;

public class FormulaParser extends GeneratedFormulaParser
{
  // This is my parser class
  private OperatorFactory operatorFactory;

  public FormulaParser()
  {
    super(new StringReader(""));
    operatorFactory = new DefaultOperatorFactory();
    operatorFactory.initalize(LibFormulaBoot.getInstance().getGlobalConfig());
  }

  protected OperatorFactory getOperatorFactory()
  {
    return operatorFactory;
  }

  public LValue parse(String formula) throws ParseException
  {
    if (formula == null)
    {
      throw new NullPointerException("Formula-text given must not be null.");
    }
    ReInit(new StringReader(formula));
    return getExpression();
  }

  public static void main(String[] args)
      throws ParseException, EvaluationException
  {
    FormulaParser parser = new FormulaParser();

    LValue x;
//    x = parser.parse("1 * 2 + 3 * 4");
//    x.initialize(new DefaultFormulaContext());
//    System.out.println(x);
//
//    x = parser.parse("[a] * [b] + [c] * [d]");
//    x.initialize(new DefaultFormulaContext());
//    System.out.println(x);
//
//    x = parser.parse("IF([A];[B];[C])");
//    x.initialize(new DefaultFormulaContext());
//    System.out.println(x);
//
//    x = parser.parse("1 + ( 2+ (3 + (400 + 200)))");
//    x.initialize(new DefaultFormulaContext());
//    System.out.println(x);

    x = parser.parse("(1)()");
    x.initialize(new DefaultFormulaContext());
    System.out.println(x);
  }
}
