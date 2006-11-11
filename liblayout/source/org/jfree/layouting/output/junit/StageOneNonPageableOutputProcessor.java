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
 * StageOneNonPageableOutputProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StageOneNonPageableOutputProcessor.java,v 1.4 2006/10/31 11:14:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.junit;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.StreamingRenderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.normalizer.content.ContentNormalizer;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.normalizer.displaymodel.DisplayModelBuilder;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.generator.DefaultContentGenerator;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.graphics.PageDrawableImpl;
import org.jfree.layouting.output.streaming.html.HtmlOutputProcessorMetaData;
import org.jfree.ui.DrawablePanel;

/**
 * Creation-Date: 31.05.2006, 16:28:33
 *
 * @author Thomas Morgner
 * @deprecated 
 */
public class StageOneNonPageableOutputProcessor implements OutputProcessor
{
  private FontRegistry fontRegistry;
  private FontStorage fontStorage;
  private OutputProcessorMetaData metaData;

  public StageOneNonPageableOutputProcessor()
  {
    this.fontRegistry = new AWTFontRegistry();
    this.fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData(fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public FontStorage getFontStorage()
  {
    return fontStorage;
  }

  public InputFeed createInputFeed(LayoutProcess layoutProcess)
  {
    return new DefaultInputFeed(layoutProcess);
  }

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * DOM building.
   *
   * @return
   */
  public Normalizer createNormalizer(LayoutProcess layoutProcess)
  {
    return new ContentNormalizer(layoutProcess);
  }

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that it
   * is better suited for rendering.
   *
   * @return
   */
  public ModelBuilder createModelBuilder(LayoutProcess layoutProcess)
  {
    return new DisplayModelBuilder(new DefaultContentGenerator(layoutProcess), layoutProcess);
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return new StreamingRenderer(layoutProcess);
  }

  public void processContent(LogicalPageBox logicalPage)
  {
//    final PageDrawableImpl drawable = new PageDrawableImpl(logicalPage);
//    drawable.print();
//
//    final DrawablePanel comp = new DrawablePanel();
//    comp.setDrawable(drawable);
//
//    JPanel contentPane = new JPanel();
//    contentPane.setLayout(new BorderLayout());
//    contentPane.add(comp, BorderLayout.CENTER);
//
//    JDialog dialog = new JDialog();
//    dialog.setModal(true);
//    dialog.setContentPane(contentPane);
//    dialog.setSize(800, 600);
//    dialog.setVisible(true);
  }

  /**
   * Declares, whether the logical page given in process-content must have a
   * valid physical page set. Non-pageable targets may want to access the
   * logical pagebox directly.
   *
   * @return
   */
  public boolean isPhysicalPageOutput()
  {
    return false;
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
