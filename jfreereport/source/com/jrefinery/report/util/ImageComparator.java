/**
 * Date: Jan 26, 2003
 * Time: 10:44:22 PM
 *
 * $Id: ImageComparator.java,v 1.2 2003/01/30 00:04:54 taqua Exp $
 */
package com.jrefinery.report.util;

import com.jrefinery.report.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ImageComparator
{
  private static class ImageCompareData
  {
  }

  private static class DigestImageCompareData extends ImageCompareData
  {
    private byte[] digestMD5Data;
    private byte[] digestSHAData;

    public DigestImageCompareData(byte[] digestMD5Data, byte[] digestSHAData)
    {
      this.digestMD5Data = digestMD5Data;
      this.digestSHAData = digestSHAData;
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof DigestImageCompareData)) return false;

      final DigestImageCompareData data = (DigestImageCompareData) o;

      if (!Arrays.equals(digestMD5Data, data.digestMD5Data)) return false;
      if (!Arrays.equals(digestSHAData, data.digestSHAData)) return false;

      return true;
    }

    public int hashCode()
    {
      return 0;
    }
  }

  private MessageDigest digestMD5;
  private MessageDigest digestSHA;

  public ImageComparator()
  {
    try
    {
      digestMD5 = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException nse)
    {
      Log.info ("No MD5 algorithm available");
    }
    try
    {
      digestSHA = MessageDigest.getInstance("SHA");
    }
    catch (NoSuchAlgorithmException nse)
    {
      Log.info ("No SHA algorithm available");
    }
  }

  public ImageCompareData createCompareData (byte[] image)
  {
    byte[] dataMD5 = null;
    if (digestMD5 != null)
    {
      dataMD5 = digestMD5.digest(image);
    }
    byte[] dataSHA = null;
    if (digestSHA != null)
    {
      dataSHA = digestSHA.digest(image);
    }
    if (dataSHA != null && dataMD5 != null)
    {
      return new DigestImageCompareData(dataMD5, dataSHA);
    }
    else
    {
      return new ImageCompareData();
    }
  }
}
