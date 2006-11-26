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
 * $Id: PageableHtmlOutputProcessor.java,v 1.3 2006/11/17 20:14:56 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.html;

import java.io.OutputStream;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.AbstractPageableProcessor;
import org.jfree.layouting.output.pageable.AllPageFlowSelector;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PageFlowSelector;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.NameGenerator;
import org.jfree.repository.dummy.DummyRepository;
import org.jfree.repository.stream.StreamRepository;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 12.11.2006, 14:11:28
 *
 * @author Thomas Morgner
 */
public class PageableHtmlOutputProcessor extends AbstractPageableProcessor
{
  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private ContentLocation dataLocation;
  private NameGenerator dataNameGenerator;
  private HtmlOutputProcessorMetaData metaData;
  private PageFlowSelector flowSelector;

  public PageableHtmlOutputProcessor(final Configuration configuration,
                                     final OutputStream out)
  {
    super(configuration);
    this.contentLocation = new StreamRepository(null, out).getRoot();
    this.contentNameGenerator = new DefaultNameGenerator(this.contentLocation);
    this.dataLocation = new DummyRepository().getRoot();
    this.dataNameGenerator = new DefaultNameGenerator(this.dataLocation);

    initialize();
  }

  public PageableHtmlOutputProcessor(final Configuration configuration,
                                     final ContentLocation contentLocation,
                                     final NameGenerator contentNameGenerator)
  {
    super(configuration);
    this.contentLocation = contentLocation;
    this.contentNameGenerator = contentNameGenerator;
    this.dataLocation = new DummyRepository().getRoot();
    this.dataNameGenerator = new DefaultNameGenerator(this.dataLocation);

    initialize();
  }

  public PageableHtmlOutputProcessor(final Configuration configuration,
                                     final ContentLocation contentLocation,
                                     final NameGenerator contentNameGenerator,
                                     final ContentLocation dataLocation,
                                     final NameGenerator dataNameGenerator)
  {
    super(configuration);
    this.contentLocation = contentLocation;
    this.contentNameGenerator = contentNameGenerator;
    this.dataLocation = dataLocation;
    this.dataNameGenerator = dataNameGenerator;

    initialize();
  }

  private void initialize()
  {
    this.flowSelector = new AllPageFlowSelector(true);

    FontRegistry fontRegistry = new AWTFontRegistry();
    FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData
        (fontStorage, HtmlOutputProcessorMetaData.PAGINATION_FULL);
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {

  }

  protected void processLogicalPage(final LogicalPageKey key,
                                    final LogicalPageBox logicalPage)
  {
    try
    {
//      final ContentItem item = contentLocation.createItem
//          (contentNameGenerator.generateName(null, "text/html"));
//      final OutputStream outputStream = item.getOutputStream();
      HtmlPrinter printer = new HtmlPrinter();
      printer.generate(logicalPage, System.out, "UTF-8", getDocumentContext());
//      outputStream.close();
    }
    catch (Exception e)
    {
      Log.error ("Failed to generate content.", e);
    }
  }

  public PageFlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public void setFlowSelector(final PageFlowSelector flowSelector)
  {
    this.flowSelector = flowSelector;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

}
