package org.jfree.report.resourceloader;

import java.io.IOException;

import org.jfree.ui.Drawable;

public interface DrawableFactoryModule extends ResourceFactoryModule
{
  public Drawable createDrawable (byte[] bytes) throws IOException;
}
