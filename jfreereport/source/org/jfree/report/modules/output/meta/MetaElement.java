/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * MetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: MetaElement.java,v 1.4 2005/01/25 00:09:53 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.meta;

import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.geom.StrictBounds;

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

  public MetaElement(final Content elementContent, final ElementStyleSheet style)
  {
    if (elementContent == null)
    {
      throw new NullPointerException("ElementContent must not be null.");
    }
    if (style == null)
    {
      throw new NullPointerException("Style is null.");
    }
    this.styleProperties = style;
    this.elementContent = elementContent;
  }

  public Object getProperty (final StyleKey key)
  {
    return styleProperties.getStyleProperty(key);
  }

  public final boolean getBooleanProperty (final StyleKey key)
  {
    return styleProperties.getBooleanStyleProperty(key);
  }

  public Object getProperty (final StyleKey key, final Object value)
  {
    return styleProperties.getStyleProperty(key, value);
  }

  public Content getContent()
  {
    return elementContent;
  }

  public final StrictBounds getBounds ()
  {
    return (StrictBounds) styleProperties.getStyleProperty(ElementStyleSheet.BOUNDS);
  }

  public final FontDefinition getFontDefinitionProperty()
  {
    return styleProperties.getFontDefinitionProperty();
  }

  public String toString ()
  {
    final StringBuffer s = new StringBuffer();
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
    // content and style is immutable...
    final MetaElement e = (MetaElement) super.clone();
    return e;
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof MetaElement))
    {
      return false;
    }

    final MetaElement element = (MetaElement) o;

    if (!elementContent.equals(element.elementContent))
    {
      return false;
    }
    if (!styleProperties.equals(element.styleProperties))
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    int result;
    result = styleProperties.hashCode();
    result = 29 * result + elementContent.hashCode();
    return result;
  }
}
