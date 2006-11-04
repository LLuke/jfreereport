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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.parser;

import java.io.StringReader;

import org.jfree.formula.LibFormulaBoot;
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

  public static void main(String[] args) throws ParseException
  {
    FormulaParser parser = new FormulaParser();
    System.out.println (parser.parse("-10 * -10% + 10 * 10"));
    System.out.println (parser.parse("10 + [Test]"));
    System.out.println (parser.parse("+\"quoted string \\n Dirty \"\" Tricks\""));
    System.out.println (parser.parse("+SUM(-SUM(10, 20), 10 + 20)"));
  }
}
