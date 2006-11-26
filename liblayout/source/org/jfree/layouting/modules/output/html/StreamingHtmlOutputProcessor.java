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
 * $Id: StreamingHtmlOutputProcessor.java,v 1.3 2006/11/13 19:14:05 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.html;

import java.io.OutputStream;

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
import org.jfree.repository.stream.StreamRepository;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 12.11.2006, 14:12:38
 *
 * @author Thomas Morgner
 */
public class StreamingHtmlOutputProcessor extends AbstractOutputProcessor
{
  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private ContentLocation dataLocation;
  private NameGenerator dataNameGenerator;

  private HtmlOutputProcessorMetaData metaData;
  private PrototypeBuildingRenderer prototypeBuilder;

  public StreamingHtmlOutputProcessor(final Configuration configuration,
                                      final OutputStream out)
  {
    super(configuration);
    this.contentLocation = new StreamRepository(null, out).getRoot();
    this.contentNameGenerator = new DefaultNameGenerator(this.contentLocation);
    this.dataLocation = new DummyRepository().getRoot();
    this.dataNameGenerator = new DefaultNameGenerator(this.dataLocation);

    initialize();
  }

  public StreamingHtmlOutputProcessor(final Configuration configuration,
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

  public StreamingHtmlOutputProcessor(final Configuration configuration,
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
    FontRegistry fontRegistry = new AWTFontRegistry();
    FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData
        (fontStorage, HtmlOutputProcessorMetaData.PAGINATION_NONE);
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

  }
}
