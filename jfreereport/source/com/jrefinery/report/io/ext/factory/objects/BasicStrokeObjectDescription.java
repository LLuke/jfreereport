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
 * BasicStrokeObjectDescription.java
 * ---------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.objects;

import java.awt.BasicStroke;

/**
 * An object-description for a <code>BasicStroke</code> object.
 * 
 * @author Thomas Morgner
 */
public class BasicStrokeObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public BasicStrokeObjectDescription()
  {
    super(BasicStroke.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Returns a parameter as a float.
   * 
   * @param param  the parameter name.
   * 
   * @return The float value.
   */
  private float getFloatParameter (String param)
  {
    String p = (String) getParameter(param);
    if (p == null) 
    {
      return 0;
    }
    try
    {
      return Float.parseFloat(p);
    }
    catch (Exception e)
    {
      return 0;
    }
  }

  /**
   * Creates a new <code>BasicStroke</code> object based on this description.
   * 
   * @return The <code>BasicStroke</code> object.
   */
  public Object createObject()
  {
    float width = getFloatParameter("value");
    return new BasicStroke(width);
  }

  /**
   * Sets the parameters for this description to match the supplied object.
   * 
   * @param o  the object (instance of <code>BasicStroke</code> required).
   * 
   * @throws ObjectFactoryException if the supplied object is not an instance of 
   *         <code>BasicStroke</code>.
   */
  public void setParameterFromObject(Object o) throws ObjectFactoryException
  {
    if ((o instanceof BasicStroke) == false)
    {
      throw new ObjectFactoryException("Expected object of type BasicStroke");
    }
    BasicStroke bs = (BasicStroke) o;
    setParameter("value", String.valueOf(bs.getLineWidth()));
  }
}
