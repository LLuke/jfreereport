package org.jfree.report.resourceloader;

import java.awt.Image;
import java.io.InputStream;
import java.io.IOException;

public interface ImageFactoryModule extends ResourceFactoryModule
{
  public Image createImage (byte[] imageData) throws IOException;
}
