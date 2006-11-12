/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.layouting.output.pageable.AbstractPageableProcessor;
import org.jfree.layouting.output.pageable.PageFlowSelector;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PhysicalPageBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.util.Configuration;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.awt.AWTFontRegistry;

/**
 * Creation-Date: 02.01.2006, 19:55:14
 *
 * @author Thomas Morgner
 */
public class FlowGraphicsOutputProcessor extends AbstractPageableProcessor
{
  private int pageCursor;
  private OutputProcessorMetaData metaData;
  private GraphicsContentInterceptor interceptor;

  public FlowGraphicsOutputProcessor(Configuration configuration)
  {
    super(configuration);
    DefaultFontStorage fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new GraphicsOutputProcessorMetaData(fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public GraphicsContentInterceptor getInterceptor()
  {
    return interceptor;
  }

  public void setInterceptor(final GraphicsContentInterceptor interceptor)
  {
    this.interceptor = interceptor;
  }

  protected PageFlowSelector getFlowSelector()
  {
    return getInterceptor();
  }


  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {
    final PhysicalPageBox page = pageGrid.getPage(row, col);
    final PageDrawableImpl drawable = new PageDrawableImpl(page,
        page.getWidth(), page.getHeight());
    interceptor.processPhysicalPage(pageKey, drawable);
  }

  protected void processLogicalPage (LogicalPageKey key, LogicalPageBox logicalPage)
  {
    final PageDrawableImpl page = new PageDrawableImpl(logicalPage,
        logicalPage.getPageWidth(), logicalPage.getPageHeight());
    interceptor.processLogicalPage(key, page);
  }
}
