// VIntOutputStream - Variable length integer format.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie;

import gnu.bhresearch.quant.Assert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 Variable length integer format. Integers are stored in as many
 4-bit nibbles as are needed. The low 3 bits contribute to the
 number, the high bit is 1 is another nibble is to come.
 */
public class VIntOutputStream extends DataOutputStream
{
  public static final boolean DEBUG_VINT = false;

  public VIntOutputStream (OutputStream out)
  {
    super (out);
  }

  /** Flush - and flush our private state too. */
  public void flush () throws IOException
  {
    flushVInt ();
    super.flush ();
  }

  private int nibBuf = 0;
  private boolean nibBufEmpty = true;

/*
	public int getVIntState() {
		return nibBufEmpty ? 0x010 : (nibBuf & 0x00f);
	}

	public void setVIntState( int state ) {
		nibBufEmpty = (state & 0x010) != 0;
		nibBuf = state & 0x0f;
	}
*/

  /** Output any outstanding nibbles. */
  public void flushVInt () throws IOException
  {
    if (!nibBufEmpty)
    {
      nibBufEmpty = true;
      writeByte (nibBuf);
    }
    Assert.assert (nibBufEmpty);
  }

  /** Write a signed variable length integer. */
  public void writeVInt (int num) throws IOException
  {
    // Store sign in lowest bit, where it is easy to find.
    if (num >= 0)
      writeUnsignedVInt (num << 1);
    else
    {
      // Eg -3 is encoded as +5.
      int pnum = -(num << 1) - 1;
      Assert.assert ((pnum & 0x01) == 0x01);
      writeUnsignedVInt (pnum);
    }
  }

  /** Write an unsigned variable length integer. (This saves a bit!) */
  public void writeUnsignedVInt (int num) throws IOException
  {
    Assert.assert (num >= 0);
    if (DEBUG_VINT)
    {
      //Debug.println( "v " + num + " " + signed );
      writeInt (num);
      return;
    }
    else
    {
      int needed = nibblesNeeded (num);
      // Debug.println( "v " + num + " " + signed );
      for (int i = needed - 1; i >= 0; i--)
      {
        int nibble = (num >> (i * 3)) & 0x07;	// Extract 3 bits.
        if (i > 0)	// Set 4th bit if there's more to come.
          nibble |= 0x08;
        //Debug.println( "  n " + nibble );
        if (nibBufEmpty)
        {  // Buffer up nibble.
          nibBuf = nibble;
          nibBufEmpty = false;
        }
        else
        {		// Output 2 nibbles.
          writeByte (nibBuf | (nibble << 4));
          nibBufEmpty = true;
        }
      }
    }
  }

  /** Return the number of nibbles needed to store unsigned num. */
  private int nibblesNeeded (int num)
  {
    Assert.assert (num >= 0);
    int bitsNeeded = 0;
    while ((num >> bitsNeeded) != 0)
      bitsNeeded++;
    // Allow 3 bits per nibble.
    return (bitsNeeded - 1) / 3 + 1;
  }
}
