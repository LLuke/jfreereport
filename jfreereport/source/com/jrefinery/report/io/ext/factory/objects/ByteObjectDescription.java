/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * --------------------------
 * ByteObjectDescription.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

/**
 * An object-description for a <code>Byte</code> object.
 * 
 * @author Thomas Morgner
 */
public class ByteObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public ByteObjectDescription()
  {
    super(Byte.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates a new object (<code>Byte</code>) based on this description object.
   * 
   * @return The <code>Byte</code> object.
   */
  public Object createObject()
  {
    String o = (String) getParameter("value");
    return Byte.valueOf(o);
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   * 
   * @param o  the object (should be an instance of <code>Byte</code>.
   * @throws ObjectFactoryException if there is a problem while reading the
   * properties of the given object.
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if (o instanceof Byte)
    {
      throw new ObjectFactoryException("The given object is no java.lang.Byte.");
    }
    setParameter("value", String.valueOf(o));
  }
}
