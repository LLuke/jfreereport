// MfHeader.java - A buffer which represents a Metafile header.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;

/**
 A buffer which represents a Metafile header.
 */
public class MfHeader extends Buffer
{
  public static final int QUALITY_NO = 0;    // Can't convert.
  public static final int QUALITY_MAYBE = 1; // Might be able to convert.
  public static final int QUALITY_YES = 2;   // Can convert.

  public static final int PLACEABLE_HEADER_SIZE = 22;
  public static final int STANDARD_HEADER_SIZE = 18;

  public static final int WMF_FILE_TYPE = 22;     // WORD
  public static final int WMF_HEADER_SIZE = 24;   // WORD
  public static final int WMF_VERSION = 26;       // WORD
  public static final int WMF_FILE_SIZE = 28;     // DWORD
  public static final int WMF_NUM_OF_REC = 32;    // WORD
  public static final int WMF_MAX_REC_SIZE = 34;  // DWORD
  public static final int WMF_NUM_PARAMS = 38;    // WORD always 0 not used

  public static final int WMF_TYPE_MEM = 0;
  public static final int WMF_TYPE_DISK = 1;

  public static final int ALDUS_MAGIC_NUMBER_VAL = 0x9ac6cdd7;

  public static final int ALDUS_MAGIC_NUMBER_POS = 0;
  public static final int ALDUS_HANDLE_POS = 4;
  public static final int ALDUS_POS_LEFT = 6;
  public static final int ALDUS_POS_TOP = 8;
  public static final int ALDUS_POS_RIGHT = 10;
  public static final int ALDUS_POS_BOTTOM = 12;
  public static final int ALDUS_RESOLUTION = 14; // units per inch
  public static final int ALDUS_RESERVED = 16;
  public static final int ALDUS_CHECKSUM = 20;

  /** Is the given input a metafile? We have to guess. */
  public static int isMetafile (String inName, InputStream in)
  {
    if (in != null)
    {
      // See if we have a valid header.
      MfHeader header = new MfHeader ();
      try
      {
        in.mark (PLACEABLE_HEADER_SIZE + STANDARD_HEADER_SIZE);
        header.read (in, inName);
      }
      catch (IOException e)
      {
        header = null;
      }
      finally
      {
        try
        {
          in.reset ();
        }
        catch (IOException e)
        {
        }
      }
      if (header == null || !header.isValid ())
        return QUALITY_NO;
      if (header.isPlaceable ())
        return QUALITY_YES;
      // We are not so confident of identifying non-placeable
      // metafiles, so we require both isValid() and the file
      // extension to match.
    }

    // True if the extension is .wmf.
    if (inName.regionMatches (true, inName.length () - 4, ".wmf", 0, 4))
    {
      return QUALITY_MAYBE;
    }

    return QUALITY_NO;
  }

  /** Read the header from the given input. */
  public void read (InputStream in, String inName) throws IOException
  {
    int total = PLACEABLE_HEADER_SIZE + STANDARD_HEADER_SIZE;
    setCapacity (total);

    read (in, 0, 4, inName);
    if (isPlaceable ())
    {
      read (in, 4, total - 4, inName);
    }
    else
    {
      // Ignore the space for the placeable header
      for (int i = 0; i < 4; i++)
      {
        bytes[PLACEABLE_HEADER_SIZE + i] = bytes[i];
      }
      read (in, PLACEABLE_HEADER_SIZE + 4, STANDARD_HEADER_SIZE - 4, inName);
    }

    // Now have the placeable header at the start of the headers buffer,
    // and the windows header following it.
  }

  /** True if this is an Aldus placeable header. */
  public boolean isPlaceable ()
  {
    // Verify magic number.
    return getInt (ALDUS_MAGIC_NUMBER_POS) == ALDUS_MAGIC_NUMBER_VAL;
  }

  /** True if it looks like a real metafile. */
  public boolean isValid ()
  {
    int type = getShort (WMF_FILE_TYPE);  // Memory or disk.
    if (type != WMF_TYPE_DISK && type != 2)
    {
      // type == null means this is a wmf from memory. we don't want that
      return false;
    }
    if (getShort (WMF_HEADER_SIZE) != 9)  // Header size.
    {
      // A VALID wmf-File has always a standard-header size of 9 WORDS == 18 bytes
      return false;
    }
    return true;
  }

  /**
   * Return the bounding box of this metafile.
   * This returns an empty (0,0,0,0) rectangle if this file is not placeable.
   *
   */
  public Rectangle getBBox ()
  {
    int left = getShort (ALDUS_POS_LEFT);
    int top = getShort (ALDUS_POS_TOP);
    int right = getShort (ALDUS_POS_RIGHT);
    int bottom = getShort (ALDUS_POS_BOTTOM);
    return new Rectangle (left, top, right - left, bottom - top);
  }

  public int getUnitsPerInch ()
  {
    return getShort (ALDUS_RESOLUTION);
  }

  public int getFileSize ()
  {
    return getInt (WMF_FILE_SIZE) * 2;
  }

  public int getObjectsSize ()
  {
    return getShort (WMF_NUM_OF_REC);
  }

  public int getMaxRecordSize ()
  {
    return getInt (WMF_MAX_REC_SIZE) * 2;
  }

  public int getHeaderSize ()
  {
    if (isPlaceable ())
      return PLACEABLE_HEADER_SIZE + STANDARD_HEADER_SIZE;
    else
      return STANDARD_HEADER_SIZE;
  }

}
