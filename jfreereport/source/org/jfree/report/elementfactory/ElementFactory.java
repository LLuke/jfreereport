/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * ElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ElementFactory.java,v 1.9 2004/05/07 08:24:41 mungady Exp $
 *
 * Changes
 * -------------------------
 * 09-Jun-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.jfree.report.Element;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;

/**
 * The class element factory is the base class for all Element Factories.
 * Element factories can be used to create predefined element types. The
 * properties allow null values, if a property is null, it will not be
 * defined. Undefined properties can inherit their values from the element's
 * parent bands.
 *
 * @author Thomas Morgner
 */
public abstract class ElementFactory
{
  /** The name of the new element. */
  private String name;
  /** The elements minimum size. */
  private Dimension2D minimumSize;
  /** The elements maximum size. */
  private Dimension2D maximumSize;
  /** The elements preferred size. */
  private Dimension2D preferredSize;
  /** The elements absolute position. */
  private Point2D absolutePosition;
  /** The elements dynamic content height flag. */
  private Boolean dynamicHeight;
  /** The elements layout cachable flag. */
  private Boolean layoutCachable;
  /** The elements visible flag. */
  private Boolean visible;

  /**
   * Default Constructor.
   */
  public ElementFactory()
  {
  }

  /**
   * Returns the name of the new element.
   *
   * @return the name of the element.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Defines the name of the element. If the name is null, the default (anonymous)
   * name will be used.
   *
   * @param name the element name.
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * Returns the element's minimum size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @return the element's minimum size.
   */
  public Dimension2D getMinimumSize()
  {
    return minimumSize;
  }

  /**
   * Defines the element's minimum size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @param minimumSize the element's minimum size.
   */
  public void setMinimumSize(final Dimension2D minimumSize)
  {
    this.minimumSize = minimumSize;
  }

  /**
   * Returns the element's maximum size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @return the element's maximum size.
   */
  public Dimension2D getMaximumSize()
  {
    return maximumSize;
  }

  /**
   * Defines the element's maximum size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @param maximumSize the element's maximum size.
   */
  public void setMaximumSize(final Dimension2D maximumSize)
  {
    this.maximumSize = maximumSize;
  }

  /**
   * Returns the element's preferred size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @return the element's preferred size.
   */
  public Dimension2D getPreferredSize()
  {
    return preferredSize;
  }

  /**
   * Returns the element's preferred size. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @param preferredSize the element's preferred size.
   */
  public void setPreferredSize(final Dimension2D preferredSize)
  {
    this.preferredSize = preferredSize;
  }

  /**
   * Returns the element's absolute position. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @return the element's absolute position.
   */
  public Point2D getAbsolutePosition()
  {
    return absolutePosition;
  }

  /**
   * Returns the element's absolute position. Whether this property is used during the
   * layouting depends on the layout manager implementation of the container.
   *
   * @param absolutePosition the element's absolute position.
   */
  public void setAbsolutePosition(final Point2D absolutePosition)
  {
    this.absolutePosition = absolutePosition;
  }

  /**
   * Defines whether the element's height should be adjusted automaticly. This feature
   * is expensive and be used with care. Set the value to null, to declare that the
   * dynamic feature is undefined.
   *
   * @return the state of the dynamic feature or null, if the feature is undefined.
   */
  public Boolean getDynamicHeight()
  {
    return dynamicHeight;
  }

  /**
   * Defines the state of the element's dynamic content height feature.
   *
   * @param dynamicHeight the new value of the elements dynamic height feature.
   */
  public void setDynamicHeight(final Boolean dynamicHeight)
  {
    this.dynamicHeight = dynamicHeight;
  }

  public Boolean getLayoutCachable ()
  {
    return layoutCachable;
  }

  public void setLayoutCachable (final Boolean layoutCachable)
  {
    this.layoutCachable = layoutCachable;
  }

  public Boolean getVisible ()
  {
    return visible;
  }

  public void setVisible (final Boolean visible)
  {
    this.visible = visible;
  }

  /**
   * Applies the defined name to the created element.
   * 
   * @param e the element which was created.
   */
  protected void applyElementName (final Element e)
  {
    if (getName() != null)
    {
      e.setName(getName());
    }
  }

  /**
   * Applies the style definition to the elements stylesheet.
   *  
   * @param style the element stylesheet which should receive the style definition.
   */
  protected void applyStyle (final ElementStyleSheet style)
  {
    style.setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, getAbsolutePosition());
    style.setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, getDynamicHeight());
    style.setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, getMaximumSize());
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, getMinimumSize());
    style.setStyleProperty(ElementStyleSheet.PREFERREDSIZE, getPreferredSize());
    style.setStyleProperty(ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE, getLayoutCachable());
    style.setStyleProperty(ElementStyleSheet.VISIBLE, getVisible());
  }

  /**
   * Creates a new instance of the element.
   *
   * @return the newly generated instance of the element.
   */
  public abstract Element createElement();
}
