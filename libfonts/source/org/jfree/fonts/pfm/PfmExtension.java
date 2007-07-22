package org.jfree.fonts.pfm;

import java.io.IOException;

import org.jfree.fonts.LEByteAccessUtilities;

/**
 * This table contains offsets to other tables. It directly follows the header.
 *
 * @author Thomas Morgner
 */
public class PfmExtension
{
  private int extMetricsOffset;
  private int extentTable;
  private int originTable;
  private int pairKernTable;
  private int trackKernTable;
  private int driverInfo;
  public static final int LENGTH = 30;

  public PfmExtension(final byte[] data) throws IOException
  {
    if (LEByteAccessUtilities.readShort(data, 0) != 30)
    {
      throw new IOException ("Extended-Header must be 30 bytes long.");
    }
    extMetricsOffset = LEByteAccessUtilities.readLong(data, 2);
    extentTable = LEByteAccessUtilities.readLong(data, 6);
    originTable = LEByteAccessUtilities.readLong(data, 10);
    pairKernTable = LEByteAccessUtilities.readLong(data, 14);
    trackKernTable = LEByteAccessUtilities.readLong(data, 18);

    // driver info is a pointer to the full-qualified font name. Unlike Adobes description, this is *not* part
    // of the postscript information section (which is no three-entry-section anyway.)
    driverInfo = LEByteAccessUtilities.readLong(data, 22);
    // reserved 4 bytes follow
  }

  public int getExtMetricsOffset()
  {
    return extMetricsOffset;
  }

  public int getExtentTable()
  {
    return extentTable;
  }

  public int getOriginTable()
  {
    return originTable;
  }

  public int getPairKernTable()
  {
    return pairKernTable;
  }

  public int getTrackKernTable()
  {
    return trackKernTable;
  }

  public int getDriverInfo()
  {
    return driverInfo;
  }
}
