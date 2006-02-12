package org.jfree.layouting.output.pageable.dummy;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.normalizer.pagable.ContentGeneratingNormalizer;
import org.jfree.layouting.normalizer.pagable.PaginatingNormalizer;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;

public class DummyOutputProcessor implements PageableOutputProcessor
{

  private FontStorage fontStorage;
  private OutputProcessorMetaData metaData;

  public DummyOutputProcessor ()
  {
    fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new DummyOutputProcessorMetaData(fontStorage);
  }

  public FontStorage getFontStorage ()
  {
    return fontStorage;
  }

  public OutputProcessorMetaData getMetaData ()
  {
    return metaData;
  }

  public PageContext createPageContext(int pageNumber)
  {
    return null;
  }

  public ContentGeneratingNormalizer createContentNormalizer()
  {
    return null;
  }

  public PaginatingNormalizer createPaginatingNormalizer()
  {
    return null;
  }
}
