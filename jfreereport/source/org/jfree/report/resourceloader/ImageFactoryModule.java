package org.jfree.report.resourceloader;

import java.awt.Image;
import java.io.IOException;

public interface ImageFactoryModule extends ResourceFactoryModule
{
  public Image createImage (byte[] imageData, String fileName, String mimeType)
          throws IOException;
}
