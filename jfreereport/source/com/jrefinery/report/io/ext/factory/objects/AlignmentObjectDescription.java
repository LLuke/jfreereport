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
 * -------------------------------
 * AlignmentObjectDescription.java
 * -------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AlignmentObjectDescription.java,v 1.6 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.factory.objects;

import com.jrefinery.report.ElementAlignment;
import org.jfree.xml.factory.objects.AbstractObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * An object-description for an {@link ElementAlignment} object.
 *
 * @author Thomas Morgner
 */
public class AlignmentObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public AlignmentObjectDescription()
  {
    super(ElementAlignment.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates an {@link ElementAlignment} object based on this description.
   *
   * @return The object.
   */
  public Object createObject()
  {
    final String o = (String) getParameter("value");
    if (o == null)
    {
      return null;
    }
    if (o.equalsIgnoreCase("left"))
    {
      return ElementAlignment.LEFT;
    }
    if (o.equalsIgnoreCase("right"))
    {
      return ElementAlignment.RIGHT;
    }
    if (o.equalsIgnoreCase("center"))
    {
      return ElementAlignment.CENTER;
    }
    if (o.equalsIgnoreCase("top"))
    {
      return ElementAlignment.TOP;
    }
    if (o.equalsIgnoreCase("middle"))
    {
      return ElementAlignment.MIDDLE;
    }
    if (o.equalsIgnoreCase("bottom"))
    {
      return ElementAlignment.BOTTOM;
    }
    return null;
  }

  /**
   * Sets the parameters in the object description to match the specified object.
   *
   * @param o  the object (an {@link ElementAlignment} instance).
   *
   * @throws ObjectFactoryException if the object is not recognised.
   */
  public void setParameterFromObject(final Object o) throws ObjectFactoryException
  {
    if (o.equals(ElementAlignment.BOTTOM))
    {
      setParameter("value", "bottom");
    }
    else if (o.equals(ElementAlignment.MIDDLE))
    {
      setParameter("value", "middle");
    }
    else if (o.equals(ElementAlignment.TOP))
    {
      setParameter("value", "top");
    }
    else if (o.equals(ElementAlignment.CENTER))
    {
      setParameter("value", "center");
    }
    else if (o.equals(ElementAlignment.RIGHT))
    {
      setParameter("value", "right");
    }
    else if (o.equals(ElementAlignment.LEFT))
    {
      setParameter("value", "left");
    }
    else
    {
      throw new ObjectFactoryException("Invalid value specified for ElementAlignment");
    }
  }

}
