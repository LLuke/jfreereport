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
 * $Id: Formula.java,v 1.12 2007/05/12 23:53:15 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula;

import java.io.Serializable;

import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.parser.FormulaParser;
import org.jfree.formula.parser.ParseException;
import org.jfree.formula.parser.TokenMgrError;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
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
    try
    {
      final FormulaParser parser = new FormulaParser();
      this.rootReference = parser.parse(formulaText);
    }
    catch(TokenMgrError tokenMgrError)
    {
      // This is ugly.
      throw new ParseException(tokenMgrError.getMessage());
    }
  }

  public Formula(final LValue rootReference)
  {
    this.rootReference = rootReference;
  }

  public void initialize (final FormulaContext context) throws EvaluationException
  {
    rootReference.initialize(context);
  }

  /**
   * Returns the root reference for this formula. This allows external programms
   * to modify the formula directly.
   *
   * @return
   */
  public LValue getRootReference()
  {
    return rootReference;
  }

  public TypeValuePair evaluateTyped ()
  {
    try
    {
      final TypeValuePair typeValuePair = rootReference.evaluate();
      if (typeValuePair == null)
      {
        return new TypeValuePair
            (ErrorType.TYPE, LibFormulaErrorValue.ERROR_NA_VALUE);
      }
      if(typeValuePair.getType().isFlagSet(Type.ERROR_TYPE))
      {
        Log.debug ("Error: " + typeValuePair.getValue());
      }
      return typeValuePair;
    }
    catch(EvaluationException ee)
    {
      Log.warn ("Evaluation failed: ", ee);
      return new TypeValuePair(ErrorType.TYPE, ee.getErrorValue());
    }
    catch (Exception e)
    {
      Log.warn ("Evaluation failed: ", e);
      return new TypeValuePair(ErrorType.TYPE, LibFormulaErrorValue.ERROR_UNEXPECTED_VALUE);
    }
  }
  
  public Object evaluate ()
  {
    return evaluateTyped().getValue();
  }

  public Object clone () throws CloneNotSupportedException
  {
    final Formula o = (Formula) super.clone();
    o.rootReference = (LValue) rootReference.clone();
    return o;
  }
}
