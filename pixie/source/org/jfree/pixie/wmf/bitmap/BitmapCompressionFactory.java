package org.jfree.pixie.wmf.bitmap;

import org.jfree.pixie.wmf.bitmap.BitFieldsCompression;
import org.jfree.pixie.wmf.bitmap.BitmapCompression;


public class BitmapCompressionFactory
{
  public static BitmapCompression getHandler (int comp)
  {
    switch (comp)
    {
      case BitmapHeader.BI_RGB:
        return new RGBCompression ();
      case BitmapHeader.BI_RLE4:
        return new RLE4Compression ();
      case BitmapHeader.BI_RLE8:
        return new RLE8Compression ();
      case BitmapHeader.BI_BITFIELDS:
        return new BitFieldsCompression ();
      default:
        throw new IllegalArgumentException ("Unknown compression: " + comp);
    }
  }
}