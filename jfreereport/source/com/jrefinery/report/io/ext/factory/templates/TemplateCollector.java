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
 * TemplateCollector.java
 * ----------------------
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

package com.jrefinery.report.io.ext.factory.templates;

import com.jrefinery.report.filter.templates.Template;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A template collection.
 * 
 * @author Thomas Morgner
 */
public class TemplateCollector extends TemplateCollection
{
  /** Storage for the factories. */
  private ArrayList factories;

  /**
   * Creates a new template collector.
   */
  public TemplateCollector()
  {
    factories = new ArrayList();
  }

  /**
   * Adds a template collection.
   * 
   * @param tc  the template collection.
   */
  public void addTemplateCollection (TemplateCollection tc)
  {
    factories.add(tc);
  }

  /**
   * Returns an iterator that provides access to the factories.
   * 
   * @return The iterator.
   */
  public Iterator getFactories ()
  {
    return factories.iterator();
  }
  
  /**
   * Returns a template description.
   * 
   * @param name  the name.
   * 
   * @return The template description.
   */
  public TemplateDescription getTemplate(String name)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      TemplateCollection fact = (TemplateCollection) factories.get(i);
      TemplateDescription o = fact.getTemplate(name);
      if (o != null) 
      {
        return o;
      }
    }
    return super.getTemplate(name);
  }

  /**
   * Returns a template description.
   * 
   * @param template  the template.
   * 
   * @return The description.
   */
  public TemplateDescription getDescription(Template template)
  {
    for (int i = 0; i < factories.size(); i++)
    {
      TemplateCollection fact = (TemplateCollection) factories.get(i);
      TemplateDescription o = fact.getDescription(template);
      if (o != null) 
      {
        return o;
      }
    }
    return super.getDescription(template);
  }
}
