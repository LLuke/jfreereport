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
 * ---------------------------------
 * ClassLoaderObjectDescription.java
 * ---------------------------------
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
 * An object-description for a class loader.
 * 
 * @author Thomas Morgner
 */
public class ClassLoaderObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public ClassLoaderObjectDescription()
  {
    super(Object.class);
    setParameterDefinition("class", String.class);
  }

  /**
   * Creates an object based on this object description.
   * 
   * @return The object.
   */
  public Object createObject()
  {
    try
    {
      String o = (String) getParameter("class");
      return getClass().getClassLoader().loadClass(o).newInstance();
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Sets the parameters of the object description to match the supplied object.
   * 
   * @param o  the object.
   * 
   * @throws ObjectFactoryException ??
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    setParameter("value", o.getClass().getName());
  }
}
