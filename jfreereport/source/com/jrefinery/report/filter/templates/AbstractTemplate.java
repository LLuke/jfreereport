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
 * ---------------------
 * AbstractTemplate.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.filter.templates;

/**
 * An abstract base class that implements the {@link Template} interface.
 * 
 * @author Thomas Morgner
 */
public abstract class AbstractTemplate implements Template
{
  /** The template name. */
  private String name;

  /**
   * Creates a new template.
   */
  public AbstractTemplate()
  {
  }

  /**
   * Sets the template name.
   * 
   * @param name  the name (<code>null</code> not permitted).
   */
  public void setName(String name)
  {
    if (name == null) 
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  /**
   * Returns the template name.
   * 
   * @return The name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Returns an instance of the template by cloning.
   * 
   * @return A clone.
   */
  public Template getInstance()
  {
    try
    {
      return (Template) clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone not supported");
    }
  }

}
