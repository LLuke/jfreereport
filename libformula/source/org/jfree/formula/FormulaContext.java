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
 * ReferenceContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FormulaContext.java,v 1.1 2006/11/04 15:40:58 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula;

import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 31.10.2006, 14:11:52
 *
 * @author Thomas Morgner
 */
public interface FormulaContext
{
  public boolean isReferenceDirty(Object name);
  public Object resolveReference (Object name);
  public Type resolveReferenceType (Object name);

  public LocalizationContext getLocalizationContext();
  public Configuration getConfiguration();

  public FunctionRegistry getFunctionRegistry ();
  public TypeRegistry getTypeRegistry();
  public OperatorFactory getOperatorFactory();
}
