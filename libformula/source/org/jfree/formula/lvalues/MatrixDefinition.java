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
 * MatrixReference.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MatrixDefinition.java,v 1.1 2006/11/04 15:44:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;

/**
 * An Array. If many arrays are stacked together, we get a multidimensional
 * array.
 *
 * Not yet used.
 *
 * @author Thomas Morgner
 */
public class MatrixDefinition extends AbstractLValue
{
  private LValue[] matrix;

  public MatrixDefinition(final LValue[] matrix)
  {
    this.matrix = matrix;
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    // this should return an array of values ...
    throw new UnsupportedOperationException("Implement me, please!");
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    for (int i = 0; i < matrix.length; i++)
    {
      Object rawvalue = matrix[i];
      if (rawvalue instanceof LValue)
      {
        LValue lval = (LValue) rawvalue;
        if (lval.isConstant() == false)
        {
          return false;
        }
      }

    }
    return true;
  }
}
