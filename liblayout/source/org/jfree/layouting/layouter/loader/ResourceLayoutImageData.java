package org.jfree.layouting.layouter.loader;

import java.awt.Image;
import java.net.URL;

import org.jfree.layouting.input.AWTLayoutImageData;
import org.jfree.layouting.input.ExternalLayoutImageData;

public class ResourceLayoutImageData
        extends AWTLayoutImageData implements ExternalLayoutImageData
{
  private URL url;

  public ResourceLayoutImageData (final Image image, final URL url)
  {
    super(image);
    this.url = url;
  }

  public URL getSource ()
  {
    return url;
  }

  public String getUri ()
  {
    return url.toExternalForm();
  }
}
