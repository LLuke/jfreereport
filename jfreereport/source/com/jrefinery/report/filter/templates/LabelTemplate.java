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
 * ------------------
 * LabelTemplate.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LabelTemplate.java,v 1.6 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.filter.templates;

import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;

/**
 * A label template can be used to describe static text content.
 *
 * @author Thomas Morgner
 */
public class LabelTemplate extends AbstractTemplate
{
  /** A static data source. */
  private StaticDataSource staticDataSource;

  /** A string filter. */
  private StringFilter stringFilter;

  /**
   * Creates a new label template.
   */
  public LabelTemplate()
  {
    staticDataSource = new StaticDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(staticDataSource);
  }

  /**
   * Sets the text for the label.
   *
   * @param content  the text.
   */
  public void setContent(final String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the text for the label.
   *
   * @return The text.
   */
  public String getContent()
  {
    return (String) (staticDataSource.getValue());
  }

  /**
   * Returns the string that represents <code>null</code>.
   *
   * @return The string that represents <code>null</code>.
   */
  public String getNullValue()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents <code>null</code>.
   *
   * @param nullValue  the string.
   */
  public void setNullValue(final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return stringFilter.getValue();
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
    final LabelTemplate template = (LabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.staticDataSource = (StaticDataSource) template.stringFilter.getDataSource();
    return template;
  }

}
