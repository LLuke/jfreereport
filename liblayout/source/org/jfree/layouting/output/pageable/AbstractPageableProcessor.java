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
 * $Id: AbstractPageableProcessor.java,v 1.1 2006/11/12 14:29:58 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.output.pageable;

import java.util.ArrayList;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.output.AbstractOutputProcessor;
import org.jfree.layouting.renderer.PaginatingRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 10.11.2006, 20:05:11
 *
 * @author Thomas Morgner
 */
public abstract class AbstractPageableProcessor extends AbstractOutputProcessor
    implements PageableOutputProcessor
{
  protected static final int PROCESSING_GLOBAL_CONTENT = 0;
  protected static final int PROCESSING_PAGES = 1;
  protected static final int PROCESSING_CONTENT = 2;
  private int processingState;
  private ArrayList physicalPages;
  private ArrayList logicalPages;
  private int pageCursor;

  protected AbstractPageableProcessor(final Configuration configuration)
  {
    super(configuration);
    this.physicalPages = new ArrayList();
    this.logicalPages = new ArrayList();
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    //return new PrintingRenderer (new PaginatingRenderer(layoutProcess));
    return new PaginatingRenderer(layoutProcess);
  }

  /**
   * Checks, whether the 'processingFinished' event had been received at least
   * once.
   *
   * @return
   */
  public boolean isPaginationFinished()
  {
    return processingState == PROCESSING_CONTENT;
  }

  /**
   * Notifies the output processor, that the processing has been finished and
   * that the input-feed received the last event.
   */
  public void processingFinished()
  {
    pageCursor = 0;
    if (processingState == PROCESSING_GLOBAL_CONTENT)
    {
      // the global content is complete. fine, lets repaginate ...
      processingState = PROCESSING_PAGES;
    }
    else if (processingState == PROCESSING_PAGES)
    {
      // the pagination is complete. So, now we can produce real content.
      processingState = PROCESSING_CONTENT;
    }
  }

  /**
   * This flag indicates, whether the global content has been computed. Global
   * content consists of global counters (except the pages counter) and derived
   * information like table of contents, the global directory of images or
   * tables etc.
   * <p/>
   * The global state must be computed before paginating can be attempted (if
   * the output target is paginating at all).
   *
   * @return true, if the global state has been computed, false otherwise.
   */
  public boolean isGlobalStateComputed()
  {
    return processingState > PROCESSING_GLOBAL_CONTENT;
  }

  public int getProcessingState()
  {
    return processingState;
  }

  /**
   * This flag indicates, whether the output processor has collected enough
   * information to start the content generation.
   *
   * @return
   */
  public boolean isContentGeneratable()
  {
    return processingState == PROCESSING_CONTENT;
  }

  public int getLogicalPageCount()
  {
    return logicalPages.size();
  }

  public int getPhysicalPageCount()
  {
    return physicalPages.size();
  }

  public LogicalPageKey getLogicalPage(int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (LogicalPageKey) logicalPages.get(page);
  }

  public PhysicalPageKey getPhysicalPage(int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (PhysicalPageKey) physicalPages.get(page);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    final LogicalPageKey key =
        new LogicalPageKey(logicalPages.size(), width, height);
    logicalPages.add(key);
    for (int h = 0; h < key.getHeight(); h++)
    {
      for (int w = 0; w < key.getWidth(); w++)
      {
        physicalPages.add(key.getPage(w, h));
      }
    }
    return key;
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
      final PageFlowSelector selector = getFlowSelector();
      if (selector != null)
      {
        LogicalPageKey logicalPageKey = getLogicalPage(pageCursor);
        if (selector.isLogicalPageAccepted(logicalPageKey))
        {
          processLogicalPage(logicalPageKey, logicalPage);
        }

        for (int row = 0; row < rowCount; row++)
        {
          for (int col = 0; col < colCount; col++)
          {
            PhysicalPageKey pageKey = logicalPageKey.getPage(col, row);
            if (selector.isPhysicalPageAccepted(pageKey))
            {
              processPhysicalPage(pageGrid, logicalPage, row, col, pageKey);
            }
          }
        }
      }

      pageCursor += 1;
    }
  }

  protected abstract void processPhysicalPage(final PageGrid pageGrid,
                                              final LogicalPageBox logicalPage,
                                              final int row,
                                              final int col,
                                              final PhysicalPageKey pageKey);

  protected abstract void processLogicalPage(final LogicalPageKey key,
                                             final LogicalPageBox logicalPage);


  public int getPageCursor()
  {
    return pageCursor;
  }

  public void setPageCursor(final int pageCursor)
  {
    this.pageCursor = pageCursor;
  }

  protected abstract PageFlowSelector getFlowSelector();
}
