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
 * $Id: PdfOutputProcessor.java,v 1.3 2006/11/26 19:43:13 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.pdf;

import java.io.OutputStream;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.AbstractPageableProcessor;
import org.jfree.layouting.output.pageable.AllPageFlowSelector;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PageFlowSelector;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.util.Configuration;

/**
 * A streaming target, which produces a PDF document.
 *
 * @author Thomas Morgner
 */
public class PdfOutputProcessor extends AbstractPageableProcessor
{
  private PdfOutputProcessorMetaData metaData;
  private PageFlowSelector flowSelector;
  private OutputStream outputStream;
  private PdfDocumentWriter writer;

  public PdfOutputProcessor(final Configuration configuration,
                            final OutputStream outputStream)
  {
    super(configuration);
    if (outputStream == null)
    {
      throw new NullPointerException();
    }

    this.outputStream = outputStream;
    this.flowSelector = new AllPageFlowSelector();

    // for the sake of simplicity, we use the AWT font registry for now.
    // This is less accurate than using the iText fonts, but completing
    // the TrueType registry or implementing an iText registry is too expensive
    // for now.
    final DefaultFontStorage fontStorage =
        new DefaultFontStorage(new AWTFontRegistry());
    metaData = new PdfOutputProcessorMetaData(fontStorage);

  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public PageFlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public void setFlowSelector(final PageFlowSelector flowSelector)
  {
    if (flowSelector == null)
    {
      throw new NullPointerException();
    }

    this.flowSelector = flowSelector;
  }

  public void processDocumentMetaData(DocumentContext documentContext)
  {
    super.processDocumentMetaData(documentContext);
    // we grab a few of them later ... like author, title etc
  }

  protected void processingContentFinished()
  {
    if (writer != null)
    {
      writer.close();
    }
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {
    try
    {
      if (writer == null)
      {
        writer = new PdfDocumentWriter(getConfiguration(), outputStream);
        writer.open();
      }
      writer.processPhysicalPage(pageGrid,  logicalPage, row, col, pageKey);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  protected void processLogicalPage(LogicalPageKey key,
                                    LogicalPageBox logicalPage)
  {
    try
    {
      if (writer == null)
      {
        writer = new PdfDocumentWriter(getConfiguration(), outputStream);
        writer.open();
      }
      writer.processLogicalPage(key, logicalPage);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

}
