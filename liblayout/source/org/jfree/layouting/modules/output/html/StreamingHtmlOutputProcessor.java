/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: StreamingHtmlOutputProcessor.java,v 1.9 2007/04/02 11:41:15 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.output.AbstractOutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.renderer.PrototypeBuildingRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.StreamingRenderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.NameGenerator;
import org.jfree.repository.dummy.DummyRepository;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 12.11.2006, 14:12:38
 *
 * @author Thomas Morgner
 */
public class StreamingHtmlOutputProcessor extends AbstractOutputProcessor
  implements HtmlOutputProcessor
{
  private HtmlOutputProcessorMetaData metaData;
  private PrototypeBuildingRenderer prototypeBuilder;
  private HtmlPrinter printer;

  public StreamingHtmlOutputProcessor(final Configuration configuration)
  {
    super(configuration);

    FontRegistry fontRegistry = new AWTFontRegistry();
    FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData
        (fontStorage, HtmlOutputProcessorMetaData.PAGINATION_NONE);

    ContentLocation contentLocation = new DummyRepository().getRoot();
    NameGenerator contentNameGenerator = new DefaultNameGenerator(contentLocation);
    ContentLocation dataLocation = new DummyRepository().getRoot();
    NameGenerator dataNameGenerator = new DefaultNameGenerator(dataLocation);

    this.printer = new HtmlPrinter();
    this.printer.setContentWriter(contentLocation, contentNameGenerator);
    this.printer.setDataWriter(dataLocation, dataNameGenerator);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    if (isGlobalStateComputed() == false)
    {
      prototypeBuilder = new PrototypeBuildingRenderer(layoutProcess);
      return prototypeBuilder;
    }

    return new StreamingRenderer(layoutProcess);
  }

  protected void processPageContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPage)
  {
    try
    {
      printer.generate(logicalPage, getDocumentContext());
    }
    catch (Exception e)
    {
      Log.error ("Failed to generate content.", e);
    }
  }

  public HtmlPrinter getPrinter()
  {
    return printer;
  }

  public void setPrinter(final HtmlPrinter printer)
  {
    this.printer = printer;
  }
}
