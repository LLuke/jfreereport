package org.jfree.report.resourceloader;

import java.io.IOException;

import org.jfree.ui.Drawable;

public interface DrawableFactoryModule extends ResourceFactoryModule
{
  public Drawable createDrawable (final byte[] imageData,
                                  final String fileName,
                                  final String mimeType)
          throws IOException;
}
