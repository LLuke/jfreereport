package org.jfree.pixie.wmf.bitmap;

import org.jfree.pixie.wmf.MfRecord;
import org.jfree.pixie.wmf.bitmap.BitmapCompression;
import org.jfree.pixie.wmf.bitmap.BitmapCompressionFactory;
import org.jfree.pixie.wmf.bitmap.BitmapHeader;

import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;

public class DIBReader
{
  private GDIPalette palette; // as GDI Color value
  private BitmapHeader header;
  
  public DIBReader ()
  {
  }

  public BufferedImage setRecord (org.jfree.pixie.wmf.MfRecord record)
    throws IOException
  {
    header = new BitmapHeader ();
    header.setRecord (record);
    palette = new GDIPalette ();
    palette.setNoOfColors (header.getNoOfColors());
    
    int width = header.getWidth ();
    int height = header.getHeight ();
    
    int paletteStart = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + header.getHeaderSize() + 4;
    InputStream dataIn = record.getInputStream(paletteStart);
    palette.readPalette(dataIn);
    
    int compression = header.getCompression ();
    BitmapCompression comHandler = BitmapCompressionFactory.getHandler(compression);
    comHandler.setDimension(width, height);
    comHandler.setBpp (header.getBitsPerPixel());
    int[] data = comHandler.decompress (dataIn, palette);
    
    BufferedImage retval = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
    retval.setRGB (0,0, width, height, data, 0, width);
    return retval;
  }
}
