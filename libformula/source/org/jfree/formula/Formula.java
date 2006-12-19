/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: Formula.java,v 1.5 2006/12/03 19:22:27 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula;

import java.io.Serializable;

import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.parser.FormulaParser;
import org.jfree.formula.parser.ParseException;
import org.jfree.util.Log;

/**
 * Creation-Date: 31.10.2006, 14:43:05
 *
 * @author Thomas Morgner
 */
public class Formula implements Serializable, Cloneable
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
      final TypeValuePair typeValuePair = rootReference.evaluate();
      if (typeValuePair.getValue() instanceof LibFormulaErrorValue)
      {
        Log.debug ("Error: " + typeValuePair.getValue());
      }
      return typeValuePair.getValue();
    }
    catch(EvaluationException ee)
    {
      return ee.getErrorValue();
    }
    catch (Exception e)
    {
      Log.warn ("Evaluation failed: ", e);
      return new LibFormulaErrorValue(0);
    }
  }

  public Object clone () throws CloneNotSupportedException
  {
    final Formula o = (Formula) super.clone();
    o.rootReference = (LValue) rootReference.clone();
    return o;
  }

  public static void main(String[] args)
      throws ParseException
  {
    LibFormulaBoot.getInstance().start();
    Formula f = new Formula("0");
  }
}
