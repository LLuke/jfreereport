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
 * ----------------------
 * ObjectDescription.java
 * ----------------------
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

import java.util.Iterator;

/**
 * An interface for object descriptions. 
 * 
 * @author Thomas Morgner
 */
public interface ObjectDescription
{
  /**
   * Returns a definition.
   * 
   * @param name  the definition name.
   * 
   * @return The class.
   */
  public Class getParameterDefinition (String name);

  /**
   * Sets the value of a parameter.
   * 
   * @param name  the parameter name.
   * @param value  the parameter value.
   */
  public void setParameter (String name, Object value);

  /**
   * Returns the value of a parameter.
   * 
   * @param name  the parameter name.
   * 
   * @return The value.
   */
  public Object getParameter (String name);

  /**
   * Returns an iterator the provides access to the parameter names.
   * 
   * @return The iterator.
   */
  public Iterator getParameterNames ();

  /**
   * Returns the object class.
   * 
   * @return The Class.
   */
  public Class getObjectClass ();

  /**
   * Creates an object based on the description.
   * 
   * @return The object.
   */
  public Object createObject ();

  /**
   * Returns a new instance of the object description.
   * 
   * @return The object description.
   */
  public ObjectDescription getInstance();

  /**
   * Sets the parameters of this description object to match the supplied object.
   * 
   * @param o  the object.
   * 
   * @throws ObjectFactoryException if there is a problem while reading the
   * properties of the given object.
   */
  public void setParameterFromObject (Object o) throws ObjectFactoryException;
}
