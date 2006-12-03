/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;

/**
 * A content function is only valid when evaluating either the 'content'
 * property or the 'string-set' property. These functions produce ContentToken.
 *
 * ContentFunctions are only valid as first-level functions. That means, when
 * evaluating nested function calls like 'url(attr(blah))', the URL function
 * will be resolved as ContentFunction, but the attr-function will be resolved
 * as ordinary CSSValue function.
 *
 * @author Thomas Morgner
 */
public interface ContentFunction
{
  public ContentToken evaluate (final LayoutProcess layoutProcess,
                                final LayoutElement element,
                                final CSSFunctionValue function)
          throws FunctionEvaluationException;

}
