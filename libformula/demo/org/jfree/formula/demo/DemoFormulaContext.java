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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.formula.demo;

import javax.swing.JOptionPane;

import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.AnyType;

/**
 * A formula context that asks the user for input.
 *
 * @author Thomas Morgner
 */
public class DemoFormulaContext extends DefaultFormulaContext
{
  public DemoFormulaContext()
  {
  }

  public Object resolveReference(Object name)
  {
    final String input = JOptionPane.showInputDialog
        ("Please enter a value for '" + name + "'");
    return input;
  }

  public Type resolveReferenceType(Object name)
  {
    // by returning the correct type of the reference, you can speed up
    // the formula computation a little bit, as we dont have to guess the
    // type from scratch.

    // If you dont know the type, return ANYTYPE. We will start looking at
    // the referenced object in that case.
    return AnyType.TYPE;
  }
}
