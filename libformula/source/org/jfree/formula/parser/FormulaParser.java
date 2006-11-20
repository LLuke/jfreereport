/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * FormulaParser.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FormulaParser.java,v 1.2 2006/11/04 17:27:37 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.parser;

import java.io.StringReader;

import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.operators.DefaultOperatorFactory;
import org.jfree.formula.operators.OperatorFactory;

public class FormulaParser extends GeneratedFormulaParser {
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

  public LValue parse (String formula) throws ParseException
  {
    ReInit(new StringReader(formula));
    return getExpression(0);
  }

  public static void main(String[] args)
      throws ParseException, EvaluationException
  {
    FormulaParser parser = new FormulaParser();

    LValue x = parser.parse("1 * 2 + 3 * 4");
    x.initialize(new DefaultFormulaContext());
    System.out.println (x);

    x = parser.parse("[a] * [b] + [c] * [d]");
    x.initialize(new DefaultFormulaContext());
    System.out.println (x);

    x = parser.parse("IF([A];[B];[C])");
    x.initialize(new DefaultFormulaContext());
    System.out.println (x);


  }
}
