package org.jfree.layouting.output.streaming.oowriter;

import java.io.UnsupportedEncodingException;

import org.jfree.layouting.output.streaming.StreamingOutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.layouting.normalizer.DisplayModelNormalizer;
import org.jfree.layouting.normalizer.Normalizer;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.StreamingLayoutProcess;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.awt.AWTFontRegistry;

public class OOWriterOutputProcessor implements StreamingOutputProcessor
{
  private OOWriterOutputProcessorMetaData metaData;
  private FontRegistry fontRegistry;
  private FontStorage fontStorage;

  public OOWriterOutputProcessor ()
  {
    this.fontRegistry = new AWTFontRegistry();
    this.fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new OOWriterOutputProcessorMetaData(fontRegistry);
  }

  public Normalizer createNormalizer
          (StreamingLayoutProcess defaultStreamingLayoutProcess)
  {
    // todo implement me
    try
    {
      return new DisplayModelNormalizer(new OOWriterContentGenerator(System.out));
    }
    catch (UnsupportedEncodingException e)
    {
      throw new IllegalStateException();
    }
  }

  public PageContext createPageContext (int pageNumber)
  {
    // todo implement me
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
}
