// MfRecord.java - A Windows metafile record.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie.wmf;

import java.io.InputStream;
import java.io.IOException;
import gnu.bhresearch.quant.Debug;
import gnu.bhresearch.quant.Assert;

/**
	A Windows metafile record.
*/
public class MfRecord extends Buffer 
{
  public static int RECORD_HEADER = 6;

  private MfType type = null;

  /** Read a record from an input stream. */
  public void read( InputStream in, String inName ) 
    throws IOException
  {
    super.read( in, 0, 6, inName );
    int remaining = getInt( 0 ) * 2 - 6;
    if (remaining > 0)
    {
      super.read( in, 6, remaining, inName );
    }
    type = MfType.get( getType() );
  }

  /** The 16-bit type of this record. */
  public int getType()
  {
    return getShort(4);
  }

  /** Return a 16-bit param from the given offset. Offset is in 16-bit
  words. */
  public int getParam( int p )
  {
    return getShort( p*2+6 );
  }

  /** Return a 32-bit param from the given offset. Offset is in 16-bit
  words. */
  public int getLongParam( int p )
  {  // Offset is in 16-bit words.
    return getInt( p*2+6 );
  }

  /** Return a string param from the given offset. Offset is in 16-bit
  words. */
  public String getStringParam( int p, int len )
  {
    return getString( p*2+6, len );
  }

  /** Return the name of this type of record. */
  public String getName()
  {
    return type.getName();
  }

  /** Return debug info. */
  public String toString()
  {
    StringBuffer result = new StringBuffer ();
    result.append (type);
    result.append (" ");
    result.append (getName());
    result.append (": ");

    StringBuffer str = new StringBuffer ();
    
    int len = (getInt( 0 ) - 3) * 2;
    for (int i = 0; i < len; i++)
    {
      if ((i % 16) == 0)
      {
        result.append ("\n");
        str.append ("\n");
      }
      else
      if ((i % 8) == 0)
      {
        result.append (" ");
      }

      int by = getByte (i + RECORD_HEADER);

      if (by < 16)
      {
        result.append ("0");
      }  
      result.append (Integer.toHexString (by));
      //str.append ((char) by);
      result.append (" ");
    }  
    return result.toString();
  }

  /** 
   * True if this record marks the screen. Currently such records are ignored. 
   */
  public boolean doesMark()
  {
    return (type.doesMark());
  }

  /** 
   * True if this record affects mapping modes. 
   */
  public boolean isMappingMode()
  {
    return (type.isMappingMode());
  }
}
