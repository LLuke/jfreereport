package org.jfree.report.modules.output.support.itext;

import java.io.IOException;
import java.net.URL;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import org.jfree.report.ImageContainer;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.util.KeyedQueue;
import org.jfree.report.util.Log;
import org.jfree.report.util.WaitingImageObserver;

public class ITextImageCache
{
  private KeyedQueue cachedImages;

  public ITextImageCache ()
  {
    this.cachedImages = new KeyedQueue(0);
  }

  /**
   * Helperfunction to extract an image from an imagereference. If the image is contained
   * as java.awt.Image object only, the image is recoded into an PNG-Image.
   *
   * @param imageRef the image reference.
   * @return an image.
   *
   * @throws com.lowagie.text.DocumentException if no PDFImageElement could be created using the given
   *                           ImageReference.
   * @throws java.io.IOException       if the image could not be read.
   */
  public Image getImage (final ImageContainer imageRef)
          throws DocumentException, IOException
  {
    if (imageRef instanceof URLImageContainer)
    {
      final URLImageContainer urlImageContainer = (URLImageContainer) imageRef;
      if (urlImageContainer.isLoadable())
      {
        try
        {
          final URL sourceURL = urlImageContainer.getSourceURL();
          if (sourceURL != null)
          {
            Image image = (Image) cachedImages.get(sourceURL);
            if (image == null)
            {
              image = Image.getInstance(sourceURL);
              cachedImages.put(sourceURL, image);
            }
            return image;
          }
        }
        catch (BadElementException be)
        {
          Log.info("Caught illegal Image, will recode to PNG instead", be);
        }
        catch (IOException ioe)
        {
          Log.info("Unable to read the raw-data, will try to recode image-data.", ioe);
        }
      }
    }

    if (imageRef instanceof LocalImageContainer)
    {
      final LocalImageContainer localImageContainer =
              (LocalImageContainer) imageRef;

      if (localImageContainer.getImage() != null)
      {
        // check, if the content was cached ...
        final Object identity = localImageContainer.getIdentity();
        Image image;
        if (identity != null)
        {
          image = (Image) cachedImages.get(identity);
          if (image == null)
          {
            image = Image.getInstance(localImageContainer.getImage(), null, false);
            cachedImages.put(identity, image);
          }
        }
        else
        {
          // iText is able to handle image conversion by itself ...
          image = Image.getInstance(localImageContainer.getImage(), null, false);
        }

        // since version 0.99 iText supports Alpha-PNGs
        final WaitingImageObserver obs = new WaitingImageObserver(localImageContainer.getImage());
        obs.waitImageLoaded();
        return image;
      }
    }

    throw new DocumentException("Neither an URL nor an Image was given to paint the graphics");
  }

}
