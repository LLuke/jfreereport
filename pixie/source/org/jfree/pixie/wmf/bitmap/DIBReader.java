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

  public BufferedImage setRecord (final MfRecord record)
    throws IOException
  {
    return setRecord(record, 0);
  }

  public BufferedImage setRecord (final MfRecord record, final int offset)
    throws IOException
  {
    header = new BitmapHeader ();
    header.setRecord (record, offset);
    palette = new GDIPalette ();
    palette.setNoOfColors (header.getNoOfColors());
    
    final int width = header.getWidth ();
    final int height = header.getHeight ();
    
    final int paletteStart = MfRecord.RECORD_HEADER_SIZE + header.getHeaderSize() + 4 + offset;
    final InputStream dataIn = record.getInputStream(paletteStart);
    palette.readPalette(dataIn);
    
    final int compression = header.getCompression ();
    final BitmapCompression comHandler = BitmapCompressionFactory.getHandler(compression);
    comHandler.setDimension(width, height);
    comHandler.setBpp (header.getBitsPerPixel());
    final int[] data = comHandler.decompress (dataIn, palette);
    
    final BufferedImage retval = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
    retval.setRGB (0,0, width, height, data, 0, width);
    return retval;
  }
}
