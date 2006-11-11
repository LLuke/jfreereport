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
 * GraphicsOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: GraphicsOutputProcessor.java,v 1.5 2006/10/27 18:25:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.pageable.graphics;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PhysicalPageBox;

/**
 * Creation-Date: 02.01.2006, 19:55:14
 *
 * @author Thomas Morgner
 */
public class GraphicsOutputProcessor extends AbstractPageableProcessor
{
  private int pageCursor;
  private OutputProcessorMetaData metaData;
  private GraphicsContentInterceptor interceptor;

  public GraphicsOutputProcessor()
  {
    DefaultFontStorage fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new GraphicsOutputProcessorMetaData(fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public void processContent(LogicalPageBox logicalPage)
  {
    final PageGrid pageGrid = logicalPage.getPageGrid();
    final int rowCount = pageGrid.getRowCount();
    final int colCount = pageGrid.getColumnCount();

    if (getProcessingState() == PROCESSING_PAGES)
    {
      final LogicalPageKey key = createLogicalPage(colCount, rowCount);
      if (key.getPosition() != pageCursor)
      {
        throw new IllegalStateException("Expected position " + pageCursor + " is not the key's position " + key.getPosition());
      }
      pageCursor += 1;
      return;
    }

    if (isContentGeneratable())
    {
      if (interceptor == null)
      {
        return;
      }

      LogicalPageKey logicalPageKey = getLogicalPage(pageCursor);
      if (interceptor.isLogicalPageAccepted(logicalPageKey))
      {
        final PageDrawableImpl page = new PageDrawableImpl(logicalPage,
            logicalPage.getPageWidth(), logicalPage.getPageHeight());
        interceptor.processLogicalPage(logicalPageKey, page);
      }

      for (int row = 0; row < rowCount; row++)
      {
        for (int col = 0; col < colCount; col++)
        {
          PhysicalPageKey pageKey = logicalPageKey.getPage(col, row);
          if (interceptor.isPhysicalPageAccepted(pageKey))
          {
            final PhysicalPageBox page = pageGrid.getPage(row, col);
            final PageDrawableImpl drawable = new PageDrawableImpl(page,
                page.getWidth(), page.getHeight());
            interceptor.processPhysicalPage(pageKey, drawable);
          }
        }
      }

      pageCursor += 1;
    }
  }

  public GraphicsContentInterceptor getInterceptor()
  {
    return interceptor;
  }

  public void setInterceptor(final GraphicsContentInterceptor interceptor)
  {
    this.interceptor = interceptor;
  }

  /**
   * Notifies the output processor, that the processing has been finished and
   * that the input-feed received the last event.
   */
  public void processingFinished()
  {
    super.processingFinished();
    pageCursor = 0;
  }

  public int getPageCursor()
  {
    return pageCursor;
  }

  public void setPageCursor(final int pageCursor)
  {
    this.pageCursor = pageCursor;
  }
}
