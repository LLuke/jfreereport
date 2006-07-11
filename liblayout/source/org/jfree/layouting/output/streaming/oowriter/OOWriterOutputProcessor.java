package org.jfree.layouting.output.streaming.oowriter;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.renderer.Renderer;
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
//    try
//    {
//      return new DisplayModelBuilder(new OOWriterContentGenerator(System.out));
//    }
//    catch (UnsupportedEncodingException e)
//    {
//      throw new IllegalStateException();
//    }
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
}
