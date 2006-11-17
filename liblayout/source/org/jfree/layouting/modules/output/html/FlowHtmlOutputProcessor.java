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
 * $Id: FlowHtmlOutputProcessor.java,v 1.1 2006/11/12 14:22:10 taqua Exp $
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
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.StreamingRenderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.NameGenerator;
import org.jfree.repository.stream.StreamRepository;
import org.jfree.repository.dummy.DummyRepository;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 12.11.2006, 14:12:38
 *
 * @author Thomas Morgner
 */
public class FlowHtmlOutputProcessor extends AbstractOutputProcessor
{
  private HtmlOutputProcessorMetaData metaData;
  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private ContentLocation dataLocation;
  private NameGenerator dataNameGenerator;

  public FlowHtmlOutputProcessor(final Configuration configuration,
                                 final OutputStream out)
  {
    super(configuration);
    this.contentLocation = new StreamRepository(null, out).getRoot();
    this.contentNameGenerator = new DefaultNameGenerator(this.contentLocation);
    this.dataLocation = new DummyRepository().getRoot();
    this.dataNameGenerator = new DefaultNameGenerator(this.dataLocation);

    initialize();
  }

  public FlowHtmlOutputProcessor(final Configuration configuration,
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

  public FlowHtmlOutputProcessor(final Configuration configuration,
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
    this.metaData = new HtmlOutputProcessorMetaData(fontStorage, true);
  }


  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return new StreamingRenderer(layoutProcess);
  }

  public void processContent(LogicalPageBox logicalPage)
  {

  }

  /**
   * Notifies the output processor, that the processing has been finished and
   * that the input-feed received the last event.
   */
  public void processingFinished()
  {

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
    return false;
  }

  /**
   * This flag indicates, whether the output processor has collected enough
   * information to start the content generation.
   *
   * @return
   */
  public boolean isContentGeneratable()
  {
    return false;
  }
}