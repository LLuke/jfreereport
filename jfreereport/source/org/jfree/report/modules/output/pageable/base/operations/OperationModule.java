/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------
 * OperationModule.java
 * --------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationModule.java,v 1.7 2003/09/13 15:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 07-Feb-2003 : ContentCreation extracted into separate package
 */
package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;

/**
 * The base class for an operation module. Operation modules can be either
 * specific modules for a certain specialized type of content ("text/plain", for instance)
 * or a module can be a generic handler for a certain group of content ("text/*").
 * <p>
 * While a generic handler may not be as performant as a specialized handler,
 * that handler may be useful for displaying at least some of the content.
 * <p>
 * todo 090 add support for generic handlers to the operation factory.
 *
 * @author Thomas Morgner
 */
public abstract strictfp class OperationModule
{
  /** The module content type. */
  private String moduleContentType;

  /** A flag that defines that the operation module is a generic content handler. */
  private boolean generic;

  /**
   * Creates a new module.
   *
   * @param content  the content type (null not permitted).
   */
  protected OperationModule(final String content)
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
   * Returns <code>true</code> if this is a 'generic' module, and <code>false</code> otherwise.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean isGeneric()
  {
    return generic;
  }

  /**
   * Returns <code>true</code> if the module can handle the specified content type, and
   * <code>false</code> otherwise.
   *
   * @param contentType  the content type.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  public boolean canHandleContent(final String contentType)
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
   * @param col the operations collector.
   */
  public abstract void createOperations(PhysicalOperationsCollector col, Element e, Content value,
                                        Rectangle2D bounds);

  /**
   * Translates the given element alignment into a vertical alignment object.
   *  
   * @param va the element alignment.
   * @param bounds the bounds of the element
   * @return the created alignment object.
   */
  public static VerticalBoundsAlignment getVerticalLayout (ElementAlignment va, Rectangle2D bounds)
  {
    if (va.equals(ElementAlignment.TOP))
    {
      return new TopAlignment(bounds);
    }
    else if (va.equals(ElementAlignment.MIDDLE))
    {
      return new MiddleAlignment(bounds);
    }
    else
    {
      return new BottomAlignment(bounds);
    }
  }

  /**
   * Translates the given element alignment into a horizontal alignment object.
   *  
   * @param ha the element alignment.
   * @param bounds the bounds of the element
   * @return the created alignment object.
   */
  public static HorizontalBoundsAlignment getHorizontalLayout
      (ElementAlignment ha, Rectangle2D bounds)
  {
    if (ha.equals(ElementAlignment.CENTER))
    {
      return new CenterAlignment(bounds);
    }
    else if (ha.equals(ElementAlignment.RIGHT))
    {
      return new RightAlignment(bounds);
    }
    else
    {
      return new LeftAlignment(bounds);
    }
  }

  /**
   * Computes the alignment for the given element and content.
   * 
   * @param e the element that was used to create the content.
   * @param content the content that should be displayed.
   * @param bounds the bounds as computed from the layoutmanager.
   * @return the aligned content bounds.
   */
  public static Rectangle2D computeAlignmentBounds
      (Element e, Content content, Rectangle2D bounds)
  {
    Rectangle2D cbounds = content.getMinimumContentSize();
    if (cbounds == null)
    {
      // if the content could not determine its minimum bounds, then skip ...
      cbounds = bounds.getBounds2D();
    }

    final ElementAlignment va
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final VerticalBoundsAlignment vba = getVerticalLayout(va, bounds);

    final ElementAlignment ha
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    HorizontalBoundsAlignment hba = getHorizontalLayout(ha, bounds);

    final Rectangle2D abounds = vba.align(hba.align(content.getBounds()));
    return abounds;
  }
}
