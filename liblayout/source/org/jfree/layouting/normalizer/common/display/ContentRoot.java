package org.jfree.layouting.normalizer.common.display;

import org.jfree.layouting.normalizer.ContentGenerator;
import org.jfree.layouting.normalizer.common.display.BlockCompositionStrategy;
import org.jfree.layouting.normalizer.common.display.ContentBox;

public class ContentRoot extends ContentBox
{
  private ContentGenerator generator;

  public ContentRoot ()
  {
    super(null, new BlockCompositionStrategy());
  }

  public ContentGenerator getGenerator ()
  {
    return generator;
  }

  public void setGenerator (ContentGenerator generator)
  {
    this.generator = generator;
  }

  public ContentRoot getRoot ()
  {
    return this;
  }
}
