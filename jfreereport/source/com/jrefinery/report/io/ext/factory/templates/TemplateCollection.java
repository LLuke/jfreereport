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
 * -----------------------
 * TemplateCollection.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplateCollection.java,v 1.7 2003/03/07 16:56:00 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.templates;

import java.util.HashMap;
import java.util.Iterator;

import com.jrefinery.report.filter.templates.Template;

/**
 * A template collection.
 * 
 * @author Thomas Morgner
 */
public class TemplateCollection
{
  /** Storage for the templates. */
  private HashMap templates;

  /**
   * Creates a new collection.
   */
  public TemplateCollection()
  {
    templates = new HashMap();
  }

  /**
   * Adds a template.
   * 
   * @param template  the template.
   */
  public void addTemplate (TemplateDescription template)
  {
    templates.put(template.getName(), template);
  }

  /**
   * Returns a template.
   * 
   * @param name  the template name.
   * 
   * @return The template description.
   */
  public TemplateDescription getTemplate (String name)
  {
    return (TemplateDescription) templates.get (name);
  }

  /**
   * Returns a template description.
   * 
   * @param template  the template.
   * 
   * @return The description.
   */
  public TemplateDescription getDescription (Template template)
  {
    Iterator enum = templates.keySet().iterator();
    while (enum.hasNext())
    {
      TemplateDescription td = (TemplateDescription) enum.next();
      if (td.getObjectClass().equals(template.getClass()))
      {
        return td;
      }
    }
    return null;
  }
}
