/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * RenderPageContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderPageContext.java,v 1.1 2006/07/29 18:58:07 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.page;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.DefaultPageGrid;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * This is a running page context, which contains a list of watched strings
 * and counters. Whenever one of these counters or strings change, a new
 * page context is generated.
 *
 * This page context also contains the special 'page' counter. That counter
 * is maintained by the system, initialized with '1' and is increased by
 * the number of physical pages at every physical split.
 *
 * @author Thomas Morgner
 */
public class RenderPageContext
{
  private PageContext pageContext;
  private boolean firstPage;
  private int pageCounter;
  private boolean leftPage;

  public RenderPageContext(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }
    this.pageContext = pageContext;
  }

  public PageContext getPageContext()
  {
    return pageContext;
  }

  public RenderPageContext update (final PageContext pageContext)
  {
    // this should preserve all recorded trails ..
    return new RenderPageContext(pageContext);
  }

  /**
   * This method should check the layout context for updated counters and strings.
   *
   * @param layoutContext
   * @return
   */
  public RenderPageContext update(final LayoutContext layoutContext)
  {
    return this;
  }

  public PageGrid createPageGrid(final OutputProcessorMetaData outputMetaData)
  {
    return new DefaultPageGrid(pageContext, outputMetaData);
  }
}
