/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * OperationModul.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationModul.java,v 1.2 2002/12/05 12:05:10 mungady Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.contents.Content;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * The base class for an operation module. Operation modules can be either
 * specific modules for a certain specialized type of content ("text/plain", for instance)
 * or a modul can be a generic handler for a certain group of content ("text/*").
 * <p>
 * While a generic handler may not be as performant as a specialized handler,
 * that handler may be usefull for displaying at least some of the content.
 * <p>
 * todo: add support for generic handlers to the operation factory. 
 *
 * @author Thomas Morgner
 */
public abstract class OperationModul
{
  /** The module type. */
  private String moduleContentType;
  
  /** A flag that defines that the operation modul is a generic content handler */
  private boolean generic;

  /**
   * Creates a new module.
   *
   * @param content  the content type (null not permitted).
   */
  public OperationModul (String content)
  {
    if (content == null) 
    {
      throw new NullPointerException();
    }
    this.moduleContentType = content;
    this.generic = false;

    if (moduleContentType.endsWith("*"))
    {
      generic = true;
      moduleContentType = moduleContentType.substring(0, moduleContentType.length() - 1);
    }
  }

  /**
   * Returns the module content type. 
   *
   * @return the type.
   */
  public String getModuleContentType()
  {
    return moduleContentType;
  }

  /**
   * Returns the 'generic' flag.
   *
   * @return true or false.
   */
  public boolean isGeneric()
  {
    return generic;
  }

  /**
   * Returns true if the module can handle the specified content type, and false 
   * otherwise.
   *
   * @param contentType  the content type.
   *
   * @return true or false.
   */
  public boolean canHandleContent (String contentType)
  {
    if (isGeneric())
    {
      return (contentType.startsWith(getModuleContentType()));
    }
    else
    {
      return (contentType.equals(getModuleContentType()));
    }
  }

  /**
   * Creates a list of operations for an element.
   *
   * @param e  the element.
   * @param value  the value.
   * @param bounds  the bounds.
   *
   * @return the list of operations.
   */
  public abstract List createOperations (Element e, Content value, Rectangle2D bounds);

  /**
   * Creates content for an element.
   * 
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws OutputTargetException if there is a problem with the OutputTarget.
   */
  public abstract Content createContentForElement(Element e, Rectangle2D bounds, OutputTarget ot)
      throws OutputTargetException;
}
