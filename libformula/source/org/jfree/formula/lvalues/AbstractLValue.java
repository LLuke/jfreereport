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
 * AbstractLValue.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractLValue.java,v 1.1 2006/11/04 15:44:32 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.typing.Type;

/**
 * Creation-Date: 01.11.2006, 18:19:00
 *
 * @author Thomas Morgner
 */
public abstract class AbstractLValue implements LValue
{
  private static LValue[] EMPTY_CHILDS = new LValue[0];

  private transient FormulaContext context;

  protected AbstractLValue()
  {
  }

  public void initialize(FormulaContext context) throws EvaluationException
  {
    this.context = context;
  }

  public FormulaContext getContext()
  {
    if (context == null) throw new NullPointerException();
    return context;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return (AbstractLValue) super.clone();
  }

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues()
  {
    return EMPTY_CHILDS;
  }

  /**
   * Querying the value type is only valid *after* the value has been
   * evaluated.
   *
   * @return
   */
  public Type getValueType()
  {
    return null;
  }
}
