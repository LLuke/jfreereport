package org.jfree.pixie.wmf.bitmap;

import org.jfree.pixie.wmf.MfRecord;

public class BitmapHeader
{
  public static final int BI_RGB = 0;
  public static final int BI_RLE8 = 1;
  public static final int BI_RLE4 = 2;
  public static final int BI_BITFIELDS = 3;

  private int BMPH_DATA_OFFSET = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 0;
  private int BMPH_HEADERSIZE = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 4;
  private int BMPH_WIDTH = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 8;
  private int BMPH_HEIGHT = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 12;
  private int BMPH_NO_PLANES = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 14;
  private int BMPH_BPP = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 16;
  private int BMPH_COMPRESSION = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 20;
  private int BMPH_DATASIZE = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 24;
  private int BMPH_HRES = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 28;
  private int BMPH_VRES = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 32;
  private int BMPH_NO_COLORS = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 36;
  private int BMPH_NO_IMPORTANT_COLORS = org.jfree.pixie.wmf.MfRecord.RECORD_HEADER_SIZE + 40;

  private int dataOffset;
  private int headerSize;
  private int width;
  private int height;
  private int noPlanes;
  private int bitPerPixel;
  private int compression;
  private int dataSize;
  private int hres; // ignored
  private int vres; // ignored
  private int noColors; // in palette
  private int noImportantColors; // is <= noColors
  private boolean isTopDown;
  private GDIPalette palette;

  public void setRecord (org.jfree.pixie.wmf.MfRecord record)
  {
    dataOffset = record.getInt (BMPH_DATA_OFFSET);
    headerSize = record.getInt (BMPH_HEADERSIZE);
    width = record.getInt (BMPH_WIDTH);
    height = record.getInt (BMPH_HEIGHT);
    noPlanes = record.getShort (BMPH_NO_PLANES);
    bitPerPixel = record.getShort (BMPH_BPP);
    compression = record.getInt (BMPH_COMPRESSION);
    dataSize = record.getInt (BMPH_DATASIZE);
    hres = record.getInt (BMPH_HRES);
    vres = record.getInt (BMPH_VRES);
    noColors = record.getInt (BMPH_NO_COLORS);
    noImportantColors = record.getInt (BMPH_NO_IMPORTANT_COLORS);

    if (height < 0)
    {
      isTopDown = true;
      height = -height;
    }

    fixPalette ();
  }

  private void fixPalette ()
  {
    if (bitPerPixel < 16)
    {
      if (noColors == 0) noColors = (int) Math.pow (2, bitPerPixel);
      if (noImportantColors == 0) noImportantColors = (int) Math.pow (2, bitPerPixel);
    }
  }

  public int getHeaderSize ()
  {
    return headerSize;
  }

  public int getCompression ()
  {
    return compression;
  }

  public int getBitsPerPixel ()
  {
    return bitPerPixel;
  }

  public int getHRes ()
  {
    return hres;
  }

  public int getVRes ()
  {
    return vres;
  }

  public int getWidth ()
  {
    return width;
  }

  public int getHeight ()
  {
    return height;
  }

  public int getNoOfColors ()
  {
    return noColors;
  }

  public int getNoOfImportantColors ()
  {
    return noImportantColors;
  }

  public boolean isTopDown ()
  {
    return isTopDown;
  }
}