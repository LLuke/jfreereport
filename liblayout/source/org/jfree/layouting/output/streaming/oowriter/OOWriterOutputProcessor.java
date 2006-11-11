package org.jfree.layouting.output.streaming.oowriter;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.generator.ContentGenerator;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.streaming.StreamingOutputProcessor;

public class OOWriterOutputProcessor implements StreamingOutputProcessor
{
  private OOWriterOutputProcessorMetaData metaData;
  private FontRegistry fontRegistry;
  private FontStorage fontStorage;

  public OOWriterOutputProcessor ()
  {
    this.fontRegistry = new AWTFontRegistry();
    this.fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new OOWriterOutputProcessorMetaData(fontStorage);
  }

  public Normalizer createNormalizer
          (LayoutProcess defaultStreamingLayoutProcess)
  {
    // todo implement me
    return null;
  }

  public InputFeed createInputFeed(LayoutProcess layoutProcess)
  {
    return null;
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
    return null;
  }

  public FontStorage getFontStorage ()
  {
    return fontStorage;
  }

  public OutputProcessorMetaData getMetaData ()
  {
    return metaData;
  }

  /**
   * Creates a new content generator. The content generator is responsible for
   * creating the visual content from the display model.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created content generator.
   */
  public ContentGenerator createContentGenerator(LayoutProcess layoutProcess)
  {
    return null;
  }

  public Renderer createRenderer(LayoutProcess layoutProcess)
  {
    return null;
  }

  public void processContent(LogicalPageBox logicalPage)
  {

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
