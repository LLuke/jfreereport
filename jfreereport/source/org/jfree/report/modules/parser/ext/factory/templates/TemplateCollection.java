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
 * -----------------------
 * TemplateCollection.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplateCollection.java,v 1.4 2003/07/23 16:02:22 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.templates;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.filter.templates.Template;
import org.jfree.util.Configuration;

/**
 * A template collection.
 *
 * @author Thomas Morgner
 */
public class TemplateCollection implements Serializable
{
  /** Storage for the templates. */
  private HashMap templates;

  /** The parser/report configuration. */
  private Configuration config;

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
  public void addTemplate(final TemplateDescription template)
  {
    templates.put(template.getName(), template);
    if (getConfig() != null)
    {
      template.configure(getConfig());
    }
  }

  /**
   * Returns a template.
   *
   * @param name  the template name.
   *
   * @return The template description.
   */
  public TemplateDescription getTemplate(final String name)
  {
    final TemplateDescription td = (TemplateDescription) templates.get(name);
    if (td != null)
    {
      return (TemplateDescription) td.getInstance();
    }
    return null;
  }

  /**
   * Returns a template description.
   *
   * @param template  the template.
   *
   * @return The description.
   */
  public TemplateDescription getDescription(final Template template)
  {
    final Iterator enum = templates.values().iterator();
    while (enum.hasNext())
    {
      final TemplateDescription td = (TemplateDescription) enum.next();
      if (td.getObjectClass().equals(template.getClass()))
      {
        return (TemplateDescription) td.getInstance();
      }
    }
    return null;
  }


  /**
   * Configures this factory. The configuration contains several keys and
   * their defined values. The given reference to the configuration object
   * will remain valid until the report parsing or writing ends.
   * <p>
   * The configuration contents may change during the reporting.
   *
   * @param config the configuration, never null
   */
  public void configure(final Configuration config)
  {
    if (config == null)
    {
      throw new NullPointerException("The given configuration is null");
    }
    if (this.config != null)
    {
      // already configured ... ignored
      return;
    }

    this.config = config;
    final Iterator it = templates.values().iterator();
    while (it.hasNext())
    {
      final TemplateDescription od = (TemplateDescription) it.next();
      od.configure(config);
    }

  }

  /**
   * Returns the currently set configuration or null, if none was set.
   *
   * @return the configuration.
   */
  public Configuration getConfig()
  {
    return config;
  }

  public boolean equals(Object o)
  {
    if (this == o)
    { 
      return true;
    }
    if (!(o instanceof TemplateCollection))
    { 
      return false;
    }

    final TemplateCollection templateCollection = (TemplateCollection) o;

    if (!templates.equals(templateCollection.templates))
    { 
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return templates.hashCode();
  }
}
