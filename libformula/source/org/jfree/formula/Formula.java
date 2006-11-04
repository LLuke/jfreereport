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
 * Formula.java
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
package org.jfree.formula;

import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.parser.FormulaParser;
import org.jfree.formula.parser.ParseException;

/**
 * Creation-Date: 31.10.2006, 14:43:05
 *
 * @author Thomas Morgner
 */
public class Formula
{
  private LValue rootReference;

  public Formula(final String formulaText) throws ParseException
  {
    FormulaParser parser = new FormulaParser();
    this.rootReference = parser.parse(formulaText);
  }

  public Formula(final LValue rootReference)
  {
    this.rootReference = rootReference;
  }

  public void initialize (FormulaContext context) throws EvaluationException
  {
    rootReference.initialize(context);
  }

  public Object evaluate ()
  {
    try
    {
      return rootReference.evaluate();
    }
    catch (EvaluationException e)
    {
      return new LibFormulaErrorValue(0);
    }
  }
}
