// Buffer.java - block of raw memory.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import gnu.bhresearch.quant.Assert;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 A block of raw mmeory. This is used to store various metafile
 objects as they are read in from file.
 */
public class Buffer
{
  /** The memory itself. */
  protected byte bytes[] = null;

  /**
   * The current length of the memory.
   */
  protected int len;

  public Buffer ()
  {
  }

  /**
   * The size of the memory.
   */
  public final int getLength ()
  {
    return len;
  }

  /** Ensure we have enough space. */
  public void setCapacity (int capacity)
  {
    Assert.assert (capacity >= len);

    if (bytes == null || bytes.length == 0)
    {
      bytes = new byte[capacity];
    }
    else if (capacity != bytes.length)
    {
      byte[] old = bytes;
      bytes = new byte[capacity];
      System.arraycopy (old, 0, bytes, 0, Math.min (old.length, capacity));
    }
  }

  /**
   * Initialise this buffer from another one.
   */
  public void set (Buffer buf)
  {
    set (buf, 0, buf.getLength ());
  }

  /**
   * Initialise this buffer from part of another one.
   */
  public void set (Buffer buf, int orig, int len)
  {
    Assert.assert (orig >= 0);
    Assert.assert (len >= 0);
    Assert.assert (orig + len <= buf.len);

    this.len = len;
    bytes = new byte[len];
    System.arraycopy (buf.bytes, orig, bytes, 0, len);
  }

  /**
   * Read memory from a stream.
   */
  public void read (InputStream in, int offset, int len, String filename)
          throws IOException
  {
    if (bytes == null || offset + len > bytes.length)
      setCapacity (offset + len);

    //in.readFully( bytes, offset, len );
    while (len > 0)
    {
      int blockSize = in.read (bytes, offset, len);
      if (blockSize <= 0)
        throw new EOFException (filename);
      offset += blockSize;
      len -= blockSize;
      this.len = offset;
    }
  }

  /**
   * Return the 32-bit int at the given byte offset.
   */
  public int getInt (int offset)
  {
    Assert.assert (offset <= len - 4);
    return (getShort (offset) & 0x0ffff) | (getShort (offset + 2) << 16);
  }

  /**
   * Return the 16-bit int at the given byte offset.
   */
  public int getShort (int offset)
  {
    Assert.assert (offset <= len - 2);
    return (bytes[offset] & 0x0ff) | (bytes[offset + 1] << 8);
  }

  /** Return the 8-bit int at the given byte offset. */
  public int getByte (int offset)
  {
    Assert.assert (offset <= len - 1);
    return bytes[offset] & 0x0ff;
  }

  /**
   * Return the string at the given byte offset.
   */
  public String getString (int offset, int len)
  {
    int i;
    for (i = 0; i < len; i++)
    {
      if (bytes[offset + i] == 0)
        break;
    }
    return new String (bytes, offset, i);
  }

  public InputStream getInputStream (int offset)
  {
    return new ByteArrayInputStream (bytes, offset, bytes.length - offset);
  }
}
