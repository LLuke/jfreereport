package org.jfree.layouting.layouter.loader;

import java.io.IOException;
import java.awt.Image;
import java.net.URL;

import org.jfree.loader.resourceloader.AbstractResourceFactory;
import org.jfree.loader.resourceloader.ResourceFactoryModule;
import org.jfree.loader.resourceloader.ImageFactoryModule;
import org.jfree.loader.resourceloader.DrawableFactoryModule;
import org.jfree.ui.Drawable;

public class GenericResourceFactory extends AbstractResourceFactory
{
  public GenericResourceFactory ()
  {
  }

  protected Object loadResource (final ResourceFactoryModule module,
                                 final byte[] data,
                                 final URL source,
                                 final String fileName,
                                 final String mimeType)
          throws IOException
  {
    if (module instanceof ImageFactoryModule)
    {
      ImageFactoryModule imageFactoryModule = (ImageFactoryModule) module;
      return imageFactoryModule.createImage(data, fileName, mimeType);
    }
    if (module instanceof DrawableFactoryModule)
    {
      DrawableFactoryModule drawableFactoryModule = (DrawableFactoryModule) module;
      return drawableFactoryModule.createDrawable(data, fileName, mimeType);
    }
    return null;
  }

  protected Object loadContent (URL source)
          throws IOException
  {
    Object o = super.loadContent(source);
    if (o instanceof Drawable)
    {
      return new ResourceDrawableData((Drawable) o, source);
    }
    else if (o instanceof Image)
    {
      return new ResourceLayoutImageData((Image) o, source);
    }
    else if (o != null)
    {
      // return a generic wrapper ..
      return new GenericExternalResource(o, source);
    }
    return null;
  }
}
