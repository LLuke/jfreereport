/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ---------------
 * ImageComparator.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageComparator.java,v 1.3 2003/02/05 17:56:02 taqua Exp $
 *
 * Changes
 * -------
 * 26-Jan-2003 : Initial version
 * 05-Feb-2003 : Documentation
 */
package com.jrefinery.report.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * The ImageComparator tries to compare a byte[] for equality by creating
 * 2 hashes for the bytearray and comparing thoose hashes. If no digest
 * algorithms are available, then the complete byte[] is used for comparison.
 */
public class ImageComparator
{
  /**
   * Dummy Class to create a common root for all compare results.
   */
  private abstract static class ImageCompareData
  {
  }

  /**
   * A ImageCompareData that uses the complete image data for comparison.
   */
  private static class CompleteImageCompareData extends ImageCompareData
  {
    private byte[] image;

    /**
     * Create a new CompleteImageCompareData instance.
     * @param image the image data used for comparison.
     */
    public CompleteImageCompareData(byte[] image)
    {
      this.image = image;
    }

    /**
     * Checks whether the given Object equals this object.
     *
     * @param o the to be compared object
     * @return true, if both objects are equal
     */
    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof CompleteImageCompareData)) return false;

      final CompleteImageCompareData data = (CompleteImageCompareData) o;

      if (!Arrays.equals(image, data.image)) return false;

      return true;
    }

    /**
     * returns a hashcode for this class.
     * @return always 0.
     */
    public int hashCode()
    {
      return 0;
    }
  }

  /**
   * An ImageComparator which uses precomputed Message-Digests to compare the
   * image.
   */
  private static class DigestImageCompareData extends ImageCompareData
  {
    private byte[] digestMD5Data;
    private byte[] digestSHAData;

    /**
     * Creates a new DigestImageCompareData instance.
     *
     * @param digestMD5Data the MD5 digest data
     * @param digestSHAData the SHA1 digest data
     */
    public DigestImageCompareData(byte[] digestMD5Data, byte[] digestSHAData)
    {
      if (digestMD5Data == null ||  digestSHAData == null)
        throw new NullPointerException();

      this.digestMD5Data = digestMD5Data;
      this.digestSHAData = digestSHAData;
    }

    /**
     * Checks whether the given Object equals this object.
     *
     * @param o the to be compared object
     * @return true, if both objects are equal
     */
    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof DigestImageCompareData)) return false;

      final DigestImageCompareData data = (DigestImageCompareData) o;

      if (!Arrays.equals(digestMD5Data, data.digestMD5Data)) return false;
      if (!Arrays.equals(digestSHAData, data.digestSHAData)) return false;

      return true;
    }

    /**
     * returns a hashcode for this class.
     * @return always 0.
     */
    public int hashCode()
    {
      return 0;
    }
  }

  private MessageDigest digestMD5;
  private MessageDigest digestSHA;

  /**
   * Creates a new ImageComparator. The comparator checks whether the MD5
   * and the SHA message digest implementations are available. If they are
   * not available, an alternative comparison method is used, which is more
   * memory consuming.
   */
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

  /**
   * Creates 2 comparable objects. These objects can be compared for equality.
   *
   * @param image the image data which should be prepared for comparison
   * @return the prepared image data.
   */
  public ImageCompareData createCompareData (byte[] image)
  {
    if (digestMD5 != null && digestSHA != null)
    {
      byte[] dataMD5 = digestMD5.digest(image);
      byte[] dataSHA = digestSHA.digest(image);
      if (dataSHA != null && dataMD5 != null)
      {
        return new DigestImageCompareData(dataMD5, dataSHA);
      }
    }
    return new CompleteImageCompareData(image);
  }
}
