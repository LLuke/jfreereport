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
 * --------------------------------
 * AbstractTemplateDescription.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractTemplateDescription.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext.factory.templates;

import org.jfree.report.filter.templates.Template;
import org.jfree.xml.factory.objects.BeanObjectDescription;

/**
 * An abstract class for implementing the {@link TemplateDescription} interface.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractTemplateDescription
    extends BeanObjectDescription implements TemplateDescription
{
  /** The name. */
  private String name;

  /**
   * Creates a new description.
   *
   * @param name  the name.
   * @param template  the template class.
   * @param init  initialise?
   */
  public AbstractTemplateDescription(final String name, final Class template, final boolean init)
  {
    super(template, init);
    this.name = name;
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name  the name (<code>null</code> not allowed).
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  /**
   * Creates a template.
   *
   * @return The template.
   */
  public Template createTemplate()
  {
    return (Template) createObject();
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof AbstractTemplateDescription)) return false;
    if (!super.equals(o)) return false;

    final AbstractTemplateDescription abstractTemplateDescription = (AbstractTemplateDescription) o;

    if (name != null ? !name.equals(abstractTemplateDescription.name) : abstractTemplateDescription.name != null) return false;

    return true;
  }

  public int hashCode()
  {
    int result = super.hashCode();
    result = 29 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
