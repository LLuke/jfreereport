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
 * $Id: DemoApplication.java,v 1.2 2007/02/28 19:30:15 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.formula.demo;

import javax.swing.JOptionPane;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.Formula;
import org.jfree.formula.LibFormulaBoot;
import org.jfree.formula.parser.ParseException;

/**
 * Creation-Date: Feb 21, 2007, 2:55:48 PM
 *
 * @author Thomas Morgner
 */
public class DemoApplication
{
  private DemoApplication()
  {
  }

  public static void main(final String[] args)
      throws ParseException, EvaluationException
  {
    LibFormulaBoot.getInstance().start();

    final String formula = JOptionPane.showInputDialog
        ("Please enter a formula.");

    if (formula == null)
    {
      return;
    }

    // first parse the formula. This checks the general syntax, but does not
    // check whether the used functions or references are actually valid.
    final Formula f = new Formula(formula);

    // connects the parsed formula to the context. The context provides the
    // operator and function implementations and resolves the references.
    f.initialize(new DemoFormulaContext());

    JOptionPane.showMessageDialog(null, "The result is " + f.evaluate(),
        "Result", JOptionPane.INFORMATION_MESSAGE);

    System.exit(0);
  }
}
