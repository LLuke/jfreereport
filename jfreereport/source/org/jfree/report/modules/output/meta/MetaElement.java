/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * MetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.meta;

import java.awt.geom.Rectangle2D;

import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.FontDefinition;

/**
 * Lightweight structures to carry layout information.
 * <p>
 * These elements are produced by the OutputFunction and will
 * be transformed into the final output by the OutputProcessor.
 * This two-step process replaces the old direct way of producing
 * the output.
 * <p>
 * The content of the styleProperties is defined by the OutputFunction,
 * and should contain the complete subset necessary to display the
 * content correctly. The properties do not inherit from enclosing
 * bands, all inheritance must have been resolved while the element
 * gets created.
 * <p>
 * Once created these elements should be considered independent of
 * the created objects.
 */
public class MetaElement implements Cloneable
{
  private ElementStyleSheet styleProperties;
  private Content elementContent;

  public MetaElement(Content elementContent, ElementStyleSheet style)
  {
    if (elementContent == null)
    {
      throw new NullPointerException("ElementContent must not be null.");
    }
    this.styleProperties = style;
    this.elementContent = elementContent;
  }

  public Object getProperty (StyleKey key)
  {
    return styleProperties.getStyleProperty(key);
  }

  public final boolean getBooleanProperty (StyleKey key)
  {
    return styleProperties.getBooleanStyleProperty(key);
  }

  public Object getProperty (StyleKey key, Object value)
  {
    return styleProperties.getStyleProperty(key, value);
  }

  public Content getContent()
  {
    return elementContent;
  }

  public final Rectangle2D getBounds ()
  {
    return (Rectangle2D) styleProperties.getStyleProperty(ElementStyleSheet.BOUNDS);
  }

  public final FontDefinition getFontDefinitionProperty()
  {
    return styleProperties.getFontDefinitionProperty();
  }

  public String toString ()
  {
    StringBuffer s = new StringBuffer();
    s.append("MetaElement={bounds=");
    s.append(getBounds());
    s.append(", content=");
    s.append(getContent());
    s.append("}");
    return s.toString();
  }

  /**
   * Creates and returns a copy of this object.  The precise meaning
   * of "copy" may depend on the class of the object.
   *
   * @return     a clone of this instance.
   * @exception  CloneNotSupportedException  if the object's class does not
   *               support the <code>Cloneable</code> interface. Subclasses
   *               that override the <code>clone</code> method can also
   *               throw this exception to indicate that an instance cannot
   *               be cloned.
   * @see Cloneable
   */
  public Object clone() throws CloneNotSupportedException
  {
    // content is immutable...
    MetaElement e = (MetaElement) super.clone();
    e.styleProperties = (ElementStyleSheet) styleProperties.clone();
    return e;
  }
}
