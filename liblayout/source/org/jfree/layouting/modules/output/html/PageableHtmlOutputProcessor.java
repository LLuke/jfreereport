/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

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
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.NameGenerator;
import org.jfree.repository.dummy.DummyRepository;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 12.11.2006, 14:11:28
 *
 * @author Thomas Morgner
 */
public class PageableHtmlOutputProcessor extends AbstractPageableProcessor
  implements HtmlOutputProcessor
{
  private HtmlOutputProcessorMetaData metaData;
  private PageFlowSelector flowSelector;
  private HtmlPrinter printer;

  public PageableHtmlOutputProcessor(final Configuration configuration)
  {
    super(configuration);
    this.flowSelector = new AllPageFlowSelector(true);

    final FontRegistry fontRegistry = new AWTFontRegistry();
    final FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData
        (fontStorage, HtmlOutputProcessorMetaData.PAGINATION_FULL);


    ContentLocation contentLocation = new DummyRepository().getRoot();
    NameGenerator contentNameGenerator = new DefaultNameGenerator(contentLocation);
    ContentLocation dataLocation = new DummyRepository().getRoot();
    NameGenerator dataNameGenerator = new DefaultNameGenerator(dataLocation);

    this.printer = new HtmlPrinter();
    this.printer.setContentWriter(contentLocation, contentNameGenerator);
    this.printer.setDataWriter(dataLocation, dataNameGenerator);
  }

  public HtmlPrinter getPrinter()
  {
    return printer;
  }

  public void setPrinter(final HtmlPrinter printer)
  {
    this.printer = printer;
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
      printer.generate(logicalPage, getDocumentContext());
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
