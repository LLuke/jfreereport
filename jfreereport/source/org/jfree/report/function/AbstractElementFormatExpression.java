/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * AbstractElementFormatFunction.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractElementFormatFunction.java,v 1.5 2005/04/09 17:43:13 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function;

import org.jfree.report.DataSourceException;
import org.jfree.report.structure.Element;

public abstract class AbstractElementFormatExpression extends AbstractExpression
{
  private String element;
  private String namespace;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  protected AbstractElementFormatExpression ()
  {
  }

  /**
   * Sets the element name. The name denotes an element within the item band. The element
   * will be retrieved using the getElement(String) function.
   *
   * @param name The element name.
   * @see org.jfree.report.structure.Section#getElement(String)
   */
  public void setElement (final String name)
  {
    this.element = name;
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   */
  public String getElement ()
  {
    return element;
  }

  /**
   * The advance method is a signal for the function to update its internal
   * state.
   */
  public Object getValue() throws DataSourceException
  {
    processElement(getRuntime().getDeclaringParent());
    return null;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public void setNamespace(final String namespace)
  {
    this.namespace = namespace;
  }

  protected abstract void processElement (Element element) throws DataSourceException;
}
